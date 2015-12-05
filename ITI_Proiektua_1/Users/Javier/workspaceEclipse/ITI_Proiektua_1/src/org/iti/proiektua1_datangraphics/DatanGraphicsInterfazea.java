package org.iti.proiektua1_datangraphics;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DatanGraphicsInterfazea extends JFrame{
	
	private DatanGraphicsLogika datanGraphicsLogika;
	private JTextArea textAreaCodigo;
	private JTextArea textAreaExplicacion;
	private JButton btnSiguiente;
	private JButton btnSalirAtras;
	
	public DatanGraphicsInterfazea() {
		
		setSize(600, 600);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		btnSalirAtras = new JButton("Salir");
		panel.add(btnSalirAtras, BorderLayout.WEST);
		btnSalirAtras.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (datanGraphicsLogika.lehenengoPosizioa()){
					dispose();
				}else{
					aurrekoa();
				}
			}
		});
		
		btnSiguiente = new JButton("Siguiente");
		panel.add(btnSiguiente, BorderLayout.CENTER);
		
		btnSiguiente.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (datanGraphicsLogika.azkenengoPosizioa()){
					datanGraphicsLogika.kodeaExekutatu();
				}else{
					hurrengoa();
				}
			}
		});
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));

		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		panel_2.setBorder(BorderFactory.createEmptyBorder(0,8,0,8));
		
		JLabel lblNewLabel = new JLabel("C\u00F3digo:");
		panel_2.add(lblNewLabel, BorderLayout.NORTH);
		
		textAreaCodigo = new JTextArea();
		textAreaCodigo.setLineWrap(true);
		JScrollPane scroll1 = new JScrollPane(textAreaCodigo);
		panel_2.add(scroll1, BorderLayout.CENTER);
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		panel_3.setBorder(BorderFactory.createEmptyBorder(0,8,0,8));

		
		JLabel lblNewLabel_1 = new JLabel("Explicaci\u00F3n:");
		panel_3.add(lblNewLabel_1, BorderLayout.NORTH);
		
		textAreaExplicacion = new JTextArea();
		textAreaExplicacion.setLineWrap(true);
		JScrollPane scroll2 = new JScrollPane(textAreaExplicacion);
		panel_3.add(scroll2, BorderLayout.CENTER);
		

		try {
			datanGraphicsLogika = new DatanGraphicsLogika();
			datuakKargatu();
		} catch (SeparatuKopuruEzberdinException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	/**
	 * Pausu honetako datuak textareatan kargatzen du
	 */
	private void datuakKargatu(){
		textAreaCodigo.setText(datanGraphicsLogika.getKodeaString());
		textAreaExplicacion.setText(datanGraphicsLogika.getAzalpenaString());
	}
	
	/**
	 * Hurrengo pausura pasatzen da
	 */
	private void hurrengoa(){
		if (datanGraphicsLogika.hurrengoa()){
			btnSiguiente.setText("Ejecutar");
		}else{
			btnSiguiente.setText("Siguiente");
		}
		datuakKargatu();
		btnSalirAtras.setText("Anterior");
	}
	
	/**
	 * Aurreko pausura pasatzen da
	 */
	private void aurrekoa(){
		if (datanGraphicsLogika.aurrekoa()){
			btnSalirAtras.setText("Salir");
		}else{
			btnSalirAtras.setText("Anterior");
		}
		datuakKargatu();
		btnSiguiente.setText("Siguiente");
	}
	

}
