package com.priyanshumaurya8868.aurictask

import com.priyanshumaurya8868.aurictask.api.RandomAPI

class Repo(private val api : RandomAPI) {

    suspend fun getUsers() = api.getUsers()

    suspend fun getTransaction(userID : Int) = api.getTransactions(userID)
}