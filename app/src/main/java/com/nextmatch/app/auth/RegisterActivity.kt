package com.nextmatch.app.auth

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.nextmatch.app.R

/**
 * RegisterActivity - Pantalla de creación de cuenta
 * Permite al usuario registrarse con nombre de usuario, correo, contraseña y confirmación
 */
class RegisterActivity : AppCompatActivity() {

    // Declaración de vistas
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var cbTerms: CheckBox
    private lateinit var btnCreateAccount: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializar vistas
        initializeViews()

        // Configurar listeners
        setupListeners()
    }

    /**
     * Inicializa las referencias a las vistas del layout
     */
    private fun initializeViews() {
        etUsername = findViewById(R.id.et_username)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        etConfirmPassword = findViewById(R.id.et_confirm_password)
        cbTerms = findViewById(R.id.cb_terms)
        btnCreateAccount = findViewById(R.id.btn_create_account)
        btnBack = findViewById(R.id.btn_back)
    }

    /**
     * Configura los listeners de los botones
     */
    private fun setupListeners() {
        // Botón Crear Cuenta
        btnCreateAccount.setOnClickListener {
            performRegister()
        }

        // Botón Atrás
        btnBack.setOnClickListener {
            finish()
        }
    }

    /**
     * Realiza la validación y registro del usuario
     */
    private fun performRegister() {
        val username = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()
        val acceptsTerms = cbTerms.isChecked

        // Validación de campos
        when {
            username.isEmpty() -> {
                showError("Por favor ingresa un nombre de usuario")
            }
            email.isEmpty() -> {
                showError("Por favor ingresa tu correo")
            }
            !isValidEmail(email) -> {
                showError("Correo inválido")
            }
            password.isEmpty() -> {
                showError("Por favor ingresa una contraseña")
            }
            password.length < 6 -> {
                showError("La contraseña debe tener al menos 6 caracteres")
            }
            confirmPassword.isEmpty() -> {
                showError("Por favor confirma tu contraseña")
            }
            password != confirmPassword -> {
                showError("Las contraseñas no coinciden")
            }
            !acceptsTerms -> {
                showError("Debes aceptar los términos y condiciones")
            }
            else -> {
                // TODO: Realizar registro con backend
                performRegistrationSuccess()
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
        android.util.Log.e("RegisterActivity", message)
    }

    /**
     * Maneja el registro exitoso
     */
    private fun performRegistrationSuccess() {
        // TODO: Navegar a la pantalla principal o pantalla de bienvenida
        showError("Registro exitoso - Implementar navegación")
        finish()
    }
}