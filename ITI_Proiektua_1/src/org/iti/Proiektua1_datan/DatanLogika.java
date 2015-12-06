package org.iti.Proiektua1_datan;

import org.iti.proiektua1_datangraphics.SeparatuKopuruEzberdinException;

public class DatanLogika {

	private String fullKodigo;
	private String fullExplic;
	private String[] lineaKod;
	private String[] lineaExp;
	private int pos;
	
public DatanLogika() throws SeparatuKopuruEzberdinException{
		//Code Artxiboa Kargatu
	
		//Azalpen Artxiboa Kargatu
	
		//Codea Zatitu
		kodeaZatitu(fullKodigo);
		//Azalpena Zatitu
		azalpenaZatitu(fullExplic);
		
		if(lineaKod.length != lineaExp.length){
			
			throw new SeparatuKopuruEzberdinException();
		}
		
	
	}
//se corta el string del codigo completao para guadarlo en un array
public void kodeaZatitu(String code){
	
}
//se corta el string de la explicación completa para guadarla en un array
public void azalpenaZatitu(String expl){
	
}
//esta función sirve para obtener la línea del codigo que se desee y su correspondiente explicacion. Pos indica que línea se desea
public String[] textuakHartu(int pos){
	return lineaExp;
	
}

public int getPos(){
	return pos;
}

public void actPos(boolean mas){
	if(mas){
		pos ++;
	}else{
		pos --;
	}
}

public void exekutatu(){
	
}
	
}
