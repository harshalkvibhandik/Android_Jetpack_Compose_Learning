package com.harshalv.jetpackcompose.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.harshalv.jetpackcompose.MainApp
import com.harshalv.jetpackcompose.data.models.TopNewsResponse
import com.harshalv.jetpackcompose.model.ArticleCategory
import com.harshalv.jetpackcompose.model.getArticleCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = getApplication<MainApp>().repository

    private val _newsResponse = MutableStateFlow(TopNewsResponse())
    val newsResponse: StateFlow<TopNewsResponse>
        get() = _newsResponse

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getTopArticles() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _newsResponse.value = repository.getArticles()
        }
        _isLoading.value = false
    }

    private val _selectedCategory: MutableStateFlow<ArticleCategory?> = MutableStateFlow(null)
    val selectedCategory: StateFlow<ArticleCategory?>
        get() = _selectedCategory

    private val _getArticleByCategory =
        MutableStateFlow(TopNewsResponse())
    val getArticleByCategory: StateFlow<TopNewsResponse>
        get() = _getArticleByCategory

    fun getArticlesByCategory(category: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _getArticleByCategory.value = repository.getArticleByCategory(category = category)
        }
        _isLoading.value = false
    }

    fun onSelectedCategoryChanged(category: String) {
        val newCategory = getArticleCategory(category = category)
        _selectedCategory.value = newCategory
    }


    // Step 4: create a holder for sourceName with setter and getter for article by sources
    //start
    val sourceName = MutableStateFlow("engadget")

    private val _getArticleBySource = MutableStateFlow(TopNewsResponse())
    val getArticleBySource: StateFlow<TopNewsResponse>
        get() = _getArticleBySource
    //end

    // Step 5: create a query string to keep track of search word then a setter and getter for searched articles
    //start
    val query = MutableStateFlow("")

    private val _searchedNewsResponse =
        MutableStateFlow(TopNewsResponse())
    val searchedNewsResponse: StateFlow<TopNewsResponse>
        get() = _searchedNewsResponse
    //end

    // Step 6: create a method to launch getArticlesBySource and set response to its setter
    fun getArticleBySource() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _getArticleBySource.value = repository.getArticlesBySource(sourceName.value)
        }
        _isLoading.value = true
    }

    /**Todo 7: create a method to launch getSearchedArticle for @param [query]
     *  and set response to its setter
     */

    fun getSearchedArticles(query: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _searchedNewsResponse.value = repository.getSearchedArticles(query)
        }
        _isLoading.value = true
    }
}