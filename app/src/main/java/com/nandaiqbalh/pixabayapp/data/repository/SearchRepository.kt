package com.nandaiqbalh.pixabayapp.data.repository

import com.nandaiqbalh.pixabayapp.data.network.datasource.SearchDataSource
import com.nandaiqbalh.pixabayapp.model.SearchResponse
import com.nandaiqbalh.pixabayapp.wrapper.Resource

interface SearchRepository {
    suspend fun searchPhoto(q: String): Resource<SearchResponse>
}

class SearchRepositoryImpl(private val dataSource: SearchDataSource) : SearchRepository {

    override suspend fun searchPhoto(q: String): Resource<SearchResponse> {
        return try {
            val data = dataSource.searchPhoto(q)
            if (data.posts.isNullOrEmpty()) Resource.Empty() else Resource.Success(data)
        } catch (exception: Exception) {
            Resource.Error(exception)
        }
    }

}