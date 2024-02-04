package com.example.articles.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.articles.data.ListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.lifecycle.viewModelScope
import com.example.articles.data.Source
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ArticleViewModel : ViewModel() {
    private val _listData = MutableLiveData<List<ListItem>>()
    val listData: LiveData<List<ListItem>> get() = _listData

    fun fetchList() {
        viewModelScope.launch {
            _listData.value = fetchListData()
        }
    }
    fun updateListData(newList: List<ListItem>) {
        _listData.value = newList
    }
    suspend fun fetchListData(): List<ListItem> = withContext(Dispatchers.IO) {
        val url =
            URL("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json")
        val connection = url.openConnection() as HttpURLConnection

        try {
            val inputStream = connection.inputStream
            val jsonString = BufferedReader(InputStreamReader(inputStream)).readText()
            parseJson(jsonString)
        } finally {
            connection.disconnect()
        }
    }

    private fun parseJson(jsonString: String): List<ListItem> {
        val articlesList = mutableListOf<ListItem>()

        try {
            val jsonArray = JSONObject(jsonString).getJSONArray("articles")

            for (i in 0 until jsonArray.length()) {
                val articleObject = jsonArray.getJSONObject(i)
                val article = ListItem(
                    source = parseSource(articleObject.getJSONObject("source")),
                    author = articleObject.optString("author"),
                    title = articleObject.optString("title"),
                    url = articleObject.optString("url"),
                    publishedAt = articleObject.optString("publishedAt"),
                    content = articleObject.optString("content")
                )
                articlesList.add(article)
            }
        } catch (e: Exception) {
            // Handle JSON parsing exceptions
        }

        return articlesList
    }

    private fun parseSource(sourceObject: JSONObject): Source {
        return Source(
            id = sourceObject.optString("id"),
            name = sourceObject.optString("name")
        )
    }

    fun sortByDateAscending() {
        _listData.value = _listData.value?.sortedBy { it.publishedAt }
    }

    fun sortByDateDescending() {
        _listData.value = _listData.value?.sortedByDescending { it.publishedAt }
    }

    }




