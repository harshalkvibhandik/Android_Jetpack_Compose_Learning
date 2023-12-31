package com.harshalv.jetpackcompose.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    val query by viewModel.query.collectAsState()
    // Step 11 collect loading and error state from viewModel
    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.isError.collectAsState()
    articles.addAll(topArticles ?: listOf())
    NavHost(
        navController = navController,
        startDestination = BottomMenuScreen.TopNews.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        val queryState =
            mutableStateOf(query)
        // Step 12 set to a mutable state
        val isLoading = mutableStateOf(loading)
        val isError = mutableStateOf(error)
        // Step 13: pass in the state values as argument yo bottomNavigation
        bottomNavigation(
            navController = navController, articles, viewModel = viewModel, query = queryState,
            isError = isError, isLoading = isLoading
        )
        composable("Detail/{index}",
            arguments = listOf(
                navArgument("index") { type = NavType.IntType }
            )) { navBackStackEntry ->
            val index = navBackStackEntry.arguments?.getInt("index")
            index?.let {
                if (queryState.value != "") {
                    articles.clear()
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

// Step 9: create the loading and error state parameter
fun NavGraphBuilder.bottomNavigation(
    navController: NavController, articles: List<TopNewsArticle>, query: MutableState<String>,
    viewModel: MainViewModel,
    isLoading: MutableState<Boolean>, isError: MutableState<Boolean>
) {

    composable(BottomMenuScreen.TopNews.route) {
        // Step 10: pass in the loading and error value as argument
        TopNews(
            navController = navController,
            articles,
            query,
            viewModel = viewModel,
            isLoading = isLoading,
            isError = isError
        )
    }
    composable(BottomMenuScreen.Categories.route) {
        viewModel.getArticlesByCategory("business")
        viewModel.onSelectedCategoryChanged("business")
        Categories(viewModel = viewModel, onFetchCategory = {
            viewModel.onSelectedCategoryChanged(it)
            viewModel.getArticlesByCategory(it)
        }, isError = isError, isLoading = isLoading)
    }
    composable(BottomMenuScreen.Sources.route) {
        // Step 16: pass in the loading and error state argument already in the bottom navigation paranthesis
        Sources(viewModel = viewModel, isLoading = isLoading, isError = isError)
    }
}