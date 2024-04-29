package com.example.repository.impl

import com.example.database.DatabaseSchemas.Articles
import com.example.database.getConnection
import com.example.model.Article
import com.example.repository.ArticlesRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import kotlin.random.Random

class DatabaseArticlesRepository: ArticlesRepository {

    private val database: Database = getConnection()

    override fun getAll(): Collection<Article> {
        val queryResult = transaction {
            Articles.selectAll().map {
                    Article(
                        it[Articles.id],
                        it[Articles.contents],
                        it[Articles.creationDate],
                        it[Articles.changeDate],
                        it[Articles.author]
                    )
                }
        }
        return queryResult
    }

    override fun hasId(id: Int): Boolean {
        val queryResult = transaction {
            Articles.selectAll().where { Articles.id eq id }
                .map {
                    Article(
                        it[Articles.id],
                        it[Articles.contents],
                        it[Articles.creationDate],
                        it[Articles.changeDate],
                        it[Articles.author]
                    )
                }
                .singleOrNull()
        }
        return queryResult != null
    }

    override fun getById(id: Int): Article? {
        val queryResult = transaction {
            Articles.selectAll().where { Articles.id eq id }
                .map {
                    Article(
                        it[Articles.id],
                        it[Articles.contents],
                        it[Articles.creationDate],
                        it[Articles.changeDate],
                        it[Articles.author]
                    )
                }
                .singleOrNull()
        }
        return queryResult
    }

    override fun dropId(id: Int) {
        transaction {
            Articles.deleteWhere { Articles.id eq id }
        }
    }

    override fun updateById(id: Int, newText: String) {
        val curTime = Instant.now().toString()
        transaction {
            Articles.update({ Articles.id eq id }) {
                it[Articles.contents] = newText
                it[Articles.changeDate] = curTime
            }
        }

    }

    override fun createArticle(articleText: String, author: String): Article {
        val curTime = Instant.now().toString()
        val newId = Random.nextInt()
        val createdArticle = Article(
            id = newId,
            contents = articleText,
            creationDate = curTime,
            changeDate = curTime,
            author = author,
        )
        transaction {
            Articles.insert {
                it[Articles.id] = newId
                it[Articles.contents] = articleText
                it[Articles.creationDate] = curTime
                it[Articles.changeDate] = curTime
                it[Articles.author] = author
            }
        }
        return createdArticle
    }
}