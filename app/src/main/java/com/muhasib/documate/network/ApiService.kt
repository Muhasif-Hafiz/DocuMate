package com.muhasib.documate.network

import com.muhasib.documate.models.ApiItem
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("objects")
    suspend fun getItems(): List<ApiItem>

    @GET("objects/{id}")
    suspend fun getItemById(@Path("id") id: String): ApiItem

    @PUT("objects/{id}")
    suspend fun updateItem(@Path("id") id: String, @Body item: ApiItem): ApiItem

    @DELETE("objects/{id}")
    suspend fun deleteItem(@Path("id") id: String)
}