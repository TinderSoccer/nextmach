# ⚡ Quick Test Guide - NextMatch

## 🚀 **PRUEBA RÁPIDA EN 3 PASOS**

### **PASO 1: Asegúrate que Android Studio esté abierto**
```bash
# O inicia el emulador desde terminal
~/Library/Android/sdk/emulator/emulator -avd Medium_Phone_API_36 -no-audio &

# Espera 20 segundos a que cargue
sleep 20
```

### **PASO 2: Instala la app**
```bash
cd /Users/juliosilvabobadilla/AndroidStudioProjects/NextMatch
./gradlew installDebug
```

### **PASO 3: Lanza la app**
```bash
# Opción A: Desde terminal
~/Library/Android/sdk/platform-tools/adb shell am start -n com.nextmatch.app/.MainActivity

# Opción B: Desde Android Studio
# Run → Run 'app'
# O presiona Control+R
```

---

## 🎯 **PANTALLAS QUE PUEDES PROBAR AHORA**

### **1️⃣ HomeScreen (YA IMPLEMENTADA ✅)**

**Ubicación:** `MainActivity.kt`

**Lo que verás:**
- Logo balón con fuego
- Botones verdes neón
- Título "NextMatch"
- Texto "Bienvenido a NextMatch ⚽"

**Qué hacer:**
1. Haz click en "Registro de Usuario" (botón verde)
2. Haz click en "Formulario de Contacto" (botón púrpura)
3. Observa la navegación entre pantallas

---

## 📋 **PANTALLAS GENERADAS (NECESITAN ACTIVITIES KOTLIN)**

Estos layouts XML están listos, pero necesitan que crees las Activities Kotlin.

### **2️⃣ LoginScreen**
**Archivo:** `app/src/main/res/layout/activity_login.xml`

Para activarla, crea:
```kotlin
// Archivo: app/src/main/java/com/nextmatch/app/auth/LoginActivity.kt
```

Haz que se abra así:
```bash
~/Library/Android/sdk/platform-tools/adb shell am start -n com.nextmatch.app/.auth.LoginActivity
```

---

### **3️⃣ RegisterScreen**
**Archivo:** `app/src/main/res/layout/activity_register.xml`

```bash
~/Library/Android/sdk/platform-tools/adb shell am start -n com.nextmatch.app/.auth.RegisterActivity
```

---

### **4️⃣ MatchmakingScreen (Búsqueda de Oponentes)**
**Archivo:** `app/src/main/res/layout/activity_matchmaking.xml`

Características:
- ProgressBar circular animado
- Texto "Buscando oponentes..."
- Botón "Cancelar búsqueda"

```bash
~/Library/Android/sdk/platform-tools/adb shell am start -n com.nextmatch.app/.matchmaking.MatchmakingActivity
```

---

### **5️⃣ TeamsListScreen (Lista de Equipos)**
**Archivo:** `app/src/main/res/layout/activity_teams_list.xml`

Características:
- RecyclerView con items de equipos
- FAB "Crear equipo" (botón verde flotante)
- Headers y footers

```bash
~/Library/Android/sdk/platform-tools/adb shell am start -n com.nextmatch.app/.teams.TeamsListActivity
```

---

### **6️⃣ TeamProfileScreen (Perfil Tinder-like)**
**Archivo:** `app/src/main/res/layout/activity_team_profile.xml`

Características:
- Imagen grande de equipo
- Estadísticas (goles, partidos)
- Botones de acción

```bash
~/Library/Android/sdk/platform-tools/adb shell am start -n com.nextmatch.app/.teams.TeamProfileActivity
```

---

### **7️⃣ FieldBookingScreen (Mapa de Canchas)**
**Archivo:** `app/src/main/res/layout/activity_field_booking.xml`

Características:
- Buscador de canchas
- Mapa simulado
- BottomSheet con información

```bash
~/Library/Android/sdk/platform-tools/adb shell am start -n com.nextmatch.app/.booking.FieldBookingActivity
```

---

### **8️⃣ CalendarAvailabilityScreen**
**Archivo:** `app/src/main/res/layout/activity_calendar_availability.xml`

Características:
- Grid de calendario
- Horarios disponibles
- Botón "Continuar"

```bash
~/Library/Android/sdk/platform-tools/adb shell am start -n com.nextmatch.app/.booking.CalendarAvailabilityActivity
```

---

### **9️⃣ BookingConfirmationScreen**
**Archivo:** `app/src/main/res/layout/activity_booking_confirmation.xml`

Características:
- Resumen de reserva
- Detalles: fecha, hora, costo
- Botón confirmar

```bash
~/Library/Android/sdk/platform-tools/adb shell am start -n com.nextmatch.app/.booking.BookingConfirmationActivity
```

---

### **🔟 MessagesListScreen (Chats)**
**Archivo:** `app/src/main/res/layout/activity_messages_list.xml`

Características:
- Lista de conversaciones
- Avatar, nombre, último mensaje
- Hora del último mensaje

```bash
~/Library/Android/sdk/platform-tools/adb shell am start -n com.nextmatch.app/.messages.MessagesListActivity
```

---

### **1️⃣1️⃣ ChatScreenActivity**
**Archivo:** `app/src/main/res/layout/activity_chat_screen.xml`

Características:
- Burbujas de chat (propias/ajenas)
- Input de mensaje
- RecyclerView de mensajes

