package org.iti.proiektua1_menuNagusia;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.iti.proiektua1_datangraphics.DatanGraphicsInterfazea;
import org.iti.proiektua1_fitxategiGordetzailea.FitxategiGordetzailea;
import org.iti.proiektua1_login.LoginInterfazea;
import org.iti.proiektua1_login.LoginLogika;

public class MenuNagusiaInterfazea extends JFrame implements ActionListener {

	// Interfazeak izango dituen atributuak
	private JTextField pathText;
	private JTextField fitxategiText;
	private JLabel pathLabel;
	private JLabel fitxategiLabel;
	private JButton path;

	// Fitxategien gordeketaren logika kudeatzeko
	private FitxategiGordetzailea fitxategiGordetzailea;

	// Mezuak adierazteko erabiliko den interfazeko elementua
	private JDialog dialog;
	
	//erabiltzailearen erabiltzaile-izena gordeko duen aldagaia
	private static String username;
	
	
	

	// Erabiltzen ari garen sistemaren itxura hartzen du programaren interfazeak
	private static void sistemarenLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
	}

	public MenuNagusiaInterfazea(String pUsername) {
		
		//super();
		
		//username-a kargatu
		username = pUsername;
		
		// Interfazearen elementuak hasieratzen dira
		sortu();
		this.setVisible(true);
		this.setSize(600, 350);

		// Lehioa pantailan zentratzeko
		this.setLocationRelativeTo(null);

		// Fitxategiak gordetzeko logikaren kudeatzailearen instantzia lortzeko
		fitxategiGordetzailea = FitxategiGordetzailea.getFitxategiGordetzailea();
	}

	/*
	 * Interfazearen elementuak hasieratzen dituen metodoa
	 * 
	 */

	private void sortu() {

		getContentPane().setLayout(null);

		pathLabel = new JLabel("Path:");
		pathLabel.setBounds(33, 78, 111, 16);
		getContentPane().add(pathLabel);

		fitxategiLabel = new JLabel("Fitxategi-izena:");
		fitxategiLabel.setBounds(33, 138, 111, 16);
		getContentPane().add(fitxategiLabel);

		pathText = new JTextField();
		pathText.setBounds(166, 75, 270, 22);
		getContentPane().add(pathText);
		pathText.setColumns(10);

		fitxategiText = new JTextField();
		fitxategiText.setBounds(166, 135, 270, 22);
		getContentPane().add(fitxategiText);
		fitxategiText.setColumns(10);

		path = new JButton("Aukeratu");
		path.setBounds(453, 74, 117, 25);
		getContentPane().add(path);

		JLabel lblAukeratuLanaGordetzeko = new JLabel("Aukeratu lana gordetzeko Path eta fitxategi-izen bat.");
		lblAukeratuLanaGordetzeko.setBounds(28, 27, 500, 16);
		getContentPane().add(lblAukeratuLanaGordetzeko);
		
		JButton datanButton = new JButton("Datan adibidea");
		datanButton.addActionListener(this);
		datanButton.setBounds(63, 230, 174, 47);
		getContentPane().add(datanButton);
		
		JButton datangraphicsButton = new JButton("Datangraphics adibidea");
		datangraphicsButton.addActionListener(this);
		datangraphicsButton.setBounds(337, 230, 174, 47);
		getContentPane().add(datangraphicsButton);
		
		JLabel lblAukeratuTutorialBat = new JLabel("Aukeratu tutorial bat:");
		lblAukeratuTutorialBat.setBounds(236, 187, 200, 16);
		getContentPane().add(lblAukeratuTutorialBat);
		path.addActionListener(this);

	}

	// Getter eta Setter-ak beste klaseetatik atzigarri egoteko

	public JTextField getErabiltzaileaText() {
		return pathText;
	}

	public void setErabiltzaileaText(JTextField erabiltzaileaText) {
		this.pathText = erabiltzaileaText;
	}

	public JTextField getPasahitzaText() {
		return fitxategiText;
	}

	public void setPasahitzaText(JTextField pasahitzaText) {
		this.fitxategiText = pasahitzaText;
	}

	public JLabel getErabiltzaileaLabel() {
		return pathLabel;
	}

	public void setErabiltzaileaLabel(JLabel erabiltzaileaLabel) {
		this.pathLabel = erabiltzaileaLabel;
	}

	public JLabel getPasahitzaLabel() {
		return fitxategiLabel;
	}

	public void setPasahitzaLabel(JLabel pasahitzaLabel) {
		this.fitxategiLabel = pasahitzaLabel;
	}

	public JButton getErregistratuButton() {
		return path;
	}

	public void setErregistratuButton(JButton erregistratuButton) {
		this.path = erregistratuButton;
	}

	// Path-a eskuratzeko erabiltzen den metodo grafikoa
			private String pathEskuratu() {
				JFileChooser fc = new JFileChooser();
				
				//karpetak bakarrik aukeratzeko modua esleituko da
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				int returnVal = fc.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					return fc.getSelectedFile().getAbsolutePath();
				} else {
					return null;
				}
			}

	// Listener-ak kudeatzeko metodoa
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//errore mezua adierazten duen aldagaia; HTML kodea erabili da lerro-jauziak simulatzeko
		String errorea = "<html>Ondorengo arazoetako bat gertatu da:<br>   -Fitxategia gordeko den direktorioa ez da espezifikatu.<br>   -Fitxategi izena ez da espezifikatu edo existitzen da jada.</html>";

		//errore bat gertatu den edo ez adierazteko flag bat
		boolean errorerikBai = false;
		
		if (e.getActionCommand().equals("Aukeratu")) {
			
			//erabiltzaileak aukeratutako path-a eskuratzen da
			String fitxategiarenPatha = pathEskuratu();
			pathText.setText(fitxategiarenPatha);
		}
		
		else if(e.getActionCommand().equals("Datan adibidea")){
					
			//fitxategiarentzako izen bat eta path bat egon behar dira esleituta
			if(!pathText.getText().isEmpty() && !fitxategiText.getText().isEmpty()){
				
				//adierazitako path eta fitxategia zuzenak baldin badira
				if(fitxategiGordetzailea.fitxategiaGordetzekoDirektorioaEsleitu(pathText.getText(), fitxategiText.getText(), username)){
					
					//TODO:datan aukera irekitzen da
					System.out.println("datan ongi!");
				}
				else{
					errorerikBai = true;
				}
				
			}
			else{
				errorerikBai = true;
			}
			
			
		}
		
		else if (e.getActionCommand().equals("Datangraphics adibidea")){
			//fitxategiarentzako izen bat eta path bat egon behar dira esleituta
			if(!pathText.getText().isEmpty() && !fitxategiText.getText().isEmpty()){
				
				//adierazitako path eta fitxategia zuzenak baldin badira
				if(fitxategiGordetzailea.fitxategiaGordetzekoDirektorioaEsleitu(pathText.getText(), fitxategiText.getText(), username)){
					
					//datangraphics aukera irekitzen da
					
					DatanGraphicsInterfazea dgi = new DatanGraphicsInterfazea(username);
					dgi.setVisible(true);
				}
				else{
					errorerikBai = true;
				}
				
			}
			else{
				errorerikBai = true;
			}
		}
		
		if(errorerikBai){
			
			//Mezu bat adieraziko da horretaz informatzen
			dialog = new JDialog(this, "Informazioa");
			JLabel etiqueta = new JLabel(errorea);
			dialog.getContentPane().add(etiqueta);
			dialog.setSize(600, 250);
			
			//Dialogoa zentratzeko pantailan
			dialog.setLocationRelativeTo(null);
			
			dialog.setVisible(true);
		}
		
	}	
}

