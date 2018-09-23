/*         Project    : Ecriture d'une application cliente en Java d'analyse de la carte SIM  */
/*         Author     : BOURI Mohamed						              */
/*         Version    : 1.0 					   			      */
/*         Date       : 03/2017                                      			      */

package fr.cryptis.SIM;

import javax.swing.JOptionPane;

public class Util {
	
	Util(){ }	
	
	public static String ByteArrayToHexString(byte[] bytes) {
		
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2]; // Each byte has two hex characters (nibbles)
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF; // Cast bytes[j] to int, treating as unsigned value
            hexChars[j * 2] = hexArray[v >>> 4]; // Select hex character from upper nibble
            hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Select hex character from lower nibble
        }
        return new String(hexChars);
    }	
	
	
	public static byte[] HexStringToByteArray(String s) throws IllegalArgumentException {
		int len = s.length();
		if (len % 2 == 1) {
			throw new IllegalArgumentException("Hex string must have even number of characters");
		}
		byte[] data = new byte[len / 2]; 
		for (int i = 0; i< len;i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i+1), 16));
		}
		return data;
	}
	
	public static String convrt_hex_chain(byte [] bytes){
		char c[]=new char[bytes.length*2];
		for(int i=0;i<bytes.length;i++){
			c[i]=(char)bytes[i];
			if((int)c[i]>255)
				c[i]='.';
		}
		return new String(c);
	}
	
	
	public static int calcule_taille_2(byte [] taille_memoire){
		
		return taille_memoire[0]<<8 & 0xff00 | taille_memoire[1]<<0 & 0x00ff ;
		
	}
	
	public static int calcule_taille_donnée(Byte d){
		
		
	
		return d.intValue();
		
	}
	
	public static boolean test_poid_fort(byte b ) {
		
		return ( b & 0x80 ) == 0x80; 
	
	}
	
	public static String entrer_Interface(){
		String str=" cou ";
		str=JOptionPane.showInputDialog(null, "Saisissez votre Code PIN ");
		return str;
	}
	
	public static void IMSI_inverse(char a, char b){
		
		char tmp;
		tmp=a;
		a=b;
		b=tmp;
		
	}

	
	public static String Services(Byte b) {
		
		int [] service= new int [8];
		String info="";
		if((b & 0x80) == 0x80){
			service[0]=1;
			info+="\nLe service n 1 : permet la désativation du code PIN utilisateur (CHV1) : est present";
		}
		else{
			service[0]=0;
			info+="\nLe service n 1 : permet la désativation du code PIN utilisateur (CHV1) : n'est pas present";

		}
			
		
		if((b & 0x40) == 0x40){
			service[1]=1;
			info+=" ,et il est active";
		}
		else{
			service[1]=0;
			info+=" ,et il n'est pas active";
		}
		
		if((b & 0x20) == 0x20){
			service[2]=1;
			info+="\nLe service n 2 : notifie la presence d'un annuaire de numero abreges (ADN) : est present";
		}
		else{
			service[2]=0;
			info+="\nLe service n 2 : notifie la presence d'un annuaire de numero abreges (ADN) : n'est pas present";

		}
		
		if((b & 0x10) == 0x10){
			service[3]=1;
			info+=" ,et il est active";
		}
		else{
			service[3]=0;
			info+=" ,et il n'est pas active";
		}
		
		if((b & 0x08) == 0x08){
			service[4]=1;
			info+="\nLe service n 3 : est un annuaire de numeros non abreges (FDN) : est present";
		}
		else{
			service[4]=0;
			info+="\nLe service n 3 : est un annuaire de numeros non abreges (FDN) : n'est pas present";
		}
		
		if((b & 0x04) == 0x04){
			service[5]=1;
			info+=" ,et il est active";
		}
		else{
			service[5]=0;
			info+=" ,et il n'est pas active";
		}
		if((b & 0x02) == 0x02){
			service[6]=1;
			info+="\nLe service n 4 : indique l'existence d'un fichier EF-SMS realisant le stockae des SMS : est present";
		}
		else{
			service[6]=0;
			info+="\nLe service n 4 : indique l'existence d'un fichier EF-SMS realisant le stockae des SMS : n'est pas present";
		}
		if((b & 0x01) == 0x01){
			service[6]=1;
			info+=" ,et il est active";
		}
		else{
			service[7]=0;
			info+=" ,et il n'est pas active";
		}
		
		
		return info; 
	}
	

}
