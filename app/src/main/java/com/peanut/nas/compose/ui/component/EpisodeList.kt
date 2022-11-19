package com.peanut.nas.compose.ui.component

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.peanut.nas.compose.gson.Episode
import com.peanut.nas.compose.model.EpisodeViewModel
import com.peanut.nas.compose.utils.SettingManager

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EpisodeList(episodes: MutableList<Episode>,
                viewModel: EpisodeViewModel){
    val serverIp = viewModel.configuration.collectAsState()
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.padding(start = 4.dp, end = 4.dp)) {
        items(items = episodes, key = { item: Episode -> item.hashKey }){
            EpisodeCard(episode = it){ path ->
                return@EpisodeCard "${serverIp.value.serverIp}/getVideoPreview?" +
                        "path=${Uri.encode(path)}" +
                        "&token=${SettingManager.token}"
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeCard(episode: Episode, resolveUrl:(albumRelativePath:String)->String) {
    OutlinedCard(
        onClick = { }, modifier = Modifier.padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        var ratio by remember { mutableStateOf(1280f/720f) }
        AsyncImage(
            model = resolveUrl(episode.absolutePath), contentDescription = null, modifier = Modifier
                .clip(CardDefaults.outlinedShape)
                .fillMaxWidth()
                .aspectRatio(ratio),
            onSuccess = {
                it.result.drawable.let { d ->
                    ratio = d.intrinsicWidth.toFloat() / d.intrinsicHeight.toFloat()
                }
            },
            contentScale = ContentScale.Crop
        )
        EpisodeCardContent(episode = episode)
    }
}

@Composable
fun EpisodeCardContent(episode: Episode){
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = episode.episodeDisplayName, fontSize = 14.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            //会先绘制unweighted，再分配剩余空间给weighted组件
            Text(text = episode.bitrateDescription + " " + episode.downloadDate, fontSize = 12.sp, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PlayAction {

                }
                DownloadAction {

                }
                BookmarkAction {

                }
            }
        }
    }
}

@Preview
@Composable
fun EpisodeCardPreview(){
//    EpisodeCard(episode = Episode.Example){
//        return@EpisodeCard "http://192.168.211.208/getVideoPreview?" +
//                "path=${Uri.encode(it)}" +
//                "&token=${SettingManager.token}"
//    }
    val episode = Episode.Example
    EpisodeCardContent(episode = episode)
}