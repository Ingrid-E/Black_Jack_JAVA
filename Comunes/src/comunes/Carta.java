package comunes;

import java.io.Serializable;

public class Carta implements Serializable{
	//Atributos
	private static final long serialVersionUID = 1L;
	private String valor;
    private String palo;
 	/**
 	 * Constructor principal de la clase carta
 	 * 
 	 * @param valor
 	 * @param palo
 	 */
    public Carta(String valor, String palo) {
		this.valor = valor;
		this.palo = palo;
	}
    /**
     * Obtiene el valor de 
     * la carta
     * @return string
     */
	public String getValor() {
		return valor;
	}
	/**
	 * Pone el valor de la carta
	 * @param valor
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
	/**
	 * Obtiene el valor del palo
	 * @return string
	 */
	public String getPalo() {
		return palo;
	}
	/**
	 *Pone el valor de la carta
	 * @param palo
	 */
	public void setPalo(String palo) {
		this.palo = palo;
	}
	/**
	 * Vuelve el valor de la carta a un string
	 */
	public String toString() {
		return valor+palo;
	}
	
}
