package org.iti.Proiektua1_datan;

import org.iti.Proiektua1_Utility.FitxeroUtil;
import org.iti.Proiektua1_Utility.SeparatuKopuruEzberdinException;

public class DatanLogika {

	private String fullKodigo;
	private String fullExplic;
	private String[] lineasKod;
	private String[] lineasExp;
	private int pos;
	private String spliZati;
	private String[] eguneroko;
	private int egunPos;
	
public DatanLogika() throws SeparatuKopuruEzberdinException{
		pos = 0;
		//se define el string con el que se har� el split de el fllcodigo y el fullexplicacion
		spliZati = "//Zatileku";
		//Code Artxiboa Kargatu
		fullKodigo = getFitxategi("");
		//Azalpen Artxiboa Kargatu
	    fullExplic = getFitxategi("");
		//Codea Zatitu
		kodeaZatitu(fullKodigo);
		//Azalpena Zatitu
		azalpenaZatitu(fullExplic);
		
		if(lineasKod.length != lineasExp.length){
			
			throw new SeparatuKopuruEzberdinException();
		}
		
	
	}

private String getFitxategi(String fitxategiIzena){
		String kodea;
		FitxeroUtil fitx = new FitxeroUtil();
		kodea = fitx.getStringFitxategitik(fitxategiIzena);
		return kodea;
	
}
//se corta el string del codigo completao para guadarlo en un array
public void kodeaZatitu(String banatzaile){	
	lineasKod = fullKodigo.split(banatzaile);
}
//se corta el string de la explicaci�n completa para guadarla en un array
public void azalpenaZatitu(String banatzaile){
	lineasExp = fullExplic.split(banatzaile);
}
//esta funci�n sirve para obtener la l�nea del codigo que se desee y su correspondiente explicacion. Pos indica que l�nea se desea
public String[] textuakHartu(int pos){
	String[] textuak = null;
	textuak[0] = lineasKod[pos];
	textuak[1] = lineasExp[pos];
	
	return textuak;
	
}

public int getPos(){
	return pos;
}
//funci�n para actualizar el valor de la posicion
public void actPos(boolean mas){
	if(mas){
		pos ++;
		if(pos >fullKodigo.length()){
			pos = -2;
		}
	}else{
		pos --;
		if(pos< 0){
			pos = -1;
		}
	}
}

public void exekutatu(){
	
}
//Guarda las acciones que hace el usuario
public void eguneroko(String ekintza){
	eguneroko[egunPos] = ekintza;	
}

//Pasa el diario de acciones a un documento
public void egunerokoaItxi(){
	
}
	
}
