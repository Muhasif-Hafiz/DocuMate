package com.muhasib.documate.utils
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

class MySharedPreferences(context: Context) {

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    // Save String value
    fun saveString(key: String, value: String) {
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }

    fun saveToken(token: String) {
        saveString("auth_token", token)
    }

    fun getToken(): String {
        return getString("auth_token")
    }

    fun clearToken() {
        with(sharedPref.edit()) {
            remove("auth_token")
            apply()
        }
    }

    fun getSwiped(): Boolean {
        return sharedPref.getBoolean("swiped", false)
    }

    fun setSwiped(value: Boolean) {
        sharedPref.edit().putBoolean("swiped", value).apply()
    }

    fun clearAll() {
        sharedPref.edit().clear().apply()
    }

}