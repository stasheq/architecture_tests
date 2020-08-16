package me.szymanski.logic.rest

data class RestConfig(val baseUrl: String, val user: String, val limit: Int)

data class Repository(val id: String, val name: String, val description: String)