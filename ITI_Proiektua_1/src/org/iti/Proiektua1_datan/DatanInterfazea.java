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

import org.iti.Proiektua1_Utility.SeparatuKopuruEzberdinException;
import org.iti.proiektua1_datangraphics.DatanGraphicsLogika;
import org.iti.proiektua1_datangraphics.examples.E8Mtx;
import org.iti.proiektua1_fitxategiGordetzailea.FitxategiGordetzailea;

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
	private String username;
	private int pos;
	private boolean heldu;
	
	
	public DatanInterfazea(String pUsername) {
		
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
		username = pUsername;
		pos = 0;
		heldu=false;
	
		JPanel panelBtn = new JPanel();
		getContentPane().add(panelBtn, BorderLayout.SOUTH);
		panelBtn.setLayout(new BorderLayout(0, 0));
		
		btnSalir = new JButton("Irten");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				datanLogika.getPos();
				//en caso de que pos este en la posicion inicial, vease la pantalla 1 se cerrará esta ventana
				if(pos <= 0){
					FitxategiGordetzailea.getFitxategiGordetzailea().fitxategianGorde("El usuario ha salido del tutorial Datan\n", username);
					dispose();
				}else{//en caso contrario se volvera una ventana atras, reduciendo el valor de pos y llamando a aurreko para actualizar los valores
					datanLogika.actPos(false);
					aurreko();
				}
			}
		});
		panelBtn.add(btnSalir, BorderLayout.WEST);
		
		btnSiguiente = new JButton("Aurrera");
		btnSiguiente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pos = datanLogika.getPos();
				if(pos == -2){
					exekutatu();
				}else{
					datanLogika.actPos(true);
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
		
		JLabel lblCodigo = new JLabel("Kodea");
		panelCodigo.add(lblCodigo, BorderLayout.NORTH);
		
		JScrollPane scrollCodigo = new JScrollPane();
		panelCodigo.add(scrollCodigo, BorderLayout.CENTER);
		
		textCodigo = new JTextArea();
		scrollCodigo.setViewportView(textCodigo);
		
		JPanel panelExplic = new JPanel();
		panelCentro.add(panelExplic);
		panelExplic.setLayout(new BorderLayout(0, 0));
		
		lblExplic = new JLabel("Azalpena");
		panelExplic.add(lblExplic, BorderLayout.NORTH);
		
		JScrollPane scrollExplic = new JScrollPane();
		panelExplic.add(scrollExplic, BorderLayout.CENTER);
		
		textExplic = new JTextArea();
		scrollExplic.setViewportView(textExplic);
		
		String[] textuak = new String[2];
	    textuak = datanLogika.textuakHartu(pos);
	    System.out.println(textuak[0]);
	    textCodigo.setText(textuak[0]);
	    textExplic.setText(textuak[1]);
		
	}
	
	private void aurreko(){
		datanLogika.getPos();
		String[] textuak;
		textuak = datanLogika.textuakHartu(pos);
		textCodigo.setText(textuak[0]);
		textExplic.setText(textuak[1]);
		if(heldu){
			btnSiguiente.setText("Aurrera");
			heldu = false;
		}
		if(pos == 0){
			btnSalir.setText("Irten");
		}
		FitxategiGordetzailea.getFitxategiGordetzailea().fitxategianGorde("El usuario ha pulsado atras en el tutorial DatanGraphics\n", username);
	}
	
	private void hurrengo(){
		pos = datanLogika.getPos();
		String[] textuak;
		textuak = datanLogika.textuakHartu(pos);
		textCodigo.setText(textuak[0]);
		textExplic.setText(textuak[1]);
		if(pos == -2){
			btnSiguiente.setText("Exekutatu");
			heldu = true;
		}else{
			if(pos == 1){
				btnSalir.setText("Atzera");
			}
		}
		FitxategiGordetzailea.getFitxategiGordetzailea().fitxategianGorde("El usuario ha pulsado siquiente en el tutorial Datan\n", username);
	}
	
	private void exekutatu(){
		FitxategiGordetzailea.getFitxategiGordetzailea().fitxategianGorde("El usuario ha acabado el tutorial Datan", username);
		datanLogika.exekutatu();
	}

}
