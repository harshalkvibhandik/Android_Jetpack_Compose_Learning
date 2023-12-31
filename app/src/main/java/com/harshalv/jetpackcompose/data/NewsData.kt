package com.harshalv.jetpackcompose.data

import com.harshalv.jetpackcompose.R

data class NewsData(
    val id: Int,
    val image: Int = R.drawable.breaking_news,
    val author: String,
    val title: String,
    val description: String,
    val publishedAt: String
)