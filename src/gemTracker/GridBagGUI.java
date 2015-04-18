package gemTracker;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import gemTracker.CTG;

import org.json.*;

public class GridBagGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8747879380999760687L;

	public static Boolean showGem = false, showCustom = false, CustomGem = false,
						  notifyOn = false;
	
	private static JLabel minL, gL, sL, cL, gemL;
	private static JFormattedTextField timeTF, gTF, sTF, cTF, gemTF;
	private static JButton goldB, gemB, customB, autoB, aoffB, notificationB, noffB;
	public static JTextArea pricesTA;
	
	private static GoldButtonHandler gdbHandler;
	private static GemButtonHandler gmbHandler;
	private static CustomButtonHandler cbHandler;
	private static UpdateButtonHandler ubHandler;
	private static NotifyButtonHandler nbHandler;
	private static NOffButtonHandler nobHandler;
	private static AOffButtonHandler aobHandler;
	
    private static ScheduledExecutorService executor; 
	
	private static final int CUSTOMWIDTH = 350;
	private static final int CUSTOMHEIGHT = 110;
	
	private static Future<?> future = null;
	public static Integer CGem, CG, CS, CC, notifyAmount, notifyGem;
	public static long time = 0;
	
	/*
	 * Main panel that holds the other 4 panels
	 */
	public static void addComponent(Container mPanel) {		
		mPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 7;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		mPanel.add(buttonPanel(), c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 7;
		c.anchor = GridBagConstraints.NORTH;
		c.insets = new Insets(5,5,5,5);
		mPanel.add(displayPanel(), c);
		
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.SOUTHWEST;
		mPanel.add(autoUpdatePanel(), c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 6;
		c.anchor = GridBagConstraints.SOUTHEAST;
		mPanel.add(notifyPanel(), c);
		}
	
	/*
	 * The top panel which contains the 3 button: Gold, Gem, Custom
	 */
	private static JPanel buttonPanel() {
		/*
		 * Build Panel
		 */
		JPanel bPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		bPanel.setLayout(new GridBagLayout());
		
		/*
		 * Add containers onto panel
		 */
		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		goldB = new JButton("Gold");
		gdbHandler = new GoldButtonHandler();
		goldB.addActionListener(gdbHandler);
		c.weightx = 1;
		c.weighty = 0.3;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		bPanel.add(goldB, c);
	
		gemB = new JButton("Gem");
		gmbHandler = new GemButtonHandler();
		gemB.addActionListener(gmbHandler);
		c.weightx = 1;
		c.weighty = 0.3;
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		bPanel.add(gemB, c);
		
		customB = new JButton("Custom");
		cbHandler = new CustomButtonHandler();
		customB.addActionListener(cbHandler);
		c.weightx = 1;
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 1;
		bPanel.add(customB, c); 
		
		return bPanel;
	}
	
	/*
	 * Build the display panel (big black box). All results are shown here.
	 */
	private static JPanel displayPanel() {
		/*
		 * Build Panel
		 */
		JPanel dPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		dPanel.setLayout(new GridBagLayout());
		
		/*
		 * Add components
		 */
		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		pricesTA = new JTextArea(9,30);
		pricesTA.setBackground(Color.BLACK);
		pricesTA.setForeground(Color.WHITE);
		pricesTA.setFont(new Font("SANS_SERIF", Font.BOLD, 20));
		pricesTA.setTabSize(4);
		
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 0;
		dPanel.add(pricesTA, c);

		dPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		return dPanel;
	}
	
	/*
	 * Panel for Auto Update
	 */
	private static JPanel autoUpdatePanel() {
		/*
		 * Build Panel
		 */
		JPanel auPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		auPanel.setLayout(new GridBagLayout());
		
		/*
		 * Add Components
		 */
		
		c.fill = GridBagConstraints.HORIZONTAL;

		autoB = new JButton("Set");
		ubHandler = new UpdateButtonHandler();
		autoB.addActionListener(ubHandler);
		c.anchor = GridBagConstraints.PAGE_END;
		c.weightx = 0.3;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.insets = new Insets(0,10,5,5);
		auPanel.add(autoB, c);
		
		aoffB = new JButton("Off");
		aobHandler = new AOffButtonHandler();
		aoffB.addActionListener(aobHandler);
		c.anchor = GridBagConstraints.PAGE_END;
		c.weightx = 0.3;
		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 2;
		c.insets = new Insets(0,0,5,10);
		auPanel.add(aoffB, c);
		
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setGroupingUsed(false);
		format.setMaximumIntegerDigits(3);
		timeTF = new JFormattedTextField(format);
		timeTF.setHorizontalAlignment(JTextField.RIGHT);
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(10,10,5,5);
		auPanel.add(timeTF, c);
		
		minL = new JLabel("minute(s)");
		c.weightx = 0.2;
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0,0,5,10);
		auPanel.add(minL, c);
		
		auPanel.setBorder(BorderFactory.createTitledBorder("Auto Updater"));
		
		return auPanel;
	}
	
	/*
	 * Panel to set notification for when a specific amount of gold
	 * drops to a specific amount of gems
	 */
	private static JPanel notifyPanel() {
		JPanel nPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		nPanel.setLayout(new GridBagLayout());
		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		/*
		 * Limit input: format2 = 2 integers, format8 = 8 integers
		 */
		NumberFormat format2 = NumberFormat.getNumberInstance();
		format2.setGroupingUsed(false);
		format2.setMaximumIntegerDigits(2);
		
		NumberFormat format6 = NumberFormat.getNumberInstance();
		format6.setGroupingUsed(false);
		format6.setMaximumIntegerDigits(6);
		
		/*
		 * Add components to Panel
		 */
		
		gTF = new JFormattedTextField(format6);
		gTF.setHorizontalAlignment(JTextField.RIGHT);
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0,10,5,0);
		nPanel.add(gTF, c);
		
		gL = new JLabel("g");
		c.weightx = 0.3;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0,2,5,0);
		nPanel.add(gL, c);
		
		sTF = new JFormattedTextField(format2);
		sTF.setHorizontalAlignment(JTextField.RIGHT);
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0,5,5,0);
		nPanel.add(sTF, c);
		
		sL = new JLabel("s");
		c.weightx = 0.3;
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0,2,5,0);
		nPanel.add(sL, c);
		
		cTF = new JFormattedTextField(format2);
		cTF.setHorizontalAlignment(JTextField.RIGHT);
		c.weightx = 0.5;
		c.gridx = 4;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0,5,5,0);
		nPanel.add(cTF, c);
		
		cL = new JLabel("c");
		c.weightx = 0.3;
		c.gridx = 5;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0,2,5,5);
		nPanel.add(cL, c);
		
		gemTF = new JFormattedTextField(format6);
		gemTF.setHorizontalAlignment(JTextField.RIGHT);
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 3;
		c.insets = new Insets(5,5,5,0);
		nPanel.add(gemTF, c);
		
		gemL = new JLabel("Gems");
		c.weightx = 0.3;
		c.gridx = 5;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0,2,5,5);
		nPanel.add(gemL, c);
		
		notificationB = new JButton("Set");
		nbHandler = new NotifyButtonHandler();
		notificationB.addActionListener(nbHandler);
		c.weightx = 0.1;
		c.gridx = 6;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(5,0,5,10);
		nPanel.add(notificationB, c);
		
		noffB = new JButton("Off");
		nobHandler = new NOffButtonHandler();
		noffB.addActionListener(nobHandler);
		c.weightx = 0.1;
		c.gridx = 6;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0,0,5,10);
		nPanel.add(noffB, c); 
		
		nPanel.setBorder(BorderFactory.createTitledBorder("Set Notification"));
		
		return nPanel;
	}

	/*
	 * Handler functions for when the button gets pressed
	 */
	/*
	 * "Gold" Button from the Buttons Panel to show the gold view
	 */
	public static class GoldButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			showGem = false;
			showCustom = false;
			new show().start();
		}
	}
	
	/*
	 * "Gem" Button from the Buttons Panel to show the gem view
	 */
	public static class GemButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			showGem = true;
			showCustom = false;
			new show().start();
		}
	}
	
	/*
	 * Notify button from the Notification Panel 
	 */
	public static class NotifyButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String g, s, c, gem;
			
			notifyOn = true;
			
			g = gTF.getText();
			s = sTF.getText();
			c = sTF.getText();
			gem = gemTF.getText();
			
			if (g.isEmpty()) {
				gTF.setText("0");
				g = new String("0");
			}

			if (s.isEmpty()) {
				sTF.setText("0");
				s = new String("0");
			}
			
			if (c.isEmpty()) {
				cTF.setText("0");
				c = new String("0");
			}
			
			if (gem.isEmpty()) {
				gemTF.setText("0");
				gem = new String("0");
			}
			
			gTF.setForeground(Color.RED);
			sTF.setForeground(Color.RED);
			cTF.setForeground(Color.RED);
			gemTF.setForeground(Color.BLUE);
			
			notifyAmount = (Integer.parseInt(g)*10000)+(Integer.parseInt(s)*100)+(Integer.parseInt(c));
			notifyGem = Integer.parseInt(gem);
		}
	}
	
	/* 
	 * Off button in the Notification Panel
	 */
	public static class NOffButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
				notifyOn = false;
				notifyAmount = 0;
				notifyGem = 0;
				gTF.setText("");
				sTF.setText("");
				cTF.setText("");
				gemTF.setText("");
				gTF.setForeground(Color.BLACK);
				sTF.setForeground(Color.BLACK);
				cTF.setForeground(Color.BLACK);
				gemTF.setForeground(Color.BLACK);
		}
	}
	
	/*
	 * Custom Button from the Buttons Panel which makes another GUI
	 * for the user to enter custom amount of gold or gems
	 */
	public static class CustomButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
	        /*
	         * Create and set up the window.
	         */
	        JFrame customframe = new JFrame("Enter Custom Amounts");
	        customframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        customframe.setResizable(false);

	        /*
	         * Set up the content pane.
	         */
	        customGUI.addComponent(customframe.getContentPane());

	        /*
	         * Display the window.
	         */
	        customframe.setSize(CUSTOMWIDTH, CUSTOMHEIGHT);
	        customframe.setVisible(true);
		}
	
	}
	
	/*
	 * Set Button from the Auto Update Panel.
	 * Schedules a task to automatically update the view (big black box)s
	 */
	public static class UpdateButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String stime;
			
			stime = timeTF.getText();
			
			if(stime.isEmpty()) {
				time = 1;
				timeTF.setText("1");
			}
			else
				Integer.parseInt(timeTF.getText());
	        
			if (time == 0) {
				timeTF.setText("1");
				time = 1;
			}
			
	        if (future != null && !future.isDone()) 
	            future.cancel(true); 
	        
	        timeTF.setForeground(Color.RED);

	        executor = Executors.newScheduledThreadPool(1);
			future = executor.scheduleWithFixedDelay(new task(), 0, time, TimeUnit.MINUTES);

		}
	}
	
	/*
	 * Off button from Automatic Update
	 */
	public static class AOffButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
				if (future != null) {
					future.cancel(true);
					executor.shutdown();
				}
				
				timeTF.setText("");
				timeTF.setForeground(Color.BLACK);
		}
	}
	
	/*
	 * This function displays the results onto the displayPanel (big black box).
	 */
	public static void showPrices() {
		try {
			if (showGem) {
				pricesTA.setText(CTG.reverseTable());
			}
			else {
				if (!showCustom)
					pricesTA.setText(CTG.showTable());
				else {
					if (CustomGem)
						pricesTA.setText(CTG.customGemAmount(CGem));
					else
						pricesTA.setText(CTG.customAmount(CG,CS,CC));
				}
			}
				
		} catch (IOException e1) {
			pricesTA.setText("Failed to retrieve prices.");
			e1.printStackTrace();
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
	
	/*
	 * This class checks if the user-entered amount of gold is able to purchase
	 * the user-entered amount of gems. If it can, the program will play a
	 * notification sound to alert the user.
	 */
	private static class task implements Runnable {
		public void run() {
			showPrices();
			
			int gem = 0;
			
			if (notifyOn) {
				try {
					JSONObject obj = jGW2API.getCoins(notifyAmount.toString());
					gem = obj.getInt("quantity");
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (gem >= notifyGem) {
					new beep().start();
				}
			}
		}
	}
	
	/*
	 * Plays the beep sound 10 times
	 */
	static class beep extends Thread {
		public void run() {
			for(int i = 0; i < 10; i++) {
				Toolkit.getDefaultToolkit().beep();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/*
	 * Set up new Thread to show prices for gold + gem button
	 */
	static class show extends Thread {
		public void run() {
			showPrices();
		}
	}
	
	/*
	 * Sets up GUI
	 */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("GW2 Gold to Gem Rate Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //Set up the content pane.
        addComponent(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

	public static void main(String[] args) {
		createAndShowGUI();
	}
}
