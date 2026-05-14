package com.harshalv.jetpackcompose

class CharacterRepository(private val api: ApiService) {
    suspend fun getCharacters(): List<Character> {
        return api.getCharacters().results.map { it.toDomain() }
    }
}
