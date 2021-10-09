package comunes;

import java.io.Serializable;

public class Carta implements Serializable{
	private static final long serialVersionUID = 1L;
	private String valor;
    private String palo;
 	
    public Carta(String valor, String palo) {
		this.valor = valor;
		this.palo = palo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getPalo() {
		return palo;
	}

	public void setPalo(String palo) {
		this.palo = palo;
	}
	
	public String toString() {
		return valor+palo;
	}
	
}
