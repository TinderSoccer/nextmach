# NextMatch

  ## Integrantes
  - Julio Silva 
  - Ignacio Farias

  ## 1. Descripción del proyecto
  NextMatch es una plataforma móvil para organizar partidos de fútbol amateur. Permite a cada jugador registrar su perfil, crear y administrar equipos, invitar jugadores, reservar
  canchas y visualizar ubicaciones en un mapa interactivo (OpenStreetMap). Todo el backend está escrito en Spring Boot y desplegado en Render, persistiendo datos en MongoDB Atlas.

  ## 2. Funcionalidades principales
  1. **Autenticación**: registro y login de usuarios.
  2. **Gestión de equipos**:
     - Crear, editar, eliminar y listar equipos propios.
     - Visualizar equipos precargados por NextMatch (`userId = NEXTMATCH`) en modo sólo lectura.
  3. **Gestión de jugadores**:
     - Alta, edición y baja de jugadores asociados a cada equipo.
  4. **Reservas de canchas**:
     - Selector de canchas y agenda usando los datos del microservicio.
     - Visualización en mapa (OSMDroid) para elegir la ubicación de la cancha.
  5. **API externa (OpenStreetMap)**:
     - Tiles y mapas provistos por `https://tile.openstreetmap.org/{z}/{x}/{y}.png`.
  6. **Notificaciones y mensajería simulada** (plantilla de pantallas para completarse en EFT).

  ## 3. Endpoints utilizados

  ### API externa (OpenStreetMap)
  - `https://tile.openstreetmap.org` – tiles consumidos vía OSMDroid para renderizar el mapa en `MatchmakingScreenNew`.

  ### Microservicio propio (Spring Boot)
  Base URL: `https://nextmach-backend.onrender.com`

  - `POST /api/auth/login` – login de usuario.
  - `POST /api/auth/register` – registro.
  - `GET /api/teams` – lista todos los equipos (incluídos los semilla).
  - `GET /api/teams/my` – equipos del usuario autenticado.
  - `POST /api/teams` – crea un equipo.
  - `PUT /api/teams/{id}` – actualiza equipo propio.
  - `DELETE /api/teams/{id}` – elimina equipo propio.
  - `POST /api/players` – crea jugador asignado a un equipo.
  - `PUT /api/players/{id}` / `DELETE /api/players/{id}` – mantenimiento de jugadores.
  - `GET /api/players?equipoId=...` – lista jugadores de un equipo.
  - `POST /api/reservations` / `GET /api/reservations` – manejo de reservas.
  - (Opcional) `GET /api/fields` – catastro de canchas usado en el mapa.

  ## 4. Pasos para ejecutar

  ### Backend (Spring Boot)
  1. Clonar el repo backend `https://github.com/TinderSoccer/nextmach-backend.git`.
  2. Configurar el entorno:
     - Java 21.
     - Variable `MONGODB_URI` apuntando a MongoDB Atlas (por ejemplo `mongodb+srv://.../nextmatch`).
  3. Ejecutar:
     ```bash
     ./gradlew bootRun

  o desplegar en Render (ya está activo en https://nextmach-backend.onrender.com).

  ### App Android

  1. Clonar este repo (https://github.com/TinderSoccer/nextmach.git).
  2. Abrir en Android Studio (Hedgehog o superior).
  3. Confirmar que BackendApiModule.BASE_URL apunte a https://nextmach-backend.onrender.com/.
  4. Ejecutar en modo debug o instalar el APK release:

     ./gradlew :app:assembleDebug   # debug
     ./gradlew :app:assembleRelease # APK release firmado
  5. Para un build limpio, borrar app/.cxx y app/build cuando sea necesario.

  ## 5. Pruebas unitarias
juliosilvabobadilla@MacBook-Pro-de-Julio NextMatch % ./gradlew :app:testDebugUnitTest

BUILD SUCCESSFUL in 670ms
20 actionable tasks: 1 executed, 19 up-to-date

BUILD SUCCESSFUL in 1s
20 actionable tasks: 8 executed, 12 up-to-date

  ## 6. Artefactos de entrega

  Adjuntados en ava

 
  ## 7. Contexto EFT

  - Problemática: jugadores amateur tienen dificultades para organizar partidos, coordinar equipos y asegurar canchas disponibles.
  - Solución: NextMatch centraliza la creación de equipos, invitación de jugadores, agenda de partidos y reserva de canchas con ubicación en mapa.
  - Roles: Administrador de equipo, jugador invitado, administrador NextMatch (datos semilla).
  - Componentes externos: OpenStreetMap (mapa) + microservicio propio en Spring Boot desplegado en Render.
  - Arquitectura: App Android (Jetpack Compose, Kotlin, OSMDroid) + Backend Spring Boot + MongoDB Atlas.

  ———

  

  

  
