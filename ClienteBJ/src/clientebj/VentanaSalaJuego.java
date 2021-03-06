/*
 * Programacion Interactiva
 * Author: Jean Pierre Cardenas Perea - 1942703
 * Mail: jean.cardenas@correounivalle.edu.co
 * Author: Ingrid Echeverri Montoya - 1943542
 * Mail: ingrid.echeverri@correounivalle.edu.co
 * Miniproyecto 5 - Black Jack
 * Date: 10/17/2021
 */
package clientebj;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import comunes.DatosBlackJack;

public class VentanaSalaJuego extends JInternalFrame {
	//Atributos
	private static final long serialVersionUID = 1L;
		private PanelJugador dealer, yo, jugador2, jugador3;
		private JTextArea areaMensajes;
		private JButton pedir, plantar;
		private JPanel panelYo, panelBotones, yoFull, panelDealer,panelJugador2, panelJugador3;
		protected boolean reinicio = false;
		private ClienteBlackJack cliente;
		private VentanaSalaJuego ventana;
		private String yoId, jugador2Id, jugador3Id;
		private Escucha escucha;
		
		/**
		 * Constructor principal que inicia los valores y organiza a los jugadores.
		 * @param yoId
		 * @param jugador2Id
		 * @param jugador3Id
		 * @param cliente
		 */
		public VentanaSalaJuego(String yoId, String jugador2Id, String jugador3Id, ClienteBlackJack cliente) {
			//Inicializando los jugadores
			this.yoId = yoId;
			this.jugador2Id = jugador2Id;
			this.jugador3Id = jugador3Id;
			this.cliente =cliente;
			this.ventana = this;
			//Iniciar GUI
			initGUI();
			
			//default window settings
			this.setTitle("Sala de juego BlackJack - Jugador: "+yoId);
			this.setSize(ClienteBlackJack.WIDTH-15, ClienteBlackJack.HEIGHT-40);
			this.setLocation(0, 0);
			this.setResizable(false);
			this.show();
		}
		/**
		 * Inicia la parte grafica de la ventana sala juego,
		 * se inicializan los paneles y la escucha.
		 */
		private void initGUI() {
			// TODO Auto-generated method stub
			//set up JFrame Container y Layout
	        
			//Create Listeners objects
			escucha = new Escucha();
			//Create Control objects
						
			//Set up JComponents
			panelDealer = new JPanel();
			dealer = new PanelJugador("Dealer");
			panelDealer.add(dealer);
			add(panelDealer,BorderLayout.NORTH);		
			
			panelJugador2 = new JPanel();
			jugador2= new PanelJugador(jugador2Id);	
			panelJugador2.add(jugador2);
			add(panelJugador2,BorderLayout.EAST);	
			
			
			panelJugador3 = new JPanel();
			jugador3= new PanelJugador(jugador3Id);	
			panelJugador3.add(jugador3);
			
			add(panelJugador3,BorderLayout.SOUTH);	
			
			//Area donde aparecen los mensajes
			areaMensajes = new JTextArea(8,18);
			JScrollPane scroll = new JScrollPane(areaMensajes);	
			Border blackline;
			blackline = BorderFactory.createLineBorder(Color.black);
			TitledBorder bordes;
			bordes = BorderFactory.createTitledBorder(blackline, "Area de Mensajes");
	        bordes.setTitleJustification(TitledBorder.CENTER);
			scroll.setBorder(bordes);
			areaMensajes.setOpaque(false);
			areaMensajes.setBackground(new Color(0, 0, 0, 0));
			areaMensajes.setEditable(false);
			
			scroll.getViewport().setOpaque(false);
			scroll.setOpaque(false);
			//scroll.setVisible(false);
			add(scroll,BorderLayout.CENTER);
			
			panelYo = new JPanel();
			panelYo.setLayout(new BorderLayout());
			yo = new PanelJugador(yoId);
			panelYo.add(yo);
				
			pedir = new JButton("Carta");
			pedir.setEnabled(false);
			pedir.addActionListener(escucha);
			plantar = new JButton("Plantar");
			plantar.setEnabled(false);
			plantar.addActionListener(escucha);
			panelBotones = new JPanel();
			panelBotones.add(pedir);
			panelBotones.add(plantar);
			
			yoFull = new JPanel();
			yoFull.setPreferredSize(new Dimension(206,100));
			yoFull.add(panelYo);
			yoFull.add(panelBotones);
			add(yoFull,BorderLayout.WEST);
		}
		/**
		 * Activa los botones para cuando es el turno del,
		 * jugador principal.
		 * @param turno
		 */
		public void activarBotones(boolean turno) {
			pedir.setEnabled(turno);
			plantar.setEnabled(turno);
		}
		/**
		 * Pinta las cartas al iniciar la primera partida
		 * @param datosRecibidos
		 */
		public void pintarCartasInicio(DatosBlackJack datosRecibidos) {
			if(datosRecibidos.getIdJugadores()[0].equals(yoId)) {
				yo.pintarCartasInicio(datosRecibidos.getManoJugador1());
				jugador2.pintarCartasInicio(datosRecibidos.getManoJugador2());
				jugador3.pintarCartasInicio(datosRecibidos.getManoJugador3());
				
			}else if(datosRecibidos.getIdJugadores()[1].equals(yoId)) {
				yo.pintarCartasInicio(datosRecibidos.getManoJugador2());
				jugador2.pintarCartasInicio(datosRecibidos.getManoJugador1());
				jugador3.pintarCartasInicio(datosRecibidos.getManoJugador3());
			}
			else {
				yo.pintarCartasInicio(datosRecibidos.getManoJugador3());
				jugador2.pintarCartasInicio(datosRecibidos.getManoJugador1());
				jugador3.pintarCartasInicio(datosRecibidos.getManoJugador2());
			}
			dealer.pintarCartasInicio(datosRecibidos.getManoDealer());
			
			areaMensajes.append(datosRecibidos.getMensaje()+"\n");
		}
		/**
		 * Pinta las cartas al iniciar una nueva partida 
		 * @param datosRecibidos
		 */
		public void pintarCartasReinicio(DatosBlackJack datosRecibidos) {
			this.getContentPane().removeAll();
			initGUI();
			this.revalidate();
			this.repaint();
			
			if(cliente.idJugadores[0].equals(yoId)) {
				yo.pintarCartasInicio(datosRecibidos.getManoJugador1());
				jugador2.pintarCartasInicio(datosRecibidos.getManoJugador2());
				jugador3.pintarCartasInicio(datosRecibidos.getManoJugador3());
				
			}else if(cliente.idJugadores[1].equals(yoId)) {
				yo.pintarCartasInicio(datosRecibidos.getManoJugador2());
				jugador2.pintarCartasInicio(datosRecibidos.getManoJugador1());
				jugador3.pintarCartasInicio(datosRecibidos.getManoJugador3());
			}
			else {
				yo.pintarCartasInicio(datosRecibidos.getManoJugador3());
				jugador2.pintarCartasInicio(datosRecibidos.getManoJugador1());
				jugador3.pintarCartasInicio(datosRecibidos.getManoJugador2());
			}
			dealer.pintarCartasInicio(datosRecibidos.getManoDealer());
			reinicio = false;
			datosRecibidos.setJugador(cliente.idJugadores[0]);
			datosRecibidos.setJugadorEstado("iniciar");
			datosRecibidos.setMensaje("Nueva Partida!");
			pintarTurno(datosRecibidos);
		}
		
