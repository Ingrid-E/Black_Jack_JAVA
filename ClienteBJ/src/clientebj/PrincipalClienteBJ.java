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

import java.awt.EventQueue;

import javax.swing.UIManager;


public class PrincipalClienteBJ {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String className = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(className);
		}catch(Exception e) {e.printStackTrace();}

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				@SuppressWarnings("unused")
				ClienteBlackJack cliente = new ClienteBlackJack();
			}		
		});
	}
}
