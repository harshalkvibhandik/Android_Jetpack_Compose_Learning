package com.harshalv.jetpackcompose.data

import com.harshalv.jetpackcompose.data.ArticleCategory.BUSINESS
import com.harshalv.jetpackcompose.data.ArticleCategory.ENTERTAINMENT
import com.harshalv.jetpackcompose.data.ArticleCategory.GENERAL
import com.harshalv.jetpackcompose.data.ArticleCategory.HEALTH
import com.harshalv.jetpackcompose.data.ArticleCategory.SCIENCE
import com.harshalv.jetpackcompose.data.ArticleCategory.SPORTS
import com.harshalv.jetpackcompose.data.ArticleCategory.TECHNOLOGY

enum class ArticleCategory(val category: String) {
    BUSINESS("business"),
    ENTERTAINMENT("entertainment"),
    GENERAL("general"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
    TECHNOLOGY("technology")
}

fun getAllArticleCategory(): List<ArticleCategory> {
    return listOf(BUSINESS, ENTERTAINMENT, GENERAL, HEALTH, SCIENCE, SPORTS, TECHNOLOGY)
}

fun getArticleCategory(category: String): ArticleCategory? {
    val map = ArticleCategory.values().associateBy(ArticleCategory::category)
    return map[category]
}