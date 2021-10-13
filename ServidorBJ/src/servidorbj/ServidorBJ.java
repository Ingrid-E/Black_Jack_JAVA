package servidorbj;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JOptionPane;

import comunes.Baraja;
import comunes.Carta;
import comunes.DatosBlackJack;

/* Clase encargada de realizar la gesti�n del juego, esto es, el manejo de turnos y estado del juego.
 * Tambi�n gestiona al jugador Dealer. 
 * El Dealer tiene una regla de funcionamiento definida:
 * Pide carta con 16 o menos y Planta con 17 o mas.
 */
public class ServidorBJ implements Runnable{
	//constantes para manejo de la conexion.
	public static final int PUERTO=7377;
	public static final String IP="127.0.0.1";
	public static final int LONGITUD_COLA=3;

	// variables para funcionar como servidor
	private ServerSocket server;
	private Socket conexionJugador;
	
	//variables para manejo de hilos
	private ExecutorService manejadorHilos;
	private Lock bloqueoJuego;
	@SuppressWarnings("unused")
	private Condition esperarInicio, esperarTurno, finalizar;
	private Jugador[] jugadores;
	
	//variables de control del juego
	private String[] idJugadores;
	private int[] apuestaJugadores;
	private int jugadorEnTurno;
	//private boolean iniciarJuego;
	private Baraja mazo;
	private ArrayList<ArrayList<Carta>> manosJugadores;
	private ArrayList<Carta> manoJugador1, manoJugador2, manoJugador3;
	private ArrayList<Carta> manoDealer;
	private int[] valorManos;
	private DatosBlackJack datosEnviar;
	
