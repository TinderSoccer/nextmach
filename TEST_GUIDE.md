# 🧪 Guía de Pruebas - NextMatch

## 📱 Cómo Probar la Aplicación

### Opción 1: Desde Android Studio (Recomendado)

1. **Abre Android Studio**
2. **Ve a View → Appearance → Tool Windows → Device File Explorer**
3. **O usa:** `Shift + Cmd + O` → Busca "Device Manager"
4. **Selecciona el emulador** y observa en tiempo real

---

### Opción 2: Desde Terminal (Nuestra Forma)

#### **A. Ver la App en Vivo**
```bash
# Tomar screenshot
~/Library/Android/sdk/platform-tools/adb shell screencap -p /sdcard/screenshot.png
~/Library/Android/sdk/platform-tools/adb pull /sdcard/screenshot.png ~/Desktop/screenshot.png
```

#### **B. Simular Clicks**
```bash
# Formato: adb shell input tap <x> <y>

# Pantalla: HomeScreen (1080x2400)
# Botón "Registro de Usuario" (centro, ~900px)
~/Library/Android/sdk/platform-tools/adb shell input tap 540 950

# Botón "Formulario de Contacto"
~/Library/Android/sdk/platform-tools/adb shell input tap 540 1050
```

#### **C. Escribir en EditText**
```bash
# Primero haz click en el campo
~/Library/Android/sdk/platform-tools/adb shell input tap 540 500

# Luego escribe
~/Library/Android/sdk/platform-tools/adb shell input text "tu_email@example.com"
```

#### **D. Swipe (Deslizar)**
```bash
# Swipe hacia arriba
~/Library/Android/sdk/platform-tools/adb shell input swipe 540 1000 540 300

# Swipe hacia abajo
~/Library/Android/sdk/platform-tools/adb shell input swipe 540 300 540 1000
```

---

## 🎯 **Flujo de Prueba Completo**

### **Flujo 1: Pantalla de Login**

```bash
# 1. Iniciar app
~/Library/Android/sdk/platform-tools/adb shell am start -n com.nextmatch.app/.auth.LoginActivity

# 2. Screenshot
~/Library/Android/sdk/platform-tools/adb shell screencap -p /sdcard/screen1.png
~/Library/Android/sdk/platform-tools/adb pull /sdcard/screen1.png ~/Desktop/screen1.png

# 3. Click en campo Email
~/Library/Android/sdk/platform-tools/adb shell input tap 540 600

# 4. Escribir email
~/Library/Android/sdk/platform-tools/adb shell input text "usuario@nextmatch.com"

# 5. Click en campo Contraseña
~/Library/Android/sdk/platform-tools/adb shell input tap 540 700

# 6. Escribir contraseña
~/Library/Android/sdk/platform-tools/adb shell input text "password123"

# 7. Screenshot del formulario completado
~/Library/Android/sdk/platform-tools/adb shell screencap -p /sdcard/screen2.png
~/Library/Android/sdk/platform-tools/adb pull /sdcard/screen2.png ~/Desktop/screen2.png

# 8. Click en botón "Iniciar Sesión"
~/Library/Android/sdk/platform-tools/adb shell input tap 540 800
```

### **Flujo 2: Pantalla de Registro**

```bash
# Desde HomeScreen, click en "Registro de Usuario"
~/Library/Android/sdk/platform-tools/adb shell input tap 540 950

# Screenshot de formulario de registro
~/Library/Android/sdk/platform-tools/adb shell screencap -p /sdcard/screen_register.png
~/Library/Android/sdk/platform-tools/adb pull /sdcard/screen_register.png ~/Desktop/screen_register.png

# Click en campo Nombre de Usuario
~/Library/Android/sdk/platform-tools/adb shell input tap 540 500

# Escribir nombre
~/Library/Android/sdk/platform-tools/adb shell input text "jugador123"

# Click en campo Email
~/Library/Android/sdk/platform-tools/adb shell input tap 540 600

# Escribir email
~/Library/Android/sdk/platform-tools/adb shell input text "jugador@nextmatch.com"

# Scroll hacia abajo para ver más campos
~/Library/Android/sdk/platform-tools/adb shell input swipe 540 1000 540 300
```

---

## 🎨 **Coordenadas de la Pantalla**

Resoluci ón del emulador: **1080 x 2400 px**

```
┌─────────────────────┐
│  0,0              1080,0
│
│     Centro: 540, 1200
│
│
│  0,2400          1080,2400
└─────────────────────┘

Áreas principales:
- Zona Superior: y = 0-600
- Zona Media: y = 600-1400
- Zona Inferior: y = 1400-2400
- Centro X: 540
```

---

## 📋 **Tabla de Clicks Comunes**

| Elemento | Ubicación | Comando |
|----------|-----------|---------|
| Botón Atrás | Top-left (50, 50) | `adb shell input tap 50 50` |
| Centro pantalla | (540, 1200) | `adb shell input tap 540 1200` |
| Botón primario inferior | (540, 2250) | `adb shell input tap 540 2250` |
| Campo de entrada 1 | (540, 600) | `adb shell input tap 540 600` |
| Campo de entrada 2 | (540, 700) | `adb shell input tap 540 700` |

---

## 🧬 **Flujo de Prueba: Búsqueda de Equipos**

Primero necesitas crear la Activity. Mientras, aquí está la secuencia esperada:

