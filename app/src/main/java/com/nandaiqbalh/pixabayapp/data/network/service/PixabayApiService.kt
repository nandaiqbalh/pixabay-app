package com.nandaiqbalh.pixabayapp.data.network.service

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.nandaiqbalh.pixabayapp.BuildConfig
import com.nandaiqbalh.pixabayapp.model.SearchResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface PixabayApiService {

    @GET("api")
    suspend fun searchPhoto(
        @Query("q", encoded = true) query: String,
        @Query("image_type") imageType: String = TYPE_PHOTO,
    ): SearchResponse

    companion object {
        private const val TYPE_PHOTO = "photo"

        @JvmStatic
        operator fun invoke(chukerInterceptor: ChuckerInterceptor): PixabayApiService {
            val authInterceptor = Interceptor {
                val originRequest = it.request()
                val oldUrl = originRequest.url
                val newUrl = oldUrl.newBuilder().apply {
                    addQueryParameter("key", BuildConfig.API_KEY)
                }.build()
                it.proceed(originRequest.newBuilder().url(newUrl).build())
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(chukerInterceptor)
                .addInterceptor(authInterceptor)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(PixabayApiService::class.java)
        }
    }
}