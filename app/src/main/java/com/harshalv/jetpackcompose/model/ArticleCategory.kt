package com.harshalv.jetpackcompose.model

import com.harshalv.jetpackcompose.model.ArticleCategory.BUSINESS
import com.harshalv.jetpackcompose.model.ArticleCategory.ENTERTAINMENT
import com.harshalv.jetpackcompose.model.ArticleCategory.GENERAL
import com.harshalv.jetpackcompose.model.ArticleCategory.HEALTH
import com.harshalv.jetpackcompose.model.ArticleCategory.SCIENCE
import com.harshalv.jetpackcompose.model.ArticleCategory.SPORTS
import com.harshalv.jetpackcompose.model.ArticleCategory.TECHNOLOGY
import com.harshalv.jetpackcompose.model.ArticleCategory.values

enum class ArticleCategory(val categoryName: String) {
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
    val map = values().associateBy(ArticleCategory::categoryName)
    return map[category]
}