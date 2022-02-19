package me.szymanski.arch.rest

data class RestConfig(
    val baseUrl: String,
    val defaultUser: String,
    val pageLimit: Int,
    val callTimeout: Int,
    val logEnabled: Boolean
)
