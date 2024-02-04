package com.example.articles.data

import com.google.gson.annotations.SerializedName


data class NewsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("articles") val articles: List<ListItem>
)

data class ListItem(
    @SerializedName("source") val source: Source,
    @SerializedName("author") val author: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("publishedAt") val publishedAt: String?,
    @SerializedName("content") val content: String?

)

data class Source(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?
)