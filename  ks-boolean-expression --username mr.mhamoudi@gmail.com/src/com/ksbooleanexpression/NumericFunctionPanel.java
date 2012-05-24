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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


/**
 * Ouvre une boite de dialogue pour introduire<br>
 * une fonction numérique.
 * @author Hamoudi, Meradi
 *
 */

public class NumericFunctionPanel extends JDialog {
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JTextField textField_1;
	private JTextField textField;
	private JComboBox nbrVarComboBox;
	


	/**
	 * Crée et aafiche la boite de dialogue
	 */
	public NumericFunctionPanel(final Controller controller) {
		super(controller.program.getFrame());
		setResizable(false);
		setTitle(Tools.getLocalizedString("DEGITAL_FORM_TITLE"));
		setBounds(100, 100, 398, 300);
		contentPanel = new JPanel();
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		
		
		textField_1 = new JTextField();
		contentPanel.setLayout(null);
		textField_1.setBounds(100, 137, 244, 22);
		contentPanel.add(textField_1);
		
		textField = new JTextField();
		textField.setBounds(100, 183, 244, 22);
		contentPanel.add(textField);
		
		final JLabel indicationLabel = new JLabel(Tools.getLocalizedString("DEGITAL_FORM_INDICATION")
				                                 + "15}");
		indicationLabel.setBounds(28, 80, 210, 30);
		indicationLabel.setFont(new Font("Arial", Font.PLAIN, 13));
		contentPanel.add(indicationLabel);
		
		JLabel labelSetOnes = new JLabel("1");
		labelSetOnes.setBounds(62, 137, 10, 22);
		labelSetOnes.setFont(new Font("Franklin Gothic Book", Font.BOLD, 16));
		contentPanel.add(labelSetOnes);
		
		JLabel labelSetZeros = new JLabel("0");
		labelSetZeros.setBounds(62, 183, 10, 22);
		labelSetZeros.setFont(new Font("Franklin Gothic Book", Font.BOLD, 16));
		contentPanel.add(labelSetZeros);
		
		nbrVarComboBox = new JComboBox();
		nbrVarComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				int x = Integer.valueOf(nbrVarComboBox.getSelectedItem().toString());
				x = (int) Math.pow(2, x);
				indicationLabel.setText(Tools.getLocalizedString("DEGITAL_FORM_INDICATION") +
						              String.valueOf(x-1)+"}");
			}
		});
		nbrVarComboBox.setFont(new Font("Tahoma", Font.BOLD, 11));
		nbrVarComboBox.setModel(new DefaultComboBoxModel(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8}));
		nbrVarComboBox.setSelectedIndex(3);
		nbrVarComboBox.setBounds(269, 37, 39, 20);
		contentPanel.add(nbrVarComboBox);
		
		JLabel nbrVarLabel = new JLabel((Tools.getLocalizedString("NUMBER_OF_VARIABLES")));
		nbrVarLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
		nbrVarLabel.setBounds(95, 37, 164, 20);
		contentPanel.add(nbrVarLabel);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
				
			JButton cancelButton = new JButton((Tools.getLocalizedString("ButtonCancel")));
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
						dispose();
					}});
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int nrVar=Integer.valueOf(nbrVarComboBox.getSelectedItem().toString());	
					controller.simplifyNumericFunction(getSets0(),getSets1(),nrVar,getVarOrder() );
					dispose();
					}});
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setVisible(true);	
		}
	
	/**
	 * Retourne les lignes de la tables de vérité<br>
	 * où la fonction et à 1.
	 */
	public String[] getSets1(){
		String[] sets=null;
		try{
		sets = textField_1.getText().split(",");
		if(textField_1.getText().length()<1) return null; 
		int x = Integer.valueOf(nbrVarComboBox.getSelectedItem().toString());
		x = (int) Math.pow(2, x);
		for(String s : sets){
			int set = Integer.valueOf(s);
			if (!(set <x))return null;
		}
			
	}
	catch(Exception e){}
		return sets;
		
	}
	
	/**
	 * Retourne les lignes de la tables de vérité<br>
	 * où la fonction et à 0.
	 */
	public String[] getSets0(){
		String[] sets =null;
		try{
		sets = textField.getText().split(",");
		if(textField_1.getText().length()<1) return null;
		int x = Integer.valueOf(nbrVarComboBox.getSelectedItem().toString());
		x = (int) Math.pow(2, x);
		for(String s : sets){
			int set = Integer.valueOf(s);
			if (!(set <x))return null;
		}
		}
		catch(Exception e){}
		return sets;
		
	}
	
	
	/**
	 * Retourne l'ordre des variables
	 */
	public String getVarOrder()
	{
		int s=Integer.valueOf(nbrVarComboBox.getSelectedItem().toString());
		String ordrVar="";
		for(int i=0; i<s; i++)
		{
			char c = (char) (65+i);
			ordrVar = ordrVar+ c;
			
		}
		return ordrVar;
		
	}
	
}


