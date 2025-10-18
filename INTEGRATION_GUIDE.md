# 📖 Guía de Integración - NextMatch Screens

## 🎯 Objetivo
Este documento te guía a través de los pasos para integrar todos los layouts y código generados en tu proyecto Android Studio.

---

## ✅ Verificación de Archivos

### Paso 1: Verificar Estructura de Directorios
Asegúrate de que estos directorios existan en tu proyecto:

```bash
app/src/main/
├── java/com/nextmatch/app/
│   ├── auth/
│   ├── teams/
│   ├── matchmaking/
│   ├── booking/
│   └── messages/
└── res/
    ├── layout/
    ├── drawable/
    └── values/
```

### Paso 2: Verificar Archivos XML Generados

**Layouts XML** (ubicación: `app/src/main/res/layout/`):
- ✅ `activity_login.xml`
- ✅ `activity_register.xml`
- ✅ `activity_matchmaking.xml`
- ✅ `activity_teams_list.xml`
- ✅ `item_team.xml`
- ✅ `activity_team_profile.xml`
- ✅ `activity_field_booking.xml`
- ✅ `activity_calendar_availability.xml`
- ✅ `activity_booking_confirmation.xml`
- ✅ `activity_messages_list.xml`
- ✅ `item_chat_preview.xml`
- ✅ `activity_chat_screen.xml`
- ✅ `item_message_bubble.xml`

**Drawables** (ubicación: `app/src/main/res/drawable/`):
- ✅ `bg_edittext_dark.xml`
- ✅ `bg_button_primary.xml`
- ✅ `bg_button_secondary.xml`
- ✅ `bg_button_icon.xml`
- ✅ `bg_marker.xml`
- ✅ `bg_badge.xml`
- ✅ `bg_message_bubble_own.xml`
- ✅ `bg_message_bubble_other.xml`
- ✅ `ic_search.xml`
- ✅ `ic_flame.xml`

**Archivos Generados** (ubicación: `app/src/main/res/values/`):
- ✅ `colors.xml` (ACTUALIZADO)
- ✅ `strings.xml` (ACTUALIZADO)

---

## 🔧 Pasos de Integración

### Paso 3: Crear Android Manifest Entries

Abre `app/src/main/AndroidManifest.xml` y añade las siguientes Activities dentro de `<application>`:

```xml
<!-- Authentication -->
<activity
    android:name=".auth.LoginActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>

<activity
    android:name=".auth.RegisterActivity"
    android:exported="false"
    android:parentActivityName=".auth.LoginActivity" />

<!-- Matchmaking -->
<activity
    android:name=".matchmaking.MatchmakingActivity"
    android:exported="false" />

<!-- Teams -->
<activity
    android:name=".teams.TeamsListActivity"
    android:exported="false" />

<activity
    android:name=".teams.TeamProfileActivity"
    android:exported="false" />

<!-- Booking -->
<activity
    android:name=".booking.FieldBookingActivity"
    android:exported="false" />

<activity
    android:name=".booking.CalendarAvailabilityActivity"
    android:exported="false" />

<activity
    android:name=".booking.BookingConfirmationActivity"
    android:exported="false" />

<!-- Messages -->
<activity
    android:name=".messages.MessagesListActivity"
    android:exported="false" />

<activity
    android:name=".messages.ChatScreenActivity"
    android:exported="false" />
```

### Paso 4: Crear las Activities Kotlin

Necesitas crear los archivos Kotlin para cada Activity. Aquí están los templates básicos:

#### `MatchmakingActivity.kt`
```kotlin
package com.nextmatch.app.matchmaking

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nextmatch.app.R

class MatchmakingActivity : AppCompatActivity() {
    private lateinit var pbLoading: ProgressBar
    private lateinit var tvSearching: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnCancelSearch: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matchmaking)

        pbLoading = findViewById(R.id.pb_loading)
        tvSearching = findViewById(R.id.tv_searching)
        tvStatus = findViewById(R.id.tv_status)
        btnCancelSearch = findViewById(R.id.btn_cancel_search)

        btnCancelSearch.setOnClickListener {
            finish()
        }

        // TODO: Implementar lógica de búsqueda
    }
}
```

