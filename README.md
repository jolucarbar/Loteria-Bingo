# Loteria-Bingo
# 🎱 Lotería / Bingo de 90 Bolas

Una aplicación nativa para Android que simula el clásico juego de Lotería o Bingo de 90 números. Diseñada para ser intuitiva, rápida y accesible, ideal para jugar en familia o con amigos.

## ✨ Características Principales

* **Sorteo Aleatorio Garantizado:** Extrae números del 1 al 90 sin repeticiones.
* **Tablero Interactivo:** Una cuadrícula visual que se actualiza en tiempo real marcando (en color rojo) los números que ya han salido.
* **Historial en Vivo:** Una barra lateral que muestra siempre los últimos 5 números extraídos, destacando el más reciente.
* **Cantacantadas por Voz (TTS):** Integración con el motor *Text-to-Speech* nativo de Android para "cantar" los números en español al instante de ser extraídos.
* **Reinicio Rápido:** Un botón de seguridad para limpiar el tablero, vaciar el historial y volver a mezclar las bolas en cualquier momento.

## 🛠️ Estructura Técnica y Arquitectura

Este proyecto está desarrollado puramente en **Java** usando **Android Studio**. La arquitectura se centra en la eficiencia y la separación clara entre la interfaz de usuario y la lógica de negocio.

### Interfaz de Usuario (UI)
* **ConstraintLayout:** Toda la pantalla principal (`activity_main.xml`) está construida sobre un único `ConstraintLayout`. Esto evita el anidamiento profundo de vistas (como ocurriría con `LinearLayout`), mejorando significativamente el rendimiento y la adaptabilidad a diferentes tamaños de pantalla.
* **RecyclerViews:** Se utilizan dos instancias de `RecyclerView` para un manejo eficiente de la memoria:
  * **Tablero:** Utiliza un `GridLayoutManager` de 10 columnas.
  * **Historial:** Utiliza un `LinearLayoutManager` estándar para una lista vertical de 5 elementos.

### Lógica de Negocio (`MainActivity.java`)
* **Gestión del Bombo:** Para garantizar que no haya números repetidos y que la extracción sea instantánea (O(1)), se genera un `ArrayList` del 1 al 90 y se mezcla una única vez usando `Collections.shuffle()` al inicio de la partida.
* **Eficiencia en el Historial:** El historial de los últimos números utiliza una estructura `LinkedList` en lugar de un `ArrayList`. Esto permite usar el método `addFirst()` para insertar el número más reciente en la parte superior de la lista con un coste computacional mínimo, desplazando y eliminando el último elemento de forma eficiente.
* **Gestión de Recursos:** El motor `TextToSpeech` se inicializa en el `onCreate` y, para evitar fugas de memoria (memory leaks), se libera correctamente sobreescribiendo el método `onDestroy()`.

## 🚀 Instalación y Uso

1. Clona este repositorio: `git clone https://github.com/tu-usuario/Loteria-Bingo.git`
2. Abre el proyecto en **Android Studio**.
3. Sincroniza los archivos de Gradle.
4. Ejecuta la aplicación en un emulador o dispositivo físico con Android.

---
*Desarrollado con ❤️ en Java y Android Studio Panda sobre Linux Mint.*
