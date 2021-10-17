package clientebj;

import java.awt.SystemColor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;

import comunes.DatosBlackJack;

/**
 * The Class ClienteBlackJack. 
 * 
 */
public class ClienteBlackJack extends JFrame implements Runnable{
	//Atributos
	private static final long serialVersionUID = 1L;
	//Constantes de Interfaz Grafica
	public static final int WIDTH=670;
	public static final int HEIGHT=450;
	
	//Constantes de conexión con el Servidor BlackJack
	public static final int PUERTO=7377;
	public static final String IP="127.0.0.1";
	
	//variables de control del juego
	private String idYo, otroJugador, otroJugador2;
	private boolean turno;
	protected DatosBlackJack datosRecibidos;
	
	//variables para manejar la conexión con el Servidor BlackJack
	private Socket conexion;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private ClienteBlackJack cliente;
	
	//Componentes Graficos
	private JDesktopPane containerInternalFrames;
	@SuppressWarnings("unused")
	private VentanaEntrada ventanaEntrada;
	private VentanaEspera ventanaEspera;
	private VentanaSalaJuego ventanaSalaJuego;
	public String[] idJugadores= new String[3];
	int jugador = 0;
	
	/**
	 * Constructor principal de la clase
	 * ClienteBlackJack
	 */
	public ClienteBlackJack() {
		initGUI();
		cliente = this;
		//default window settings
		this.setTitle("Juego BlackJack");
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Inicia la gui del juego
	 */
	private void initGUI() {
		//set up JFrame Container y Layout
        
		//Create Listeners objects
		
		//Create Control objects
		turno=false;
		//Set up JComponents
	
		this.setBackground(SystemColor.activeCaption);
		containerInternalFrames = new JDesktopPane();
		containerInternalFrames.setOpaque(false);
		this.setContentPane(containerInternalFrames);
		adicionarInternalFrame(new VentanaEntrada(this));
	}
	/**
	 * Agrega una ventana interna a la clase,
	 * para que quede adentro del JFrame
	 * @param nuevoInternalFrame
	 */
	public void adicionarInternalFrame(JInternalFrame nuevoInternalFrame) {
		add(nuevoInternalFrame);
	}
	/**
	 * Inicia el hilo de los jugadores
	 */
	public void iniciarHilo() {
		ExecutorService hiloCliente = Executors.newFixedThreadPool(1);
		hiloCliente.execute(this);
	}
	/**
	 * Guarda el nombre del jugador principal que controla la ventana
	 * @param id
	 */
	public void setIdYo(String id) {
		idYo=id;
	}
	/**
	 * Muestra los mensajes en la consola
	 * @param mensaje
	 */
	private void mostrarMensajes(String mensaje) {
		System.out.println(mensaje);
	}
	/**
	 * Envia mensajes a el servidor para correr ciertos comandos.
	 * @param mensaje
	 */
	public void enviarMensajeServidor(String mensaje) {
		try {
			out.writeObject(mensaje);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Busca el servidor cuando el jugador ingresa su nombre
	 * en la ventanaEspera.
	 */
	public void buscarServidor() {
		mostrarMensajes("Jugador buscando al servidor...");
		
		try {
			//buscar el servidor
			conexion = new Socket(IP,PUERTO);
			//obtener flujos E/S
			out = new ObjectOutputStream(conexion.getOutputStream());
			out.flush();
			in = new ObjectInputStream(conexion.getInputStream());
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mostrarMensajes("Jugador conectado al servidor");
		mostrarMensajes("Jugador estableció Flujos E/S");
		//mandar nombre jugador
		mostrarMensajes("Jugador envio nombre "+idYo);
		enviarMensajeServidor(idYo);
		//procesar comunicación con el ServidorBlackJack
		iniciarHilo();	
	}
	/**
	 * Inicia el hilo que se encarga de mandar los mensajes recibidos
	 * por el servidor a la clase ventanaSalaJuego para saber que pide.
	 * 
	 * Organiza el orden de los jugadores segun la ventana correspondiente.
	 */
	@Override
	public void run() {
		//datosRecibidos = new DatosBlackJack();
		// TODO Auto-generated method stub
		//mostrar bienvenida al jugador	
		   
			try {
				datosRecibidos = new DatosBlackJack();
				datosRecibidos = (DatosBlackJack) in.readObject();
				for(int i=0; i<3; i++) {
					idJugadores[i] = datosRecibidos.getIdJugadores()[i];
				}
				if(datosRecibidos.getIdJugadores()[0].equals(idYo)) {
					otroJugador=datosRecibidos.getIdJugadores()[1];
					otroJugador2=datosRecibidos.getIdJugadores()[2];
					turno=true;
				}else if(datosRecibidos.getIdJugadores()[1].equals(idYo)){
					otroJugador=datosRecibidos.getIdJugadores()[0];
					otroJugador2 = datosRecibidos.getIdJugadores()[2];
				}else {
					otroJugador=datosRecibidos.getIdJugadores()[0];
					otroJugador2 = datosRecibidos.getIdJugadores()[1];
				}
				this.habilitarSalaJuego(datosRecibidos);
				jugador++;
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//buscando nombre del OtroJugador
			
			//procesar turnos
			
			while(true) {
				try {
					datosRecibidos = new DatosBlackJack();
					datosRecibidos = (DatosBlackJack)in.readObject();
					mostrarMensajes("Cliente hilo run recibiendo mensaje servidor ");
					if(!ventanaSalaJuego.reinicio) {
						mostrarMensajes(datosRecibidos.getJugador()+" "+datosRecibidos.getJugadorEstado());
						System.out.println(datosRecibidos.getReiniciar());
						ventanaSalaJuego.pintarTurno(datosRecibidos);
					}else {
						System.out.println("Juego se esta reiniciando");
					}
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		
	}
	/**
	 * Inicia la clase ventanaSalaJuego y cierra la ventana
	 * Espera, para inicializar la clase con sus jugadores correspondientes.
	 * @param datosRecibidos
	 */
	private void habilitarSalaJuego(DatosBlackJack datosRecibidos) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ventanaEspera = (VentanaEspera)containerInternalFrames.getComponent(0);
				ventanaEspera.cerrarSalaEspera();
				ventanaSalaJuego = new VentanaSalaJuego(idYo,otroJugador, otroJugador2, cliente);
				ventanaSalaJuego.pintarCartasInicio(datosRecibidos);
				adicionarInternalFrame(ventanaSalaJuego);
                if(turno) {
                	ventanaSalaJuego.activarBotones(turno);
                }
			}
			
		});
	}
	/**
	 * Cierra la conexion del cliente con el servidor
	 */
	@SuppressWarnings("unused")
	private void cerrarConexion() {
		// TODO Auto-generated method stub
		try {
			in.close();
			out.close();
			conexion.close();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	/**
	 * Pone el turno que toca
	 * @param turno
	 */
	public void setTurno(boolean turno) {
		this.turno=turno;
	}	
}
