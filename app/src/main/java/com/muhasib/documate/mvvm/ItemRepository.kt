import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.muhasib.documate.R
import com.muhasib.documate.data.ItemDao
import com.muhasib.documate.data.ItemEntity
import com.muhasib.documate.models.ApiItem
import com.muhasib.documate.models.ItemData
import com.muhasib.documate.network.ApiService
import com.muhasib.documate.utils.NotificationPreferenceHelper
import kotlinx.coroutines.flow.Flow

class ItemRepository(
    private val itemDao: ItemDao,
    private val apiService: ApiService,
    private val context: Context
) {
    private fun ApiItem.toEntity(): ItemEntity {
        return ItemEntity(
            id = this.id,
            name = this.name,
            color = this.data?.color ?: this.data?.colorAlt ?: this.data?.strapColor,
            capacity = this.data?.capacity ?: this.data?.capacityAlt ?: this.data?.capacityGB?.toString(),
            price = this.data?.price ?: this.data?.priceString?.toDoubleOrNull(),
            generation = this.data?.generation ?: this.data?.generationAlt,
            year = this.data?.year,
            cpuModel = this.data?.cpuModel,
            hardDiskSize = this.data?.hardDiskSize,
            strapColor = this.data?.strapColor,
            caseSize = this.data?.caseSize,
            description = this.data?.Description,
            screenSize = this.data?.screenSize
        )
    }

    suspend fun fetchItemsFromApiAndStore() {
        try {
            val apiItems = apiService.getItems()
            val itemEntities = apiItems.map { it.toEntity() }
            itemEntities.forEach { itemDao.insertItem(it) }
        } catch (e: Exception) {
            throw Exception("Failed to fetch items: ${e.message}")
        }
    }

     fun getAllItems(): Flow<List<ItemEntity>> = itemDao.getAllItems()

    @SuppressLint("MissingPermission", "ServiceCast")
    private fun sendDeleteNotification(item: ItemEntity) {
        if (NotificationPreferenceHelper.getNotificationStatus(context)) {
            val notificationId = System.currentTimeMillis().toInt()

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notificationText = buildString {
                append("${item.name} (ID: ${item.id}) has been deleted\n")
                item.price?.let { append("Price: $${"%.2f".format(it)}\n") }
                item.color?.let { append("Color: $it") }
            }

            val notification = NotificationCompat.Builder(context, "item_deletion_channel")
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Item Deleted")
                .setContentText(notificationText.trim())
                .setStyle(NotificationCompat.BigTextStyle().bigText(notificationText.trim()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(notificationId, notification)
        } else {
            Log.d("Notifications", "User has disabled notifications.")
            Toast.makeText(context, "User has disabled notifications.", Toast.LENGTH_SHORT).show()
        }
    }
    suspend fun deleteItemLocally(item: ItemEntity) {
        try {
            itemDao.deleteItem(item.id)
            sendDeleteNotification(item)
        } catch (e: Exception) {
            throw Exception("Failed to delete item: ${e.message}")
        }
    }

    // Modified update function for local database
    suspend fun updateItemLocally(item: ItemEntity) {
        try {
            itemDao.updateItem(item)
        } catch (e: Exception) {
            throw Exception("Failed to update item: ${e.message}")
        }
    }
}