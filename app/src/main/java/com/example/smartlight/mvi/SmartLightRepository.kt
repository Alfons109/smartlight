package com.example.smartlight.mvi

import android.util.Log
import com.example.smartlight.SmartLightConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SmartLightRepository {

    // do async task
    suspend fun fetchConfigAsync(): SmartLightConfig {
        var randomConfig = SmartLightConfig.OFF

        withContext(Dispatchers.IO) {
            try {
                Log.d(javaClass.simpleName, "Fetching Config.")
                randomConfig = SmartLightConfig.values()[(0..2).random()]
                Thread.sleep(3000)
                Log.d(javaClass.simpleName, "Fetch Complete long task - $randomConfig")
            } catch (ex: Exception) {
                Log.d(javaClass.simpleName, "Exception- $ex")
            }
        }
        return randomConfig
    }

    // do async task
    suspend fun asyncSmartLightConfigurationUpdate(configValue: SmartLightConfig): SmartLightConfig {
        withContext(Dispatchers.IO) {
            try {
                Log.d(javaClass.simpleName, "Starting long task - $configValue")
                Thread.sleep(3000)
                Log.d(javaClass.simpleName, "Completed long task - $configValue")
            } catch (ex: java.lang.Exception) {
                Log.d(javaClass.simpleName, "Exception- $ex")
            }
        }
        return configValue
    }
}