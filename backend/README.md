# NextMatch Backend

Spring Boot API que actúa como puente entre la app Android y MongoDB (Atlas o local).

## Requisitos
- JDK 17
- Gradle 8 (se usa el wrapper del proyecto)
- Una instancia de MongoDB (Atlas recomendado)

## Configuración
1. Crea un usuario en tu cluster Atlas y copia la URI.
2. Exporta la variable `MONGODB_URI` antes de levantar la app:
   ```bash
   export MONGODB_URI="mongodb+srv://usuario:clave@cluster.mongodb.net/nextmatch"
   ```
   Si prefieres correrlo local, deja que use el valor por defecto `mongodb://localhost:27017/nextmatch`.

## Ejecutar
Desde la raíz del repo:
```bash
./gradlew :backend:bootRun
```
El API quedará disponible en `http://localhost:8080` y la documentación Swagger en `http://localhost:8080/swagger-ui.html`.

## Endpoints principales
- `POST /api/teams`, `GET /api/teams`, `PUT /api/teams/{id}`, `DELETE /api/teams/{id}`
- `POST /api/players`, `GET /api/players?equipoId=...`, `PUT /api/players/{id}`, `DELETE /api/players/{id}`
- `POST /api/fields`, `GET /api/fields`, `PUT /api/fields/{id}`, `DELETE /api/fields/{id}` (lat/lon, dirección, servicios)
- `POST /api/reservations`, `GET /api/reservations?equipoId=...&fecha=YYYY-MM-DD`, `PUT /api/reservations/{id}`, `DELETE /api/reservations/{id}` (ahora pueden incluir `fieldId` además de `cancha`)

## Próximos pasos
- Añadir autenticación (API keys o JWT) si se requiere.
- Conectar la app Android usando Retrofit: bastaría con crear servicios `TeamApi`, `PlayerApi`, `ReservationApi` apuntando a estos endpoints y mapear las respuestas al UI state.
