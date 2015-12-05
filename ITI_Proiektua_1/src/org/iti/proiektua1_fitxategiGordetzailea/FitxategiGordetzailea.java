package org.iti.proiektua1_fitxategiGordetzailea;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class FitxategiGordetzailea {
	
	private static FitxategiGordetzailea nireFitxategiGordetzailea = null;
	
	//Erabiltzaile bakoitzaren fitxategiaren path-ak gordeko duen aldagaia
	private static HashMap<String, String> fitxategiarenPatha;
	
	private FitxategiGordetzailea(){
		
		fitxategiarenPatha = new HashMap<String, String>();
		
		
	}
	
	//EMA atzitzeko metodoa
	public static synchronized FitxategiGordetzailea getFitxategiGordetzailea(){
		
		if(nireFitxategiGordetzailea == null){
			nireFitxategiGordetzailea = new FitxategiGordetzailea();
		}
		
		return nireFitxategiGordetzailea;
		
	}
	

	//Erabiltzaileak informazioa gordetzeko fitxategi bat esleituta daukan edo ez jakiteko emtodoa da
	public boolean fitxategiaEsleitutaDauka(String username){
		
		//Emaitza jasotzeko aldagaia
		boolean badauka = true;
		
		//Fitxategiaren patha gordetzen duen aldagaia null bada, ez dauka esleituta
		if(fitxategiarenPatha.get(username) == null){
			badauka = false;
		}
				
		return badauka;
	}
	
		
		
		
	private boolean fitxategiaExistitzenAlDa(String path, String fitxategia){
		
		//Fitxategia existitzen den ala ez jakiteko kodea
		boolean existitzenDa = false;
		
		//Path-a fitxategiarekin lotzen da ikusteko ea existitzen den edo ez
		String fitxategiaPatharekin = path + "\\" + fitxategia;
		
		//Fitxategia existitzen den edo ez ikusten da
		File file = new File(fitxategiaPatharekin);
		
        if (file.exists()){
            existitzenDa = true;
            
        }
        
		
		return existitzenDa;
	}
	
	
	//Fitxategia zein tokitan gordeko den adieraziko duen path-a
	public boolean fitxategiaGordetzekoDirektorioaEsleitu(String path, String fitxategiIzena, String username){
		
		//Fitxategiaren path-a ondo esleitu dela adierazten duen aldagia
		boolean ondoEsleituDa = false;
		
		//Konprobatu nahi den patha
		String pathKonprobatu = path + "\\" + fitxategiIzena;
				
		//Fitxategia existitzen ez baldin bada
		if(!fitxategiaExistitzenAlDa(path, fitxategiIzena)){
			
			//Path-a aldagai pribatuan gordeko da, dagokion erabiltzaile-izenari lotuta
			fitxategiarenPatha.put(username, pathKonprobatu);
			System.out.println(pathKonprobatu);
			
			//fitxategia sortzen da
			File fitx = new File(fitxategiarenPatha.get(username));
			try {
				fitx.createNewFile();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			ondoEsleituDa = true;
		}
		
		//Fitxategia existitzen bada baina erablitzaileak esleituta duena baldin bada
		
		else if(fitxategiaExistitzenAlDa(path, fitxategiIzena) && pathKonprobatu.equals(fitxategiarenPatha.get(username))){
			
			ondoEsleituDa = true;
		}
					
		System.out.println("esleitu --> " + ondoEsleituDa);
			
		return ondoEsleituDa;
		
	}
	
	
	//Fitxategi batetan infomazioa gordetzeko erabiliko den metodoa
	public boolean fitxategianGorde(String textua, String username){
		
		//Ondo gorde den edo ez adierazteko aldagaia
		boolean ondoGordeDa = false;
		
		
		//Fitxategia eskuratzen da
		File fitxategia = new File(fitxategiarenPatha.get(username));
		
		//Fitxategia existitzen den konprobatzen da, badaezpada
		if(fitxategia.exists()){

			//Textua gordetzen da fitxategian
			BufferedWriter bw;
			
			try {
				bw = new BufferedWriter(new FileWriter(fitxategia));
				bw.write(textua);
			    bw.close();
			}
			
			catch (IOException e) {
				
				e.printStackTrace();
			}
		    
		    
			ondoGordeDa = true;
		}	
		
		return ondoGordeDa;
		
	}

}
