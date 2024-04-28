package com.example.api.model

import kotlinx.serialization.Serializable

@Serializable
data class DeleteArticleRequest(
    val id: Int,
)