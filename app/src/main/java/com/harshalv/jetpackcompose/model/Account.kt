package com.harshalv.jetpackcompose.model

//Todo 1:create an account data class
data class Account(
    val icon: Int? = null,
    val userName: String,
    val email: String,
    val unReadMails: Int
)