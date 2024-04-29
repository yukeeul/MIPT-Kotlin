package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: Int,
    var contents: String,
    val creationDate: String,
    var changeDate: String,
    val author: String
)