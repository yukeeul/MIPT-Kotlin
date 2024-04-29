package com.example.repository

import com.example.model.Article

interface ArticlesRepository {

    fun getAll(): Collection<Article>

    fun hasId(id: Int): Boolean

    fun getById(id: Int): Article?

    fun dropId(id: Int)

    fun updateById(id: Int, newText: String)

    fun createArticle(articleText: String, author: String): Article
}