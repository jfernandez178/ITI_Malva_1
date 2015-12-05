package org.iti.proiektua1_login;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.iti.proiektua1_datuKudeatzailea.DatuKudeatzailea;

public class LoginLogika{
	
	private static LoginLogika nireLoginogika;
	private DatuKudeatzailea datuKudeatzailea;
	
	private LoginLogika(){
		datuKudeatzailea = DatuKudeatzailea.getDatuKudeatzailea();
		
	}
	
	public static synchronized LoginLogika getLoginLogika(){
		
		if(nireLoginogika == null){
			nireLoginogika = new LoginLogika();
		}
		
		return nireLoginogika;
	}
	
	//username bat-i dagkion pashitza lortzen da
	  private String erabiltzailearenPasahitzaLortu(String username){
		  
		  String pasahitza = null;
		  
		  //SQL sententzia exekutatzen da erabiltzaileari dagokion pasahitza lortzeko
		  String query = "SELECT password FROM Users Where username='" + username + "';";
		  
		  try {
			
			ResultSet emaitza = datuKudeatzailea.selectQuery(query);
			while(emaitza.next()){
				pasahitza = emaitza.getString("password");
			}
			
		  	  
		  } catch (SQLException e) {
			
			e.printStackTrace();
		}
		  //pasahitza bueltatzen da
		  return pasahitza;
	  }
	  
	  
	  //Erabiltzailearen login-a zuzena den edo ez adierazten duen metodoa
	  public boolean loginZuzena(String usernameTest, String passwordTest){
		  boolean sarreraZuzena = false;
		  
		  //erabiltzaileak sartu duen pashaitzaren hash-a lortzen dugu
		  String passwordTestHash = "" + passwordTest.hashCode();
		  
		  //erabiltzaile hori existitzen bada, datu-basean daukan pasahitza eskuratzen da
		  String passwordHashDatubase = erabiltzailearenPasahitzaLortu(usernameTest);
		  
		  //Erabiltzailea existitzen bada
		  if (passwordHashDatubase != null){
			  
			  //pasahitzek koinziditzen badute
			  if(passwordHashDatubase.equals(passwordTestHash)){
				  
				  //sarrera zuzena dela adierazten da
				  sarreraZuzena = true;
			  }
		  }
		  
		  	  
		  
		  return sarreraZuzena;
		  
	  }
	  
	  //Erabiltzaile bat erregistratzeaz arduratuko den metodoa da
	  public boolean erabitzaileaErregistratu(String username, String password){
		  
		  //Erabiltzailea sortu den edo ez adierazten duen flag-a
		  boolean sortuta = false;
		  
		  //erregistratu nahi den erabiltzailearentzat ez badago pasahitzik gordeta datu-basean, ez da existitzen
		  if(erabiltzailearenPasahitzaLortu(username) == null){
			  
			  //Pasahitzaren hash kodea lortzen da
			  String passwordHash = "" + password.hashCode();
			  			  			  
			  //Erabiltzaile berria sortzen da eta datu-basean gordetzen da
			  String query = "INSERT INTO Users values('" + username + "', '" + passwordHash + "')";
			  System.out.println("Query -> " + query);
			  sortuta = datuKudeatzailea.otherQuery(query);
			  			  
		  }
		  
		  return sortuta;
		  		  
	  }

	
}
