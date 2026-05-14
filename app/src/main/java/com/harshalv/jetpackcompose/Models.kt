package com.harshalv.jetpackcompose

data class ApiResponse(val results: List<CharacterDto>)

data class CharacterDto(val id: Int, val name: String, val origin: OriginDto)

data class OriginDto(val name: String)

data class Character(val id: Int, val name: String, val originName: String)

fun CharacterDto.toDomain() = Character(id, name, origin.name)
