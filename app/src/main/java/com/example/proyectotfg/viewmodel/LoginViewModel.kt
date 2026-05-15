package com.example.proyectotfg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectotfg.model.*
import com.example.proyectotfg.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona la lógica de autenticación del usuario.
 */
class LoginViewModel : ViewModel() {
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _loginResponse = MutableStateFlow<LoginResponse?>(null)
    val loginResponse: StateFlow<LoginResponse?> = _loginResponse.asStateFlow()

    fun onUsernameChange(nuevoNombre: String) {
        _username.value = nuevoNombre
        _error.value = null
    }

    fun onPasswordChange(nuevaContrasena: String) {
        _password.value = nuevaContrasena
        _error.value = null
    }

    fun login() {
        if (_username.value.isBlank() || _password.value.isBlank()) {
            _error.value = "Los campos no pueden estar vacíos"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val respuesta = RetrofitClient.api.login(LoginRequest(_username.value, _password.value))
                if (respuesta.isSuccessful && respuesta.body() != null) {
                    _loginResponse.value = respuesta.body()
                } else {
                    val mensajeError = respuesta.errorBody()?.string() ?: "Error de autenticación"
                    _error.value = if (respuesta.code() == 401) "Usuario o contraseña incorrectos" else mensajeError
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

}