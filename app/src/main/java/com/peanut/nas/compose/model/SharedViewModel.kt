package com.peanut.nas.compose.model

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peanut.nas.compose.data.Configuration
import com.peanut.nas.compose.data.RequestStore
import com.peanut.nas.compose.gson.Album
import com.peanut.nas.compose.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedViewModel: ViewModel() {
    var albumResponse: MutableState<RequestStore<Album>> = mutableStateOf(RequestStore.Empty())

    fun getAlbumList(){
        Log.d("SharedViewModel", "getAlbumList")
        viewModelScope.launch {
            albumResponse.value = RequestStore.Loading()
            val apiService = ApiService.getInstance(configuration.value)
            try {
                val data: Array<Album> = apiService.getAlbumList()
                albumResponse.value = RequestStore.Success(data.toMutableList())
            }catch (e:Exception){
                e.printStackTrace()
                albumResponse.value = RequestStore.Failure(e.localizedMessage?:"unknown error")
            }
        }
    }

    val loginState: MutableState<RequestStore<Int>> = mutableStateOf(RequestStore.Empty())
    fun userLogin(){
        viewModelScope.launch {
            val configuration = configuration.value
            loginState.value = RequestStore.Loading()
            val apiService = ApiService.getInstance(configuration)
            try {
                apiService.userLogin(name = configuration.userName, password = configuration.userPassword)
                loginState.value = RequestStore.Success(0)
            }catch (e:Exception){
                loginState.value = RequestStore.Failure(e.localizedMessage?:"unknown error")
            }
        }
    }

    private val _configuration = MutableStateFlow(Configuration.Empty)
    val configuration: StateFlow<Configuration> = _configuration

    suspend fun loadConfiguration(dataStore: ConfigurationDataStore){
        dataStore.getConfiguration.collect{
            _configuration.value = it?: Configuration.Empty
        }
    }

    fun setConfiguration(configuration: Configuration) {
        _configuration.value = configuration
    }
}