#### `TeamsListActivity.kt`
```kotlin
package com.nextmatch.app.teams

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nextmatch.app.R

class TeamsListActivity : AppCompatActivity() {
    private lateinit var rvTeams: RecyclerView
    private lateinit var btnBack: Button
    private lateinit var btnCreateTeam: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teams_list)

        rvTeams = findViewById(R.id.rv_teams)
        btnBack = findViewById(R.id.btn_back)
        btnCreateTeam = findViewById(R.id.btn_create_team)

        // Configurar RecyclerView
        rvTeams.layoutManager = LinearLayoutManager(this)
        val teams = getMockTeams()
        rvTeams.adapter = TeamsAdapter(teams, ::onTeamClick, ::onTeamJoinClick)

        btnBack.setOnClickListener { finish() }
        btnCreateTeam.setOnClickListener { createNewTeam() }

        // TODO: Cargar equipos desde backend
    }

    private fun getMockTeams(): List<Team> {
        return listOf(
            Team("1", "Equipo Rojo", "Santiago", 4.5f, 5),
            Team("2", "Equipo Azul", "Providencia", 4.0f, 8),
            Team("3", "Equipo Verde", "Las Condes", 3.5f, 3)
        )
    }

    private fun onTeamClick(team: Team) {
        // TODO: Navegar a TeamProfileActivity
    }

    private fun onTeamJoinClick(team: Team) {
        // TODO: Enviar solicitud de unirse
    }

    private fun createNewTeam() {
        // TODO: Navegar a CreateTeamActivity
    }
}
```

#### `TeamProfileActivity.kt`
```kotlin
package com.nextmatch.app.teams

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nextmatch.app.R

class TeamProfileActivity : AppCompatActivity() {
    private lateinit var btnClose: Button
    private lateinit var btnViewProfile: Button
    private lateinit var btnRequestJoin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_profile)

        btnClose = findViewById(R.id.btn_close)
        btnViewProfile = findViewById(R.id.btn_view_profile)
        btnRequestJoin = findViewById(R.id.btn_request_join)

        btnClose.setOnClickListener { finish() }
        btnViewProfile.setOnClickListener { openFullProfile() }
        btnRequestJoin.setOnClickListener { sendJoinRequest() }

        // TODO: Cargar datos del equipo desde intent/bundle
    }

    private fun openFullProfile() {
        // TODO: Implementar
    }

    private fun sendJoinRequest() {
        // TODO: Implementar
    }
}
```

#### `FieldBookingActivity.kt`
```kotlin
package com.nextmatch.app.booking

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.nextmatch.app.R

class FieldBookingActivity : AppCompatActivity() {
    private lateinit var etSearchField: EditText
    private lateinit var btnFieldDetails: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_field_booking)

        etSearchField = findViewById(R.id.et_search_field)
        btnFieldDetails = findViewById(R.id.btn_field_details)

        btnFieldDetails.setOnClickListener { navigateToCalendar() }

        // TODO: Implementar búsqueda de canchas
        // TODO: Integrar Google Maps API
    }

    private fun navigateToCalendar() {
        // TODO: Navegar a CalendarAvailabilityActivity
    }
}
```

#### `CalendarAvailabilityActivity.kt`
```kotlin
package com.nextmatch.app.booking

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.nextmatch.app.R

class CalendarAvailabilityActivity : AppCompatActivity() {
    private lateinit var btnContinue: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_availability)

        btnContinue = findViewById(R.id.btn_continue)
        btnContinue.setOnClickListener { navigateToConfirmation() }

        // TODO: Implementar calendario
    }

    private fun navigateToConfirmation() {
        // TODO: Navegar a BookingConfirmationActivity
    }
}
```

