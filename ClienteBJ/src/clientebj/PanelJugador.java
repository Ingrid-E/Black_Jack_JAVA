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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import comunes.Carta;

public class PanelJugador extends JPanel {
	private static final long serialVersionUID = 1L;
	//constantes de clase
	private static final int ANCHO = 206;
	private static final int ALTO = 89;
	private static int ALTOCARTA = 65;
	private static int ANCHOCARTA= 35;
	private ImageIcon[][] cartas;
	private JPanel cartasJugador;
	private String nombreJugador;
	private int x;
	private int cantidadCartas = 0;
	/**
	 * Constructor principal de la clase
	 * PanelJugador. 
	 * 
	 * Inicia y agrega los elementos principales
	 * al Panel.
	 * @param nombreJugador
	 */
	public PanelJugador(String nombreJugador) {
		//this.setBackground(Color.GREEN);
		this.setLayout(null);
		//dibujoRecordar = new ArrayList<Recuerdo>();
		this.cartasJugador = new JPanel();
		this.cartasJugador.setLayout(null);
		this.cartasJugador.setBounds(12,16,182,65);
		this.cartasJugador.setOpaque(false);
		this.cartas = dividirImagen();
		
		this.nombreJugador = nombreJugador;
		
		this.setPreferredSize(new Dimension(ANCHO,ALTO));
		TitledBorder bordes;
		bordes = BorderFactory.createTitledBorder(nombreJugador);
		this.setBorder(bordes);
		this.add(cartasJugador);
	}
	/**
	 * Pinta las cartas al inicio de la partida.
	 * @param manoJugador
	 */
	public void pintarCartasInicio(ArrayList<Carta> manoJugador) {
		cartasJugador.removeAll();
		System.out.print(this.nombreJugador + " cartas en mano: ");
	    for(int i=manoJugador.size()-1;i>=0;i--) {

	    	JLabel pintarCarta = new JLabel();
	    	pintarCarta.setBounds(i*21, 0, ANCHOCARTA, ALTOCARTA);
			pintarCarta.setIcon(getCarta(manoJugador.get(i).getPalo(), manoJugador.get(i).getValor()));
			this.cartasJugador.add(pintarCarta);
			cantidadCartas++;

	    }	
	    System.out.println();
	    cartasJugador.revalidate();
	    cartasJugador.repaint();

	}
	/**
	 * Agrega una carta al panel del jugador
	 * visualmente
	 * @param carta
	 */
	public void pintarLaCarta (Carta carta) {
		
		Component[] cartasAntiguas = this.cartasJugador.getComponents();
		cartasJugador.removeAll();
    	
    	JLabel pintarCarta = new JLabel();
    	pintarCarta.setBounds(this.cantidadCartas*21, 0, ANCHOCARTA, ALTOCARTA);
    	pintarCarta.setIcon(getCarta(carta.getPalo(), carta.getValor()));
		this.cartasJugador.add(pintarCarta);
		for(Component antiguas: cartasAntiguas) {
			this.cartasJugador.add(antiguas);
		}

		this.cartasJugador.revalidate();
		this.cartasJugador.repaint();
		this.cantidadCartas++;
    	
	}
	
	
	/**
	 * Divide la imagen que contiene todas las cartas 
	 * en subImagenes para facilitar su uso.
	 * 
	 * Cada fila es un simbolo: C,P,D,T
	 * Cada columna es un valor , 2,3,4,5,Q,T
	 * @return
	 */
	private ImageIcon[][] dividirImagen() {
		ImageIcon[][] cartas = new ImageIcon[4][12];
		InputStream input = PanelJugador.class.getResourceAsStream("/img/Cartas.png");
		try {
			BufferedImage imgCartas = ImageIO.read(input);
			for(int i=0; i < 4; i++) {
				for(int j=0; j < 12; j++) {
					cartas[i][j] = new ImageIcon(imgCartas.getSubimage(j*ANCHOCARTA, i*ALTOCARTA, ANCHOCARTA, ALTOCARTA));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cartas;
		
	}
	/**
	 * Obtiene la carta por medio de la posicion
	 * en el array segun el simbolo y valor
	 * especificado.
	 * @param simbolo
	 * @param valor
	 * @return
	 */
	public ImageIcon getCarta(String simbolo, String valor) {
		int fila = 0;
		int posicion = 0;
		
		
		switch(simbolo) {
    	case "P":
    		fila = 0;
    		break;
    	case "T":
    		fila = 1;
    		break;
    	case "D":
    		fila = 2;
    		break;
    	case "C":
    		fila=3;
    		break;
    	}
    	
    	switch(valor) {
    	case "J":
    		posicion = 9;
    		break;
    	case "Q":
    		posicion = 10;
    		break;
    	case "K":
    		posicion = 11;
    		break;
    	case "As":
    		posicion = 0;
    		break;
    	default:
    		posicion = Integer.parseInt(valor)-1;
    		break;
    	}
    	
    	return cartas[fila][posicion];
    	
    	
	}


}
