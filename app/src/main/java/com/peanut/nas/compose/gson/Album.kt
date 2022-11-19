package com.peanut.nas.compose.gson

import androidx.annotation.Keep
import java.util.regex.Pattern

@Keep
data class Album (
    private val name: String,// "NAS600/Friends"
    private val length: Int,// 0
    private val lasts: Int,// -1
    private val bitrate: String,// ""
    private val desc: String,// "Fri Sep  2 14:09:23 2022"
    private val type: String,// "Directory"
    private val mime_type: String,// "application/octet-stream"
    private val watched: String,// "watched"
    private val score: Int,// 0
    private val title: String,// "老友记(1994)"
    private val bookmark_state: String// "bookmark_add"
){
    val displayTitle: String get() = this.title.getDisplayTitle()
    val relativePath: String get() = this.name
    val hashKey: Int get() = this.name.hashCode()

    companion object {
        val Example = Album(
            name = "NAS600/Friends",
            length = 0,
            lasts = -1,
            bitrate = "",
            desc = "Fri Sep  2 14:09:23 2022",
            type = "Directory",
            mime_type = "application/octet-stream",
            watched = "watched",
            score = 0,
            title = "老友记(1994)",
            bookmark_state = "bookmark_add"
        )
    }

    private fun String.getDisplayTitle(): String{
        Pattern.compile("(.*)\\(\\d{4}\\)", Pattern.MULTILINE).matcher(this).apply { this.find() }.also {
            return if (it.groupCount() == 1)
                it.group(1)?:""
            else this
        }
    }

    override fun toString(): String {
        return "Album(title=$displayTitle, path=${relativePath})"
    }
}
