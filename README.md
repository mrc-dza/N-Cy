# N-Cy Keyboard ⌨️

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Min SDK](https://img.shields.io/badge/Min_SDK-30-blue?style=for-the-badge)

**N-Cy** es un teclado IME nativo para Android, desarrollado desde cero en Java puro. Diseñado para ser extremadamente ligero y rápido, sin dependencias de terceros, con foco en dispositivos de recursos limitados.

## Características

**Rendimiento**
Renderizado por Canvas personalizado (`DibujanteTecla`, `DibujanteGestos`) que evita `requestLayout` innecesarios y minimiza la presión sobre el GC. Target: 60 FPS estables en hardware modesto.

**Gestor de portapapeles**
Historial nativo con fijación de ítems. Ciclo de vida gestionado para evitar memory leaks y hilos zombis.

**Motor de temas**
Alternancia dinámica entre Dark, Light y Neon sin reiniciar el servicio IME.

**Trackpad de cursor**
Gestos sobre la barra espaciadora vía `VelocityTracker` para posicionamiento preciso del cursor.


## Arquitectura

| Componente | Descripción |
|---|---|
| `InputMethodService` | Servicio base del IME, sin wrappers intermedios |
| `DibujanteTecla` | Renderizado de teclas sobre Canvas con soporte de densidad (dp→px) |
| `DibujanteGestos` | Manejo visual de gestos y feedback táctil |
| `RepositorioConfiguracion` | Patrón Repositorio sobre `SharedPreferences` |
| `ManejadorEdicion` | Lógica de edición e inyección de texto |
| `GestorPortapapeles` | Historial de portapapeles con ciclo de vida seguro |
| `GestorTema` | Gestión centralizada de temas visuales |
| `CalculadorLayout` | Cálculo de dimensiones en función del display |

Internamente se usan tablas de lookup (`String.indexOf()`) en lugar de `switch` para operaciones frecuentes, reduciendo overhead en el hilo de entrada.

## Requisitos

- Android **API 30** (Android 11) o superior
- Android Studio Hedgehog o posterior (para compilar)
- JDK 11+

## Instalación

```bash
git clone https://github.com/mrc-dza/N-Cy.git
cd N-Cy
```

Abrir el proyecto en Android Studio, sincronizar Gradle y ejecutar en un dispositivo o emulador con API ≥ 30.

Para activar el teclado:
1. Ajustes → Sistema → Idioma e introducción de texto → Teclado en pantalla
2. Activar **N-Cy Keyboard**
3. Seleccionarlo como teclado predeterminado

