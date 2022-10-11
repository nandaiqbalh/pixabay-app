package com.nandaiqbalh.pixabayapp.provider

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.nandaiqbalh.pixabayapp.data.network.datasource.PixabayApiDataSourceImpl
import com.nandaiqbalh.pixabayapp.data.network.datasource.SearchDataSource
import com.nandaiqbalh.pixabayapp.data.network.service.PixabayApiService
import com.nandaiqbalh.pixabayapp.data.repository.SearchRepository
import com.nandaiqbalh.pixabayapp.data.repository.SearchRepositoryImpl

object ServiceLocator {

    fun provideChucker(appContext: Context): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(appContext).build()
    }

    fun provideApiService(chuckerInterceptor: ChuckerInterceptor): PixabayApiService {
        return PixabayApiService.invoke(chuckerInterceptor)
    }

    fun provideDataSource(apiService: PixabayApiService): SearchDataSource {
        return PixabayApiDataSourceImpl(apiService)
    }

    fun provideRepository(context: Context): SearchRepository {
        return SearchRepositoryImpl(provideDataSource(provideApiService(provideChucker(context))))
    }

}