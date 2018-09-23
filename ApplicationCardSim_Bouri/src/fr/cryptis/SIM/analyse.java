/*         Project    : Ecriture d'une application cliente en Java d'analyse de la carte SIM  */
/*         Author     : BOURI Mohamed						              */
/*         Version    : 1.0 					   			      */
/*         Date       : 03/2017                                      			      */

package fr.cryptis.SIM;

import java.util.HashMap;

import javax.swing.JOptionPane;

public class analyse {
	public byte rps[];
	public String analyse;
	String reponse;
	static boolean presentation_pin = false;
	HashMap<Integer,String> repertoir;
	
	analyse(byte rps[]){
		this.rps=rps;
		analyse="";
		repertoir=new HashMap<Integer,String>();
		
		repertoir.put(202, "Grece");repertoir.put(206, "Belgique");repertoir.put(208, "France");repertoir.put(543, "France");				repertoir.put(547, "France");repertoir.put(546, "France");repertoir.put(340, "France");repertoir.put(214, "Espagne");
		repertoir.put(222, "Italie");repertoir.put(242, "Norvege");repertoir.put(240, "Suide");repertoir.put(244, "Finlande");				repertoir.put(604, "Maroc");
		repertoir.put(228, "Suisse");repertoir.put(234, "Royaume-Uni");repertoir.put(235, "Royaume-Uni");repertoir.put(303, "France");
		repertoir.put(312, "États-Unis");repertoir.put(311, "États-Unis");repertoir.put(310, "États-Unis");repertoir.put(302, "Canada"); 	
		repertoir.put(313, "États-Unis");repertoir.put(314, "États-Unis");repertoir.put(315, "États-Unis");repertoir.put(316, "Canada"); 	

	}
	
	analyse(){ }
	
