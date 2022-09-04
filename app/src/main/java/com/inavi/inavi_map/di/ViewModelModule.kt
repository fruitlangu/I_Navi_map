package com.inavi.inavi_map.di


import com.inavi.inavi_map.repository.LocationRepository
import com.inavi.inavi_map.repository.LocationRepositoryImpl
import com.inavi.inavi_map.repository.RoomRepository
import com.inavi.inavi_map.repository.RoomRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {
    @Binds
    @ViewModelScoped
    abstract fun locationRepository(repo: LocationRepositoryImpl) : LocationRepository
    @Binds
    @ViewModelScoped
    abstract fun roomRepository(repo: RoomRepositoryImpl) : RoomRepository
}