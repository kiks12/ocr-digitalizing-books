package com.example.ocr_digital.api

import com.example.ocr_digital.users.UserResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

data class UserResponse (
    val message : String,
)

data class GetUsersResponse (
    val result: List<UserResult>
)

interface UsersAPI {

    @GET("/users")
    suspend fun getUsers() : Response<GetUsersResponse>

    @Headers("Content-Type: application/json")
    @POST("/users/create")
    suspend fun createUser(@Body body: Map<String, String>) : Response<UserResponse>

    @Headers("Content-Type: application/json")
    @POST("/edit")
    suspend fun editUserProfile(@Body body: Map<String, String>) : Response<UserResponse>

    @DELETE("/delete/{uid}")
    suspend fun deleteUser(@Path("uid") uid: String) : Response<UserResponse>

    @Headers("Content-Type: application/json")
    @PATCH("/disable")
    suspend fun disableUserAccount(@Body body : Map<String, String>) : Response<UserResponse>

    @Headers("Content-Type: application/json")
    @PATCH("/enable")
    suspend fun enableUserAccount(@Body body : Map<String, String>) : Response<UserResponse>
}