package com.muhasib.documate.mvvm

import ItemRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.muhasib.documate.data.AppDatabase
import com.muhasib.documate.data.ItemEntity
import com.muhasib.documate.network.RetrofitClient
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ItemRepository
    val allItems: LiveData<List<ItemEntity>>
    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    init {
        val itemDao = AppDatabase.getDatabase(application).itemDao()
        repository = ItemRepository(
            itemDao,
            RetrofitClient.instance,
            application
        )
        allItems = repository.getAllItems().asLiveData()
    }



    fun fetchItems() {
        viewModelScope.launch {
            isLoading.postValue(true)
            try {
                repository.fetchItemsFromApiAndStore()
            } catch (e: Exception) {
                errorMessage.postValue(e.message)
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    fun deleteItem(item: ItemEntity) {
        viewModelScope.launch {
            try {
                repository.deleteItemLocally(item)

            } catch (e: Exception) {
                errorMessage.postValue(e.message)
            }
        }
    }

    fun updateItem(item: ItemEntity) {
        viewModelScope.launch {
            try {
                repository.updateItemLocally(item)
            } catch (e: Exception) {
                errorMessage.postValue(e.message)
            }
        }
    }

    private fun validateItem(item: ItemEntity): Boolean {
        if (item.name.isBlank()) {
            errorMessage.postValue("Name cannot be empty")
            return false
        }

        if (item.price != null && item.price < 0) {
            errorMessage.postValue("Price cannot be negative")
            return false
        }

        return true
    }



}