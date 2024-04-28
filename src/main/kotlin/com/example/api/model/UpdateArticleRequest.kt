package com.example.api.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateArticleRequest(
    val id: Int,
    val articleText: String
)