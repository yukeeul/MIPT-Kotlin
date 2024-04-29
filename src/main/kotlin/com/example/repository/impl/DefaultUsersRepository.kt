package com.example.repository.impl

import com.example.model.User
import kotlin.collections.HashMap
import com.example.repository.UsersRepository
import java.time.Instant

class DefaultUsersRepository: UsersRepository {

    private val users: HashMap<String, User> = HashMap<String, User> ()

    override fun getByLogin(login: String): User? {
        return users[login]
    }

    override fun hasLogin(login: String): Boolean{
        return users.containsKey(login)
    }

    override fun createUser(login: String, password: String, name: String): User? {
        if (users.containsKey(login)) {
            return null
        }
        val curTime = Instant.now().toString()
        val createdUser = User(
            login = login,
            password = password,
            name = name,
            creationDate = curTime,
        )
        users[login] = createdUser
        return createdUser
    }


}