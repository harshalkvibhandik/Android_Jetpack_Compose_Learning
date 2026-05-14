package com.harshalv.jetpackcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UiState(
    val isLoading: Boolean = false,
    val characters: List<Character> = emptyList(),
    val error: String? = null
)

class CharacterViewModel : ViewModel() {

    private val api = RetrofitInstance.api
    private val repository = CharacterRepository(api)

    private val _state = MutableStateFlow(UiState(isLoading = true))
    val state: StateFlow<UiState> = _state

    init {
        fetchCharacters()
    }

    private fun fetchCharacters() {
        viewModelScope.launch {
            try {
                val data = repository.getCharacters()
                _state.value = UiState(characters = data)
            } catch (e: Exception) {
                _state.value = UiState(error = e.message)
            }
        }
    }
}
