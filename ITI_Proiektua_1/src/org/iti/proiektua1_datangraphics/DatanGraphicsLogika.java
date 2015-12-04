package org.iti.proiektua1_datangraphics;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.iti.proiektua1_datangraphics.examples.E1Gr;

public class DatanGraphicsLogika {

	private String kodeOsoa;
	private String azalpenOsoa;
	private String[] kodea;
	private String[] azalpena;
	private int posizioa;
	
	public DatanGraphicsLogika(){
		posizioa = 0;
		kodeOsoa = getStringFitxategitik("E1Gr.java");
		kodea = zatiakSeparatu(kodeOsoa);
		azalpenOsoa = getStringFitxategitik("E1Gr.txt");
		azalpena = zatiakSeparatu(azalpenOsoa);
		
		//Separatu kendu
		azalpenOsoa = azalpenOsoa.replaceAll("//separatu", "");
		kodeOsoa = kodeOsoa.replaceAll("//separatu", "");
	}
	
	/**
	 * Hurrengo posiziora pasatzen da
	 * @return Azkenengo posizioa den
	 */
	public boolean hurrengoa(){
		posizioa++;
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
		try {
			String path = "src" + File.separatorChar + "org" + File.separatorChar + "iti" + File.separatorChar + "proiektua1_datangraphics" + File.separatorChar + "examples" + File.separatorChar;
			List<String> lines = Files.readAllLines(Paths.get(path + fitxategiIzena), Charset.defaultCharset());
			String kodea = "";
			Iterator<String> itr = lines.iterator();
			while (itr.hasNext()){
				kodea = kodea + "\n" + itr.next();
			}
			return kodea;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
