package com.inavi.inavi_map.db.user

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.inavi.inavi_map.utils.Constants.LOCATION_TABLE
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = LOCATION_TABLE)
data class UserLocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val address: String? = null,
    val city: String? = null,
    val addInfo: String? = null,
    val latitude: Double,
    val longitude: Double,
    val currentUse: Boolean = false
): Parcelable