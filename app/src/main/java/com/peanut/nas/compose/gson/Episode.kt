package com.peanut.nas.compose.gson

import androidx.annotation.Keep

//
//import android.net.Uri
//import android.util.Base64
//import com.peanut.nas.compose.utils.SettingManager
//
//class Episode(episodePath: String,
//              val timeSeconds: Double,
//              val bitrate: String,
//              val date: String) {
//
//    val episodeName: String
//    val previewUrl: String
//    val timeLasts: String
//
//    fun String.encodeBased64(): String = Base64.encodeToString(this.toByteArray(), Base64.NO_WRAP)
//
//    init {
//        episodeName = episodePath.name()
//        previewUrl = "${SettingManager.serverIp}/getVideoPreview?path=${Uri.encode(episodePath)}" +
//                "&token=${SettingManager.token}"
//        timeLasts = timeSeconds.second2TimeDesc()
//    }
//    val desc get() = "$bitrate $date"
//

//
//    fun getRawLink(album: String):String{
//        return "${SettingManager.serverIp}/getFile2/${episodeName}?" +
//                "path=${("/$album/$episodeName").encodeBased64()}&" +
//                "token=${SettingManager.getValue("token", "")}"
//    }
//}

@Keep
data class Episode(
    private val name: String,// "NAS600/She-hulk Attorney At Law S01/She-Hulk.Attorney.at.Law.S01E02.HDR.2160p.WEB.h265-KOGi.mkv"
    private val mime_type: String,// "video/x-matroska"
    private val length: Long,// 2886719812
    private val lasts: Double,// 1669.312
    private val desc: String,// "Fri Oct  7 18:42:24 2022"
    private val bitrate: String,// "14Mbps"
    private val score: Int,// -1
    private val watched: String,// "watched"
    private val type: String,// "File"
    private val title: String,// ""
    private val bookmark_state: String// "bookmark_added"
) {
    val timeLastsDescription: String
        get() = this.lasts.second2TimeDesc()

    val bitrateDescription: String
        get() = if (this.bitrate != "") this.bitrate
        else this.length.getFileLengthDesc()

    val downloadDate: String get() = this.desc

    val episodeDisplayName: String
        get() = this.name.name()

    val hashKey: Int get() = this.name.hashCode()

    val absolutePath: String get() = this.name

    val keyInfo get() = String.format("%s%s",
        if (episodeDisplayName.indexOf("2160p", 0, true) != -1) "4K " else "",
        if (episodeDisplayName.indexOf("hdr", 0, true) != -1) "HDR" else "")

    companion object {
        val Example = Episode(
            name = "NAS600/She-hulk Attorney At Law S01/She-Hulk.Attorney.at.Law.S01E02.HDR.2160p.WEB.h265-KOGi.mkv",
            mime_type = "video/x-matroska",
            length = 2886719812,
            lasts = 1669.312,
            desc = "Fri Oct  7 18:42:24 2022",
            bitrate = "14Mbps",
            score = -1,
            watched = "watched",
            type = "File",
            title = "",
            bookmark_state = "bookmark_added"
        )
    }

    private fun Double.second2TimeDesc(): String {
        val temp = this.toInt()
        val hours = temp / 3600
        val minutes = temp % 3600 / 60
        val seconds = temp % 3600 % 60
        return when {
            hours > 0 -> "$hours:${"0".repeat(if (minutes < 10) 1 else 0)}$minutes:${"0".repeat(if (seconds < 10) 1 else 0)}$seconds"
            minutes > 0 -> "$minutes:${"0".repeat(if (seconds < 10) 1 else 0)}$seconds"
            else -> "0:$seconds"
        }
    }

    private fun Long.getFileLengthDesc(): String {
        return when {
            this.shr(30) >= 1.0 -> String.format("%.2f", this / 1024.0 / 1024.0 / 1024.0) + "GB"
            this.shr(20) >= 1.0 -> String.format("%.2f", this / 1024.0 / 1024.0) + "MB"
            this.shr(10) >= 1.0 -> String.format("%.2f", this / 1024.0) + "KB"
            else -> String.format("%.2f", this / 1.0) + "B"
        }
    }

    private fun String.name() = this.substring(this.lastIndexOf("/") + 1)

}
