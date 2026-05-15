package com.example.proyectotfg.network

import com.example.proyectotfg.model.LoginRequest
import com.example.proyectotfg.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TowerDefenseApi {
    @POST("login_android")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}