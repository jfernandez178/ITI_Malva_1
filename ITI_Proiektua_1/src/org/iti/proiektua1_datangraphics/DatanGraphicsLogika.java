package org.iti.proiektua1_datangraphics;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.iti.Proiektua1_Utility.FitxeroUtil;
import org.iti.Proiektua1_Utility.SeparatuKopuruEzberdinException;
import org.iti.proiektua1_datangraphics.examples.E1Gr;

public class DatanGraphicsLogika {

	private String kodeOsoa;
	private String azalpenOsoa;
	private String[] kodea;
	private String[] azalpena;
	private int posizioa;
	
	public DatanGraphicsLogika() throws SeparatuKopuruEzberdinException{
		posizioa = 0;
		
		//Fitxategiak kargatu
		kodeOsoa = getStringFitxategitik("E1Gr.java");
		kodea = zatiakSeparatu(kodeOsoa);
		azalpenOsoa = getStringFitxategitik("E1Gr.txt");
		azalpena = zatiakSeparatu(azalpenOsoa);
		
		//Separatu kendu
		azalpenOsoa = azalpenOsoa.replaceAll("//separatu", "");
		kodeOsoa = kodeOsoa.replaceAll("//separatu", "");
		
		//Konprobatu separatu kopuru berdina dela
		if (azalpena.length != kodea.length){
			throw new SeparatuKopuruEzberdinException();
		}
		
	}
	
	/**
	 * Hurrengo posiziora pasatzen da
	 * @return Azkenengo posizioa den
	 */
	public boolean hurrengoa(){
		posizioa++;
		if (posizioa > kodea.length){
			posizioa = kodea.length;
		}
		return (posizioa==kodea.length);
	}
	
	/**
	 * Aurreko posiziora pasatzen da
	 * @return lehenengo posizioa den
	 */
	public boolean aurrekoa(){
		posizioa--;
		if (posizioa < 0){
			posizioa = 0;
		}
		return (posizioa==0);
	}
	
	/**
	 * 
	 * @return lehenengo posizioa den
	 */
	public boolean lehenengoPosizioa(){
		return (posizioa==0);
	}
	
	/**
	 * 
	 * @return azkenengo posizioa den
	 */
	public boolean azkenengoPosizioa(){
		return (posizioa==kodea.length);
	}
	
	/**
	 * Goazen posizioaren kodearen zatia bueltatzen du. Azkenengo posizioan kode osoa bueltatzen da
	 * @return Kode zatia
	 */
	public String getKodeaString(){
		if (posizioa == kodea.length){
			return kodeOsoa;
		}else{
			return kodea[posizioa];
		}
	}
	
	/**
	 * Azalpenaren zatia bueltatzen du. Azkenengo posizioan bagaude azalpen osoa bueltatzen da.
	 * @return azalpena
	 */
	public String getAzalpenaString(){
		if (posizioa == azalpena.length){
			return azalpenOsoa;
		}else{
			return azalpena[posizioa];
		} 
	}
	 
	/**
	 * Adibideko kodea exekutatzen du.
	 */
	public void kodeaExekutatu(){
		new E1Gr();
	}
	
	/**
	 * Textua //separatu indikatzen duen tokietan banatuko da
	 * @param text String formatuan textua
	 * @return Textua zatitan banatuta
	 */
	public String[] zatiakSeparatu(String text){
		return text.split("//separatu");
	}
	
	/**
	 * datangraphics/examples karpetaren barruko fitxategiak string batera pasatzeko metodoa
	 * @param fitxategiIzena Fitxategiaren izena
	 * @return String bat fitxategiaren edukiarekin
	 */
	private String getStringFitxategitik(String fitxategiIzena){
		String kodea;
		FitxeroUtil fitx = new FitxeroUtil();
		kodea = fitx.getStringFitxategitik(fitxategiIzena);
		return kodea;
	}
	
}