```bash
~/Library/Android/sdk/platform-tools/adb shell am start -n com.nextmatch.app/.messages.ChatScreenActivity
```

---

## 🎮 **PRUEBA INTERACTIVA CON CLICKS**

### **Script para probar navegación:**

```bash
#!/bin/bash

# 1. Lanza la app
echo "🚀 Lanzando NextMatch..."
~/Library/Android/sdk/platform-tools/adb shell am start -n com.nextmatch.app/.MainActivity
sleep 2

# 2. Toma screenshot
echo "📸 Capturando pantalla..."
~/Library/Android/sdk/platform-tools/adb shell screencap -p /sdcard/home.png
~/Library/Android/sdk/platform-tools/adb pull /sdcard/home.png ~/Desktop/home.png

# 3. Click en "Registro de Usuario"
echo "👆 Haciendo click en Registro..."
~/Library/Android/sdk/platform-tools/adb shell input tap 540 950
sleep 2

# 4. Screenshot de registro
echo "📸 Capturando pantalla de registro..."
~/Library/Android/sdk/platform-tools/adb shell screencap -p /sdcard/registro.png
~/Library/Android/sdk/platform-tools/adb pull /sdcard/registro.png ~/Desktop/registro.png

# 5. Vuelve a home
echo "👈 Volviendo a home..."
~/Library/Android/sdk/platform-tools/adb shell input keyevent 4  # Botón atrás

echo "✅ Prueba completada. Revisa ~/Desktop/"
```

Guárdalo como `test_nextmatch.sh`:
```bash
chmod +x test_nextmatch.sh
./test_nextmatch.sh
```

---

## 🖼️ **LAYOUTS LISTOS (Sin Activities Aún)**

| Pantalla | Layout XML | Status |
|----------|-----------|---------|
| Home | activity_home.kt | ✅ Implementada (Compose) |
| Login | activity_login.xml | ⏳ XML listo, falta Activity |
| Registro | activity_register.xml | ⏳ XML listo, falta Activity |
| Búsqueda | activity_matchmaking.xml | ⏳ XML listo, falta Activity |
| Equipos | activity_teams_list.xml | ⏳ XML listo, falta Activity |
| Perfil Equipo | activity_team_profile.xml | ⏳ XML listo, falta Activity |
| Mapa | activity_field_booking.xml | ⏳ XML listo, falta Activity |
| Calendario | activity_calendar_availability.xml | ⏳ XML listo, falta Activity |
| Confirmación | activity_booking_confirmation.xml | ⏳ XML listo, falta Activity |
| Mensajes | activity_messages_list.xml | ⏳ XML listo, falta Activity |
| Chat | activity_chat_screen.xml | ⏳ XML listo, falta Activity |

---

## 🎬 **OPCIÓN FÁCIL: ANDROID STUDIO**

### **Si prefieres una interfaz gráfica:**

1. **Abre Android Studio**
2. **File → Open** → Selecciona `/Users/juliosilvabobadilla/AndroidStudioProjects/NextMatch`
3. **VER LAYOUTS EN PREVIEW:**
   - Abre cualquier XML en `res/layout/`
   - En la zona superior derecha, verás un botón "Design"
   - Haz click para ver preview del layout

4. **EJECUTAR LA APP:**
   - Click en ▶️ (Run button) o `Shift+F10`
   - Selecciona el emulador
   - ¡Listo!

5. **NAVEGAR EN LA APP:**
   - Haz clicks normalmente en la pantalla del emulador
   - Usa botones virtuales en la base del emulador

---

## ✨ **PRÓXIMAS ACTIVIDADES A CREAR**

Para que funcionen todos los layouts, crea estos archivos Kotlin:

```
app/src/main/java/com/nextmatch/app/
├── auth/
│   ├── LoginActivity.kt ✅ (YA EXISTE)
│   └── RegisterActivity.kt ✅ (YA EXISTE)
├── matchmaking/
│   └── MatchmakingActivity.kt ❌ (CREAR)
├── teams/
│   ├── TeamsListActivity.kt ❌ (CREAR)
│   └── TeamProfileActivity.kt ❌ (CREAR)
├── booking/
│   ├── FieldBookingActivity.kt ❌ (CREAR)
│   ├── CalendarAvailabilityActivity.kt ❌ (CREAR)
│   └── BookingConfirmationActivity.kt ❌ (CREAR)
└── messages/
    ├── MessagesListActivity.kt ❌ (CREAR)
    └── ChatScreenActivity.kt ❌ (CREAR)
```

---

## 📚 **REFERENCIAS**

- **Guía completa:** `INTEGRATION_GUIDE.md`
- **Resumen de archivos:** `GENERATED_FILES_SUMMARY.md`
- **Guía extendida de testing:** `TEST_GUIDE.md`

---

## 🎯 **TL;DR (La Versión Corta)**

```bash
# 1. Instala
cd /Users/juliosilvabobadilla/AndroidStudioProjects/NextMatch
./gradlew installDebug

# 2. Corre
~/Library/Android/sdk/platform-tools/adb shell am start -n com.nextmatch.app/.MainActivity

# 3. Captura screenshot
~/Library/Android/sdk/platform-tools/adb shell screencap -p /sdcard/screen.png
~/Library/Android/sdk/platform-tools/adb pull /sdcard/screen.png ~/Desktop/
```

¡**LISTO! Tu app NextMatch está corriendo.** 🎉