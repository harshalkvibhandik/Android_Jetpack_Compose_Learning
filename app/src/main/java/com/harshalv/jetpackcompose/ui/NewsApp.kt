package com.harshalv.jetpackcompose.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.harshalv.jetpackcompose.BottomMenuScreen
import com.harshalv.jetpackcompose.components.BottomMenu
import com.harshalv.jetpackcompose.data.models.TopNewsArticle
import com.harshalv.jetpackcompose.ui.screen.Categories
import com.harshalv.jetpackcompose.ui.screen.DetailScreen
import com.harshalv.jetpackcompose.ui.screen.Sources
import com.harshalv.jetpackcompose.ui.screen.TopNews

@Composable
fun NewsApp(mainViewModel: MainViewModel) {
    val scrollState = rememberScrollState()
    val navController = rememberNavController()

    MainScreen(navController = navController, scrollState, mainViewModel)
}

@Composable
fun MainScreen(
    navController: NavHostController,
    scrollState: ScrollState,
    mainViewModel: MainViewModel
) {
    Scaffold(bottomBar = {
        BottomMenu(navController = navController)
    }) {
        Navigation(
            navController = navController,
            scrollState = scrollState,
            paddingValues = it,
            viewModel = mainViewModel
        )
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
    scrollState: ScrollState,
    paddingValues: PaddingValues,
    viewModel: MainViewModel
) {
    val articles = mutableListOf(TopNewsArticle())
    val topArticles = viewModel.newsResponse.collectAsState().value.articles
    articles.addAll(topArticles ?: listOf())
    NavHost(
        navController = navController,
        startDestination = BottomMenuScreen.TopNews.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        // Step 22 create a queryState and set query value from viewmodel,pass in querystate into bottomNavigation
        val queryState =
            mutableStateOf(viewModel.query.value)
        bottomNavigation(
            navController = navController,
            articles,
            viewModel = viewModel,
            query = queryState
        )
        composable("Detail/{index}",
            arguments = listOf(
                navArgument("index") { type = NavType.IntType }
            )) { navBackStackEntry ->
            val index = navBackStackEntry.arguments?.getInt("index")
            index?.let {
                if (queryState.value != "") {
                    articles.clear()
                    // Step 21 collect searchedNewsResponse from viewModel
                    articles.addAll(
                        viewModel.searchedNewsResponse.collectAsState().value.articles ?: listOf()
                    )
                } else {
                    articles.clear()
                    articles.addAll(topArticles ?: listOf())
                }
                val article = articles[index]
                DetailScreen(article, scrollState, navController)
            }
        }
    }
}

// Step 19:create a query variable
fun NavGraphBuilder.bottomNavigation(
    navController: NavController, articles: List<TopNewsArticle>, query: MutableState<String>,
    viewModel: MainViewModel
) {
    composable(BottomMenuScreen.TopNews.route) {

        // Step 20: replace newsManager with viewModel and pass in a query parameter
        TopNews(navController = navController, articles, query, viewModel = viewModel)
    }
    composable(BottomMenuScreen.Categories.route) {
        viewModel.getArticlesByCategory("business")
        viewModel.onSelectedCategoryChanged("business")
        Categories(viewModel = viewModel, onFetchCategory = {
            viewModel.onSelectedCategoryChanged(it)
            viewModel.getArticlesByCategory(it)
        })
    }
    composable(BottomMenuScreen.Sources.route) {
        // Step 13: pass in viewmodel as argument
        Sources(viewModel = viewModel)
    }
}