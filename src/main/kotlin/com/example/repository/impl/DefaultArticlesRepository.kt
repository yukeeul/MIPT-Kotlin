package com.example.repository.impl

import com.example.model.Article
import kotlin.collections.HashMap
import com.example.repository.ArticlesRepository
import java.time.Instant
import kotlin.random.Random

class DefaultArticlesRepository: ArticlesRepository {

    private val articles: HashMap<Int, Article> = HashMap<Int, Article> ()

    override fun getAll(): Collection<Article> {
        return articles.values.toList()
    }

    override fun hasId(id: Int): Boolean {
        return articles.containsKey(id)
    }

    override fun getById(id: Int): Article? {
        return articles[id]
    }

    override fun dropId(id: Int) {
        articles.remove(id)
    }

    override fun updateById(id: Int, newText: String) {
        articles[id]?.contents = newText
        articles[id]?.changeTime = Instant.now().toString()

    }

    override fun createArticle(articleText: String): Article {
        val curTime = Instant.now().toString()
        val newId = Random.nextInt()
        val createdArticle = Article(
            id = newId,
            contents = articleText,
            creationTime = curTime,
            changeTime = curTime,
        )
        articles[newId] = createdArticle
        return createdArticle
    }
}