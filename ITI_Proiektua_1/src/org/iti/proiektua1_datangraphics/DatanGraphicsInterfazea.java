package org.iti.proiektua1_datangraphics;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

public class DatanGraphicsInterfazea extends JFrame{
	public DatanGraphicsInterfazea() {
		
		setSize(600, 600);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JButton btnNewButton = new JButton("Salir");
		panel.add(btnNewButton, BorderLayout.WEST);
		
		JButton btnNewButton_1 = new JButton("Siguiente");
		panel.add(btnNewButton_1, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("C\u00F3digo:");
		panel_2.add(lblNewLabel, BorderLayout.NORTH);
		
		JTextArea textArea = new JTextArea();
		panel_2.add(textArea, BorderLayout.CENTER);
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_1 = new JLabel("Explicaci\u00F3n:");
		panel_3.add(lblNewLabel_1, BorderLayout.NORTH);
		
		JTextArea textArea_1 = new JTextArea();
		panel_3.add(textArea_1, BorderLayout.CENTER);
		
		//TODO GEro kendu probak egiteko bakarrik
		DatanGraphicsLogika l = new DatanGraphicsLogika();
		l.getAzalpenaString();
		l.kodeaExekutatu();
		while(!l.hurrengoa()){
			System.out.println("----------------------------");
			System.out.println(l.getKodeaString());
			System.out.println(l.getAzalpenaString());
		}
		System.out.println("----------------------------");
		System.out.println(l.getKodeaString());
		System.out.println(l.getAzalpenaString());
	}

}
