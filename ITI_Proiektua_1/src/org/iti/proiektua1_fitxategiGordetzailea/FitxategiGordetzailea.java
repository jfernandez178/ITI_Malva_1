package org.iti.proiektua1_fitxategiGordetzailea;

public class FitxategiGordetzailea {
	
	private static FitxategiGordetzailea nireFitxategiGordetzailea = null;
	
	//Fitxategiaren path-a gordeko duen aldagaia
	private String fitxategiarenPatha;
	
	private FitxategiGordetzailea(){
		
		fitxategiarenPatha = null;
		
		
	}
	
	//EMA atzitzeko metodoa
	public static synchronized FitxategiGordetzailea getFitxategiGordetzailea(){
		
		if(nireFitxategiGordetzailea == null){
			nireFitxategiGordetzailea = new FitxategiGordetzailea();
		}
		
		return nireFitxategiGordetzailea;
		
	}
	
	
	//Fitxategia zein tokitan gordeko den adieraziko duen path-a
	public boolean fitxategiaGordetzekoDirektorioaEsleitu(String path){
		
		//Fitxategiaren path-a ondo esleitu dela adierazten duen aldagia
		boolean ondoEsleituDa = false;
		
		//TODO:
		
		return ondoEsleituDa;
		
	}
	
	
	//Fitxategi batetan infomazioa gordetzeko erabiliko den metodoa
	public boolean fitxategianGorde(String textua){
		
		//Ondo gorde den edo ez adierazteko aldagaia
		boolean ondoGordeDa = false;
		
		//TODO:
		
		return ondoGordeDa;
		
	}

}
