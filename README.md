# Neon Defender - Android Dashboard

![Versión](https://img.shields.io/badge/Versión-1.0.0-blueviolet)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0-orange)
![Compose](https://img.shields.io/badge/Jetpack_Compose-Material3-green)
![AWS](https://img.shields.io/badge/Backend-AWS_Lambda-ff9900)

**Neon Defender** es una aplicación móvil nativa desarrollada para visualizar las estadísticas y el historial del videojuego. La app destaca por su interfaz futurista con estética neón y una integración fluida con servicios en la nube.

---

## Características Principales

*   **Autenticación Real:** Conexión con una API Gateway y AWS Lambda para validar usuarios en una base de datos PostgreSQL remota.
*   **Diseño Inmersivo (Neon Style):** Interfaz personalizada con temas oscuros, gradientes neón y componentes Material 3.
*   **Historial de Batalla:** Visualización de las últimas 20 partidas mediante un *Navigation Drawer* interactivo.
*   **Estadísticas Detalladas:**
    *   Resultados de victoria, derrota o rendición con código de colores.
    *   Desglose de daño infligido y recibido.
    *   Gestión de economía (Oro ganado y gastado).
    *   **Tablas Dinámicas:** Listado de torres utilizadas y enemigos eliminados con carga de iconos en tiempo real basada en datos de la BD.
*   **Responsividad:** Pantalla de login adaptada para modo vertical y horizontal con soporte para scroll.

---

## Arquitectura y Tecnologías

La aplicación sigue los principios de **Clean Code** y las mejores prácticas recomendadas por Google:

*   **Arquitectura:** MVVM (Model-View-ViewModel).
*   **UI:** Jetpack Compose (Declarativa).
*   **Navegación:** Navigation Compose con rutas seguras.
*   **Red:** Retrofit 2 + OkHttp (con interceptor de logs).
*   **Serialización:** GSON para el mapeo del JSON dinámico.
*   **Gestión de Estado:** StateFlow y MutableState para una UI reactiva.
*   **Inyección de Dependencias:** Patrón Singleton para el cliente de red.

---

## Estructura del Proyecto (Paquetes)

```text
com.example.proyectotfg/
├── model/           # Clases de datos (Dominio y Red)
├── viewmodel/       # Lógica de negocio y gestión de estados
├── ui/
│   ├── screens/     # Pantallas (Login, Dashboard)
│   └── theme/       # Personalización de colores, tipografía y tema
├── navigation/      # Configuración del NavHost y rutas
└── network/         # Cliente Retrofit e interfaces de API
```

---

## Configuración y Ejecución

### Requisitos previos
*   **Android Studio** (Versión Ladybug o superior recomendada).
*   **JDK 17** configurado en el proyecto.
*   **Android SDK 36** (o compatible).

### Instalación
1. Clonar el repositorio.
2. Realizar un **Gradle Sync** para descargar las dependencias (Retrofit, Compose, etc.).
3. Configurar la URL de la API en `network/RetrofitClient.kt` si es necesario.
4. Ejecutar en un emulador o dispositivo físico.

---

## Integración con AWS Lambda

La aplicación consume un endpoint POST que devuelve un JSON estructurado con el siguiente formato:

```json
{
  "usuario": "Nickname",
  "partidas": [
    {
      "id_partida": 1,
      "fecha": "YYYY-MM-DD HH:MM:SS",
      "estado": "victoria",
      "nivel": "normal",
      "estadisticas": { "dano_hecho": 100, ... },
      "torres": [ { "nombre": "Tesla", "cantidad": 5 }, ... ],
      "enemigos": [ { "nombre": "Pyxer", "cantidad": 10 }, ... ]
    }
  ]
}
```

---

Este proyecto ha sido desarrollado como parte del **Trabajo de Fin de Grado: NEON DEFENDER**.
