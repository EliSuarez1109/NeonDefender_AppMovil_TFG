package com.example.proyectotfg.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente Retrofit configurado para la comunicación con la API Gateway de AWS.
 * Centraliza la configuración de red y la creación del servicio TowerDefenseApi.
 */
object RetrofitClient {
    // URL base de la API Gateway
    private const val BASE_URL = "https://zne23sxln0.execute-api.us-east-1.amazonaws.com/"

    // Interceptor para depurar las peticiones y respuestas en el Logcat
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente OkHttp que incluye el interceptor de registro
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // Instancia perezosa del servicio de la API
    val api: TowerDefenseApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(TowerDefenseApi::class.java)
    }
}