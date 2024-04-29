package com.example.plugins

import com.example.api.*
import com.example.database.hasConnection
import com.example.repository.*
import com.example.repository.impl.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*



fun Application.configureRouting() {
    val values: ArticlesRepository
    val users: UsersRepository
    if (hasConnection()) {
        values = DatabaseArticlesRepository()
        users = DatabaseUsersRepository()
    } else {
        values = DefaultArticlesRepository()
        users = DefaultUsersRepository()
    }
    values.createArticle("example", "unknown")

    routing {
        post("/registration") {
            handleRegistration(call, values, users)
        }

        post("/login") {
            handleLogin(call, values, users)
        }
        get("/") {
            handleRoot(call, values, users)
        }

        get("/articles/all") {
            handleGetAll(call, values, users)
        }

        get("/articles/{id}") {
            handleGet(call, values, users)
        }
        authenticate("auth-jwt") {
            put("/articles/create") {
                handleCreate(call, values, users)
            }
            patch("/articles/update") {
                handleUpdate(call, values, users)
            }
            delete("/articles/delete") {
                handleDelete(call, values, users)
            }
        }
    }
}


