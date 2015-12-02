package org.iti.proiektua1_login;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;

public class LoginInterfazea extends JFrame implements ActionListener{
	
	//Interfazeak izango dituen atributuak
	private JTextField erabiltzaileaText;
	private JTextField pasahitzaText;
	private JLabel erabiltzaileaLabel;
	private JLabel pasahitzaLabel;
	private JButton loginButton;
	private JButton erregistratuButton;
	
	//Login-eko logika kudeatzeko
	private LoginLogika loginLogika;
	
	//Mezuak adierazteko erabiliko den interfazeko elementua
	private JDialog dialog;
	
	
	public static void main(String[] args) {
		LoginInterfazea l = new LoginInterfazea();
	}
	
	
	public LoginInterfazea(){
		
		super();
		
		//Interfazearen elementuak hasieratzen dira
		sortu();
		this.setVisible(true);
		this.setSize(600, 300);
		
		//Lehioa pantailan zentratzeko
		this.setLocationRelativeTo(null);
		
		//Login-aren logikaren kudeatzailearen instantzia lortzeko
		loginLogika = LoginLogika.getLoginLogika();
	}

	/*
	 Interfazearen elementuak hasieratzen dituen metodoa 

	 * */
	 
	private void sortu() {
		
		getContentPane().setLayout(null);
		
				
		erabiltzaileaLabel = new JLabel("Erabiltzailea:");
		erabiltzaileaLabel.setBounds(29, 44, 111, 16);
		getContentPane().add(erabiltzaileaLabel);
		
		pasahitzaLabel = new JLabel("Pasahitza:");
		pasahitzaLabel.setBounds(29, 103, 79, 16);
		getContentPane().add(pasahitzaLabel);
		
		erabiltzaileaText = new JTextField();
		erabiltzaileaText.setBounds(166, 41, 270, 22);
		getContentPane().add(erabiltzaileaText);
		erabiltzaileaText.setColumns(10);
		
		pasahitzaText = new JTextField();
		pasahitzaText.setBounds(166, 100, 270, 22);
		getContentPane().add(pasahitzaText);
		pasahitzaText.setColumns(10);
		
		loginButton = new JButton("Login");
		loginButton.setBounds(320, 145, 97, 25);
		getContentPane().add(loginButton);
		
		erregistratuButton = new JButton("Erregistratu");
		erregistratuButton.setBounds(129, 145, 117, 25);
		getContentPane().add(erregistratuButton);
		
		
		//Interfazeko botoien ekintzak adierazten dituen kode-atala
		loginButton.addActionListener(this);
		erregistratuButton.addActionListener(this);
		
		
	}
	
	//Getter eta Setter-ak beste klaseetatik atzigarri egoteko

	public JTextField getErabiltzaileaText() {
		return erabiltzaileaText;
	}

	public void setErabiltzaileaText(JTextField erabiltzaileaText) {
		this.erabiltzaileaText = erabiltzaileaText;
	}

	public JTextField getPasahitzaText() {
		return pasahitzaText;
	}

	public void setPasahitzaText(JTextField pasahitzaText) {
		this.pasahitzaText = pasahitzaText;
	}

	public JLabel getErabiltzaileaLabel() {
		return erabiltzaileaLabel;
	}

	public void setErabiltzaileaLabel(JLabel erabiltzaileaLabel) {
		this.erabiltzaileaLabel = erabiltzaileaLabel;
	}

	public JLabel getPasahitzaLabel() {
		return pasahitzaLabel;
	}

	public void setPasahitzaLabel(JLabel pasahitzaLabel) {
		this.pasahitzaLabel = pasahitzaLabel;
	}

	public JButton getLoginButton() {
		return loginButton;
	}

	public void setLoginButton(JButton loginButton) {
		this.loginButton = loginButton;
	}

	public JButton getErregistratuButton() {
		return erregistratuButton;
	}

	public void setErregistratuButton(JButton erregistratuButton) {
		this.erregistratuButton = erregistratuButton;
	}

	
	
	//Listener-ak kudeatzeko metodoa
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//Konbinazio bakoitzean agertuko den mezua adierazteko aldagaia
		String mezua = "Erabiltzaile-izen edo pasahitza ez dira zuzenak.";
		
		if(e.getActionCommand().equals("Login")){
			
			//Flag bat ondo logeatu den jakiteko
			boolean loginZuzena = false;
			
			//Erabiltzaile-izena hutsa ez bada
			if(!erabiltzaileaText.getText().equals("")){
				
				String username = erabiltzaileaText.getText();
				String pasahitza = pasahitzaText.getText();
								
				//Pasahitza zuzena baldin bada
				if(loginLogika.loginZuzena(username, pasahitza)){
					
					System.out.println("Ondo logeatu da!");
					loginZuzena = true;
					//TODO: Sesioa ireki eta erabiltzaileizena bidali parametro bezala fitxategietan gordetzeko jakiteko
					
				}
							
				
			}
			
			//Sesio-irekiera ez baldin bada zuzena izan
			if(!loginZuzena){
				
				//Mezu bat adieraziko da horretaz informatzen
				dialog = new JDialog(this, "Informazioa");
				JLabel etiqueta = new JLabel(mezua);
				dialog.getContentPane().add(etiqueta);
				dialog.setSize(600, 150);
				
				//Dialogoa zentratzeko pantailan
				dialog.setLocationRelativeTo(null);
				
				dialog.setVisible(true);
				
			}
			
			
		}
		
		else if(e.getActionCommand().equals("Erregistratu")){
			
			//Erregistroa ondo joan den edo ez adierazten duen flag-a
			boolean ondoErregistratuDa = false;
			
			//Erregistroa gaizki joan bada agertuko den informazio mezua
			mezua = "Erabiltzaile izen bat eta pasahitz bat definitu behar dira derrigorrez!";
			
			//Erabiltzaile-izena hutsa ez bada, eta pasahitza ere hutsa ez bada
			if(!erabiltzaileaText.getText().equals("") && !pasahitzaText.getText().equals("")){
				
				String username = erabiltzaileaText.getText();
				String pasahitza = pasahitzaText.getText();
				
				//Erabiltzailea erregistratzen saiatzen da
				ondoErregistratuDa = loginLogika.erabitzaileaErregistratu(username, pasahitza);
				
				//Errore bat gertatu baldin bada, erabiltzailea ez delako existitzen izan da
				mezua = "Erabiltzaile hori existitzen da jada. Aukeratu beste bat.";						
			}
			
			//Ondo erregistratu baldin bada
			if(ondoErregistratuDa){
				
				//Mezu bat adierazten da ondo joan dela dana esanez
				mezua = "Ondo erregistratu da erabiltzailea!";
			}	
			
			//Dialog bat agertarazten da mezuarekin
			//http://entrebastidores.jnjsite.com/2012/12/java-swing-3-jugando-con-un-jframe-un.html
			dialog = new JDialog(this, "Informazioa");
			JLabel etiqueta = new JLabel(mezua);
			dialog.getContentPane().add(etiqueta);
			dialog.setSize(600, 150);
			
			//Dialogoa zentratzeko pantailan
			dialog.setLocationRelativeTo(null);
			
			dialog.setVisible(true);
			
		}
		
	}
	
	
	
}