	public String  analyser_GSM(){
		
		reponse= new Util().ByteArrayToHexString(this.rps);
		int i=0,b,j=0;
		byte [] byte_taille=new byte[2];
		byte_taille[0]=rps[2];
		byte_taille[1]=rps[3];
		analyse +=reponse.charAt(i*2)+""+reponse.charAt(i*2+1)+" "+reponse.charAt(i*2+2)+""+reponse.charAt(i*2+3)+" :  Sans signification\n";
		i=2;
		analyse +=reponse.charAt(i*2)+""+reponse.charAt(i*2+1)+" "+reponse.charAt(i*2+2)+""+reponse.charAt(i*2+3)+" :  Taille memoire non utilisee - "+new Util().calcule_taille_2(byte_taille)+" octets- \n"; 
		i=4;
		if(rps[i] == 0x7F )
			analyse +=reponse.charAt(i*2)+""+reponse.charAt(i*2+1)+" "+reponse.charAt(i*2+2)+""+reponse.charAt(i*2+3)+" :  Nom de repertoire "; 
		i+=2;
		analyse +="\n"+ reponse.charAt(i*2)+""+reponse.charAt(i*2+1)+"  : Type repertoire\n";
		i+=1;
		while(j<5){
		
			analyse += reponse.charAt(i*2)+""+reponse.charAt(i*2+1)+" ";
			j++;
			i++;
		}
		analyse +=" : Sans signification ";
		analyse += "\n"+reponse.charAt(i*2)+""+reponse.charAt(i*2+1)+" : "+new Util().calcule_taille_donnée(rps[i])+" octets de données GSM";
		i++;
		analyse +="\n"+reponse.charAt(i*2)+""+reponse.charAt(i*2+1)+" ";
		if(!new Util().test_poid_fort(rps[i])){
			analyse += " : bit de poids fort a 0 implique la presentation du code PIN ";
			//Si true une fenetre de saisie de code pin est déclenchée par la suite !!
			System.out.println("\n OUi OUi  analyse  il a de pin");
			new analyse().presentation_pin = true;
		}
		else
			analyse += " : bit de poids fort a 1 implique la non presentation du code PIN ";
		i++;
		analyse +="\n"+reponse.charAt(i*2)+""+reponse.charAt(i*2+1)+" : Nombre de sous-repertoire - "+new Util().calcule_taille_donnée(rps[i])+" octets - ";
		i++;
		analyse +="\n"+reponse.charAt(i*2)+""+reponse.charAt(i*2+1)+" : Nombre de fichiers - "+new Util().calcule_taille_donnée(rps[i])+" octets - ";
		i+=1;
		analyse +="\n"+reponse.charAt(i*2)+""+reponse.charAt(i*2+1)+" "+reponse.charAt(i*2+2)+""+reponse.charAt(i*2+3)+" :  Sans signification ";
		i+=2;
		b=rps[i] & 0x0f;
		analyse +="\n"+reponse.charAt(i*2)+""+reponse.charAt(i*2+1)+" : Code CHV1 (PIN utilisateur) initialise, "+b+" essais de PIN1 possibles ";
		i+=1;
		b=rps[i] & 0x0F;
		analyse +="\n"+reponse.charAt(i*2)+""+reponse.charAt(i*2+1)+" : Code initialise, "+b+" essais PUK1 possibles "; 
		i+=1;
		b=rps[i] & 0x0F;
		analyse +="\n"+reponse.charAt(i*2)+""+reponse.charAt(i*2+1)+" : Code CHV2 (PIN utilisateur) initialise, "+b+" essais de PIN2 possibles ";
		i+=1;
		b=rps[i]&0x0F;
		analyse +="\n"+reponse.charAt(i*2)+""+reponse.charAt(i*2+1)+" : Code initialise, "+b+" essais PUK2 possibles \n"; 
		i+=1;
		//System.out.println(" \n\n cou cou la taille de reponse est : "+reponse.length());
     	while(rps[i]!=(byte)0x90){
			analyse += reponse.charAt(i*2)+""+reponse.charAt(i*2+1)+" ";
			System.out.println(" \n\n cou cou la taille de reponse est : "+(i*2+2));
			i++;
		}
		analyse +=" : Non commentes \n";
		analyse +=""+ reponse.charAt(i*2)+""+reponse.charAt(i*2+1)+" "+reponse.charAt(i*2+2)+""+reponse.charAt(i*2+3)+" :  Status word (succes de la commande ) "; 
		return analyse;
	}
	
	
	public String analyse_IMSI(){
		
		reponse= new Util().ByteArrayToHexString(this.rps);
		System.out.println(" reponse est ::: "+reponse+" sa taille est "+reponse.length());
		String tmp="";
		char imsi_inverse[]=new char[reponse.length()-6];
		System.out.println(" la taille de imsi_inverse est "+imsi_inverse.length+"\ntest tes tes ");
		
		for(int i=2;i<imsi_inverse.length+2;i++){
			imsi_inverse[i-2]=reponse.charAt(i);
			tmp+=""+imsi_inverse[i-2];
		}
		analyse+= "\n IMSI : "+tmp;
		char tmpp;
		System.out.println(" la taille de imsi_inverse est "+imsi_inverse.length+"\ntest tes tes ");
		for(int i=0;i<imsi_inverse.length;i++){
			System.out.print(imsi_inverse[i]);
		}
		int j=0;
		char a,b;
		for(int i=0;i<imsi_inverse.length;i=i+2){
			j=i+1;
			a=imsi_inverse[i];b=imsi_inverse[j];
			new Util().IMSI_inverse(a,b);
			imsi_inverse[i]=b;imsi_inverse[j]=a;
		}
		
		System.out.println(" resultat  ");
		for(int i=0;i<imsi_inverse.length;i++){
			System.out.print(imsi_inverse[i]);
		}
		analyse+="\n IMSI inversé : "+new String(imsi_inverse);
		
		char code_paye[]=new char[3]; 
		code_paye[0]=imsi_inverse[1];
		code_paye[1]=imsi_inverse[2];
		code_paye[2]=imsi_inverse[3];
		int codepaye=Integer.parseInt(new String(code_paye));
		
		tmp="";
		for(int i=1;i<imsi_inverse.length;i++){
			
			tmp+=imsi_inverse[i];
		}
		
		analyse+= "\n IMSI pour analyse : "+tmp;
		analyse+= "\n( "+codepaye+" : Mobile Country Code de la "+repertoir.get(codepaye)+" ) ";
		return analyse;
		
	}
	
	public String analyse_TMSI_LAI(){
		
		reponse= new Util().ByteArrayToHexString(this.rps);
		String tmp="";
		
		for(int i=0;i<8;i++){
			tmp+=reponse.charAt(i)+"";
		}
		analyse+="\n TMSI sur 4 octets : "+tmp+"";
		tmp="";
		for(int i=8;i<18;i++){
			tmp+=reponse.charAt(i)+"";
		}
		analyse+="\n LAI sur 5 octets : "+tmp+"";
		analyse+="\n TMSI-TIME sur 1 octets : "+reponse.charAt(18)+""+reponse.charAt(19)+"";
		analyse+="\n Location-UPDATE sur 1 octets : "+reponse.charAt(20)+""+reponse.charAt(21)+"\n";
		return analyse;
		
	}
	
	public String analyse_auth_GSM(){
		
		reponse= new Util().ByteArrayToHexString(this.rps);
		String tmp="";
		
		for(int i=0;i<8;i++){
			tmp+=reponse.charAt(i)+"";
		}
		analyse+="\n SRES sur 4 octets : "+tmp+"";
		tmp="";
		for(int i=8;i<24;i++){
			tmp+=reponse.charAt(i)+"";
		}
		analyse+="\n Kc sur 8 octets       : "+tmp+"";
		return analyse;
	}
	
