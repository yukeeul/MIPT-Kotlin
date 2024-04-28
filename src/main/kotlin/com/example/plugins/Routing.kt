package com.example.plugins

import com.example.api.model.CreateArticleRequest
import com.example.api.model.DeleteArticleRequest
import com.example.api.model.UpdateArticleRequest
import com.example.repository.ArticlesRepository
import com.example.repository.impl.DefaultArticlesRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    var values : ArticlesRepository = DefaultArticlesRepository ()
    values.createArticle("aba")

    routing {
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

        put("/articles/create") {
            val request = call.receive<CreateArticleRequest>()
            if (request.articleText.length > 500) {
                call.respond(HttpStatusCode.PreconditionFailed)
            }
            val createdArticle = values.createArticle(request.articleText)
            call.respond(createdArticle)
        }
        patch("/articles/update") {
            val request = call.receive<UpdateArticleRequest>()
            if (!values.hasId(request.id)) {
                call.respond(HttpStatusCode.NotFound)
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
            values.dropId(request.id)
            call.respond(HttpStatusCode.OK)
        }
    }
}