#### `BookingConfirmationActivity.kt`
```kotlin
package com.nextmatch.app.booking

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.nextmatch.app.R

class BookingConfirmationActivity : AppCompatActivity() {
    private lateinit var btnConfirmBooking: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_confirmation)

        btnConfirmBooking = findViewById(R.id.btn_confirm_booking)
        btnConfirmBooking.setOnClickListener { confirmBooking() }

        // TODO: Cargar datos de la reserva
    }

    private fun confirmBooking() {
        // TODO: Enviar confirmación al backend
    }
}
```

#### `MessagesListActivity.kt`
```kotlin
package com.nextmatch.app.messages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nextmatch.app.R

class MessagesListActivity : AppCompatActivity() {
    private lateinit var rvMessages: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages_list)

        rvMessages = findViewById(R.id.rv_messages)
        rvMessages.layoutManager = LinearLayoutManager(this)

        // TODO: Cargar chats desde backend
        // TODO: Implementar adapter para mensajes
    }
}
```

#### `ChatScreenActivity.kt`
```kotlin
package com.nextmatch.app.messages

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nextmatch.app.R

class ChatScreenActivity : AppCompatActivity() {
    private lateinit var rvMessages: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_screen)

        rvMessages = findViewById(R.id.rv_messages)
        etMessage = findViewById(R.id.et_message)
        btnSend = findViewById(R.id.btn_send)
        btnBack = findViewById(R.id.btn_back)

        rvMessages.layoutManager = LinearLayoutManager(this)

        btnBack.setOnClickListener { finish() }
        btnSend.setOnClickListener { sendMessage() }

        // TODO: Cargar mensajes previos
        // TODO: Conectar a WebSocket para mensajes en tiempo real
    }

    private fun sendMessage() {
        val message = etMessage.text.toString().trim()
        if (message.isNotEmpty()) {
            // TODO: Enviar mensaje
            etMessage.text.clear()
        }
    }
}
```

### Paso 5: Verificar Dependencias en `build.gradle.kts`

Asegúrate de tener las siguientes dependencias:

```kotlin
dependencies {
    // AndroidX Core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    // Material Design 3
    implementation("com.google.android.material:material:1.11.0")

    // ConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    // Composables (si usas Compose)
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("androidx.compose.material3:material3:1.1.2")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
```

### Paso 6: Compilar y Ejecutar

```bash
# En Android Studio o terminal
./gradlew clean build

# O ejecutar directamente en Android Studio
Build → Make Project
Run → Run 'app'
```

---

## 🐛 Troubleshooting

### Error: "Resource not found"
- Asegúrate de que todos los archivos XML están en `res/layout/`, `res/drawable/`, `res/values/`
- Ejecuta `Build → Clean Project` y luego `Build → Rebuild Project`

### Error: "Activity not found"
- Verifica que todas las Activities están registradas en `AndroidManifest.xml`
- Comprueba que el package name es correcto

### Error: "RecyclerView adapter"
- Asegúrate de que el adaptador hereda de `RecyclerView.Adapter<>`
- Implementa todos los métodos abstractos (`onCreateViewHolder`, `onBindViewHolder`, `getItemCount`)

### Layouts no se renderizan correctamente
- En Android Studio, abre el Preview del layout XML
- Selecciona un device API 30+ para mejor soporte
- Verifica que los colores existen en `colors.xml`

---

## 📱 Pruebas Recomendadas

1. **Navegación**: Verifica que todas las transiciones entre Activities funcionan
2. **Layouts**: Prueba en diferentes tamaños de pantalla (phones, tablets)
3. **Inputs**: Verifica las validaciones en Login y Register
4. **RecyclerView**: Scroll, clicks en items
5. **Temas**: Verifica colores y contraste

---

## ✨ Siguientes Mejoras

1. Implementar ViewModel y LiveData
2. Agregar Dagger/Hilt para inyección de dependencias
3. Conectar con API backend
4. Implementar persistencia con Room
5. Agregar Google Maps
6. Configurar Firebase para chat en tiempo real
7. Agregar animaciones de transición

---

**¡Listo!** Tu proyecto NextMatch está configurado con todos los layouts y Activities base.

Para más información sobre Android Development, consulta la [documentación oficial](https://developer.android.com).