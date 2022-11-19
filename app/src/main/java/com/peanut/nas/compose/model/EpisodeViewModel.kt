package com.peanut.nas.compose.model

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peanut.nas.compose.data.Configuration
import com.peanut.nas.compose.data.RequestStore
import com.peanut.nas.compose.gson.Episode
import com.peanut.nas.compose.gson.TMDB
import com.peanut.nas.compose.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EpisodeViewModel: ViewModel() {
    var episodeResponse: MutableState<RequestStore<Episode>> = mutableStateOf(RequestStore.Empty())
    var tmdbResponse: MutableState<RequestStore<TMDB>> = mutableStateOf(RequestStore.Empty())

    private val _configuration = MutableStateFlow(Configuration.Empty)
    val configuration: StateFlow<Configuration> = _configuration

    fun loadConfiguration(dataStore: ConfigurationDataStore){
        viewModelScope.launch {
            dataStore.getConfiguration.collect{
                _configuration.value = it?: Configuration.Empty
            }
        }
    }

    fun getEpisodes(path: String){
        Log.d("EpisodeViewModel", "getEpisodes for ($path)")
        viewModelScope.launch {
            episodeResponse.value = RequestStore.Loading()
            val apiService = ApiService.getInstance(configuration.value)
            try {
                val data: Array<Episode> = apiService.getEpisodeList(path = path)
                Log.d("EpisodeViewModel", "getEpisodes ${data.size}")
                episodeResponse.value = RequestStore.Success(data.toMutableList())
            }catch (e:Exception){
                e.printStackTrace()
                episodeResponse.value = RequestStore.Failure(e.localizedMessage?:"unknown error")
            }
        }
    }

    fun getTmdbInfo(path: String){
        Log.d("EpisodeViewModel", "getTmdbInfo for ($path)")
        viewModelScope.launch {
            tmdbResponse.value = RequestStore.Loading()
            val apiService = ApiService.getInstance(configuration.value)
            try {
                val data: TMDB = apiService.getTmdbInfo(path = path)
                Log.d("EpisodeViewModel", "getTmdbInfo $data")
                tmdbResponse.value = RequestStore.Success(data)
            }catch (e:Exception){
                e.printStackTrace()
                tmdbResponse.value = RequestStore.Failure(e.localizedMessage?:"unknown error")
            }
        }
    }

    companion object{
        fun calculateColorLightValue(argb: Int):Double{
            return try {
                val r = argb shr 16 and 0xff
                val g = argb shr 8 and 0xff
                val b = argb and 0xff
                (0.299 * r + 0.587 * g + 0.114 * b)/255.0
            }catch (e:Exception){
                e.printStackTrace()
                0.0
            }
        }
    }

}