package com.inavi.inavi_map.db.user

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {


    //UserLocation
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(userLocationEntity: UserLocationEntity)
    @Update
    suspend fun updateLocation(userLocationEntity: UserLocationEntity)
    @Query("UPDATE location_table SET currentUse = :current_use")
    suspend fun updateLocations(current_use: Boolean = false)
    @Delete
    suspend fun deleteLocation(userLocationEntity: UserLocationEntity)
    @Query("DELETE FROM location_table")
    suspend fun deleteAllAddress()
    @Transaction
    @Query("SELECT * FROM location_table")
    fun getLocation(): LiveData<List<UserLocationEntity>>

}