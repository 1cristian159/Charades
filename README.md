# Charades - Aplicación Android

Una aplicación completa de Charadas desarrollada en Android con Kotlin y Jetpack Compose.

## Características

### Funcionalidades Implementadas

- **Selección de Categorías**: 5 categorías disponibles (Animales, Películas, Profesiones, Deportes, Comida)
- **Temporizador Visual**: Contador circular con 1 minuto por ronda
- **Sistema de Equipos**: Juego para 2 equipos con puntuación independiente
- **Control de Tiempo**: Pausar/reanudar el juego
- **Palabras Aleatorias**: Selección aleatoria dentro de la categoría elegida
- **Efectos Visuales**: 
  - Cambio de color del temporizador según el tiempo restante
  - Animaciones de puntuación
  - Indicadores visuales para el equipo activo
- **Pantalla de Resultados**: Estadísticas completas del juego
- **Navegación Completa**: Entre todas las pantallas con botones de reinicio

### Reglas del Juego

1. **Selección de Categoría**: El jugador elige una categoría de 5 disponibles
2. **Presentación de Palabra**: Aparece una palabra que el jugador debe representar sin hablar
3. **Tiempo Limitado**: Cada ronda tiene 1 minuto de duración
4. **Adivinanza**: El equipo contrario debe adivinar la palabra dentro del tiempo
5. **Puntuación**: Se suma un punto por cada acierto en el tiempo límite
6. **Fin del Juego**: Después de 10 rondas, se muestra el puntaje acumulado

### 🎨 Interfaz Gráfica

- **Menú de Categorías**: Grid con 5 categorías disponibles
- **Pantalla de Juego**: 
  - Temporizador circular animado
  - Palabra actual en pantalla grande
  - Puntuación de ambos equipos
  - Controles para respuesta correcta/saltar
  - Botón de pausa/reanudar
- **Pantalla de Resultados**: 
  - Ganador destacado
  - Puntuaciones finales
  - Estadísticas del juego
  - Botones para reiniciar o volver al menú

### 🛠️ Tecnologías Utilizadas

- **Kotlin**: Lenguaje de programación
- **Jetpack Compose**: Framework de UI moderno
- **Material 3**: Sistema de diseño
- **ViewModel**: Gestión del estado de la aplicación
- **StateFlow**: Flujo de datos reactivo
- **Coroutines**: Programación asíncrona para el temporizador

### 📱 Requisitos del Sistema

- **Android**: API 24+ (Android 7.0)
- **Target SDK**: 36 (Android 14)
- **Compile SDK**: 36

### 🚀 Instalación y Uso

1. Clona el repositorio
2. Abre el proyecto en Android Studio
3. Sincroniza las dependencias de Gradle
4. Ejecuta la aplicación en un dispositivo o emulador

### 🎯 Flujo de la Aplicación

1. **Inicio**: Pantalla de selección de categorías
2. **Juego**: 
   - Seleccionar categoría
   - Presionar "Comenzar Juego"
   - Representar palabras durante 1 minuto
   - Usar botones "Correcto" o "Saltar"
   - Pausar/reanudar según necesidad
3. **Resultados**: Ver puntuación final y opciones de reinicio

### 🎨 Efectos Visuales

- **Temporizador**: Cambia de azul → naranja → rojo según tiempo restante
- **Animaciones**: Puntuación animada en resultados
- **Colores**: Esquema de colores Material 3 personalizado
- **Estados**: Indicadores visuales para equipo activo y tiempo agotado

### 🔧 Estructura del Proyecto

```
app/src/main/java/com/example/charades/
├── data/
│   └── GameData.kt              # Datos de categorías y palabras
├── model/
│   └── GameState.kt             # Modelo de estado del juego
├── ui/
│   ├── screens/
│   │   ├── CategorySelectionScreen.kt
│   │   ├── GameScreen.kt
│   │   └── ResultsScreen.kt
│   └── theme/                   # Tema y colores
├── viewmodel/
│   └── GameViewModel.kt         # Lógica del juego
└── MainActivity.kt              # Actividad principal
```

### 🎉 Características Adicionales

- **Responsive Design**: Adaptable a diferentes tamaños de pantalla
- **Accesibilidad**: Iconos descriptivos y texto claro
- **Performance**: Uso eficiente de recursos con Compose
- **Código Limpio**: Arquitectura MVVM con separación de responsabilidades

¡Disfruta jugando Charadas con esta aplicación completa y moderna!
