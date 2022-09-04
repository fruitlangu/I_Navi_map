package com.inavi.inavi_map.di

import android.content.Context
import android.location.Geocoder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@ExperimentalCoroutinesApi
object UtilModule {

    @Singleton
    @Provides
    fun provideGeocoder(@ApplicationContext app: Context): Geocoder {
        return Geocoder(app)
    }

}