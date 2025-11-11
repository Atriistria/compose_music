package com.atri.composemusic.core.data.repository

import com.atri.composemusic.core.network.retrofit.UserApi
import javax.inject.Inject

fun interface LoginRepository {
    suspend fun login(username: String, password: String): Result<String>
}

class LoginRepositoryImpl @Inject constructor(
    private val api: UserApi
): LoginRepository {
    override suspend fun login(username: String, password: String): Result<String> {
        return try {
            val response = api.login(username, password)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception(response.errorBody().toString()))
            }
        }catch (e: Exception) {
            Result.failure(e)
        }
    }
}