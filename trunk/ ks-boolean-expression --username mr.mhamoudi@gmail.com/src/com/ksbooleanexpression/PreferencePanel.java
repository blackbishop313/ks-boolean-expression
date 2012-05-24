/*
# KS Boolean Expression, Copyright (c) 2012 The Authors. / ks.contrubutors@gmail.com
#
# This program is free software; you can redistribute it and/or modify it under
# the terms of the GNU General Public License as published by the Free Software
# Foundation; either version 3 of the License, or (at your option) any later
# version.
#
# This program is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
# FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
# details.
#
# You should have received a copy of the GNU General Public License along with
# this program; if not, write to the Free Software Foundation,  Inc., 
# 675 Mass Ave, Cambridge, MA 02139, USA.
*/


package com.ksbooleanexpression;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.prefs.Preferences;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;


/**
 * Ouvre une boite de dialogue pour modifier<br>
 * les préférences.
 *@author Meradi, Hamoudi
 *
 */
public class PreferencePanel  extends JDialog implements View{

	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private UserPreferences pref ;
	public Preferences preferences = Preferences.userNodeForPackage(Program.class);
	public  JLabel labelLangue;
	public  JComboBox languageComboBox;
	public  JLabel labelLookAndFeel;
	public  JComboBox lookAndFeelComboBox; 
	private Controller controller;
	private JCheckBox chckbxDetailedSol;
	private JCheckBox chckbxMiniFunc;

