package com.example.smartlight.mvvm

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartlight.SmartLightConfig
import java.lang.Exception
import java.util.concurrent.Executors

//View model not aware of view
//lifecycle aware
class MvvmViewModel: ViewModel() {

    private var _currentConfig = MutableLiveData<SmartLightConfig>()
    fun currentConfig(): LiveData<SmartLightConfig> = _currentConfig

    fun asyncSmartLightConfigurationUpdate(configValue: SmartLightConfig) {
        // do async task, provide callback or listener to receive response
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executor.execute {
            try {
                Log.d(javaClass.simpleName, "Starting long task - $configValue")
                Thread.sleep(3000)
                Log.d(javaClass.simpleName, "Completed long task - $configValue")
            }catch (ex: Exception) {
                Log.d(javaClass.simpleName, "Exception- $ex")
            }
            handler.post {
//                onConfigCallback(configValue)
                _currentConfig.value = configValue
            }
        }
    }
}