```bash
# 1. Navegar a pantalla de búsqueda (cuando esté implementada)
~/Library/Android/sdk/platform-tools/adb shell am start -n com.nextmatch.app/.matchmaking.MatchmakingActivity

# 2. Screenshot de pantalla de carga
~/Library/Android/sdk/platform-tools/adb shell screencap -p /sdcard/matching.png
~/Library/Android/sdk/platform-tools/adb pull /sdcard/matching.png ~/Desktop/matching.png

# 3. Esperar a que termine la búsqueda
sleep 10

# 4. Screenshot final
~/Library/Android/sdk/platform-tools/adb shell screencap -p /sdcard/matching_results.png
~/Library/Android/sdk/platform-tools/adb pull /sdcard/matching_results.png ~/Desktop/matching_results.png

# 5. Click en equipo para ver perfil
~/Library/Android/sdk/platform-tools/adb shell input tap 540 800
```

---

## 📸 **Automatizar Screenshots de Todas las Pantallas**

Crea un script `test_all_screens.sh`:

```bash
#!/bin/bash

# Array de activities
activities=(
    "com.nextmatch.app/.MainActivity"
    "com.nextmatch.app/.auth.LoginActivity"
    "com.nextmatch.app/.auth.RegisterActivity"
    # Agrega las demás cuando estén creadas
)

counter=1
for activity in "${activities[@]}"; do
    echo "Capturando: $activity"

    # Iniciar activity
    ~/Library/Android/sdk/platform-tools/adb shell am start -n $activity

    # Esperar a que cargue
    sleep 2

    # Tomar screenshot
    ~/Library/Android/sdk/platform-tools/adb shell screencap -p /sdcard/screen_$counter.png

    # Descargar
    ~/Library/Android/sdk/platform-tools/adb pull /sdcard/screen_$counter.png ~/Desktop/screen_$counter.png

    echo "✓ Screenshot guardado: screen_$counter.png"

    ((counter++))
done

echo "✅ Todos los screenshots capturados en ~/Desktop/"
```

Ejecutar:
```bash
chmod +x test_all_screens.sh
./test_all_screens.sh
```

---

## 🔍 **Ver Logs en Tiempo Real**

```bash
# Ver todos los logs
~/Library/Android/sdk/platform-tools/adb logcat

# Ver solo logs de tu app
~/Library/Android/sdk/platform-tools/adb logcat | grep nextmatch

# Guardar logs a archivo
~/Library/Android/sdk/platform-tools/adb logcat > ~/Desktop/app_logs.txt
```

---

## 🐛 **Debugging**

### Ver crasheos de la app:
```bash
~/Library/Android/sdk/platform-tools/adb logcat | grep -i "exception\|crash\|error"
```

### Ver eventos de navegación:
```bash
~/Library/Android/sdk/platform-tools/adb shell am get-app-links com.nextmatch.app
```

### Reinstalar app:
```bash
~/Library/Android/sdk/platform-tools/adb uninstall com.nextmatch.app
./gradlew installDebug
```

---

## ✅ **Checklist de Pruebas**

### **HomeScreen**
- [ ] Logo visible
- [ ] Botones con colores correctos (verde neón)
- [ ] Texto centrado
- [ ] Responsive en diferentes tamaños

### **LoginScreen** (una vez implementada)
- [ ] Campos de entrada funcionales
- [ ] Validación de email
- [ ] Validación de contraseña
- [ ] Botones de acción

### **RegisterScreen** (una vez implementada)
- [ ] Scroll funcional
- [ ] Confirmación de contraseña
- [ ] Checkbox de términos
- [ ] Validaciones

### **Equipos**
- [ ] RecyclerView carga items
- [ ] Click en item abre perfil
- [ ] Click en botón "Unirse" funciona
- [ ] FAB "Crear equipo" funciona

### **Chat**
- [ ] Burbujas de mensajes propios/ajenos diferenciadas
- [ ] Scroll en mensajes
- [ ] Envío de mensaje funciona

---

## 🎥 **Grabar Video de la App**

```bash
# Iniciar grabación
~/Library/Android/sdk/platform-tools/adb shell screenrecord --verbose /sdcard/nextmatch_demo.mp4

# Interactuar con la app por ~30 segundos

# Detener (Ctrl+C)

# Descargar video
~/Library/Android/sdk/platform-tools/adb pull /sdcard/nextmatch_demo.mp4 ~/Desktop/nextmatch_demo.mp4
```

---

## 💡 **Tips Útiles**

1. **Limpiar datos de app:**
   ```bash
   ~/Library/Android/sdk/platform-tools/adb shell pm clear com.nextmatch.app
   ```

2. **Ver permiso otorgados:**
   ```bash
   ~/Library/Android/sdk/platform-tools/adb shell pm list permissions -g
   ```

3. **Acelerar emulador:**
   Apagar: `~/Library/Android/sdk/platform-tools/adb emu kill`
   Reiniciar: `~/Library/Android/sdk/emulator/emulator -avd Medium_Phone_API_36 -gpu host-composition`

4. **Múltiples emuladores:**
   ```bash
   ~/Library/Android/sdk/platform-tools/adb devices -l
   ~/Library/Android/sdk/platform-tools/adb -s emulator-5554 shell ...
   ```

---

**¡Listo! Usa estos comandos para probar toda tu app NextMatch desde terminal.**