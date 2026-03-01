# 🎱 Lotería / Bingo 90 Bolas - Android Native

Una aplicación nativa para Android de alto rendimiento que simula la extracción de números para el clásico juego de Lotería o Bingo de 90 bolas. El objetivo principal de este proyecto es ofrecer una interfaz fluida, intuitiva y accesible, garantizando un rendimiento óptimo en la lógica de extracción aleatoria y una excelente experiencia de usuario (UX).

## ✨ Características Principales

* **Sorteo Aleatorio Garantizado:** Extrae números del 1 al 90 sin repeticiones de forma instantánea.
* **Tablero Interactivo:** Una cuadrícula visual que se actualiza en tiempo real, marcando en color rojo los números que ya han salido.
* **Historial en Vivo:** Una barra lateral que muestra los últimos 5 números extraídos, destacando el más reciente.
* **Cantadas por Voz (TTS):** Integración con el motor Text-to-Speech nativo de Android para "cantar" los números en español al instante de ser extraídos.
* **Reinicio Rápido:** Un botón de seguridad para limpiar el tablero, vaciar el historial y volver a mezclar las bolas en cualquier momento.

## 💻 Stack Tecnológico

* **Entorno de Desarrollo:** Android Studio Panda sobre Linux Mint 22.
* **Lenguaje:** Java.
* **Control de Versiones:** Git / GitHub.
* **UI Core:** XML, Material Design Components.

---

## 🛠️ Arquitectura y Decisiones Técnicas

Este proyecto se centra en la eficiencia, la gestión de memoria y la separación clara entre la interfaz de usuario y la lógica de negocio.

### 1. Diseño de Interfaz de Usuario (UI)
El diseño visual se construyó priorizando el rendimiento y la escalabilidad, evitando sobrecargar el hilo principal (Main Thread).

* **Jerarquía Plana con `ConstraintLayout`:** Toda la pantalla principal (`activity_main.xml`) se sostiene sobre una única raíz de `ConstraintLayout`. Esto evita el anidamiento profundo de vistas (problema común al usar múltiples `LinearLayout`), permitiendo al sistema operativo calcular las posiciones mucho más rápido y ahorrando batería.


* **Renderizado Eficiente con `RecyclerView`:** En lugar de "pintar" 90 `TextViews` estáticos en el XML (lo cual saturaría la memoria), se utilizan dos instancias de `RecyclerView` que solo renderizan y "reciclan" las celdas visibles:
  * **Tablero:** Utiliza un `GridLayoutManager` de 10 columnas.
  * **Historial:** Utiliza un `LinearLayoutManager` estándar para una lista vertical. *Nota: Para maximizar la eficiencia, las vistas del historial se instancian programáticamente en Java, evitando inflar archivos XML innecesarios.*


### 2. Lógica de Negocio y Estructuras de Datos
La lógica central requiere extraer números del 1 al 90 sin repeticiones y de forma instantánea. Para ello, se eligieron las estructuras de datos más eficientes según la operación:

| Componente | Estructura Usada | Complejidad | Justificación Técnica |
| :--- | :--- | :--- | :--- |
| **Bombo de Extracción** | `ArrayList` + `Collections.shuffle()` | **O(1)** | Usar `Math.random()` repetidamente obligaría a comprobar si el número ya salió, generando bucles `while` cada vez más largos y costosos (ineficiencia de CPU). Al pre-mezclar la lista, extraer el índice `0` es instantáneo. |
| **Historial Reciente** | `LinkedList` | **O(1)** | Insertar al principio y borrar al final en un `ArrayList` obliga a desplazar todos los índices en memoria. `LinkedList` permite usar `addFirst()` y `removeLast()` de manera directa y altamente eficiente. |

### 3. Accesibilidad y Gestión de Memoria

* **Integración Text-to-Speech (TTS):** Se utiliza el motor nativo de Android con la flag `QUEUE_FLUSH`. Esto garantiza que, si el usuario pulsa el botón de extracción muy rápido, el motor descarte el audio anterior en lugar de encolarlo, mejorando drásticamente la UX y la accesibilidad.
* **Prevención de Fugas de Memoria (Memory Leaks):** El motor TTS se apaga y destruye explícitamente sobrescribiendo el método del ciclo de vida `onDestroy()`. Dejar procesos de hardware vivos tras cerrar la `Activity` drena la batería y causa fugas de memoria.


---

## 🚀 Instalación y Uso

1. Clona este repositorio:
   ```bash
   git clone [https://github.com/tu-usuario/Loteria-Bingo.git](https://github.com/tu-usuario/Loteria-Bingo.git)
