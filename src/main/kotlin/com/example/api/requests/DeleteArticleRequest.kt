package com.example.api.requests

import kotlinx.serialization.Serializable

@Serializable
data class DeleteArticleRequest(
    val id: Int,
    val token: String
)