package com.peanut.nas.compose.ui.component

import android.app.Activity
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.WindowCompat
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.peanut.nas.compose.data.Configuration
import com.peanut.nas.compose.gson.TMDB
import com.peanut.nas.compose.model.EpisodeViewModel.Companion.calculateColorLightValue
import com.peanut.nas.compose.utils.SettingManager
import kotlinx.coroutines.launch

@Composable
fun EpisodePost(tmdb: TMDB, configuration: Configuration) {
    Surface(
        Modifier
            .fillMaxWidth()
            .height(230.dp)
    ) {
        val context = LocalContext.current
        val view = LocalView.current
        val viewScope = rememberCoroutineScope()
        var vibrantBody by remember { mutableStateOf(android.graphics.Color.parseColor("#7367EF")) }
        var textColor by remember { mutableStateOf(android.graphics.Color.BLACK) }

        LaunchedEffect(key1 = textColor) {
            WindowCompat.getInsetsController((view.context as Activity).window, view).isAppearanceLightStatusBars = textColor == android.graphics.Color.BLACK
        }

        AsyncImage(
            model = "${configuration.serverIp}/getFile/get_album_post?path=/${Uri.encode(tmdb._albumPath)}/.post&token=${SettingManager.token}", contentDescription = null, modifier = Modifier
                .fillMaxSize()
                .background(Color(vibrantBody)), contentScale = ContentScale.Crop, alpha = 0.15f
        )
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp.plus(24.dp), bottom = 16.dp)) {
            AsyncImage(model = ImageRequest.Builder(context)
                .data("${configuration.serverIp}/getCover?cover=${Uri.encode(tmdb._albumPath)}&token=${SettingManager.token}")
                .allowHardware(false).build(), contentDescription = null, modifier = Modifier
                .wrapContentWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable { /* todo download attachments */ },
                onSuccess = {
                    Palette.Builder(it.result.drawable.toBitmap()).generate { palette ->
                        println(palette?.dominantSwatch?.rgb)
                        viewScope.launch {
                            vibrantBody = (palette?.dominantSwatch?.rgb)
                                ?: android.graphics.Color.parseColor("#7367EF")
                            val light = calculateColorLightValue(vibrantBody)
                            textColor = if (light < 0.4) android.graphics.Color.WHITE else android.graphics.Color.BLACK
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.padding(end = 16.dp))
            Column {
                Text(text = tmdb.displayTitle, fontSize = 24.sp, fontWeight = FontWeight.Bold, maxLines = 1, color = Color(textColor))
                Text(text = tmdb.infoDescription, fontSize = 16.sp, maxLines = 2, color = Color(textColor))
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RatingBar(progress = tmdb.userScoreRating)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "用户评价", fontSize = 16.sp, fontStyle = FontStyle.Italic, maxLines = 1, overflow = TextOverflow.Ellipsis, color = Color(textColor))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = tmdb.displayTagDescription, fontSize = 16.sp, fontStyle = FontStyle.Italic, maxLines = 1, overflow = TextOverflow.Ellipsis, color = Color(textColor))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun EpisodePostPreview() {
    EpisodePost(TMDB.Example, Configuration(host = "192.168.211.208", "", ""))
}