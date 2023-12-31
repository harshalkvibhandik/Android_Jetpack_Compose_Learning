package com.harshalv.jetpackcompose.data.network

import com.harshalv.jetpackcompose.data.models.TopNewsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsManager(private val service: NewsService) {

    suspend fun getArticles(country: String): TopNewsResponse = withContext(Dispatchers.IO) {
        service.getTopArticles(country)
    }

    suspend fun getArticlesByCategory(category: String): TopNewsResponse =
        withContext(Dispatchers.IO) {
            service.getArticlesByCategories(category)
        }

    suspend fun getArticleBySource(source: String): TopNewsResponse = withContext(Dispatchers.IO) {
        service.getArticlesBySources(source)
    }

    suspend fun getSearchedArticles(query: String): TopNewsResponse = withContext(Dispatchers.IO) {
        service.searchArticles(query)
    }
}