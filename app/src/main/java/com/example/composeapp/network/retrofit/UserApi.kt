package com.example.composeapp.network.retrofit

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserApi {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(@Field("username")username: String,@Field("password") password: String): Response<String>

    @POST("register")
    suspend fun register(username: String, password: String)
}