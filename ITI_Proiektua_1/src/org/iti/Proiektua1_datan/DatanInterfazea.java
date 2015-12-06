package org.iti.Proiektua1_datan;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.net.ssl.SSLEngineResult.Status;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.JTextArea;

import org.iti.proiektua1_datangraphics.DatanGraphicsLogika;
import org.iti.proiektua1_datangraphics.SeparatuKopuruEzberdinException;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DatanInterfazea extends JFrame {
	private DatanLogika datanLogika;
	private JLabel lblExplic;
	private JButton btnSalir;
	private JButton btnSiguiente;
	private JTextArea textCodigo;
	private JTextArea textExplic;
	private int pos;
	private int max;
	
	
	public DatanInterfazea() {
		
		setSize(600,600);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		try{			
			datanLogika = new DatanLogika();
			
		}
		catch(SeparatuKopuruEzberdinException e){
				e.printStackTrace();
				System.out.println("Existe disparidad entre el conto de codigo y de explicaciones");
				System.exit(ERROR);
		}
		

		
		JPanel panelBtn = new JPanel();
		getContentPane().add(panelBtn, BorderLayout.SOUTH);
		panelBtn.setLayout(new BorderLayout(0, 0));
		
		btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//en caso de que pos este en la posicion inicial, vease la pantalla 1 se cerrará esta ventana
				if(pos == 0){
					dispose();
				}else{//en caso contrario se volvera una ventana atras, reduciendo el valor de pos y llamando a aurreko para actualizar los valores
					pos--;
					aurreko();
				}
			}
		});
		panelBtn.add(btnSalir, BorderLayout.WEST);
		
		btnSiguiente = new JButton("Siguiente");
		btnSiguiente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(pos == max){
					exekutatu();
				}else{
					pos ++;
					hurrengo();
				}
			}
		});
		panelBtn.add(btnSiguiente, BorderLayout.CENTER);
		
		JPanel panelCentro = new JPanel();
		getContentPane().add(panelCentro, BorderLayout.CENTER);
		panelCentro.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panelCodigo = new JPanel();
		panelCentro.add(panelCodigo);
		panelCodigo.setLayout(new BorderLayout(0, 0));
		
		JLabel lblCodigo = new JLabel("Codigo");
		panelCodigo.add(lblCodigo, BorderLayout.NORTH);
		
		JScrollPane scrollCodigo = new JScrollPane();
		panelCodigo.add(scrollCodigo, BorderLayout.CENTER);
		
		textCodigo = new JTextArea();
		scrollCodigo.setViewportView(textCodigo);
		
		JPanel panelExplic = new JPanel();
		panelCentro.add(panelExplic);
		panelExplic.setLayout(new BorderLayout(0, 0));
		
		lblExplic = new JLabel("Explicacion");
		panelExplic.add(lblExplic, BorderLayout.NORTH);
		
		JScrollPane scrollExplic = new JScrollPane();
		panelExplic.add(scrollExplic, BorderLayout.CENTER);
		
		textExplic = new JTextArea();
		scrollExplic.setViewportView(textExplic);
	}
	
	private void aurreko(){
		
	}
	
	private void hurrengo(){
		
	}
	
	private void exekutatu(){
		
	}

}
