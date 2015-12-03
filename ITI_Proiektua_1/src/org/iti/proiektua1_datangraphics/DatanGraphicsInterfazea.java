package org.iti.proiektua1_datangraphics;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class DatanGraphicsInterfazea extends JFrame{
	public DatanGraphicsInterfazea() {
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Enunciado:");
		panel.add(lblNewLabel, BorderLayout.WEST);
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		panel.add(lblNewLabel_1, BorderLayout.CENTER);
		
		setSize(600, 600);
		
		//TODO GEro kendu probak egiteko bakarrik
		DatanGraphicsLogika l = new DatanGraphicsLogika();
		l.getAzalpenaString();
		l.kodeaExekutatu();
	}

}
