package com.peanut.nas.compose.ui.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.peanut.nas.compose.EpisodeActivity
import com.peanut.nas.compose.gson.Album
import com.peanut.nas.compose.utils.SettingManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumCard(album: Album, resolveUrl:(albumRelativePath:String)->String) {
    val context = LocalContext.current
    OutlinedCard(
        onClick = {
            context.startActivity(Intent(context, EpisodeActivity::class.java).putExtra("ALBUM", album.relativePath))
        }, modifier = Modifier.padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        AsyncImage(
            model = resolveUrl(album.relativePath), contentDescription = null, modifier = Modifier
                .clip(CardDefaults.outlinedShape)
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
        )
        Text(
            text = album.displayTitle, modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(), maxLines = 1, style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AlbumCardPreview() {
    AlbumCard(album = Album.Example){
        return@AlbumCard "http://192.168.211.208/getCover?" +
                "path=${Uri.encode(it)}" +
                "&token=${SettingManager.token}"
    }
}