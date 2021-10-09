# 🃏 Black Jack JAVA 🃏

Juego de Black Jack que consiste en 3 jugadores (Cliente), que juegan contra el Dealer (servidor).

👩‍💻 Integrantes:👨‍💻
- Ingrid-E
- Jean Pierre

🔨 Tareas:
- [ ] Crear un tercer jugador, agregarlo a Controlador y un espacio graficamente.
- [ ] El jugador debe pagar una apuesta inicial, en el inicio donde pone su nombre.
  - Puede ser un valor fijo, o todos deben de apostar lo mismo.
- [ ] Las cartas se deben de visualizar de forma gráfica
- [ ] Las apuestas se deben de visualizar de forma gráfica
- [ ] Terminar Ronda de Juego
  - Dealer page o recoga las ganancias segun el resultado
- [ ] Si un jugador saca **Black Jack** entonces el Dealer le paga:
  - Razon de 3 a 2
  - 3 pesos por cada 2 apostados
  - Ej. Si aposto 10 entonces 10/2 * 3 = 15
- [ ] Si gana con 21 puntos sumados entonces gana en razon 1 a 1
- [ ] Iniciar nueva ronda de juego
- [ ] Hacer los mensajes en tercera persona en la interfaz del jugador que no es su turno

🎮 Caracteristicas del Juego:
- Tiene 2 jugadores.
- Se inicia cuando todos los jugadores están conectados al servidor.
  - Cuando el ultimo jugador ingrese, entonces el juego comienza.
- Al iniciar la identifiacion del jugador es el nombre
- El servidor controla que cada jugador juegue en su turno y los va rotando. 
- El jugador puede visualizar la mesa de juego
  - Sus cartas
  - Las cartas de los demas (jugadores y dealer)
  - Mensajes con estado del juego
- El servidor no tiene interfaz gráfica
-  El Dealer es emulado por el servidor
  - Si tiene 16 > x puntos entonces debe pedir carta
  - Si tiene 17 < x puntos entonces planta 

Dinamica:
1. Abrir el servidor
2. Conectar cada jugador
3. Se visualiza la interfaz
4. Cojen cartas o se quedan quietos
5. Al final el que tiene mas puntos cerca de 21 gana
6. Jugar otra vez


