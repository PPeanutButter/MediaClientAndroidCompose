package com.peanut.nas.compose.ui.component

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.peanut.nas.compose.data.RequestStore
import com.peanut.nas.compose.gson.Album
import com.peanut.nas.compose.model.SharedViewModel
import com.peanut.nas.compose.utils.SettingManager

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumList(paddingValues: PaddingValues,
              albums: MutableList<Album>,
              viewModel: SharedViewModel) {
    val serverIp = viewModel.configuration.collectAsState()
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.padding(top = paddingValues.calculateTopPadding(), start = 4.dp, end = 4.dp)) {
        items(items = albums, key = { item: Album -> item.hashKey }){
            AlbumCard(album = it){ albumRelativePath ->
                return@AlbumCard "${serverIp.value.serverIp}/getCover?" +
                        "cover=${Uri.encode(albumRelativePath)}" +
                        "&token=${SettingManager.token}"
            }
        }
    }
}

@Composable
fun AlbumListRequester(paddingValues: PaddingValues, viewModel: SharedViewModel){
    if (viewModel.albumResponse.value is RequestStore.Empty)
        viewModel.getAlbumList()
    when(val result = viewModel.albumResponse.value){
        is RequestStore.Loading -> {
            //todo loading placeholder
        }
        is RequestStore.Success -> {
            AlbumList(paddingValues = paddingValues,
                albums = result._datas,
                viewModel = viewModel)
        }
        is RequestStore.Failure -> {
            //todo error message display
        }
        is RequestStore.Empty -> {
            //todo wait for login
        }
    }
}