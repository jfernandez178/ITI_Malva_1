package org.iti.Proiektua1_datan;

import org.iti.Proiektua1_Utility.FitxeroUtil;
import org.iti.Proiektua1_Utility.SeparatuKopuruEzberdinException;
import org.iti.proiektua1_datangraphics.examples.E8Mtx;

public class DatanLogika {

	private String fullKodigo;
	private String fullExplic;
	private String[] lineasKod;
	private String[] lineasExp;
	private int pos;
	private String spliZati;
	
public DatanLogika() throws SeparatuKopuruEzberdinException{
		pos = 0;
		//se define el string con el que se hará el split de el fllcodigo y el fullexplicacion
		spliZati = "//Zatileku";
		//Code Artxiboa Kargatu
		fullKodigo = getFitxategi("E8Mtx.java");
		//Azalpen Artxiboa Kargatu
	    fullExplic = getFitxategi("E8Mtx.txt");
		//Codea Zatitu
		kodeaZatitu(spliZati);
		//Azalpena Zatitu
		azalpenaZatitu(spliZati);
		
		if(lineasKod.length != lineasExp.length){
			
			throw new SeparatuKopuruEzberdinException();
		}
		
	
	}
//se llama a fitxeroutil para obtener el fichero y guardarlo en las variables globales
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
//se corta el string de la explicación completa para guadarla en un array
public void azalpenaZatitu(String banatzaile){
	lineasExp = fullExplic.split(banatzaile);
}
//esta función sirve para obtener la línea del codigo que se desee y su correspondiente explicacion. Pos indica que línea se desea

public String[] textuakHartu(int pos){
	String[] textuak =  new String[2];
	textuak[0] = lineasKod[pos];
	textuak[1] = lineasExp[pos];
	
	return textuak;
	
}

public int getPos(){
	return pos;
}
//función para actualizar el valor de la posicion
public void actPos(boolean mas){
	if(mas == true){
		pos ++;
		
	}else{
		pos --;
		
	}
}
//se ejecuta la funcion E8Mtx
public void exekutatu(){
	new E8Mtx();
}
public int getMaxOrri(){
	return lineasKod.length;
}

	
}