	public ServidorBJ() {
	    //inicializar variables de control del juego
		inicializarVariablesControlRonda();
	    //inicializar las variables de manejo de hilos
		inicializareVariablesManejoHilos();
		//crear el servidor
    	try {
    		mostrarMensaje("Iniciando el servidor...");
			server = new ServerSocket(PUERTO,LONGITUD_COLA);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    private void inicializareVariablesManejoHilos() {
		// TODO Auto-generated method stub
    	manejadorHilos = Executors.newFixedThreadPool(LONGITUD_COLA);
		bloqueoJuego = new ReentrantLock();
		esperarInicio = bloqueoJuego.newCondition();
		esperarTurno = bloqueoJuego.newCondition();
		finalizar = bloqueoJuego.newCondition();
		jugadores = new Jugador[LONGITUD_COLA];	
	}

	private void inicializarVariablesControlRonda() {
		// TODO Auto-generated method stub
    	 //Variables de control del juego.
		
		idJugadores = new String[3];
		valorManos = new int[4];
		apuestaJugadores = new int[3];
		
		mazo = new Baraja();
		Carta carta;
		
		manoJugador1 = new ArrayList<Carta>();
		manoJugador2 = new ArrayList<Carta>();
		manoJugador3 = new ArrayList<Carta>();
		manoDealer = new ArrayList<Carta>();
		
		//reparto inicial jugadores 1, 2 y 3
		for(int i=1;i<=2;i++) {
		  carta = mazo.getCarta();
		  manoJugador1.add(carta);
		  calcularValorMano(carta,0);
		  carta = mazo.getCarta();
		  manoJugador2.add(carta);
		  calcularValorMano(carta,1);
		  carta = mazo.getCarta();
		  manoJugador3.add(carta);
		  calcularValorMano(carta,2);
		}
		//Carta inicial Dealer
		carta = mazo.getCarta();
		manoDealer.add(carta);
		calcularValorMano(carta,3);
		
		//gestiona las tres manos en un solo objeto para facilitar el manejo del hilo
		manosJugadores = new ArrayList<ArrayList<Carta>>(4);
		manosJugadores.add(manoJugador1);
		manosJugadores.add(manoJugador2);
		manosJugadores.add(manoJugador3);
		manosJugadores.add(manoDealer);
	}

	private void calcularValorMano(Carta carta, int i) {
		// TODO Auto-generated method stub
    	
			if(carta.getValor().equals("As")) {
				valorManos[i]+=11;
			}else {
				if(carta.getValor().equals("J") || carta.getValor().equals("Q")
						   || carta.getValor().equals("K")) {
					valorManos[i]+=10;
				}else {
					valorManos[i]+=Integer.parseInt(carta.getValor()); 
				}
		}
	}
	
	private void determinarGanarPerder() {
		String mensajeInicial = datosEnviar.getMensaje();
		for(Jugador jugador : jugadores) {
			int i = jugador.indexJugador;
			//Operadores booleanos
			System.out.println("Jugador: " + i + " cartas: " + valorManos[i]);
			boolean dealerVolo = datosEnviar.getJugadorEstado() == "vol�" && valorManos[i] <= 21;
			boolean jugadorGanoDealer = datosEnviar.getJugadorEstado() != "vol�" && valorManos[3] < valorManos[i] && valorManos[i] <= 21;
			boolean jugadorPerdioDealer = datosEnviar.getJugadorEstado() != "vol�" && valorManos[3] > valorManos[i];
			boolean jugadorVolo = valorManos[i] > 21;
			boolean empatados = datosEnviar.getJugadorEstado() != "vol�" && valorManos[3] == valorManos[i];
			
			if(dealerVolo || jugadorGanoDealer) {
				datosEnviar.setMensaje(mensajeInicial + calcularGananciasOPerdidas(i, "Ganaste"));
				mostrarMensaje("Ganaste if");
				jugadores[i].enviarMensajeCliente(datosEnviar);
			}else if(jugadorPerdioDealer || jugadorVolo) {
				datosEnviar.setMensaje(mensajeInicial + "Perdiste! \n" + calcularGananciasOPerdidas(i, "Perdiste"));
				mostrarMensaje("Perdiste else");
				jugadores[i].enviarMensajeCliente(datosEnviar);
			}else if (empatados) {
				datosEnviar.setMensaje(mensajeInicial + "Empataste! \n" + calcularGananciasOPerdidas(i, "Empataste"));
				mostrarMensaje("empataste");
				jugadores[i].enviarMensajeCliente(datosEnviar);
			}
		}
	}
	
	public String calcularGananciasOPerdidas(int numeroJugador, String resultado) {
		switch(resultado) {
		case "Ganaste":
			if (numeroJugador == 0) {
				if (manoJugador1.size() == 2 && valorManos[numeroJugador] == 21) {
					return "Ganaste con Black Jack!! \nEl dealer paga un total de " + (apuestaJugadores[numeroJugador]/2) * 3;
				} else {
					return "Ganaste por sumatoria!! \nEl dealer paga un total de " + apuestaJugadores[numeroJugador];
				}
			}
			if (numeroJugador == 1) {
				if (manoJugador2.size() == 2 && valorManos[numeroJugador] == 21) {
					return "Ganaste con Black Jack!! \nEl dealer paga un total de " + (apuestaJugadores[numeroJugador]/2) * 3;
				} else {
					return "Ganaste por sumatoria!! \nEl dealer paga un total de " + apuestaJugadores[numeroJugador];
				}
			}
			if (numeroJugador == 2) {
				if (manoJugador3.size() == 2 && valorManos[numeroJugador] == 21) {
					return "Ganaste con Black Jack!! \nEl dealer paga un total de " + (apuestaJugadores[numeroJugador]/2) * 3;
				} else {
					return "Ganaste por sumatoria!! \nEl dealer paga un total de " + apuestaJugadores[numeroJugador];
				}
			}
			
		case "Perdiste":
			return "Perdiste tus " + apuestaJugadores[numeroJugador];
		case "Empataste":
			return "Recuperaste tus " + apuestaJugadores[numeroJugador];
		default :
			return "error";	
		}
	}
	
	public void iniciar() {
       	//esperar a los clientes
    	mostrarMensaje("Esperando a los jugadores...");
    	
    	for(int i=0; i<LONGITUD_COLA;i++) {
    		try {
				conexionJugador = server.accept();
				jugadores[i] = new Jugador(conexionJugador,i);
	    		manejadorHilos.execute(jugadores[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
    	} 	
    }
    
	private void mostrarMensaje(String mensaje) {
		System.out.println(mensaje);
	}
	
	private void iniciarRondaJuego() {
		
		this.mostrarMensaje("bloqueando al servidor para despertar al jugador 1");
    	bloqueoJuego.lock();
    	
    	//despertar al jugador 1 porque es su turno
    	try {
    		this.mostrarMensaje("Despertando al jugador 1 y 2 para que inicie el juego");
        	jugadores[0].setSuspendido(false);
        	jugadores[1].setSuspendido(false);
        	esperarInicio.signal();
    	}catch(Exception e) {
    		
    	}finally {
    		this.mostrarMensaje("Desbloqueando al servidor luego de despertar al jugador 1 y 2 para que inicie el juego");
    		bloqueoJuego.unlock();
    	}			
	}
	
    private boolean seTerminoRonda() {
       return false;	
    }
    
    private void analizarMensaje(String entrada, int indexJugador) {
		// TODO Auto-generated method stub
        //garantizar que solo se analice la petici�n del jugador en turno.
    	while(indexJugador!=jugadorEnTurno) {
    		bloqueoJuego.lock();
    		try {
				esperarTurno.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		bloqueoJuego.unlock();
    	}
    	
    	//valida turnos para jugador 0 o 1
        	
    	if(entrada.equals("pedir")) {
    		//dar carta 
    		mostrarMensaje("Se envi� carta al jugador "+idJugadores[indexJugador]);
    		Carta carta = mazo.getCarta();
    		//adicionar la carta a la mano del jugador en turno
    		manosJugadores.get(indexJugador).add(carta);
    		calcularValorMano(carta, indexJugador);
    		
    		datosEnviar = new DatosBlackJack();
    		datosEnviar.setIdJugadores(idJugadores);
			datosEnviar.setValorManos(valorManos);
			datosEnviar.setCarta(carta);
			datosEnviar.setJugador(idJugadores[indexJugador]);
    		//determinar qu� sucede con la carta dada en la mano del jugador y 
			//mandar mensaje a todos los jugadores
    		if(valorManos[indexJugador]>21) {
    			//jugador Vol�
	    		datosEnviar.setMensaje(idJugadores[indexJugador]+" tienes "+valorManos[indexJugador]+" volaste :(");	
	    		datosEnviar.setJugadorEstado("vol�");
	    		
	    		jugadores[0].enviarMensajeCliente(datosEnviar);
	    		jugadores[1].enviarMensajeCliente(datosEnviar);
	    		jugadores[2].enviarMensajeCliente(datosEnviar);
	    		
	    		//notificar a todos que jugador sigue
	    		int siguienteJugador = jugadorEnTurno+1;
	    		if(jugadorEnTurno==0 || jugadorEnTurno == 1) {
	        	
	        		datosEnviar = new DatosBlackJack();
		    		datosEnviar.setIdJugadores(idJugadores);
					datosEnviar.setValorManos(valorManos);
					datosEnviar.setJugador(idJugadores[siguienteJugador]);
					datosEnviar.setJugadorEstado("iniciar");
					datosEnviar.setMensaje(idJugadores[siguienteJugador]+" te toca jugar y tienes "+valorManos[siguienteJugador]);
					jugadores[0].enviarMensajeCliente(datosEnviar);
					jugadores[1].enviarMensajeCliente(datosEnviar);
					jugadores[2].enviarMensajeCliente(datosEnviar);
					
					//levantar al jugador en espera de turno
					
					bloqueoJuego.lock();
		    		try {
						//esperarInicio.await();
						jugadores[jugadorEnTurno].setSuspendido(true);
						esperarTurno.signalAll();
						jugadorEnTurno++;
					}finally {
						bloqueoJuego.unlock();
					}
	        	} else {//era el jugador 2 entonces se debe iniciar el dealer
	        		//notificar a todos que le toca jugar al dealer
	        		datosEnviar = new DatosBlackJack();
		    		datosEnviar.setIdJugadores(idJugadores);
					datosEnviar.setValorManos(valorManos);
					datosEnviar.setJugador("dealer");
					datosEnviar.setJugadorEstado("iniciar");
					datosEnviar.setMensaje("Dealer se repartir� carta");
					
					jugadores[0].enviarMensajeCliente(datosEnviar);
					jugadores[1].enviarMensajeCliente(datosEnviar);
					jugadores[2].enviarMensajeCliente(datosEnviar);
					iniciarDealer();
	        	}		
    		}else {//jugador no se pasa de 21 puede seguir jugando
    			datosEnviar.setCarta(carta);
    			datosEnviar.setJugador(idJugadores[indexJugador]);
    			datosEnviar.setMensaje(idJugadores[indexJugador]+" ahora tienes "+valorManos[indexJugador]);
	    		datosEnviar.setJugadorEstado("sigue");
	    		
	    		jugadores[0].enviarMensajeCliente(datosEnviar);
	    		jugadores[1].enviarMensajeCliente(datosEnviar);
	    		jugadores[2].enviarMensajeCliente(datosEnviar);
	    		
    		}
    	}else {
    		//jugador en turno plant�
    		datosEnviar = new DatosBlackJack();
    		datosEnviar.setIdJugadores(idJugadores);
			datosEnviar.setValorManos(valorManos);
			datosEnviar.setJugador(idJugadores[indexJugador]);
    		datosEnviar.setMensaje(idJugadores[indexJugador]+" se plant�");
    		datosEnviar.setJugadorEstado("plant�");
    		
    		jugadores[0].enviarMensajeCliente(datosEnviar);		    		
    		jugadores[1].enviarMensajeCliente(datosEnviar);
    		jugadores[2].enviarMensajeCliente(datosEnviar);
    		
    		
    		//notificar a todos el jugador que sigue en turno
    		if(jugadorEnTurno==0 || jugadorEnTurno == 1) {
        		int jugadorActual = jugadorEnTurno+1;
        		datosEnviar = new DatosBlackJack();
	    		datosEnviar.setIdJugadores(idJugadores);
				datosEnviar.setValorManos(valorManos);
				datosEnviar.setJugador(idJugadores[jugadorActual]);
				datosEnviar.setJugadorEstado("iniciar");
				datosEnviar.setMensaje(idJugadores[jugadorActual]+" te toca jugar y tienes "+valorManos[jugadorActual] + "\n" + "Apostaste " + apuestaJugadores[jugadorActual]);
				
				jugadores[0].enviarMensajeCliente(datosEnviar);
				jugadores[1].enviarMensajeCliente(datosEnviar);
				jugadores[2].enviarMensajeCliente(datosEnviar);
				
				//levantar al jugador en espera de turno
				
				bloqueoJuego.lock();
	    		try {
					//esperarInicio.await();
					jugadores[indexJugador].setSuspendido(true);
					esperarTurno.signalAll();
					jugadorEnTurno++;
				}finally {
					bloqueoJuego.unlock();
				}
        	} else {
        		//notificar a todos que le toca jugar al dealer
        		datosEnviar = new DatosBlackJack();
	    		datosEnviar.setIdJugadores(idJugadores);
				datosEnviar.setValorManos(valorManos);
				datosEnviar.setJugador("dealer");
				datosEnviar.setJugadorEstado("iniciar");
				datosEnviar.setMensaje("Dealer se repartir� carta");
				
				jugadores[0].enviarMensajeCliente(datosEnviar);
				jugadores[1].enviarMensajeCliente(datosEnviar);
				jugadores[2].enviarMensajeCliente(datosEnviar);
			
				iniciarDealer();
        	}	
    	}
   } 
    
    public void iniciarDealer() {
       //le toca turno al dealer.
    	Thread dealer = new Thread(this);
    	dealer.start();
    }
    
    /*The Class Jugador. Clase interna que maneja el servidor para gestionar la comunicaci�n
     * con cada cliente Jugador que se conecte
     */
    private class Jugador implements Runnable{
       
    	//varibles para gestionar la comunicaci�n con el cliente (Jugador) conectado
        @SuppressWarnings("unused")
		private Socket conexionCliente;
    	private ObjectOutputStream out;
    	private ObjectInputStream in;
    	private String entrada;
    	
    	//variables de control
    	protected int indexJugador;
    	private boolean suspendido;
  
		public Jugador(Socket conexionCliente, int indexJugador) {
			this.conexionCliente = conexionCliente;
			this.indexJugador = indexJugador;
			suspendido = true;
			//crear los flujos de E/S
			try {
				out = new ObjectOutputStream(conexionCliente.getOutputStream());
				out.flush();
				in = new ObjectInputStream(conexionCliente.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}	
				
		private void setSuspendido(boolean suspendido) {
			this.suspendido = suspendido;
		}
	   
		@Override
		public void run() {
			// TODO Auto-generated method stub	
			//procesar los mensajes eviados por el cliente
			
			//ver cual jugador es 
			if(indexJugador==0) {
				//es jugador 1, debe ponerse en espera a la llegada del otro jugador
				
				try {
					//guarda el nombre del primer jugador
					idJugadores[0] = (String)in.readObject();
					apuestaJugadores[0] = Integer.parseInt(JOptionPane.showInputDialog(null, "Valor de la apuesta: ", "Apuesta Jugador 1", JOptionPane.QUESTION_MESSAGE));
					mostrarMensaje("Hilo establecido con jugador (1) "+idJugadores[0]);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mostrarMensaje("bloquea servidor para poner en espera de inicio al jugador 1");
				bloqueoJuego.lock(); //bloquea el servidor
				
				while(suspendido) {
					mostrarMensaje("Parando al Jugador 1 en espera del otro jugador...");
					try {
						esperarInicio.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						//JOptionPane.showInputDialog(null, "Insert value: ", "The title", JOptionPane.QUESTION_MESSAGE);
						mostrarMensaje("Desbloquea Servidor luego de bloquear al jugador 1");
						jugadores[1].setSuspendido(false);
						esperarInicio.signal();
						bloqueoJuego.unlock();
					}
				}
				
				//ya se conect� el otro jugador, 
				//le manda al jugador 1 todos los datos para montar la sala de Juego
				//le toca el turno a jugador 1
				
				mostrarMensaje("manda al jugador 1 todos los datos para montar SalaJuego");
				datosEnviar = new DatosBlackJack();
				datosEnviar.setManoDealer(manosJugadores.get(3));
				datosEnviar.setManoJugador1(manosJugadores.get(0));
				datosEnviar.setManoJugador2(manosJugadores.get(1));		
				datosEnviar.setManoJugador3(manosJugadores.get(2));	
				datosEnviar.setIdJugadores(idJugadores);
				datosEnviar.setValorManos(valorManos);
				datosEnviar.setMensaje("Inicias "+idJugadores[0]+" tienes "+valorManos[jugadorEnTurno] + "\n" + "Apostaste " + apuestaJugadores[jugadorEnTurno]);
				enviarMensajeCliente(datosEnviar);
				jugadorEnTurno=0;
				
			}else if(indexJugador == 1) {
				//es jugador 2, debe ponerse en espera a la llegada del otro jugador
				
				try {
					//guarda el nombre del primer jugador
					idJugadores[1] = (String)in.readObject();
					apuestaJugadores[1] = Integer.parseInt(JOptionPane.showInputDialog(null, "Valor de la apuesta: ", "Apuesta Jugador 2", JOptionPane.QUESTION_MESSAGE));
					mostrarMensaje("Hilo establecido con jugador (2) "+idJugadores[1]);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
				
				mostrarMensaje("bloquea servidor para poner en espera de inicio al jugador 2");
				bloqueoJuego.lock(); //bloquea el servidor
				
				while(suspendido) {
					mostrarMensaje("Parando al Jugador 2 en espera del otro jugador...");
					try {
						esperarInicio.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						mostrarMensaje("Desbloquea Servidor luego de bloquear al jugador 2");
						//JOptionPane.showInputDialog(null, "Insert value: ", "The title", JOptionPane.QUESTION_MESSAGE);
						bloqueoJuego.unlock();
					}
				}
				//ya se conect� el otro jugador, 
				//le manda al jugador 1 todos los datos para montar la sala de Juego
				//le toca el turno a jugador 1
				mostrarMensaje("manda al jugador 2 todos los datos para montar SalaJuego");
				datosEnviar = new DatosBlackJack();
				datosEnviar.setManoDealer(manosJugadores.get(3));
				datosEnviar.setManoJugador1(manosJugadores.get(0));
				datosEnviar.setManoJugador2(manosJugadores.get(1));
				datosEnviar.setManoJugador3(manosJugadores.get(2));	
				datosEnviar.setIdJugadores(idJugadores);
				datosEnviar.setValorManos(valorManos);
				//Revisar valor manos
				datosEnviar.setMensaje("Empezo "+idJugadores[0]+" tiene "+valorManos[jugadorEnTurno] + "\n" + "Aposto " + apuestaJugadores[jugadorEnTurno]);
				enviarMensajeCliente(datosEnviar);
				
			}
			else {
				   //Es jugador 2
				   //le manda al jugador 2 todos los datos para montar la sala de Juego
				   //jugador 2 debe esperar su turno
				try {
					idJugadores[2] = (String)in.readObject();
					apuestaJugadores[2] = Integer.parseInt(JOptionPane.showInputDialog(null, "Valor de la apuesta: ", "Apuesta Jugador 3", JOptionPane.QUESTION_MESSAGE));
					mostrarMensaje("Hilo establecido con jugador (3) "+idJugadores[2]);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mostrarMensaje("Manda al jugador 3 el nombre del jugador 1");
				
				datosEnviar = new DatosBlackJack();
				
				System.out.println("Mano de los jugadores: ");
				System.out.println("1 " + manosJugadores.get(0).toString());
				System.out.println("2 " + manosJugadores.get(1).toString());
				System.out.println("3 " + manosJugadores.get(2).toString());
				
				datosEnviar.setManoDealer(manosJugadores.get(3));
				datosEnviar.setManoJugador1(manosJugadores.get(0));
				datosEnviar.setManoJugador2(manosJugadores.get(1));	
				
				datosEnviar.setManoJugador3(manosJugadores.get(2));			
				datosEnviar.setIdJugadores(idJugadores);
				datosEnviar.setValorManos(valorManos);
				//Revisar valor manos
				datosEnviar.setMensaje("Empezo "+idJugadores[0]+" tiene "+valorManos[jugadorEnTurno] + "\n" + "Aposto " + apuestaJugadores[jugadorEnTurno]);
				enviarMensajeCliente(datosEnviar);
				
				iniciarRondaJuego(); //despertar al jugador 1 y 2 para iniciar el juego
				mostrarMensaje("Bloquea al servidor para poner en espera de turno al jugador 3");
				bloqueoJuego.lock();
				try {
					mostrarMensaje("Pone en espera de turno al jugador 3");
					esperarTurno.await();
					mostrarMensaje("Despierta de la espera de inicio del juego al jugador 1 y 2");
                    //
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					//JOptionPane.showInputDialog(null, "Insert value: ", "The title", JOptionPane.QUESTION_MESSAGE);
					bloqueoJuego.unlock();
				}	
			}
			
			while(!seTerminoRonda()) {
				try {
					entrada = (String) in.readObject();
					analizarMensaje(entrada,indexJugador);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//controlar cuando se cierra un cliente
				}
			}
			//cerrar conexi�n
		}
		
		public void enviarMensajeCliente(Object mensaje) {
			try {  
				out.writeObject(mensaje);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}			
    }//fin inner class Jugador      

    //Jugador dealer emulado por el servidor
	@Override
	public void run() {
		// TODO Auto-generated method stub
		mostrarMensaje("Inicia el dealer ...");
        boolean pedir = true;
        
        while(pedir) {
		  	Carta carta = mazo.getCarta();
			//adicionar la carta a la mano del dealer
			manosJugadores.get(3).add(carta);
			calcularValorMano(carta, 3);
			
			mostrarMensaje("El dealer recibe "+carta.toString()+" suma "+ valorManos[3]);
			

    		datosEnviar = new DatosBlackJack();
			datosEnviar.setCarta(carta);
			datosEnviar.setJugador("dealer");
				
			if(valorManos[3]<=16) {
				datosEnviar.setJugadorEstado("sigue");
				datosEnviar.setMensaje("Dealer ahora tiene "+valorManos[3]);
				mostrarMensaje("El dealer sigue jugando");
			}else {
				if(valorManos[3]>21) {
					datosEnviar.setJugadorEstado("vol�");
					datosEnviar.setMensaje("Dealer ahora tiene "+valorManos[3]+" vol� :( \n");
					pedir=false;
					mostrarMensaje("El dealer vol�");
				}else {
					datosEnviar.setJugadorEstado("plant�");
					datosEnviar.setMensaje("Dealer ahora tiene "+valorManos[3]+" plant� \n");
					pedir=false;
					mostrarMensaje("El dealer plant�");
				}
			}
			
			//envia la jugada a los otros jugadores
			datosEnviar.setCarta(carta);
			/*jugadores[0].enviarMensajeCliente(datosEnviar);
			jugadores[1].enviarMensajeCliente(datosEnviar);
			jugadores[2].enviarMensajeCliente(datosEnviar);*/
			
			
			
        }//fin while
        determinarGanarPerder();
	}
    
}//Fin class ServidorBJ
