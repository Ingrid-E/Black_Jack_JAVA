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
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

// TODO: Auto-generated Javadoc
/**
 * La clase VentanaEntrada.
 */
public class VentanaEntrada extends JInternalFrame {
	
	private static final long serialVersionUID = 1L;
	private JLabel bienvenida, labelNombre;
	private JPanel ingreso;
	private JTextField nombreJugador;
	private JButton ingresar;
	private VentanaEspera ventanaEspera;
	private ClienteBlackJack cliente;
	private Escucha escucha;
	
	/**
	 * Instancia una nueva Ventana entrada
	 *
	 * @param cliente el cliente
	 */
	public VentanaEntrada(ClienteBlackJack cliente) {
		this.cliente=cliente;
		initInternalFrame();
		
		this.setTitle("Bienvenido a Black Jack");
		this.pack();
		this.setLocation((ClienteBlackJack.WIDTH-this.getWidth())/2, 
				         (ClienteBlackJack.HEIGHT-this.getHeight())/2);
		this.show();
	}

	/**
	 * Inicializa la interfaz del Internal Frame
	 */
	private void initInternalFrame() {
		// TODO Auto-generated method stub
		//apuestaEsCorrecta = false;
		escucha = new Escucha();
		this.getContentPane().setLayout(new BorderLayout());
		bienvenida = new JLabel("Registre su nombre y su apuesta para ingresar");
		add(bienvenida, BorderLayout.NORTH);

		ingreso = new JPanel(); 
		labelNombre= new JLabel("Nombre"); 
		nombreJugador =	new JTextField(10);
		ingresar = new JButton("Ingresar");
		ingresar.addActionListener(escucha);
		ingreso.add(labelNombre);
		ingreso.add(nombreJugador);
		ingreso.add(ingresar);
		add(ingreso,BorderLayout.CENTER);
	}
	
	/**
	 * retorna el contenedor d elos frames
	 *
	 * @return el contenedor de los frames
	 */
	private Container getContainerFrames() {
		return this.getParent();
	}
    
	/**
	 * Cerrar ventana entrada.
	 */
	private void cerrarVentanaEntrada() {
		this.dispose();
	}
	
	/**
	 * los listeners de los botones
	 */
	private class Escucha implements ActionListener{
		
		/**
		 * Action performed.
		 *
		 * @param arg0 the arg 0
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			//cargar Sala de Espera y cerrar Ventana Entrada
			if(nombreJugador.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "Debes ingresar un nombre para identificarte!!");
			}
			else {
				System.out.println("Valor que se guarda como idYo " + nombreJugador.getText());
				cliente.setIdYo(nombreJugador.getText());
				ventanaEspera = new VentanaEspera(nombreJugador.getText());
				getContainerFrames().add(ventanaEspera);
				cliente.buscarServidor();
				cerrarVentanaEntrada();
			}	
		}
	}
	

}
