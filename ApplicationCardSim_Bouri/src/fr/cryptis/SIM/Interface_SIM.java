/*         Project    : Ecriture d'une application cliente en Java d'analyse de la carte SIM  */
/*         Author     : BOURI Mohamed						              */
/*         Version    : 1.0 					   			      */
/*         Date       : 03/2017                                      			      */

package fr.cryptis.SIM;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.smartcardio.CardException;
import javax.swing.*;

public class Interface_SIM extends JFrame {
	
	JButton anal, conect,deco,clear;
	JTextArea lb;
	JPanel p1,p2;
	JScrollPane jsp ; 
	JSplitPane split;
	String analyse,pin;
	
	
	byte cmmd_GSM[][]= {
			//Repertoire GSM
			{(byte)0xA0,(byte)0xA4,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x7F,(byte)0x20},
			//Repertoire IMSI
			{(byte)0xA0,(byte)0xA4,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x6F,(byte)0x07},
			//Lecture  TMSI et LAI
			{(byte)0xA0,(byte)0xA4,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x6F,(byte)0x7E},
			// Execution de l'algorithme d'authentification du GSM
			{(byte)0xA0,(byte)0x88,(byte)0x00,(byte)0x00,(byte)0x10,(byte)0x87,(byte)0x89,(byte)0x29,(byte)0x37,(byte)0xAC,(byte)0xFE,(byte)0xAF,(byte)0x29,(byte)0x18,(byte)0x59,(byte)0x27,(byte)0x3A,(byte)0xEC,(byte)0x8E,(byte)0xA9,(byte)0xA0},
			//Repertoire EF-KC Mise a jour des fichier 
			{(byte)0xA0,(byte)0xA4,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x6F,(byte)0x20},
			//Lecture de la table des services
			{(byte)0xA0,(byte)0xA4,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x6F,(byte)0x38},
			//Repertoire DF-TELECOM 
			{(byte)0xA0,(byte)0xA4,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x7F,(byte)0x10},
			
			//Lecture et Ecriture des SMS dans la SIM
			{(byte)0xA0,(byte)0xA4,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x6F,(byte)0x3C},
			
			//Lecture et Ecriture des numeros ADN
			{(byte)0xA0,(byte)0xA4,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x6F,(byte)0x3A},
			//Lecture de l'annuaire de service FDN
			{(byte)0xA0,(byte)0xA4,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x6F,(byte)0x3B},
			//Verify CHV
			{(byte)0xA0,(byte)0x20,(byte)0x00,(byte)0x01,(byte)0x08,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF},
			
			};
	Commande_Reponse cmd;
	
