package com.inavi.inavi_map.repository

import androidx.lifecycle.LiveData
import com.inavi.inavi_map.db.user.UserLocationEntity


interface RoomRepository {


    //UserLocation
    suspend fun insertLocation(userLocationEntity: UserLocationEntity)
    suspend fun updateLocation(userLocationEntity: UserLocationEntity)
    suspend fun updateLocations()
    suspend fun deleteLocation(userLocationEntity: UserLocationEntity)
    suspend fun deleteAllAddress()
    fun getUserLocation(): LiveData<List<UserLocationEntity>>


}