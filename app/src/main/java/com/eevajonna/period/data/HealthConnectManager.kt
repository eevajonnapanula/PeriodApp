package com.eevajonna.period.data

import android.content.Context
import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.MenstruationPeriodRecord
class HealthConnectManager(private val context: Context) {
    private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context) }

    suspend fun hasAllPermissions(): Boolean = healthConnectClient.permissionController.getGrantedPermissions().containsAll(PERMISSIONS)

    fun requestPermissionsActivityContract(): ActivityResultContract<Set<String>, Set<String>> {
        return PermissionController.createRequestPermissionResultContract()
    }
}
val PERMISSIONS =
    setOf(
        HealthPermission.getReadPermission(MenstruationPeriodRecord::class),
        HealthPermission.getWritePermission(MenstruationPeriodRecord::class),
    )