		/**
		 * Revisa de quien es el turno,
		 * si es el jugador principal entonces se le activa los botones,
		 * si no solo se pinta las cartas obtenidas por los otros jugadores.
		 * @param datosRecibidos
		 */
		public void pintarTurno(DatosBlackJack datosRecibidos) {
			areaMensajes.append(datosRecibidos.getMensaje()+"\n");	
			if(!reinicio) {
				ClienteBlackJack cliente = (ClienteBlackJack)this.getTopLevelAncestor();
				
				if(datosRecibidos.getJugador().contentEquals(yoId)){
					if(datosRecibidos.getJugadorEstado().equals("iniciar")) {
						activarBotones(true);
					}else {
						if(datosRecibidos.getJugadorEstado().equals("plant?") ){
							cliente.setTurno(false);
						}else {
							yo.pintarLaCarta(datosRecibidos.getCarta());
							if(datosRecibidos.getJugadorEstado().equals("vol?")) {
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										// TODO Auto-generated method stub
										activarBotones(false);
										cliente.setTurno(false);
									}});			
							      }
							}
						} 
				 }else {//movidas de los otros jugadores
						if(datosRecibidos.getJugador().equals(jugador2Id)) {
							//mensaje para PanelJuego jugador2
							if(datosRecibidos.getJugadorEstado().equals("sigue")|| datosRecibidos.getJugadorEstado().equals("vol?")) {
								jugador2.pintarLaCarta(datosRecibidos.getCarta());
							}
						}else if(datosRecibidos.getJugador().equals(jugador3Id)) {
								//mensaje para PanelJuego jugador2
								if(datosRecibidos.getJugadorEstado().equals("sigue")|| datosRecibidos.getJugadorEstado().equals("vol?")) {
									jugador3.pintarLaCarta(datosRecibidos.getCarta());
								}
						}else {
							//mensaje para PanelJuego dealer
							if(datosRecibidos.getJugadorEstado().equals("vol?")	||
							   datosRecibidos.getJugadorEstado().equals("plant?")) {
								areaMensajes.append("DEALER PLANTO"+"\n");	
								reinicio = true;
								nuevaPartida(datosRecibidos);

								dealer.pintarLaCarta(datosRecibidos.getCarta());
							}else if(datosRecibidos.getJugadorEstado().equals("sigue")){
								dealer.pintarLaCarta(datosRecibidos.getCarta());
							}
						}
					}			 	
			}
			
		}		

	
		/**
		 * Incia una nueva partida despues de x segundos
		 * @param datosRecibidos
		 */
	   private void nuevaPartida(DatosBlackJack datosRecibidos) {
		   Timer timer = new Timer();
		   timer.scheduleAtFixedRate(new TimerTask() {
			int tiempo = 10;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ventana.setTitle("Jugador " + yoId + " Nueva Partida comenzando en: " + tiempo);
				
				if(tiempo == 0) {
					//enviarDatos("reiniciar");
					ventana.setTitle("Sala de juego BlackJack - Jugador: "+yoId);
					pintarCartasReinicio(datosRecibidos);
					timer.cancel();
				}
				tiempo--;
			}
			   
		   }, 500, 1000);
	   }
	   /**
	    * Envia mensajes a la clase ServidorBJ por medio de la clase,
	    * ClienteBlackJack, para que sepa que boton se pulso.
	    * @param mensaje
	    */
	   private void enviarDatos(String mensaje) {
			// TODO Auto-generated method stub
		  ClienteBlackJack cliente = (ClienteBlackJack)this.getTopLevelAncestor();
		  cliente.enviarMensajeServidor(mensaje);
		}
		   
	  /**
	   * Maneja las escuchas de los botones y 
	   * envia un mensaje por medio de enviarDatos.
	   * @see enviarDatos(String mensaje)	   *
	   */
	   private class Escucha implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			// TODO Auto-generated method stub
			if(actionEvent.getSource()==pedir) {
				//enviar pedir carta al servidor
				enviarDatos("pedir");				
			}else {
				//enviar plantar al servidor
				enviarDatos("plantar");
				activarBotones(false);
			}
		}
	   }
}
