# 🎯 FORMAS DE PROBAR TU APP NEXTMATCH

## **OPCIÓN 1️⃣ : LA MÁS FÁCIL (Android Studio)**

### **Paso 1: Abre Android Studio**
- Ve a `/Users/juliosilvabobadilla/AndroidStudioProjects/NextMatch`
- Abre el proyecto con Android Studio

### **Paso 2: Conecta un emulador o dispositivo**

**Si quieres usar emulador:**
- Arriba a la derecha en Android Studio, verás un selector
- Haz click en **"Device Manager"** (o Shift+Cmd+O)
- Haz click en el botón "play" junto a "Medium_Phone_API_36"
- Espera a que cargue (30-60 segundos)

**Si usas dispositivo físico:**
- Conecta tu Android por USB
- Activa "Depuración USB" en Configuración → Acerca de el teléfono

### **Paso 3: Ejecuta la app**
- En Android Studio, arriba verás un botón verde "Run ▶️"
- Haz click en él (o presiona Shift+F10)
- Selecciona el emulador/dispositivo
- ¡La app se abrirá automáticamente!

### **Paso 4: Interactúa con la app**
- Haz clicks en los botones
- Navega entre pantallas
- Escribe en los campos de texto

---

## **OPCIÓN 2️⃣ : DESDE TERMINAL (Rápido)**

### **Comando único para lanzar todo:**

```bash
# 1. Abre el proyecto
cd /Users/juliosilvabobadilla/AndroidStudioProjects/NextMatch

# 2. Inicia emulador + instala app + lanza
~/Library/Android/sdk/emulator/emulator -avd Medium_Phone_API_36 -no-audio &
sleep 30
./gradlew installDebug
~/Library/Android/sdk/platform-tools/adb shell am start -n com.nextmatch.app/.MainActivity
```

O guárdalo como script `launch_app.sh`:

```bash
#!/bin/bash
echo "🚀 Iniciando NextMatch..."

cd /Users/juliosilvabobadilla/AndroidStudioProjects/NextMatch

# Inicia emulador
~/Library/Android/sdk/emulator/emulator -avd Medium_Phone_API_36 -no-audio &
echo "⏳ Esperando a que cargue el emulador..."
sleep 30

# Instala app
echo "📦 Instalando app..."
./gradlew installDebug

# Lanza app
echo "🎮 Lanzando NextMatch..."
~/Library/Android/sdk/platform-tools/adb shell am start -n com.nextmatch.app/.MainActivity

echo "✅ ¡App lanzada! Revisa el emulador."
```

Ejecuta:
```bash
chmod +x launch_app.sh
./launch_app.sh
```

---

## **OPCIÓN 3️⃣ : VER LOS LAYOUTS SIN CORRER LA APP**

En Android Studio:

1. **Abre cualquier archivo XML:**
   - Ve a `app/src/main/res/layout/`
   - Haz click en cualquier `activity_*.xml`
   - Ejemplo: `activity_login.xml`

2. **Verás el preview del layout:**
   - Arriba a la derecha hay botones
   - Haz click en **"Design"** para ver el layout
   - Haz click en **"Code"** para ver el XML

3. **Cambia el dispositivo para preview:**
   - En el toolbar del designer, verás un selector de dispositivo
   - Cambia entre diferentes tamaños de pantalla
   - Prueba con Pixel 6, Pixel 6 Pro, Tablet, etc.

---

## 📋 **LOS 11 LAYOUTS QUE GENERÉ**

Todos están listos para visualizar en Android Studio:

### **Autenticación**
- ✅ `activity_login.xml` - Pantalla de login
- ✅ `activity_register.xml` - Pantalla de registro

### **Búsqueda**
- ✅ `activity_matchmaking.xml` - Buscar oponentes
- ✅ `activity_teams_list.xml` - Lista de equipos

### **Perfiles**
- ✅ `activity_team_profile.xml` - Perfil de equipo

### **Reservas**
- ✅ `activity_field_booking.xml` - Mapa de canchas
- ✅ `activity_calendar_availability.xml` - Calendario
- ✅ `activity_booking_confirmation.xml` - Confirmación

