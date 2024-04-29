package com.example.repository.impl

import com.example.database.DatabaseSchemas.Users
import com.example.database.getConnection
import com.example.model.User
import com.example.repository.UsersRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant


class DatabaseUsersRepository: UsersRepository {
    private val database: Database = getConnection()

    override fun getByLogin(login: String): User?{
        val queryResult = transaction {
            Users.selectAll().where { Users.login eq login }
                .map { User(it[Users.login], it[Users.password], it[Users.name], it[Users.creationDate]) }
                .singleOrNull()
        }
        return queryResult
    }

    override fun hasLogin(login: String): Boolean{
        val queryResult = transaction {
            Users.selectAll().where { Users.login eq login }
                .map { User(it[Users.login], it[Users.password], it[Users.name], it[Users.creationDate]) }
                .singleOrNull()
        }
        return queryResult != null
    }

    override fun createUser(login: String, password: String, name: String): User? {
        if (hasLogin(login)) {
            return null
        }
        val curTime = Instant.now().toString()
        val createdUser = User(
            login = login,
            password = password,
            name = name,
            creationDate = curTime,
        )

        transaction {
            Users.insert {
                it[Users.login] = login
                it[Users.password] = password
                it[Users.name] = name
                it[Users.creationDate] = curTime
            }
        }
        return createdUser
    }


}