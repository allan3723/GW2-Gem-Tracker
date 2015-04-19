package gemTracker;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.io.IOException;
import java.text.NumberFormat;

import gemTracker.GridBagGUI;

import org.json.*;

public class customGUI extends JFrame {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1051272020740104442L;
	
	private JFormattedTextField gemTF, gTF, sTF, cTF;;
	private JLabel gemL, gL, sL, cL;
	private JButton calcGMB, calcGDB;
	private CalculateGemButtonHandler cgmbHandler;
	private CalculateGoldButtonHandler cgdbHandler;
	
	private gemTracker.CTG ctg = new gemTracker.CTG();

	/* 
	 * Create the Panel
	 */
	public void addComponent(Container pane) {		
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		/*
		 * Limit input: format8 allows 8 integer characters while format2 allows 2
		 */
		NumberFormat format8 = NumberFormat.getNumberInstance();
		format8.setGroupingUsed(false);
		format8.setMaximumIntegerDigits(8);
		
		NumberFormat format2 = NumberFormat.getNumberInstance();
		format2.setGroupingUsed(false);
		format2.setMaximumIntegerDigits(2);
		
		/*
		 * Add component to panel
		 */
		gemTF = new JFormattedTextField(format8);
		gemTF.setHorizontalAlignment(JTextField.RIGHT);

		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 4;
		c.insets = new Insets(10,10,5,0); 
		pane.add(gemTF, c);
		
		gemL = new JLabel("Gems");
		c.weightx = 0.0;
		c.gridx = 6;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(10,1,5,0);
		pane.add(gemL, c);
		
		calcGMB = new JButton("Show");
		cgmbHandler = new CalculateGemButtonHandler();
		calcGMB.addActionListener(cgmbHandler);
		c.weightx = 0.5;
		c.gridx = 7;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(10,10,5,10); 
		pane.add(calcGMB, c);
		
		gTF = new JFormattedTextField(format8);
		gTF.setHorizontalAlignment(JTextField.RIGHT);
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5,10,10,0);
		pane.add(gTF, c);
		
		gL = new JLabel("g");
		c.weightx = 0.0;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5,1,10,10);
		pane.add(gL, c);
		
		sTF = new JFormattedTextField(format2);
		sTF.setHorizontalAlignment(JTextField.RIGHT);
		c.weightx = 0.5;
		c.gridx = 3;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5,10,10,0);
		pane.add(sTF, c);
		
		sL = new JLabel("s");
		c.weightx = 0.0;
		c.gridx = 4;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5,1,10,10);
		pane.add(sL, c);
		
		cTF = new JFormattedTextField(format2);
		cTF.setHorizontalAlignment(JTextField.RIGHT);
		c.weightx = 0.5;
		c.gridx = 5;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5,10,10,0);
		pane.add(cTF, c);
		
		cL = new JLabel("c");
		c.weightx = 0.0;
		c.gridx = 6;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5,1,10,0);
		pane.add(cL, c);
		
		calcGDB = new JButton("Show");
		cgdbHandler = new CalculateGoldButtonHandler();
		calcGDB.addActionListener(cgdbHandler);
		c.weightx = 0.5;
		c.gridx = 7;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(10,10,10,10); 
		pane.add(calcGDB, c);
	}
	
	private class CalculateGemButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String gem;
			
			/*
			 * Set flags to tell program to show custom prices
			 */
			GridBagGUI.showCustom = true;
			GridBagGUI.showGem = false;
			GridBagGUI.CustomGem = true;
			
			/*
			 * Get input
			 */
			gem = gemTF.getText();
			if (gem.isEmpty()) {
				gemTF.setText("0");
				GridBagGUI.CGem = 0;
			}
			else
				GridBagGUI.CGem = Integer.parseInt(gemTF.getText());
			
			/*
			 * Calculate the amount of gem and output it with new thread
			 */
			new showGem().start();
		}
	}
	
	/*
	 * Same as the function before this but for gold -> gem
	 */
	private class CalculateGoldButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String gold, silver, copper;
			
			GridBagGUI.showCustom = true;
			GridBagGUI.showGem = false;
			GridBagGUI.CustomGem = false;
			
			gold = new String(gTF.getText());
			silver = new String(sTF.getText());
			copper = new String(cTF.getText());
			
			if (gold.isEmpty()) {
				gTF.setText("0");
				GridBagGUI.CG = 0;
			}
			else
				GridBagGUI.CG = Integer.parseInt(gold);
			
			if (silver.isEmpty()) {
				sTF.setText("0");
				GridBagGUI.CS = 0;
			}
			else
				GridBagGUI.CS = Integer.parseInt(silver);
			
			if (copper.isEmpty()) {
				cTF.setText("0");
				GridBagGUI.CC = 0;
			}
			else
				GridBagGUI.CC = Integer.parseInt(copper);
			
			/*
			 * Calculate the amount of gold and output it with new thread
			 */
			new showGold().start();
		}
	}
	
	/*
	 * New thread to show custom amount for gem
	 */
	class showGem extends Thread {
		public void run() {
			String output = new String();
			
			try {
				output = ctg.customGemAmount(GridBagGUI.CGem);
			} catch (IOException e1) {
				GridBagGUI.pricesTA.setText("Error: Input value is too high or low.\n");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			
			GridBagGUI.pricesTA.setText(output);
		}
	}
	
	class showGold extends Thread {
		public void run() {
			String output = new String();
			
			try {
				output = ctg.customAmount(GridBagGUI.CG,GridBagGUI.CS,GridBagGUI.CC);
			} catch (IOException e1) {
				GridBagGUI.pricesTA.setText("Error: Input value too high or low.\n");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			
			GridBagGUI.pricesTA.setText(output);
		}
	}
}
