package com.priyanshumaurya8868.aurictask.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface RandomAPI {

    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @GET("users/{userID}/transactions/")
    suspend fun getTransactions(
        @Path("userID")
        userID: Int): Response<List<Transaction>>

}