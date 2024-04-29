package com.example.api.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateArticleRequest(
    val id: Int,
    val articleText: String,
    val token: String,
)