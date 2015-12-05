package org.iti.proiektua1_datuKudeatzailea;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;

public class DatuKudeatzailea {
	//EMA bat izango da, Login-ari dagozkion datuak eskuratzeko
	private static DatuKudeatzailea nireDatuKudeatzailea = null;
	private Connection c;
	private Statement stmt;

	  private DatuKudeatzailea()
	  {
		//Datu basearekin konexioa egingo da
		  //Datu basea ez bada existitzen, sortu egingo da
	    c = null;
	    stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:Login_Datu_Basea.sqlite");
	      System.out.println("Datu-basea ondo ireki da.");
	      	    
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    
	  }
	  
	  
	  
			  
	  
	  
	  public static synchronized DatuKudeatzailea getDatuKudeatzailea(){
		  if(nireDatuKudeatzailea == null){
			  nireDatuKudeatzailea = new DatuKudeatzailea();
		  }
		  return nireDatuKudeatzailea;
	  }
	  
	  //Datu-basetik emaitza bat lortzeko erabiliko den metodoa
	  public ResultSet selectQuery(String query){
		  ResultSet emaitza = null;
		  
		  try {
				//Emaitza lortzen da
				stmt = c.createStatement();
				System.out.println("Query -> " + query);
				emaitza = stmt.executeQuery(query);
								
			  	  
			  } catch (SQLException e) {
				
				e.printStackTrace();
			}		  
		  
		  return emaitza;
		  
	  }
	  
	  //Datu-basean bestelako eragiketa bat burutzeko metodoa
	  public boolean otherQuery(String query){
		  
		  //Eragiketa ondo atera den edo ez jakiteko metodoa
		  boolean ondoAteraDa = false;
		  
		  try {
				//Emaitza lortzen da
				stmt = c.createStatement();
				stmt.executeUpdate(query);
				ondoAteraDa = true;				
			  	  
			  } catch (SQLException e) {
				
				e.printStackTrace();
			}	
		  
		  return ondoAteraDa;
		  
	  }
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  //-------------------------
	  
	  
	  
	  
	  
}

