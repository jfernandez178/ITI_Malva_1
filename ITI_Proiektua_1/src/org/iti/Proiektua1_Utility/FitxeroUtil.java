package org.iti.Proiektua1_Utility;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public class FitxeroUtil {

	public String getStringFitxategitik(String fitxategiIzena){
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
