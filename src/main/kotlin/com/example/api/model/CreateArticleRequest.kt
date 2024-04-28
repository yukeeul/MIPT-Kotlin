package com.example.api.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateArticleRequest(
    val articleText: String
)