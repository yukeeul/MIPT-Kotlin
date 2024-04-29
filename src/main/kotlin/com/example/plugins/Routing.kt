package com.example.plugins

import com.example.api.requests.*
import com.example.api.responses.AuthResponse
import com.example.database.hasConnection
import com.example.repository.ArticlesRepository
import com.example.repository.UsersRepository
import com.example.repository.impl.DatabaseArticlesRepository
import com.example.repository.impl.DatabaseUsersRepository
import com.example.repository.impl.DefaultArticlesRepository
import com.example.repository.impl.DefaultUsersRepository
import com.example.services.impl.DefaultJWTService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
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
    val tokenService = DefaultJWTService()

    routing {
        post("/registration") {
            val request = call.receive<RegistrationRequest>()
            val result = users.createUser(request.login, request.password, request.name)
            result ?: call.respond(HttpStatusCode.Conflict)
            call.respond(HttpStatusCode.OK)
        }

        post("/login") {
            val request = call.receive<LoginRequest>()
            if (!users.hasLogin(request.login)) {
                call.respond(HttpStatusCode.Unauthorized)
            }
            val correctPassword = users.getByLogin(request.login)?.password!!
            if (correctPassword == request.password) {
                val token = tokenService.createAccessToken(request.login)
                call.respond(AuthResponse(token))
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }

        }
        get("/") {
            call.respondText("Hello, world!")
        }

        get("/articles/all") {
            val comments = values.getAll()
            call.respond(comments)
        }

        get("/articles/{id}") {
            var id = call.parameters["id"]?.toInt()
            if (id == null) {
                call.respond(HttpStatusCode.NotFound)
            }
            id = id!!
            if (values.hasId(id)) {
                call.respond(values.getById(id)!!)
            }
            else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        authenticate("auth-jwt") {
            put("/articles/create") {
                val request = call.receive<CreateArticleRequest>()
                if (request.articleText.length > 500) {
                    call.respond(HttpStatusCode.PreconditionFailed)
                }
                val username = tokenService.extractUsername(request.token)
                username ?: call.respond(HttpStatusCode.BadRequest)
                val createdArticle = values.createArticle(request.articleText, username!!)
                call.respond(createdArticle)
            }
            patch("/articles/update") {
                val request = call.receive<UpdateArticleRequest>()
                if (!values.hasId(request.id)) {
                    call.respond(HttpStatusCode.NotFound)
                }
                val username = tokenService.extractUsername(request.token)
                username ?: call.respond(HttpStatusCode.Unauthorized)
                if (username != values.getById(request.id)?.author) {
                    call.respond(HttpStatusCode.Forbidden)
                }
                if (request.articleText.length > 500) {
                    call.respond(HttpStatusCode.PreconditionFailed)
                }

                values.updateById(request.id, request.articleText)
                call.respond(values.getById(request.id)!!)
            }
            delete("/articles/delete") {
                val request = call.receive<DeleteArticleRequest>()
                if (!values.hasId(request.id)) {
                    call.respond(HttpStatusCode.NotFound)
                }
                val username = tokenService.extractUsername(request.token)
                username ?: call.respond(HttpStatusCode.Unauthorized)
                if (username != values.getById(request.id)?.author) {
                    call.respond(HttpStatusCode.Forbidden)
                }
                values.dropId(request.id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}


