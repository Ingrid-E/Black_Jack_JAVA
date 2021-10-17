/*
 * Programacion Interactiva
 * Author: Jean Pierre Cardenas Perea - 1942703
 * Mail: jean.cardenas@correounivalle.edu.co
 * Author: Ingrid Echeverri Montoya - 1943542
 * Mail: ingrid.echeverri@correounivalle.edu.co
 * Miniproyecto 5 - Black Jack
 * Date: 10/17/2021
 */
package comunes;

import java.io.Serializable;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * La clase DatosBlackJack.
 */
public class DatosBlackJack implements Serializable{

	private static final long serialVersionUID = 1L;
	private String[] idJugadores;
	private ArrayList<Carta> manoJugador1, manoJugador2, manoJugador3,manoDealer;
	private int[] valorManos;
	private Carta carta;
	private String mensaje;
	private String jugador,jugadorEstado;
	private Boolean reiniciar = false;
		
	/**
	 * Retorna el jugador
	 *
	 * @return el jugador
	 */
	public String getJugador() {
		return jugador;
	}
	
	/**
	 * Establece un valor para el jugador
	 *
	 * @param jugador el nombre del jugador
	 */
	public void setJugador(String jugador) {
		this.jugador = jugador;
	}
	
	/**
	 * Retorna el estado del jugador
	 *
	 * @return el estado del jugador
	 */
	public String getJugadorEstado() {
		return jugadorEstado;
	}
	
	/**
	 * Establece el estado del jugador
	 *
	 * @param jugadorEstado el nuevo estado del jugador
	 */
	public void setJugadorEstado(String jugadorEstado) {
		this.jugadorEstado = jugadorEstado;
	}
	
	/**
	 * Retorna el arreglo idJugadores
	 *
	 * @return el arreglo idJugadores
	 */
	public String[] getIdJugadores() {
		return idJugadores;
	}
	
	/**
	 * Establece un elemento en el arreglo idJugadores
	 *
	 * @param idJugadores el nuevo id de un jugador
	 */
	public void setIdJugadores(String[] idJugadores) {
		this.idJugadores = idJugadores;
	}
	
	/**
	 * Retorna el Arraylist manoJugador1
	 *
	 * @return la mano del jugador 1
	 */
	public ArrayList<Carta> getManoJugador1() {
		return manoJugador1;
	}
	
	/**
	 * Establece el Arraylist manoJugador1
	 *
	 * @param manoJugador1 la nueva mano del jugador 1
	 */
	public void setManoJugador1(ArrayList<Carta> manoJugador1) {
		this.manoJugador1 = manoJugador1;
	}
	
	/**
	 * Retorna el Arraylist manoJugador2
	 *
	 * @return la mano del jugador 2
	 */
	public ArrayList<Carta> getManoJugador2() {
		return manoJugador2;
	}
	
	/**
	 * Establece el Arraylist manoJugador2
	 *
	 * @param manoJugador2 la nueva mano del jugador 2
	 */
	public void setManoJugador2(ArrayList<Carta> manoJugador2) {
		this.manoJugador2 = manoJugador2;
	}
	
	/**
	 * Establece el Arraylist manoJugador3
	 *
	 * @param manoJugador3 la nueva mano del jugador 3
	 */
	public void setManoJugador3(ArrayList<Carta> manoJugador3) {
		this.manoJugador3 = manoJugador3;
	}
	
	/**
	 * Retorna el Arraylist manoJugador3
	 *
	 * @return la mano del jugador 3
	 */
	public ArrayList<Carta> getManoJugador3() {
		return manoJugador3;
	}
	
	/**
	 * Retorna el ArrayList manoDealer
	 *
	 * @return la mano del dealer
	 */
	public ArrayList<Carta> getManoDealer() {
		return manoDealer;
	}
	
	/**
	 * establece el ArrayList manoDealer
	 *
	 * @param manoDealer la mano del dealer
	 */
	public void setManoDealer(ArrayList<Carta> manoDealer) {
		this.manoDealer = manoDealer;
	}
	
	/**
	 * Retorna un mensaje
	 *
	 * @return el mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}
	
	/**
	 * Establece el mensaje
	 *
	 * @param mensaje el nuevo mensaje
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	/**
	 * Establece el valorManos dentro de un array.
	 *
	 * @param valorManos el nuevo valorManos
	 */
	public void setValorManos(int[] valorManos) {
		this.valorManos=valorManos;
	}
	
	/**
	 * retorna el array valorManos
	 *
	 * @return el array valormanos
	 */
	public int[] getValorManos() {
		return valorManos;	
	}
	
	/**
	 * Establece una carta
	 *
	 * @param carta la nueva carta
	 */
	public void setCarta(Carta carta) {
		this.carta=carta;
	}
	
	/**
	 * Retorna una carta
	 *
	 * @return la carta
	 */
	public Carta getCarta() {
		return carta;
	}
	
	/**
	 * retorna la variable reiniciar
	 *
	 * @return variable reiniciar
	 */
	public Boolean getReiniciar() {
		return reiniciar;
	}
	
	/**
	 * establece el valor de la variable reiniciar
	 *
	 * @param reiniciar el nuevo valor de la vairbale reiniciar
	 */
	public void setReiniciar(Boolean reiniciar) {
		this.reiniciar = reiniciar;
	}
}