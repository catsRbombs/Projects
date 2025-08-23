### Resumen de Instrucciones del Proyecto "Pirates of Graphebbean"

#### *Objetivo del Proyecto*
Desarrollar un juego multijugador (2 a 4 participantes) ambientado en un mar dominado por piratas, donde los jugadores compiten por el control de islas con diferentes funcionalidades. El juego debe ser accesible en red utilizando sockets y representar las áreas de cada jugador mediante grafos.

#### *Puntos Clave y Requerimientos*

1. *Espacio de Juego:*
   - *Matriz del Mar:*
     - Cada jugador posee una matriz de al menos 20x20 para su porción del mar.
     - Matriz adicional para registrar disparos y su precisión.
   - *Interfaz:*
     - Chat integrado para comunicación y mensajes de disparo.
     - Indicador del turno de disparo y estado del océano propio y ajeno.
     - Opción para visualizar el grafo gráficamente (vértices y aristas).

2. *Inicio de la Partida:*
   - Conexión de al menos dos jugadores.
   - Configuración inicial del océano por cada jugador (colocación de componentes).
   - Cada jugador comienza con:
     - 1 Fuente de Energía
     - 1 Mercado
     - $4000
   - Designación de un dueño de la partida para iniciar el juego una vez todos hayan presionado "INICIAR".

3. *Componentes del Juego:*
   - *Fuente de Energía:*
     - Tamaño: 2x2 en la matriz.
     - Destrucción causa desconexión del grafo y visibilidad de subgrafos.
     - Precio: $12000.
   - *Conectores:*
     - Tamaño: 1x1.
     - Coste: $100 cada uno.
     - No pueden conectar componentes directamente sin un conector intermedio.
   - *Mercado:*
     - Tamaño: 1x2 o 2x1.
     - Coste: $2000.
     - Permite intercambios de bienes por dinero entre jugadores.
   - *Fábricas:*
     - *Mina:*
       - Coste: $1000.
       - Producción: 50 Kg de acero cada 60 segundos.
     - *Templo de la Bruja:*
       - Coste: $2500.
       - Genera comodines cada 5 minutos.
     - *Armería:*
       - Coste: $1500.
       - Fabrica armas.
   - *Armas:*
     - *Cañón, Cañón Multiple, Bomba, Cañón Barba Roja* con diferentes costos y funcionalidades.
   - *Remolinos:*
     - Colocados aleatoriamente al inicio.
     - Devuelven disparos al jugador que dispara si son alcanzados.
   - *Barco Fantasma:*
     - Coste: $2500.
     - Proporciona información del mar enemigo tras 90 segundos.
   - *Comodines:*
     - *Escudo:* Protege de 2 a 5 disparos.
     - *Kraken:* Destruye un componente aleatorio del enemigo.

4. *Mecánicas del Juego:*
   - *Disparos:*
     - Los jugadores disparan para destruir componentes enemigos.
     - Los disparos deben registrarse en una bitácora.
   - *Bitácora y Mensajería:*
     - Registro de todos los disparos y sus resultados.
     - Chat para comunicación entre jugadores.
   - *Final de la Partida:*
     - Un jugador pierde cuando se eliminan todos sus componentes.
     - Gana el último jugador con componentes restantes.

5. *Generalidades:*
   - Fecha de entrega: Domingo 24 de noviembre de 2024.
   - Desarrollo en tríos, parejas o individual.
   - Uso de grafos y sockets.
   - Lenguaje de programación libre.

#### *Plan de Desarrollo*

1. *Planificación Inicial (Semana 1)*
   - *Formación de Equipos:* Organizarse en tríos, parejas o individual.
   - *Selección de Lenguaje y Herramientas:* Elegir el lenguaje de programación y las tecnologías a utilizar (por ejemplo, Python con sockets, Java, C#, etc.).
   - *Definición de Roles:* Asignar responsabilidades dentro del equipo (desarrollo, diseño, integración, etc.).

2. *Diseño del Juego (Semana 2-3)*
   - *Diseño de la Matriz y Componentes:*
     - Definir la estructura de las matrices (20x20).
     - Especificar cómo se representarán los diferentes componentes en la matriz.
   - *Estructura del Grafo:*
     - Decidir cómo se implementarán los grafos para representar las conexiones entre islas.
   - *Interfaz de Usuario:*
     - Diseñar prototipos de las pantallas del juego, incluyendo el chat, la matriz de disparos, indicadores de turno, etc.

3. *Desarrollo del Backend (Semana 4-6)*
   - *Implementación de la Conexión en Red:*
     - Configurar sockets para la comunicación entre jugadores.
   - *Lógica del Juego:*
     - Programar las reglas básicas del juego (colocación de componentes, turnos, disparos).
     - Implementar la lógica para la destrucción de componentes y efectos en el grafo.
   - *Gestión de Recursos:*
     - Manejar el dinero inicial, compras de componentes, y producción de recursos (como el acero).

4. *Desarrollo del Frontend (Semana 7-9)*
   - *Interfaz Gráfica:*
     - Crear la representación visual de las matrices y grafos.
     - Implementar el chat y la bitácora.
   - *Interacción del Usuario:*
     - Desarrollar la funcionalidad para seleccionar y disparar a enemigos.
     - Implementar la visualización del estado del océano y turnos.

5. *Implementación de Componentes Avanzados (Semana 10-12)*
   - *Armas y Remolinos:*
     - Programar las diferentes armas y sus efectos.
     - Implementar la funcionalidad de los remolinos.
   - *Barcos Fantasma y Comodines:*
     - Desarrollar la lógica para el uso de barcos fantasma y la generación/aplicación de comodines.

6. *Integración y Pruebas (Semana 13-14)*
   - *Integración de Backend y Frontend:*
     - Asegurar que todas las partes del juego funcionen correctamente juntas.
   - *Pruebas de Red:*
     - Verificar la estabilidad y eficiencia de la conexión entre jugadores.
   - *Depuración:*
     - Identificar y corregir errores en la lógica del juego y la interfaz.

7. *Optimización y Pulido (Semana 15-16)*
   - *Mejora de la Interfaz:*
     - Refinar el diseño gráfico y la usabilidad del juego.
   - *Optimización de Rendimiento:*
     - Mejorar la eficiencia del código y la velocidad de respuesta del juego.

8. *Documentación y Presentación (Semana 17-18)*
   - *Documentación Técnica:*
     - Crear manuales de usuario y documentación del código.
   - *Preparación de la Presentación:*
     - Preparar una demostración del juego y explicar las funcionalidades desarrolladas.

9. *Entrega Final (24 de noviembre de 2024)*
   - *Revisión Final:*
     - Realizar una última verificación para asegurar que todos los requisitos están cumplidos.
   - *Entrega del Proyecto:*
     - Subir el proyecto según las indicaciones del curso y preparar cualquier material adicional requerido.

#### *Recomendaciones Adicionales*

- *Gestión de Proyectos:*
  - Utilizar herramientas como Trello o GitHub para organizar tareas y gestionar el control de versiones.
- *Comunicación Constante:*
  - Mantener una comunicación fluida dentro del equipo para resolver dudas y coordinar el trabajo.
- *División de Tareas:*
  - Asignar tareas específicas a cada miembro del equipo según sus fortalezas.
- *Iteración y Feedback:*
  - Implementar de manera iterativa y solicitar feedback frecuente para mejorar el desarrollo del juego.

Siguiendo este plan estructurado, se asegurará una implementación organizada y eficiente del proyecto "Pirates of Graphebbean", cumpliendo con todos los requisitos y plazos establecidos.