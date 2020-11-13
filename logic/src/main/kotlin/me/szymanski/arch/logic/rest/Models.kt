package me.szymanski.arch.logic.rest

data class RestConfig(val baseUrl: String, val defaultUser: String, val limit: Int)

data class Repository(val id: String, val name: String, val description: String)

data class RepositoryDetails(val id: String, val name: String, val description: String)
