/*         Project    : Ecriture d'une application cliente en Java d'analyse de la carte SIM  */
/*         Author     : BOURI Mohamed						              */
/*         Version    : 1.0 					   			      */
/*         Date       : 03/2017                                      			      */

package fr.cryptis.SIM;
import java.util.List;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

public class Commande_Reponse {
	
	Card c;
	CardChannel canal;
	TerminalFactory  tf;
	CardTerminals liste;
	CommandAPDU commande;
	ResponseAPDU response,response1,response2,response_IMSI;
	List<CardTerminal> terminal;
	public static boolean pin;
	byte [][]cmmd;
	
	
	Commande_Reponse(byte cmmd [][]){
		
		tf = TerminalFactory.getDefault();
		liste = tf.terminals();
		terminal = null;
		this.cmmd=cmmd;
		pin=false;
		
	}
	
	public void connect_SIM(){
		
		// selection du terminal
		try{
			terminal = liste.list();
		}
		catch(CardException ce){
			ce.printStackTrace();
		}
		System.out.println(" Terminals : "+terminal);
		CardTerminal card=terminal.get(0);
		if (card == null){
			System.out.println(" Pas de lecteur detecte ");
		}
		else{
			try{
				if(!card.isCardPresent()){
					System.out.println("Inserez une carte");
					card.waitForCardPresent(0);
				}
				c=card.connect("*");
				// ouverture d'un canal 
				canal=c.getBasicChannel();
				/*	CommandAPDU commande= new CommandAPDU(
					new byte[]{(byte)0xA0,(byte)0x20,(byte)0x00,(byte)0x01,(byte)0x08,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF});
				*/			
			
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}	
	}
	
	public String Command_SIM(byte cmd[]) throws CardException{
		
		int taille,taille_fichier,taille_enrg,nmbr_enrg=0,length;
		String comande,rp;
		boolean booln,bool1=false,SMS=false,FDN=false, ADN=false;
		byte[] taille_fich=new byte[2],msg;
		
		if(cmd[5]==0x6F)
			if(cmd[6]==0x3C)
				SMS= true;
		
		if(cmd[5]==0x6F)
			if(cmd[6]==0x3A)
				ADN= true;
		
		if(cmd[5]==0x6F)
			if(cmd[6]==0x3B)
				FDN= true;
		
		commande= new CommandAPDU(cmd);
		comande ="\nCommande : "+this.ToString();
		response= canal.transmit(commande);
		comande+="\nReponse      : "+this.response_SIM();
		
		System.out.println(comande);//+"\nresponse : "+new Util().ByteArrayToHexString(response.getBytes()));
		
		
		
		
		length=new Util().calcule_taille_donnée((byte)response.getSW());
		byte w=(byte)response.getSW();
		commande= new CommandAPDU(new byte[]{(byte)0xA0,(byte)0xC0,(byte)0x00,(byte)0x00,w});
		System.out.println("\nle comande test 1 . "+new Util().ByteArrayToHexString(commande.getBytes()));
		
		comande+="\n\nCommande : "+this.ToString();
		response= canal.transmit(commande);
		if( new analyse().presentation_pin == true){
			commande= new CommandAPDU(this.cmmd[10]);
			System.out.println("\n\n ui ici commande PIN :  "+new Util().ByteArrayToHexString(cmmd[10]));
			response2=canal.transmit(commande);
			System.out.println("\n\n ui ici commmande passe le test  "+new Util().ByteArrayToHexString(response2.getBytes()));
			new analyse().presentation_pin = false;
		}
		System.out.println(" reponse test 1 : "+new Util().ByteArrayToHexString(response.getBytes()));
		comande+="\nReponse : "+new Util().ByteArrayToHexString(response.getBytes());
		response1=response;
		boolean bool=true;
		rp=new Util().ByteArrayToHexString(response.getBytes());
		if( SMS || ADN || FDN ){
			
			System.out.println(" test test 2: "+new Util().ByteArrayToHexString(response.getBytes()));

			taille_fich[0]=response.getBytes()[2];
			taille_fich[1]=response.getBytes()[3];
			taille_fichier=new Util().calcule_taille_2(taille_fich);
			if(new Util().test_poid_fort(response.getBytes()[length-1]))
				taille_enrg = 256+new Byte (response.getBytes()[length-1]).intValue();
			else
				taille_enrg = new Byte (response.getBytes()[length-1]).intValue();
			nmbr_enrg=taille_fichier/taille_enrg;
			
			comande+="\nTaille de fichier "+rp.charAt(4)+rp.charAt(5)+rp.charAt(6)+rp.charAt(7)+" : "+taille_fichier+" "
					+ " octets \nTaille de l'enregistrement  "+rp.charAt(length*2-2)+rp.charAt(length*2-1)+" : "+taille_enrg+" octets ."
							+ "\nNombre d'enregistrement : "+nmbr_enrg+"\n";
			bool1=true;
		}
		if(cmd[5]== 0x7f){
			if(cmd[6]==0x20)
				bool=false;
			if(cmd[6]==0x10)
				bool=false;
		}
	   if(!bool1){
		if( cmd[1]!= (byte)0x88 )
			if( bool==true ){
			
			// Read BINARY
			System.out.println(" \n response 11 : "+new Util().ByteArrayToHexString(response.getBytes()));
			commande= new CommandAPDU(new byte[]{(byte)0xA0,(byte)0xB0,(byte)0x00,(byte)0x00,response.getBytes()[3]});
			comande+="\n\nCommande : "+this.ToString();
			response= canal.transmit(commande);
			
			comande+="\nReponse      : "+this.response_SIM();
			bool=false;
			if(cmd[5] == (byte)0x6f){
				if(cmd[6]==(byte)0x20)
					bool=true;
			}
			
			if(bool == true){
				
				commande= new CommandAPDU(new byte[]{(byte)0xA0,(byte)0xD6,(byte)0x00,(byte)0x00,(byte)0x09,(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,(byte)0x89,(byte)0xAB,(byte)0xCD,(byte)0xEF,(byte)0x00});
				comande+="\n\nCommande : "+this.ToString();
				response= canal.transmit(commande);
				comande+="\nReponse     : "+this.response_SIM();
				System.out.println(" Verification la commande est : "+new Util().ByteArrayToHexString(cmd));
				commande= new CommandAPDU(new byte[]{(byte)0xA0,(byte)0xB0,(byte)0x00,(byte)0x00,response1.getBytes()[3]});
				comande+="\n\nCommande : "+this.ToString();
				response= canal.transmit(commande);
				System.out.println("\n Verification de la reponse  : "+this.response_SIM());
				comande+="\nReponse     : "+this.response_SIM();
			}
			
		}}else if(SMS){
			// Read RECORD
			int i=1;
			while(i<nmbr_enrg){
				
				commande= new CommandAPDU(new byte[]{(byte)0xA0,(byte)0xB2,(byte)i,(byte)0x04,response.getBytes()[length-1]});
				
			    comande+="\n\nCommande : "+this.ToString();
				response= canal.transmit(commande);
				msg=response.getBytes();
				rp=new Util().ByteArrayToHexString(response.getBytes());
				comande+="\nReponse      : "+this.response_SIM();
				
				commande= new CommandAPDU(cmd);
				response= canal.transmit(commande);
				
				length=new Util().calcule_taille_donnée((byte)response.getSW());
				byte ww=(byte)response.getSW();
				commande= new CommandAPDU(new byte[]{(byte)0xA0,(byte)0xC0,(byte)0x00,(byte)0x00,ww});
				response= canal.transmit(commande);
				comande+=new analyse().analyse_SMS(rp, msg);
				//String ttt=new analyse().analyse_SMS(rp, msg); //  test
				//System.out.println("\n\n SMS : "+ttt);
				i++;
				
			}
			
			
		}else if(ADN){
			// Read RECORD
			int i=1;
			while(i<nmbr_enrg){
				
				    commande= new CommandAPDU(new byte[]{(byte)0xA0,(byte)0xB2,(byte)i,(byte)0x04,response.getBytes()[length-1]});
				
				    comande+="\n  Commande         : "+this.ToString();
					response= canal.transmit(commande);
					msg=response.getBytes();
					comande+="\n  Nom de Contact : "+new Util().convrt_hex_chain(msg);
					//System.out.println(" \n\n  "+new Util().convrt_hex_chain(msg));
					rp=new Util().ByteArrayToHexString(response.getBytes());
					comande+="\n  Reponse              : "+this.response_SIM();
					
					commande= new CommandAPDU(cmd);
					response= canal.transmit(commande);
					
					length=new Util().calcule_taille_donnée((byte)response.getSW());
					byte ww=(byte)response.getSW();
					commande= new CommandAPDU(new byte[]{(byte)0xA0,(byte)0xC0,(byte)0x00,(byte)0x00,ww});
					comande+="\n  Commande         : "+this.ToString();
					response= canal.transmit(commande);
					comande+="\n  Reponse              : "+this.ToString_reponse();
					comande+="\n  Analyse ADN"+new analyse().analyse_ADN(rp, msg);
					i++;
				
			}
		}else if(FDN){
			// Read RECORD
			int i=1;
			while(i<nmbr_enrg){
				commande= new CommandAPDU(new byte[]{(byte)0xA0,(byte)0xB2,(byte)i,(byte)0x04,response.getBytes()[length-1]});
				
			    comande+="\nCommande : "+this.ToString();
				response= canal.transmit(commande);
				comande+="\nReponse      : "+this.response_SIM();
				
				commande= new CommandAPDU(cmd);
				response= canal.transmit(commande);
				
				length=new Util().calcule_taille_donnée((byte)response.getSW());
				byte ww=(byte)response.getSW();
				commande= new CommandAPDU(new byte[]{(byte)0xA0,(byte)0xC0,(byte)0x00,(byte)0x00,ww});
				response= canal.transmit(commande);
				i++;
				
			}
		}
		return comande;
	}
	
	public String ToString(){
		
		return new Util().ByteArrayToHexString(commande.getBytes()); 
	}
	
	public String ToString_reponse(){
		
		return new Util().ByteArrayToHexString(response.getBytes()); 
	}
	
	public String response_SIM(){
		
		String rspnse;
		
		rspnse=new Util().ByteArrayToHexString(response.getBytes());
		
		return rspnse;
				
	}
	
	public byte [] reponse_byte() {
		return response.getBytes();
	}
	
	public void deconnect_SIM() throws CardException {
	     c.disconnect(true);	
	}		
	 
}
