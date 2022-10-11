package com.nandaiqbalh.pixabayapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nandaiqbalh.pixabayapp.model.SearchResponse
import com.nandaiqbalh.pixabayapp.data.network.service.PixabayApiService
import com.nandaiqbalh.pixabayapp.data.repository.SearchRepository
import com.nandaiqbalh.pixabayapp.wrapper.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: SearchRepository) : ViewModel() {

    val searchResult = MutableLiveData<Resource<SearchResponse>>()

    fun searchPost(query: String) {
        searchResult.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.searchPhoto(query)
            viewModelScope.launch(Dispatchers.Main) {
                searchResult.postValue(data)
            }
        }
    }
}