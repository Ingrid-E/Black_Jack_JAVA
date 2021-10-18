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

import java.util.ArrayList;
import java.util.Random;

public class Baraja {	
	//Atributos
   public static final String CARTAS_FILE="/resources/cards.png";
   public static final String CARTA_TAPADA_FILE="/resources/cardBack.png";
   public static final int CARTA_WIDTH=45;
   public static final int CARTA_HEIGHT=60;
   private static final int PALOS=4;
   private static final int VALORES=13;
   @SuppressWarnings("unused")
private static final int CARTA_BACK_INDEX=PALOS*VALORES;
   @SuppressWarnings("unused")
private static final int TOTAL_IMAGES=PALOS*VALORES+1;
  
   private ArrayList<Carta> mazo;
   private Random aleatorio;
   /**
    * Constructor principal de la clase
    * Baraja que crea el mazo.
    */
   public Baraja() {
	   aleatorio = new Random();
	   mazo = new ArrayList<Carta>();
	   String valor;
	   for(int i=1;i<=4;i++) {
		   for(int j=2;j<=14;j++) {
			   switch(j) {
			   case 11: valor="J";break;
			   case 12: valor="Q";break;
			   case 13: valor="K";break;
			   case 14: valor="As";break;
			   default: valor= String.valueOf(j);break;
			   } 
			   switch(i) {
			   case 1: mazo.add(new Carta(valor,"C"));break;
			   case 2: mazo.add(new Carta(valor,"D"));break;
			   case 3: mazo.add(new Carta(valor,"P"));break;
			   case 4: mazo.add(new Carta(valor,"T"));break;
			   }
		   }
	   }
   }
    /**
     * Obtiene una carta aleatoria del mazo
     * @return
     */
   public Carta getCarta() {
	   int index = aleatorio.nextInt(mazoSize());
	   Carta carta = mazo.get(index);
	   mazo.remove(index); //elimina del mazo la carta usada
	   return carta;
   }
   /**
    * Devuelve el tamaño del mazo
    * @return
    */
   public int mazoSize() {
	   return mazo.size();
   }
}
