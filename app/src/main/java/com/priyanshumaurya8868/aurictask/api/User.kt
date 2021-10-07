package com.priyanshumaurya8868.aurictask.api


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "avatar")
    val avatar: String,
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String
) : Serializable