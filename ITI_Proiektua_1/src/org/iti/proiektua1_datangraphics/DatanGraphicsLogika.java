package org.iti.proiektua1_datangraphics;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public class DatanGraphicsLogika {

	
	public DatanGraphicsLogika(){
		
	}
	
	public String getKodeaString(){
		try {
			String path = "src" + File.separatorChar + "org" + File.separatorChar + "iti" + File.separatorChar + "proiektua1_datangraphics" + File.separatorChar + "examples" + File.separatorChar;
			System.out.println(Paths.get(path + "E1Gr.java").toFile().getAbsolutePath());
			List<String> lines = Files.readAllLines(Paths.get(path + "E1Gr.java"), Charset.defaultCharset());
			String kodea = "";
			Iterator<String> itr = lines.iterator();
			while (itr.hasNext()){
				kodea = kodea + "\n" + itr.next();
			}
			//TODO Gero kendu
			System.out.println(kodea);
			
			return kodea;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
}
