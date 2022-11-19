package com.peanut.nas.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.peanut.nas.compose.data.RequestStore
import com.peanut.nas.compose.gson.TMDB
import com.peanut.nas.compose.model.ConfigurationDataStore
import com.peanut.nas.compose.model.EpisodeViewModel
import com.peanut.nas.compose.ui.component.EpisodeList
import com.peanut.nas.compose.ui.component.EpisodePost
import com.peanut.nas.compose.ui.theme.NASTheme

class EpisodeActivity : ComponentActivity() {
    private val viewModel: EpisodeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val albumRelativePath = (intent.getStringExtra("ALBUM") ?: "错误")
        setContent {
            NASTheme {
                val context = LocalContext.current
                LaunchedEffect(key1 = true){
                    viewModel.loadConfiguration(ConfigurationDataStore(context))
                    viewModel.getEpisodes("/$albumRelativePath")
                    viewModel.getTmdbInfo("/$albumRelativePath/.info")
                }
                val configuration = viewModel.configuration.collectAsState()
                Column(modifier = Modifier.fillMaxSize()) {
                    val tmdb = viewModel.tmdbResponse.value
                    EpisodePost(tmdb = if (tmdb !is RequestStore.Success) TMDB.Empty else tmdb._data!!.apply { this._albumPath = albumRelativePath }, configuration = configuration.value)
                    when(val result = viewModel.episodeResponse.value){
                        is RequestStore.Success -> {
                            EpisodeList(episodes = result._datas, viewModel = viewModel)
                        }
                        is RequestStore.Failure -> {
                            EpisodeError(msg = result.message)
                        }
                        else -> {
                            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                Text(text = "Empty Data!")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EpisodeError(msg: String) {
    Text(text = "Error: $msg", color = Color.Red)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NASTheme {
        EpisodeError("Android")
    }
}