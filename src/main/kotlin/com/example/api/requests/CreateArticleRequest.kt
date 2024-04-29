package com.example.api.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateArticleRequest(
    val articleText: String,
    val token: String
)