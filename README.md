# N-Cy Keyboard

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Min SDK](https://img.shields.io/badge/Min_SDK-29-blue?style=for-the-badge)

N-Cy es un teclado IME nativo para Android, desarrollado desde cero en Java puro. Está diseñado para ser ligero y rápido, sin dependencias de terceros, con foco en dispositivos de recursos limitados.

## Características

**Rendimiento**
Renderizado mediante Canvas personalizado (`DibujanteTecla`, `DibujanteGestos`), evitando reconstrucciones de vista innecesarias. El teclado se reconstruye únicamente cuando cambia el tema o el layout (a través de `ReconstruibleTeclado`), no en cada pulsación.

**Gestor de portapapeles**
Historial nativo (`GestorPortapapeles`) con fijación de ítems y persistencia mediante `RepositorioHistorial`. Escucha el portapapeles del sistema a través de `EscuchadorPortapapelesSistema`, con ciclo de vida gestionado para evitar listeners huérfanos.

**Motor de temas**
`GestorTema` administra la alternancia entre temas (Oscuro, Claro, Neon Verde, NES) sin reiniciar el servicio IME, aplicando los cambios directamente sobre la vista activa.

**Trackpad de cursor**
Gestos sobre la barra espaciadora mediante `VelocityTracker` (implementado en `ControladorToqueXml`), permitiendo posicionamiento preciso del cursor sin necesidad de interactuar directamente con el campo de texto.

**Sistema de ajustes**
Pantalla de configuración (`SettingsActivity`) con opciones de vibración al pulsar, altura del teclado ajustable, mayúsculas automáticas y barra de herramientas opcional, persistidas mediante `RepositorioConfiguracion` sobre `SharedPreferences`.

## Arquitectura

| Componente | Descripción |
|---|---|
| `ServicioTeclado` | Extiende `InputMethodService` directamente, sin wrappers intermedios |
| `DibujanteTecla` | Renderizado de teclas sobre Canvas con soporte de densidad (dp a px) |
| `DibujanteGestos` | Manejo visual de gestos y feedback táctil |
| `RepositorioConfiguracion` | Patrón Repositorio sobre `SharedPreferences` |
| `ManejadorEdicion` | Lógica de edición e inyección de texto |
| `GestorPortapapeles` | Historial de portapapeles con ciclo de vida seguro |
| `GestorTema` | Gestión centralizada de temas visuales |
| `CalculadorLayout` | Cálculo de dimensiones en función del display |
| `ContenedorNcy` | Contenedor de dependencias manual, encargado de ensamblar e inyectar objetos |
| `Tecla` (y subclases) | Modelo de dominio puro, sin dependencias de UI ni de Android |

El modelo de teclas (`Tecla`, `TeclaCaracter`, `TeclaAccion`, `TeclaEmoji`, `TeclaMacro`, `TeclaModificador`) es independiente de la capa de interfaz. Los layouts del teclado (QWERTY, símbolos, emojis) se definen de forma declarativa en XML en lugar de código Java.

## Requisitos

- Android API 29 (Android 10) o superior
- Android Studio Hedgehog o posterior, para compilar
- JDK 17

## Instalación

```bash
git clone https://github.com/mrc-dza/N-Cy.git
cd N-Cy
```

Abrir el proyecto en Android Studio, sincronizar Gradle y ejecutar en un dispositivo o emulador con API 29 o superior.

Para activar el teclado:

1. Ajustes → Sistema → Idioma e introducción de texto → Teclado en pantalla
2. Activar N-Cy Keyboard
3. Seleccionarlo como teclado predeterminado

## Estado del proyecto

Versión actual: 1.0.0 (versionCode 2). El proyecto no declara dependencias externas: temas, portapapeles y renderizado están implementados únicamente con el SDK de Android.

## Licencia

Especificar aquí el tipo de licencia bajo la cual se distribuye el proyecto.
