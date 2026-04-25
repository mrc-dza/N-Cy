# N-Cy Keyboard ⌨️🚀

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![SDK](https://img.shields.io/badge/Min_SDK-30-blue?style=for-the-badge)

**N-Cy Keyboard** es un teclado IME (Input Method Editor) nativo para Android, desarrollado desde cero en Java. Está diseñado bajo una filosofía de máxima eficiencia, centrándose en ser extremadamente ligero, rápido y altamente optimizado para dispositivos con recursos limitados.

## ✨ Características Principales

* ⚡ **Rendimiento Ultraligero:** Arquitectura de renderizado optimizada que minimiza la recolección de basura (Garbage Collection) y evita llamadas redundantes al sistema (`requestLayout`), garantizando 60 FPS estables.
* 📋 **Gestor de Portapapeles Integrado:** Historial nativo avanzado con fijación de items. Procesado en memoria y con un ciclo de vida seguro que evita hilos zombis (Memory Leaks).
* 🎨 **Motor de Temas Dinámico:** Soporte nativo para alternar entre estilos visuales (Oscuro, Claro, Neón Verde) sin retrasos visuales ni necesidad de reiniciar el servicio.
* 🖱️ **Navegación Continua (Trackpad):** Sistema de gestos basado en `VelocityTracker` optimizado, permitiendo control preciso del cursor al deslizar sobre la barra espaciadora.
* 💻 **Simulador de Hardware Físico:** Mapeo avanzado de teclas (F1-F12, Inicio, Fin, Supr, teclas direccionales) y modificadores (Ctrl, Alt, Shift) para emular la experiencia completa de un teclado de PC.
* 🛠️ **Autocierre Inteligente:** Algoritmo eficiente para el manejo automático de pares de caracteres `()`, `[]`, `{}`, `""`.

## 🏗️ Arquitectura y Tecnologías

El proyecto fue construido priorizando código limpio y patrones de diseño sólidos, evitando dependencias pesadas de terceros:

* **Lenguaje:** Java puro (Android SDK).
* **UI:** XML clásico con layouts aplanados para reducir los tiempos de medición e inflado.
* **Gestión de Estado:** Implementación del patrón *Repositorio* (`RepositorioConfiguracion`) para centralizar el acceso a las `SharedPreferences`.
* **Servicio:** Implementación directa y optimizada de `InputMethodService`.

## 🚀 Instalación y Compilación

Para compilar este proyecto localmente, necesitas Android Studio o Visual Studio Code con el SDK de Android configurado.

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/mrc-dza/N-Cy.git](https://github.com/mrc-dza/N-Cy.git)