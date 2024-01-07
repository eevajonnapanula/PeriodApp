package com.eevajonna.period.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eevajonna.period.data.HealthConnectManager
import kotlinx.coroutines.launch

class PeriodViewModel(private val healthConnectManager: HealthConnectManager) : ViewModel() {
    var permissionsGranted by mutableStateOf(false)

    init {
        checkPermissions()
    }

    val permissionsLauncher = healthConnectManager.requestPermissionsActivityContract()

    private fun checkPermissions() {
        viewModelScope.launch {
            permissionsGranted = healthConnectManager.hasAllPermissions()
        }
    }
}

class PeriodViewModelFactory(
    private val healthConnectManager: HealthConnectManager,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeriodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PeriodViewModel(
                healthConnectManager = healthConnectManager,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
