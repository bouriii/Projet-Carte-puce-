/*         Project    : Ecriture d'une application cliente en Java d'analyse de la carte SIM  */
/*         Author     : BOURI Mohamed						              */
/*         Version    : 1.0 					   			      */
/*         Date       : 03/2017                                      			      */

package fr.cryptis.SIM;


import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.List;
import javax.smartcardio.*;

public class SimApplication {

	public static void main(String[] args) throws CardException {
		
		Interface_SIM a= new Interface_SIM();
		a.setVisible(true);
		
	}	
}
