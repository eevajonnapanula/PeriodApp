package com.eevajonna.period.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.MenstruationPeriodRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.LocalDateTime
class HealthConnectManager(private val context: Context) {
    private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context) }

    suspend fun hasAllPermissions(): Boolean = healthConnectClient.permissionController.getGrantedPermissions().containsAll(PERMISSIONS)

    fun requestPermissionsActivityContract(): ActivityResultContract<Set<String>, Set<String>> {
        return PermissionController.createRequestPermissionResultContract()
    }

    suspend fun readMenstruationRecords(): List<MenstruationPeriodRecord> {
        val request = ReadRecordsRequest(
            recordType = MenstruationPeriodRecord::class,
            timeRangeFilter = TimeRangeFilter.after(LocalDateTime.now().minusMonths(12)),
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
    }

    suspend fun writeMenstruationRecords(menstruationPeriodRecord: MenstruationPeriodRecord) {
        val records = listOf(menstruationPeriodRecord)
        try {
            healthConnectClient.insertRecords(records)
            Toast.makeText(context, "Successfully insert records", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            Log.e("Error", "Message: ${e.message}")
        }
    }
}
val PERMISSIONS =
    setOf(
        HealthPermission.getReadPermission(MenstruationPeriodRecord::class),
        HealthPermission.getWritePermission(MenstruationPeriodRecord::class),
    )
