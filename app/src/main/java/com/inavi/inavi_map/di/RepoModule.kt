package com.inavi.inavi_map.di

import android.content.Context
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.inavi.inavi_map.db.Database
import com.inavi.inavi_map.utils.Constants.CART_ITEM_TABLE
import com.inavi.inavi_map.utils.Constants.LOCATION_TABLE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext app: Context
    ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(app)

    @Singleton
    @Provides
    fun provideUserDao(db: Database) = db.userDao()

    @Singleton
    @Provides
    fun provideCartItemDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        Database::class.java,
        LOCATION_TABLE
    ).build()

    /*@Singleton
    @Provides
    fun providePreferences(@ApplicationContext app: Context): DataStore<Preferences> =
    PreferenceDataStoreFactory.create(
    produceFile = {
        app.preferencesDataStoreFile(PREFERENCES_STORE_NAME)
    })*/

}