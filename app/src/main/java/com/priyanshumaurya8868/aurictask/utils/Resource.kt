package com.priyanshumaurya8868.aurictask.utils
sealed class Resource<T> (
    val  data : T? =null,
    val  msg : String? = null
){
    class Success<T>(data : T): Resource<T>(data)
    class Error<T>(data: T?=null,msg: String): Resource<T>(data, msg)
    class Loading<T>(data: T?=null) : Resource<T>(data)
}