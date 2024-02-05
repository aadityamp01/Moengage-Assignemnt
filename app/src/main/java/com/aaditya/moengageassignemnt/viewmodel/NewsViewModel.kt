package com.aaditya.moengageassignemnt.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditya.moengageassignemnt.constants.APIConstant.Companion.BASE_URL
import com.aaditya.moengageassignemnt.model.Article
import com.aaditya.moengageassignemnt.model.Source
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class NewsViewModel : ViewModel() {
    private val _newsList = MutableLiveData<List<Article>?>()
    val newsList: LiveData<List<Article>?> get() = _newsList

    fun sortByOldToNew() {
        val sortedList = _newsList.value?.sortedBy { it.publishedAt }
        _newsList.postValue(sortedList)
    }

    fun sortByNewToOld() {
        val sortedList = _newsList.value?.sortedByDescending { it.publishedAt }
        _newsList.postValue(sortedList)
    }

    fun fetchNews() {
        viewModelScope.launch {
            try {
                val newsJson = withContext(Dispatchers.IO) {
                    makeApiCall(BASE_URL)
                }

                val articles = parseJson(newsJson)
                _newsList.postValue(articles)
            } catch (e: Exception) {
                // Handling the exception
                Log.e("NewsViewModel", "Error fetching news", e)
            }
        }
    }

    private fun makeApiCall(url: String): String {
        val connection = URL(url).openConnection() as HttpURLConnection

        try {
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                return connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                throw IOException("HTTP error code: ${connection.responseCode}")
            }
        } finally {
            connection.disconnect()
        }
    }

    private fun parseJson(jsonString: String): List<Article> {
        val articles = mutableListOf<Article>()

        try {
            val jsonObject = JSONObject(jsonString)
            val jsonArray = jsonObject.getJSONArray("articles")

            for (i in 0 until jsonArray.length()) {
                val articleJson = jsonArray.getJSONObject(i)

                val author = articleJson.optString("author", null)
                val content = articleJson.optString("content", null)
                val description = articleJson.optString("description", null)
                val publishedAt = articleJson.optString("publishedAt", null)

                val sourceJson = articleJson.getJSONObject("source")
                val source = Source(
                    sourceJson.getString("id"),
                    sourceJson.getString("name")
                )

                val title = articleJson.getString("title")
                val url = articleJson.getString("url")
                val urlToImage = articleJson.optString("urlToImage", null)

                val article = Article(author, content, description, publishedAt, source, title, url, urlToImage)
                //Log.e("NewsViewModel", "article - ${article}")

                articles.add(article)
            }
        } catch (e: JSONException) {
            // JSON parsing exception
            Log.e("NewsViewModel", "Error parsing JSON", e)
        }

        return articles
    }

}
