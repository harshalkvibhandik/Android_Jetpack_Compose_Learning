package com.harshalv.jetpackcompose.data

import com.harshalv.jetpackcompose.data.network.NewsManager

class Repository(private val manager: NewsManager) {

    suspend fun getArticles() = manager.getArticles("us")

    suspend fun getArticleByCategory(category: String) = manager.getArticlesByCategory(category)

    suspend fun getArticlesBySource(source: String) = manager.getArticleBySource(source = source)

    suspend fun getSearchedArticles(query: String) = manager.getSearchedArticles(query = query)

}