	public Interface_SIM(){
		
		//cmmd_GSM=new byte[]{(byte)0xA0,(byte)0xA4,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x7F,(byte)0x20};
		cmd=new Commande_Reponse(cmmd_GSM);
		
		setTitle(" Bouri - PROJET - Carte a puce GSM -");
		setSize(500,500);
		setResizable(false);
		setLocationRelativeTo(null);
		//getContentPane().setLayout(null);
		anal = new JButton("Analyser");conect = new JButton("Connecter");deco = new JButton("Deconnecter");
		clear = new JButton("Clear");
		                         
		
		lb=new JTextArea(23,44);
		
		
		p1 = new JPanel();//p1.setBackground(Color.cyan);
		p2 = new JPanel();//p2.setBackground(Color.LIGHT_GRAY);
		jsp = new JScrollPane(lb);
		p2.add(jsp);
		
		
     	p1.add(anal);p1.add(conect);p1.add(deco);p1.add(clear);
     	
     	lb.setVisible(true);
     	lb.setEditable(false);
     	p1.setBounds(1, 1, 500, 80);
		p2.setBounds(1,80,500,410);
	
		getContentPane().add(p1,BorderLayout.NORTH);
	
		getContentPane().add(p2,BorderLayout.SOUTH);
		
		conect.addActionListener(
				new ActionListener() {
					
					public void actionPerformed(ActionEvent ev) {
						if(ev.getActionCommand()== "Connecter"){
							
							cmd.connect_SIM();
							
							
							
						}
					}
				}
		);
		
		anal.addActionListener(
				new ActionListener() {
					
					public void actionPerformed(ActionEvent ev) {
						if(ev.getActionCommand()== "Analyser"){
							
							try {
								
								int pin_ent[]=new int[4];
								byte PIN[]=new byte[4];
								String s="Saisissez votre Code PIN ";
								
								analyse  = "\n                                  Analyse  : GSM \n";
								analyse += cmd.Command_SIM(cmd.cmmd[0]);
								analyse += "\n\n "+new analyse(cmd.reponse_byte()).analyser_GSM();
							    
								
								System.out.println("\n\n  Cou cou test "+new analyse().presentation_pin);
								
								if(new analyse().presentation_pin == true ){ 
							    	
							    	cmd.pin=true;
							    	do{
							    		pin=JOptionPane.showInputDialog(null,s);
							    		if(pin.length()==8){
							    			PIN  = new Util().HexStringToByteArray(pin);
							    			for(int i=5;i<9;i++)
										    	cmd.cmmd[10][i]=PIN[i-5];
							    			break;
							    		}
							    		s="Ooops saisissez votre code PIN en hexa : ";
							    			
							    	}while(2<3);
							    	
							    }
								
								
							    
							     
						  
							    analyse+= "\n\n                                   Analyse  : IMSI \n";
								
							    analyse += cmd.Command_SIM(cmd.cmmd[1]);
								analyse += "\n "+new analyse(cmd.reponse_byte()).analyse_IMSI();
						  
							    analyse+= "\n\n                                   Analyse  : TMSI & LAI \n";
								  
							    analyse += cmd.Command_SIM(cmd.cmmd[2]);
							    analyse += "\n "+new analyse(cmd.reponse_byte()).analyse_TMSI_LAI();
							    
							    analyse+= "\n                                   Analyse  : Algorithme d'authenfication du GSM \n";
								
							    analyse += cmd.Command_SIM(cmd.cmmd[3]);
							    analyse += "\n "+new analyse(cmd.reponse_byte()).analyse_auth_GSM();
							   
							    analyse+= "\n\n                                   Analyse  : EF_Kc \n";
								
							   		
							    analyse += cmd.Command_SIM(cmd.cmmd[4]);
							    analyse += "\n "+new analyse(cmd.reponse_byte()).analyse_EF_Kc();
							   
							    analyse+= "\n\n                                   Analyse  : Services SIM \n";
								
						   		
							    analyse += cmd.Command_SIM(cmd.cmmd[5]);
							    analyse += "\n "+new analyse(cmd.reponse_byte()).analyse_Service_SIM();
							    
							    analyse+= "\n\n                                   Analyse  : DF_TELECOM \n";
								
						   		
							    analyse += cmd.Command_SIM(cmd.cmmd[6]);
							    analyse += "\n "+new analyse(cmd.reponse_byte()).analyse_DF_TELECOM();
							    
							    analyse+= "\n\n                                   Analyse  : Lecture et ecriture des SMS dans la SIM\n";
						   		
							    analyse += cmd.Command_SIM(cmd.cmmd[7]);
							   // analyse += "\n "+new analyse(cmd.reponse_byte()).analyse_DF_TELECOM();
							    
							    analyse+= "\n\n                                   Analyse  : Lecture de l'annuaire des numeros ADN\n";
						   		
							    analyse += cmd.Command_SIM(cmd.cmmd[8]);
							    
							    analyse+= "\n\n                                   Analyse  : Lecture de l'annuaire de service FDN\n";
						   		
							    analyse += cmd.Command_SIM(cmd.cmmd[9]);
							
							   
								lb.setText(analyse);
								lb.setCaretPosition(0);
							
							} catch (CardException e) {
								e.printStackTrace();
							}
						}
					}
				}
		);
		

		
		deco.addActionListener(
				new ActionListener() {
					
					public void actionPerformed(ActionEvent ev) {
						if(ev.getActionCommand()== "Deconnecter"){
							try {
								cmd.deconnect_SIM();
							} catch (CardException e) {
								// TODO Auto-generated catch block
								
								
								e.printStackTrace();
							}
							
						}
					}
				}
		);
		
		
		
		
		clear.addActionListener(
				new ActionListener() {
					
					public void actionPerformed(ActionEvent ev) {
						if(ev.getActionCommand()== "Clear")
							lb.setText("");
					}
				}
		);
		
		
		
	}
	

}