	/**
	 * Crée et affiche une boite de dialogue préférences.
	 */
	public PreferencePanel(UserPreferences preferences, final Controller controller) {
		super(controller.program.getFrame());
		this.controller = controller;
		pref = preferences;
		pref = preferences;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((int)((0.35)*d.width),(int)((0.55)*d.height));
		this.setLocationByPlatform(true);
		Image img = Program.programIcon;
		setIconImage(img);
		setTitle(Tools.getLocalizedString(ActionType.PREFERENCES.name()+".Name"));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{35, 81, 90, 128, 0};
		gbl_contentPanel.rowHeights = new int[]{32, 56, 86, 82, 52, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
			labelLangue = new JLabel(Tools.getLocalizedString(ActionType.PREFERENCES.name()+".Language")+" :");
			labelLangue.setFont(UIManager.getFont("OptionPane.buttonFont"));
			GridBagConstraints gbc_lblLangue = new GridBagConstraints();
			gbc_lblLangue.fill = GridBagConstraints.BOTH;
			gbc_lblLangue.insets = new Insets(0, 0, 5, 5);
			gbc_lblLangue.gridx = 1;
			gbc_lblLangue.gridy = 1;
			contentPanel.add(labelLangue, gbc_lblLangue);
		
		
			languageComboBox = new JComboBox();	
			String [] displayLanguages = pref.getSupportedLanguages();
			for (int i=0;i<displayLanguages.length;i++){
				Locale locale = new Locale(displayLanguages[i]);
				displayLanguages[i] = Character.toUpperCase(locale.getDisplayLanguage().charAt(0)) +
						locale.getDisplayLanguage().substring(1);
			}
		    languageComboBox.setModel(new DefaultComboBoxModel(displayLanguages));
		    languageComboBox.setSelectedItem(pref.getLanguageName());
			GridBagConstraints gbc_languageComboBox = new GridBagConstraints();
			gbc_languageComboBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_languageComboBox.insets = new Insets(0, 0, 5, 0);
			gbc_languageComboBox.gridx = 3;
			gbc_languageComboBox.gridy = 1;
			contentPanel.add(languageComboBox, gbc_languageComboBox);
		
		
			labelLookAndFeel = new JLabel(Tools.getLocalizedString(ActionType.PREFERENCES.name()+".Appearance")+" :");
			labelLookAndFeel.setFont(UIManager.getFont("OptionPane.buttonFont"));
			GridBagConstraints gbc_lblLookAndFeel = new GridBagConstraints();
			gbc_lblLookAndFeel.fill = GridBagConstraints.BOTH;
			gbc_lblLookAndFeel.insets = new Insets(0, 0, 5, 5);
			gbc_lblLookAndFeel.gridx = 1;
			gbc_lblLookAndFeel.gridy = 2;
			contentPanel.add(labelLookAndFeel, gbc_lblLookAndFeel);
		
		
			UIManager.LookAndFeelInfo laf [] = UIManager.getInstalledLookAndFeels();
			String [] lafs = new String[laf.length];
			int i =0;
			UIManager.LookAndFeelInfo currentLookAndFeel = null;
			for (LookAndFeelInfo lf : laf){
				if (lf.getClassName().compareTo(pref.lookAndFeel)==0) {
					currentLookAndFeel = lf;
				}
				lafs[i] = lf.getName();
				i++;				
			}
		lookAndFeelComboBox = new JComboBox();
		lookAndFeelComboBox.setModel(new DefaultComboBoxModel(lafs));
		GridBagConstraints gbc_lookAndFeelComboBox = new GridBagConstraints();
		gbc_lookAndFeelComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_lookAndFeelComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_lookAndFeelComboBox.gridx = 3;
		gbc_lookAndFeelComboBox.gridy = 2;
		contentPanel.add(lookAndFeelComboBox, gbc_lookAndFeelComboBox);
		lookAndFeelComboBox.setSelectedItem(currentLookAndFeel.getName());
		
			JLabel lblSolution = new JLabel(Tools.getLocalizedString(ActionType.PREFERENCES.name()+".SolutionType")+" :");
			lblSolution.setFont(UIManager.getFont("OptionPane.font"));
			GridBagConstraints gbc_lblSolution = new GridBagConstraints();
			gbc_lblSolution.fill = GridBagConstraints.HORIZONTAL;
			gbc_lblSolution.insets = new Insets(0, 0, 5, 5);
			gbc_lblSolution.gridx = 1;
			gbc_lblSolution.gridy = 3;
			contentPanel.add(lblSolution, gbc_lblSolution);
		
		lookAndFeelComboBox.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
	        updateLookAndFeel();
	    }});
		ButtonGroup buttonGroup = new ButtonGroup();
		chckbxDetailedSol = new JCheckBox(Tools.
				getLocalizedString(ActionType.PREFERENCES.name()+".DetailedSolution"));
		chckbxDetailedSol.setFont(UIManager.getFont("OptionPane.buttonFont"));
		GridBagConstraints gbc_chckbxDetailedSol = new GridBagConstraints();
		gbc_chckbxDetailedSol.fill = GridBagConstraints.BOTH;
		gbc_chckbxDetailedSol.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxDetailedSol.gridx = 3;
		gbc_chckbxDetailedSol.gridy = 3;
		contentPanel.add(chckbxDetailedSol, gbc_chckbxDetailedSol);
		
		chckbxMiniFunc = new JCheckBox(Tools.
				getLocalizedString(ActionType.PREFERENCES.name()+".MinimisedFunction"));
		chckbxMiniFunc.setFont(UIManager.getFont("OptionPane.buttonFont"));
		GridBagConstraints gbc_chckbxMiniFunc = new GridBagConstraints();
		gbc_chckbxMiniFunc.anchor = GridBagConstraints.NORTH;
		gbc_chckbxMiniFunc.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxMiniFunc.gridx = 3;
		gbc_chckbxMiniFunc.gridy = 4;
		if (pref.detailledSolution)chckbxDetailedSol.setSelected(true);
		else chckbxMiniFunc.setSelected(true);
		buttonGroup.add(chckbxMiniFunc);
		buttonGroup.add(chckbxDetailedSol);
		contentPanel.add(chckbxMiniFunc, gbc_chckbxMiniFunc);
		
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton(Tools.getLocalizedString(ActionType.PREFERENCES.name()+".ButtonOK"));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							savePreferences();
							dispose();
						} catch (Exception e) {}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			
				JButton cancelButton = new JButton(Tools.getLocalizedString("ButtonCancel"));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							UIManager.setLookAndFeel(pref.getLookAndFeel());
						} catch (Exception e) {
							e.printStackTrace();
						} 
						SwingUtilities.updateComponentTreeUI(controller.program.getFrame());
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			
		setVisible(true);
	}

	/**
	 * Change le lookAndFeel de l'application.
	 */
	protected void updateLookAndFeel() {
		try{
			UIManager.setLookAndFeel(pref.getLookAndFeelClassName(this.lookAndFeelComboBox.getSelectedItem().toString()));
		}
		catch(Exception e){}
		SwingUtilities.updateComponentTreeUI(this);
		SwingUtilities.updateComponentTreeUI(controller.program.getFrame());
		
		
	}

	/**
	 * Enregistre les préférences de l'utilisateurs
	 * @throws Exception 
	 * 
	 */
	protected void savePreferences() throws Exception {
		pref.setLanguage(getISOLanguageCode());
		pref.setLookAndFeel(pref.getLookAndFeelClassName(
				lookAndFeelComboBox.getSelectedItem().toString()));
        if (chckbxDetailedSol.isSelected()) pref.detailledSolution=true;
        else pref.detailledSolution=false;
        pref.savePreferences();
	}
	
	/**
	 * Retourne le code ISO de la langue.
	 */
	private String getISOLanguageCode(){
		String[] languages = pref.getSupportedLanguages();
		for (int i=0;i<languages.length;i++){
			Locale locale = new Locale(languages[i]);
			if (locale.getDisplayLanguage().compareToIgnoreCase(languageComboBox.getSelectedItem().toString())==0) return languages[i];
		}
		return "en";
	}


	
}
