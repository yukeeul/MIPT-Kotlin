package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Article (val id: Int, var contents: String, val creationTime: String, var changeTime: String )