package com.harshalv.jetpackcompose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CharacterScreen(viewModel: CharacterViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    when {
        state.isLoading -> CircularProgressIndicator()
        state.error != null -> Text("Error: ${state.error}")
        else -> CharacterList(state.characters)
    }
}

@Composable
fun CharacterList(list: List<Character>) {
    LazyColumn {
        items(list, key = { it.id }) {
            CharacterItem(it)
        }
    }
}

@Composable
fun CharacterItem(character: Character) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = character.name)
        Text(text = character.originName)
    }
}
