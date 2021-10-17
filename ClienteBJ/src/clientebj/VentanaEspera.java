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

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;

// TODO: Auto-generated Javadoc
/**
 * La clase VentanaEspera.
 */
public class VentanaEspera extends JInternalFrame {
	
	private static final long serialVersionUID = 1L;
	private JLabel enEspera, jugador;
	
	/**
	 * Instancia un nueva VentanaEspera
	 *
	 * @param jugador the jugador
	 */
	public VentanaEspera(String jugador) {
        initInternalFrame(jugador);
		
		this.setTitle("Bienvenido a la sala de espera");
		this.pack();
		this.setResizable(true);
		this.setLocation((ClienteBlackJack.WIDTH-this.getWidth())/2, 
				         (ClienteBlackJack.HEIGHT-this.getHeight())/2);
		this.show();
	}

	/**
	 * Inicia el Internal Frame
	 *
	 * @param idJugador el id del jugador
	 */
	private void initInternalFrame(String idJugador) {
		// TODO Auto-generated method stub
		this.getContentPane().setLayout(new FlowLayout());
		
		jugador = new JLabel(idJugador);
		Font font = new Font(Font.DIALOG,Font.BOLD,15);
		jugador.setFont(font);
		jugador.setForeground(Color.BLUE);
		add(jugador);
		enEspera = new JLabel();
		enEspera.setText("debes esperar al otro jugador...");
		enEspera.setFont(font);
		add(enEspera);
	}
	
	/**
	 * Cerrar sala espera.
	 */
	public void cerrarSalaEspera() {
		this.dispose();
	}

}
