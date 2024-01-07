package com.eevajonna.period.ui

import android.os.RemoteException
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.MenstruationPeriodRecord
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eevajonna.period.data.HealthConnectManager
import kotlinx.coroutines.launch
import java.io.IOException

class PeriodViewModel(private val healthConnectManager: HealthConnectManager) : ViewModel() {
    var periods by mutableStateOf<List<MenstruationPeriodRecord>>(emptyList())
    var permissionsGranted by mutableStateOf(false)

    init {
        checkPermissions()
    }

    fun getInitialRecords() {
        viewModelScope.launch {
            tryWithPermissionsCheck {
                getPeriodRecords()
            }
        }
    }

    val permissionsLauncher = healthConnectManager.requestPermissionsActivityContract()

    fun writeMenstruationRecord(menstruationPeriodRecord: MenstruationPeriodRecord) {
        viewModelScope.launch {
            tryWithPermissionsCheck {
                healthConnectManager.writeMenstruationRecords(menstruationPeriodRecord)
                getPeriodRecords()
            }
        }
    }

    fun deleteMenstruationRecord(menstruationPeriodRecord: MenstruationPeriodRecord) {
        viewModelScope.launch {
            tryWithPermissionsCheck {
                healthConnectManager.deleteMenstruationRecords(menstruationPeriodRecord)
                getPeriodRecords()
            }
        }
    }

    fun updateMenstruationRecord(menstruationPeriodRecord: MenstruationPeriodRecord) {
        viewModelScope.launch {
            tryWithPermissionsCheck {
                healthConnectManager.updateMenstruationRecords(menstruationPeriodRecord)
                getPeriodRecords()
            }
        }
    }
    private fun checkPermissions() {
        viewModelScope.launch {
            permissionsGranted = healthConnectManager.hasAllPermissions()
        }
    }

    private suspend fun tryWithPermissionsCheck(block: suspend () -> Unit) {
        permissionsGranted = healthConnectManager.hasAllPermissions()
        try {
            if (permissionsGranted) {
                block()
            }
        } catch (remoteException: RemoteException) {
            Log.e("Error getting records:", "${remoteException.message}")
        } catch (securityException: SecurityException) {
            Log.e("Error getting records:", "${securityException.message}")
        } catch (ioException: IOException) {
            Log.e("Error getting records:", "${ioException.message}")
        } catch (illegalStateException: IllegalStateException) {
            Log.e("Error getting records:", "${illegalStateException.message}")
        } catch (e: Exception) {
            Log.e("Error getting records:", "${e.message}")
        }
    }

    private fun getPeriodRecords() {
        viewModelScope.launch {
            periods = healthConnectManager.readMenstruationRecords()
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
