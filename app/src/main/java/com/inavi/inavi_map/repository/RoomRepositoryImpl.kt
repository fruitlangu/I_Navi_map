package com.inavi.inavi_map.repository

import androidx.lifecycle.LiveData
import com.inavi.inavi_map.db.user.UserDao
import com.inavi.inavi_map.db.user.UserLocationEntity
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val userDao: UserDao,

    ) : RoomRepository {


    //UserLocation
    override suspend fun insertLocation(userLocationEntity: UserLocationEntity) = userDao.insertLocation(userLocationEntity)
    override suspend fun updateLocation(userLocationEntity: UserLocationEntity) = userDao.updateLocation(userLocationEntity)
    override suspend fun updateLocations() = userDao.updateLocations()
    override suspend fun deleteLocation(userLocationEntity: UserLocationEntity) = userDao.deleteLocation(userLocationEntity)
    override suspend fun deleteAllAddress() = userDao.deleteAllAddress()
    override fun getUserLocation(): LiveData<List<UserLocationEntity>> = userDao.getLocation()


}