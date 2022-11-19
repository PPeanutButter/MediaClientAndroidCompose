package com.peanut.nas.compose.gson

import androidx.annotation.Keep

@Keep
data class TMDB(
    private val title: String, // "She-Hulk: Attorney at Law(2022)"
    private val certification: String, // "TV-14"
    private val genres: String, // "喜剧, Sci-Fi & Fantasy"
    private val runtime: String, // "38m"
    private val tagline: String, // "You'll like her when she's angry."
    private val overview: String, // "珍妮·沃尔特斯是一位专门处理超能人类法律案件的律师，她不仅要忙于30多岁的单身生活，还要面对自己就成为身高两米的绿皮浩克的事实。"
    private val user_score_chart: Int, // 68
    private val url: String // "http://119.8.96.115:8081/panrunqiu/https://www.themoviedb.org/tv/92783"
) {
    val displayTitle: String get() = this.title
    val infoDescription: String get() = "${this.certification} ${this.genres} • ${this.runtime}"
    val displayTagDescription: String get() = "“${this.tagline}”"
    val userScoreRating: Int get() = this.user_score_chart
    var _albumPath: String = ""

    override fun toString(): String {
        return this.title
    }

    companion object {
        val Example = TMDB(
            title = "She-Hulk: Attorney at Law(2022)",
            certification = "TV-14",
            genres = "喜剧, Sci-Fi & Fantasy",
            runtime = "38m",
            tagline = "You'll like her when she's angry.",
            overview = "珍妮·沃尔特斯是一位专门处理超能人类法律案件的律师，她不仅要忙于30多岁的单身生活，还要面对自己就成为身高两米的绿皮浩克的事实。",
            user_score_chart = 68,
            url = "http://119.8.96.115:8081/panrunqiu/https://www.themoviedb.org/tv/92783",
        )

        val Empty = TMDB(
            title = "",
            certification = "",
            genres = "",
            runtime = "",
            tagline = "",
            overview = "",
            user_score_chart = 0,
            url = "",
        )
    }
}
