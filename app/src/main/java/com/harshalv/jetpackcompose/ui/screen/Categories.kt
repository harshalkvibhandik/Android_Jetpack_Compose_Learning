package com.harshalv.jetpackcompose.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.harshalv.jetpackcompose.MockData
import com.harshalv.jetpackcompose.MockData.getTimeAgo
import com.harshalv.jetpackcompose.R
import com.harshalv.jetpackcompose.data.getAllArticleCategory
import com.harshalv.jetpackcompose.model.Articles
import com.harshalv.jetpackcompose.ui.NewsManager

@Composable
fun Categories(onFetchCategory: (String) -> Unit, newsManager: NewsManager) {
    val tabsItems = getAllArticleCategory()
    Column {
        LazyRow() {
            items(tabsItems.size) {
                val category = tabsItems[it]
                CategoryTab(
                    category = category.category, onFetchCategory = onFetchCategory, isSelected =
                    newsManager.selectedCategory.value == category
                )
            }
        }
        PagerContent(
            articles = newsManager.getArticleByCategory.value.articles ?: listOf(Articles())
        )
    }
}

@Composable
fun CategoryTab(
    category: String,
    isSelected: Boolean = false,
    onFetchCategory: (String) -> Unit
) {
    val background =
        if (isSelected) colorResource(id = R.color.purple_200) else colorResource(id = R.color.purple_700)
    Surface(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 16.dp)
            .clickable {
                onFetchCategory(category)
            },
        shape = MaterialTheme.shapes.small,
        color = background
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.body2,
            color = Color.White,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
        )

    }
}

@Composable
fun PagerContent(articles: List<Articles>, modifier: Modifier = Modifier.padding(8.dp)) {
    LazyColumn {
        items(articles) { article ->
            Card(
                modifier,
                border = BorderStroke(2.dp, color = colorResource(id = R.color.purple_500))
            ) {
                Row(modifier.fillMaxWidth()) {
                    com.skydoves.landscapist.coil.CoilImage(
                        imageModel = article.urlToImage,
                        modifier = Modifier.size(100.dp)
                    )
                    Column(modifier) {
                        Text(text = article.title ?: "Not Available", fontWeight = FontWeight.Bold)
                        Row {
                            Text(text = article.author ?: "Not Available")
                            Text(
                                text = MockData.stringToDate(
                                    article.publishedAt ?: "2021-11-10T14:25:20Z"
                                ).getTimeAgo()
                            )
                        }
                    }
                }
            }
        }
    }
}