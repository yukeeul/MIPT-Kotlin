package com.example.api

import com.example.api.requests.*
import com.example.api.responses.AuthResponse
import com.example.repository.ArticlesRepository
import com.example.repository.UsersRepository
import com.example.services.impl.DefaultJWTService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

suspend fun handleRegistration(call: ApplicationCall, values: ArticlesRepository, users: UsersRepository) {
    val request = call.receive<RegistrationRequest>()
    val result = users.createUser(request.login, request.password, request.name)
    result ?: call.respond(HttpStatusCode.Conflict)
    call.respond(HttpStatusCode.OK)
}

suspend fun handleLogin(call: ApplicationCall, values: ArticlesRepository, users: UsersRepository) {
    val tokenService = DefaultJWTService()
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

suspend fun handleRoot(call: ApplicationCall, values: ArticlesRepository, users: UsersRepository) {
    call.respondText("Hello, world!")
}

suspend fun handleGetAll(call: ApplicationCall, values: ArticlesRepository, users: UsersRepository) {
    val comments = values.getAll()
    call.respond(comments)
}

suspend fun handleGet(call: ApplicationCall, values: ArticlesRepository, users: UsersRepository) {
    var id = call.parameters["id"]?.toInt()
    if (id == null) {
        call.respond(HttpStatusCode.NotFound)
    }
    id = id!!
    if (values.hasId(id)) {
        call.respond(values.getById(id)!!)
    } else {
        call.respond(HttpStatusCode.NotFound)
    }
}

suspend fun handleCreate(call: ApplicationCall, values: ArticlesRepository, users: UsersRepository) {
    val tokenService = DefaultJWTService()
    val request = call.receive<CreateArticleRequest>()
    if (request.articleText.length > 500) {
        call.respond(HttpStatusCode.PreconditionFailed)
    }
    val username = tokenService.extractUsername(request.token)
    username ?: call.respond(HttpStatusCode.BadRequest)
    val createdArticle = values.createArticle(request.articleText, username!!)
    call.respond(createdArticle)
}

suspend fun handleUpdate(call: ApplicationCall, values: ArticlesRepository, users: UsersRepository) {
    val tokenService = DefaultJWTService()
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

suspend fun handleDelete(call: ApplicationCall, values: ArticlesRepository, users: UsersRepository) {
    val tokenService = DefaultJWTService()
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