### **Mensajes**
- ✅ `activity_messages_list.xml` - Lista de chats
- ✅ `activity_chat_screen.xml` - Pantalla de chat
- ✅ `item_message_bubble.xml` - Burbujas de mensajes

---

## 🎬 **CAPTURA SCREENSHOTS**

Mientras la app esté corriendo:

```bash
# Tomar screenshot
~/Library/Android/sdk/platform-tools/adb shell screencap -p /sdcard/screenshot.png

# Descargar a tu computadora
~/Library/Android/sdk/platform-tools/adb pull /sdcard/screenshot.png ~/Desktop/

# Ver imagen (macOS)
open ~/Desktop/screenshot.png
```

---

## 🧭 **NAVEGAR EN LA APP**

Una vez que la app está corriendo:

```bash
# Hacer click en una posición de la pantalla
# Formato: adb shell input tap <x> <y>

# Pantalla es 1080x2400
# Centro es 540, 1200

# Ejemplo: Click en centro
~/Library/Android/sdk/platform-tools/adb shell input tap 540 1200

# Ejemplo: Escribir texto (primero haz click en un campo)
~/Library/Android/sdk/platform-tools/adb shell input text "usuario@email.com"

# Deslizar hacia arriba
~/Library/Android/sdk/platform-tools/adb shell input swipe 540 1000 540 300

# Botón atrás
~/Library/Android/sdk/platform-tools/adb shell input keyevent 4
```

---

## 🎨 **VERIFICAR LOS COLORES Y DISEÑO**

Los layouts incluyen:
- ✅ Fondo negro (#000000)
- ✅ Verde neón (#39FF14) - botones primarios
- ✅ Bordes grises oscuros
- ✅ Texto blanco y gris
- ✅ Esquinas redondeadas (8dp)
- ✅ RecyclerViews optimizados
- ✅ ConstraintLayout responsive

---

## 📊 **STATUS DE IMPLEMENTACIÓN**

| Componente | Status | Ubicación |
|-----------|--------|-----------|
| **Layouts XML** | ✅ Completado | `/res/layout/` |
| **Activities Kotlin** | ⏳ Parcial | `/java/com/nextmatch/app/` |
| **Tema/Colores** | ✅ Completado | `/values/colors.xml` |
| **Strings** | ✅ Completado | `/values/strings.xml` |
| **Drawables** | ✅ Completado | `/drawable/` |
| **Data Classes** | ✅ Team.kt | `/teams/` |
| **Adapters** | ✅ TeamsAdapter.kt | `/teams/` |
| **Build Config** | ✅ Actualizado | `build.gradle.kts` |

---

## ✨ **PRÓXIMOS PASOS (Opcional)**

Si quieres mejorar la app:

1. **Crear las Activities faltantes** (8 archivos .kt)
2. **Implementar ViewModels** para state management
3. **Conectar a Backend/API**
4. **Agregar Firebase para chat en tiempo real**
5. **Integrar Google Maps**

Pero todos los **layouts están 100% listos** para verlos en acción.

---

## 🆘 **TROUBLESHOOTING**

### Si no ves el emulador en Android Studio:
```bash
# Verifica dispositivos conectados
~/Library/Android/sdk/platform-tools/adb devices

# Si no aparece, reinicia el servidor adb
~/Library/Android/sdk/platform-tools/adb kill-server
~/Library/Android/sdk/platform-tools/adb start-server
```

### Si la app no instala:
```bash
# Limpiar y rebuildar
cd /Users/juliosilvabobadilla/AndroidStudioProjects/NextMatch
./gradlew clean
./gradlew build
./gradlew installDebug
```

### Si la app se cierra al abrirse:
```bash
# Ver errores
~/Library/Android/sdk/platform-tools/adb logcat | grep -i "error\|crash"
```

---

## 🎯 **LO MÁS RÁPIDO: ANDROID STUDIO**

1. Abre Android Studio
2. File → Open → NextMatch
3. Click ▶️ (Run)
4. Selecciona emulador
5. ¡Listo!

**Total: 30 segundos** ⚡

---

**¡Prueba la app ahora!** ✅