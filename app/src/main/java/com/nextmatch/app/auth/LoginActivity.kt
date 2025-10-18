package com.nextmatch.app.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nextmatch.app.R

/**
 * LoginActivity - Pantalla de inicio de sesión
 * Permite al usuario ingresar correo y contraseña
 * Ofrece opciones para registrarse u olvidar contraseña
 */
class LoginActivity : AppCompatActivity() {

    // Declaración de vistas
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var tvForgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar vistas
        initializeViews()

        // Configurar listeners
        setupListeners()
    }

    /**
     * Inicializa las referencias a las vistas del layout
     */
    private fun initializeViews() {
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        btnRegister = findViewById(R.id.btn_register)
        tvForgotPassword = findViewById(R.id.tv_forgot_password)
    }

    /**
     * Configura los listeners de los botones
     */
    private fun setupListeners() {
        // Botón de Login
        btnLogin.setOnClickListener {
            performLogin()
        }

        // Botón de Registrarse
        btnRegister.setOnClickListener {
            navigateToRegister()
        }

        // Enlace de "Olvidé contraseña"
        tvForgotPassword.setOnClickListener {
            handleForgotPassword()
        }
    }

    /**
     * Realiza la validación y login del usuario
     */
    private fun performLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validación básica
        when {
            email.isEmpty() -> {
                showError("Por favor ingresa tu correo")
            }
            password.isEmpty() -> {
                showError("Por favor ingresa tu contraseña")
            }
            !isValidEmail(email) -> {
                showError("Correo inválido")
            }
            password.length < 6 -> {
                showError("La contraseña debe tener al menos 6 caracteres")
            }
            else -> {
                // TODO: Realizar login con backend
                // Por ahora, navegar a la pantalla principal
                navigateToHome()
            }
        }
    }

    /**
     * Valida el formato del email
     */
    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }

    /**
     * Muestra un mensaje de error
     */
    private fun showError(message: String) {
        // TODO: Implementar Toast o Snackbar
        android.util.Log.e("LoginActivity", message)
    }

    /**
     * Navega a la pantalla de Registro
     */
    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    /**
     * Maneja el click en "Olvidé contraseña"
     */
    private fun handleForgotPassword() {
        // TODO: Implementar pantalla de recuperación de contraseña
        showError("Función de recuperación de contraseña en desarrollo")
    }

    /**
     * Navega a la pantalla principal después del login exitoso
     */
    private fun navigateToHome() {
        // TODO: Reemplazar con la Activity principal de la aplicación
        // val intent = Intent(this, MainActivity::class.java)
        // startActivity(intent)
        finish()
    }
}