	public String analyse_EF_Kc(){
		reponse= new Util().ByteArrayToHexString(this.rps);
		String tmp="la mise a jour de la cle Kc : ";
		
		for(int i=0;i<reponse.length()-6;i++){
			tmp+=reponse.charAt(i)+"";
		}
		analyse+=tmp;
		return analyse;
		
	}
	
	public String analyse_Service_SIM(){
		
		reponse= new Util().ByteArrayToHexString(this.rps);
		String srv1="",srv2="";
		Byte b1 =  new Byte(rps[0]);
		Byte b2 =  new Byte(rps[1]);
		int [] servc,servc1= new int [8];
		srv1= new Util().Services(b1);
		srv2= new Util().Services(b2);		
		return srv1;
		
		
	}
	
	public static String analyse_SMS(String message,byte msg[]){
		int lgr_msg;
		String tmp="\n\nLes  Octets           |  Signification\n";
		
		if(message.charAt(0)=='0' & message.charAt(1)=='0'){
				tmp+="\n"+message.charAt(0)+message.charAt(1)+"                              Pas de SMS ";
		}
		else
			tmp+="\n"+message.charAt(0)+message.charAt(1)+"                              Presence d'un SMS ";
		
		tmp+="\n"+message.charAt(2)+message.charAt(3)+"                              Longueur de l'attribut SMS informaion\n";
		for(int i=4;i<18;i++)
			tmp+=message.charAt(i);
		tmp+="  Numero du centre de SMS";
		tmp+="\n"+message.charAt(18)+message.charAt(19)+"                              Le premier octet d'un SMS deliver\n";
		tmp+=""+message.charAt(20)+""+message.charAt(21)+"                              La longueur du numero de l'emetteur du SMS\n";
		tmp+=""+message.charAt(22)+""+message.charAt(23)+"                              Type du numero de l'emetteur \n";
		for(int i=24;i<=29;i++)
			tmp+=message.charAt(i);
		tmp+="                     Numero de l'emetteur \n";
		tmp+=""+message.charAt(30)+""+message.charAt(31)+"                              L'octet TP-PID (Protocol Identifier) \n";
		tmp+=""+message.charAt(32)+""+message.charAt(33)+"                              L'octet TP-DCS (Date Coding Scheme) \n";
		for(int i=34;i<=47;i++)
			tmp+=message.charAt(i);
		tmp+="  L'octet TP-SCTS (time stamp, l'heure d'emission) \n";
		if(new Util().test_poid_fort(msg[24]))
			lgr_msg = 256+new Byte (msg[24]).intValue();
		else
			lgr_msg = new Byte (msg[24]).intValue();
		
		tmp+=message.charAt(48)+""+message.charAt(49)+"                              TP-UDL (User Date Length), longeur du message "+lgr_msg+" octets\n\n";
		tmp+=" Le message : \n";
		for(int i=50;i<message.length()-4;i++)
			tmp+=message.charAt(i);
		tmp+="\n"+message.charAt(message.length()-4)+message.charAt(message.length()-3)+message.charAt(message.length()-2)+message.charAt(message.length()-1)+"                            Succes\n";
		
		
		return tmp; 
	}
	
	
	public static String analyse_ADN(String message,byte msg[]){
		int lgr_msg;
		String tmp="\n\nLes  Octets             Signification\n\n";
		for(int i=0;i<=27;i++)
			tmp+=message.charAt(i);
		tmp+=" : Serv.client (etiquette de 14 octets associee de numero de telephone\n";
		tmp+=""+message.charAt(28)+""+message.charAt(29)+"                             : Longueur en octet des attribut TON/NPI et numero de telephone \n";
		tmp+=""+message.charAt(30)+""+message.charAt(31)+"                             : Type de numero de telephone, 81 pour GSM\n";
		for(int i=32;i<=41;i++)
			tmp+=message.charAt(i);
		tmp+="          : Numero de telephone \n";
		tmp+=""+message.charAt(42)+""+message.charAt(43)+"                             : Capability/configuration identifier, attribut non utilise\n";
		tmp+=""+message.charAt(44)+""+message.charAt(45)+"                             : Extension 1 record identifier, attribut non utilise\n";
		tmp+="\n"+message.charAt(message.length()-4)+message.charAt(message.length()-3)+message.charAt(message.length()-2)+message.charAt(message.length()-1)+"                            Succes\n";
		return tmp; 
	}
	
	
	public String analyse_DF_TELECOM(){
		
		return ""; 
	}
	
	
	
	

}
