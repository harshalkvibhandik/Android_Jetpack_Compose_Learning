package com.harshalv.jetpackcompose.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.harshalv.jetpackcompose.BottomMenuScreen
import com.harshalv.jetpackcompose.components.BottomMenu
import com.harshalv.jetpackcompose.model.Articles
import com.harshalv.jetpackcompose.ui.screen.Categories
import com.harshalv.jetpackcompose.ui.screen.DetailScreen
import com.harshalv.jetpackcompose.ui.screen.Sources
import com.harshalv.jetpackcompose.ui.screen.TopNews

@Composable
fun NewsApp() {
    val scrollState = rememberScrollState()
    val navController = rememberNavController()
    MainScreen(navController = navController,scrollState)
}

@Composable
fun MainScreen(navController: NavHostController,scrollState: ScrollState) {
    Scaffold(bottomBar ={
        BottomMenu(navController = navController)
    }) {
        Navigation(navController =navController , scrollState =scrollState,paddingValues = it)
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun Navigation(navController:NavHostController, scrollState: ScrollState, newsManager: NewsManager= NewsManager(), paddingValues: PaddingValues) {
    val articles = mutableListOf<Articles>()
            articles.addAll(newsManager.newsResponse.value.articles?: listOf(Articles()))
    NavHost(navController = navController, startDestination =BottomMenuScreen.TopNews.route,modifier = Modifier.padding(paddingValues)) {
        bottomNavigation(navController = navController,articles,newsManager)
        composable("Detail/{index}",
            arguments = listOf(
                navArgument("index") { type = NavType.IntType }
            )) { navBackStackEntry ->
            val index = navBackStackEntry.arguments?.getInt("index")
            if (index != null) {
                if (newsManager.query.value.isNotEmpty()) {
                    articles.clear()
                    articles.addAll(newsManager.searchedNewsResponse.value.articles?: listOf(Articles()))
                }else{
                    articles.clear()
                    articles.addAll(newsManager.newsResponse.value.articles?: listOf(Articles()))
                }

                DetailScreen(articles = articles[index], scrollState, navController)
            }
        }
    }
}

fun NavGraphBuilder.bottomNavigation(navController: NavController,articles: List<Articles>,newsManager: NewsManager) {
   composable(BottomMenuScreen.TopNews.route) {
        TopNews(navController = navController,articles,
            newsManager=newsManager,
            query = newsManager.query,

        )
    }
    composable(BottomMenuScreen.Categories.route) {
        newsManager.getArticlesByCategory("business")
        Categories(newsManager=newsManager,onFetchCategory = {newsManager.onSelectedCategoryChanged(it)
            newsManager.getArticlesByCategory(it)})
    }
    composable(BottomMenuScreen.Sources.route) {
        Sources()
    }
}