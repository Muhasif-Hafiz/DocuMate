package com.muhasib.documate.models

import com.google.gson.annotations.SerializedName

data class ApiItem(
    val id: String,
    val name: String,
    val data: ItemData?
)

data class ItemData(
    val color: String?,
    val capacity: String?,
    @SerializedName("capacity GB") val capacityGB: Int?,
    @SerializedName("Capacity") val capacityAlt: String?, // For alternate capitalization
    val price: Double?,
    @SerializedName("Price") val priceString: String?, // For string prices
    val generation: String?,
    @SerializedName("Generation") val generationAlt: String?, // For alternate capitalization
    val year: Int?,
    @SerializedName("CPU model") val cpuModel: String?,
    @SerializedName("Hard disk size") val hardDiskSize: String?,
    @SerializedName("Strap Colour") val strapColor: String?,
    @SerializedName("Case Size") val caseSize: String?,
    @SerializedName("Color") val colorAlt: String?, // For alternate capitalization
    val Description: String?,
    @SerializedName("Screen size") val screenSize: Double?
)