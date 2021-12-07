package com.example.smartlight.mvi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartlight.SmartLightConfig
import kotlinx.coroutines.launch

//View model not aware of view
//lifecycle aware
class MviViewModel : ViewModel() {

    private val smartLightRepository: SmartLightRepository = SmartLightRepository()

    //initial state
    private var _currentViewState =
        MutableLiveData(SmartLightViewState(isLoading = false, config = SmartLightConfig.OFF))

    fun currentViewState(): LiveData<SmartLightViewState> = _currentViewState

    fun onEvent(action: SmartLightEvent) {
        when (action) {
            is SmartLightEvent.Loading -> asyncFetchConfig()
            is SmartLightEvent.Toggle -> asyncSmartLightConfigurationUpdate(action.config)
        }
    }

    private fun reduce(result: SmartLightResult) {
        when (result) {
            is SmartLightResult.Loading -> _currentViewState.value =
                _currentViewState.value?.copy(isLoading = result.isLoading)
            is SmartLightResult.Toggle -> _currentViewState.value =
                _currentViewState.value?.copy(isLoading = false, config = result.config)
        }
    }

    private fun asyncFetchConfig() {
        reduce(SmartLightResult.Loading(true))
        viewModelScope.launch {
            val config = smartLightRepository.fetchConfigAsync()
            reduce(SmartLightResult.Toggle(config))
        }
    }

    private fun asyncSmartLightConfigurationUpdate(configValue: SmartLightConfig) {
        reduce(SmartLightResult.Loading(true))
        viewModelScope.launch {
            val config = smartLightRepository.asyncSmartLightConfigurationUpdate(configValue)
            reduce(SmartLightResult.Toggle(config = config))
        }
    }
}

sealed class SmartLightEvent {
    data class Loading(val isLoading: Boolean) : SmartLightEvent()
    data class Toggle(val config: SmartLightConfig) : SmartLightEvent()
}

sealed class SmartLightResult {
    data class Loading(val isLoading: Boolean) : SmartLightResult()
    data class Toggle(val config: SmartLightConfig) : SmartLightResult()
}

data class SmartLightViewState(
    val config: SmartLightConfig,
    val isLoading: Boolean
)