package com.nandaiqbalh.pixabayapp.data.network.datasource

import com.nandaiqbalh.pixabayapp.data.network.service.PixabayApiService
import com.nandaiqbalh.pixabayapp.model.SearchResponse

interface SearchDataSource {
    suspend fun searchPhoto(query: String): SearchResponse
}

class PixabayApiDataSourceImpl(private val api:PixabayApiService) : SearchDataSource{

    override suspend fun searchPhoto(query: String): SearchResponse {
        return api.searchPhoto(query)
    }

}