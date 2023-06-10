package com.example.moneymanagement

import android.app.Application
import com.example.moneymanagement.data.AppDataContainer

class AppApplication : Application() {
    lateinit var container: AppDataContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}