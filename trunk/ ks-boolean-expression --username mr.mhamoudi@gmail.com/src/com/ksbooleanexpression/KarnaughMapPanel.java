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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;


/**
 * Crée une boide de dialogue qui permet à<br>
 * l'utilisateur d'introduire une table de Karnaugh.
 * @author Mounir Hamoudi, Rabah Meradi
 *
 */
public class KarnaughMapPanel extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table ;
	private JSpinner spinner;
	private JScrollPane scrollPane;
	private int ktable[][];
	private boolean allZero =true;

	

	/**
	 * Crée et montre la boite de dialogue.
	 */
	public KarnaughMapPanel(final Controller controller) {
		super(controller.program.getFrame());
		setBounds(100, 100, 600, 400);
		setIconImage(Program.programIcon);
		setTitle(Tools.getLocalizedString("KARNAUGH_MAP.ShortDescription"));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

			JPanel panel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			contentPanel.add(panel, BorderLayout.NORTH);
			JLabel lblNombreDeVariazbles = new JLabel(Tools.getLocalizedString("NUMBER_OF_VARIABLES"));
			panel.add(lblNombreDeVariazbles);

				spinner = new JSpinner();
				spinner.setModel(new SpinnerNumberModel(4, 2, 8, 1));
				spinner.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent arg0) {
						updateTable();
					}
				});
				panel.add(spinner);
				
			    JPanel pan = new JPanel();
			    this.table = createTable();
			    scrollPane = new JScrollPane(table);
			    pan.add(scrollPane);
				contentPanel.add(scrollPane, BorderLayout.CENTER);
				this.contentPanel.add(scrollPane, FlowLayout.LEFT);
				
				
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						getValues();
						if(!allZero) controller.solveFromKMap(ktable, getNbrVar(), getVarOrder());
						dispose();
					}
				});
				getRootPane().setDefaultButton(okButton);
			
				JButton cancelButton = new JButton(Tools.getLocalizedString("ButtonCancel"));
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				buttonPane.add(cancelButton);
		this.setVisible(true);	
	}
	
	
	/**
	 * Permet de récupérer les valeurs introduite dans la table de Karnaugh
	 */
	protected void getValues() {
		Integer n= (Integer) spinner.getValue();
		int lines =(int) Math.pow(2, Math.floor((double)n.intValue()/2));
		int column=(int) Math.pow(2,Math.ceil( (double)n.intValue()/2));
		ktable = new int[lines][column];
		for (int i=0;i<lines;i++){
			for(int j=1; j<column+1; j++){
			String cellValue = (String) this.table.getValueAt(i,j);
			char c= cellValue.charAt(0);
			if (c=='0') ktable[i][j-1] = 0;
			else {ktable[i][j-1]=1; allZero = false;}
		}
		}
		
	}
	
	
	/**
     * Permet de réinitialiser la table de Karnaugh<br>
     * après redimentionnement de celle-ci<br>
     */
	public void updateTable() {
			this.table = createTable();
			this.scrollPane.setViewportView(table);
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			this.getContentPane().repaint();
			}

	/**
     * Crée la table de Karnaugh.
     * @return table 
     */
	private JTable createTable() {
		Integer n = (Integer) spinner.getValue();
		KMapModel model = new KMapModel(createRowData(n),createcolumnNames(n));
        table = new JTable(model);
        int lines =(int) Math.pow(2, Math.floor((double)n.intValue()/2));
		int column=(int) Math.pow(2,Math.ceil( (double)n.intValue()/2));
        for (int i=0;i<lines;i++)
        {
			for (int j=0;j<column+1;j++)
			{
				if(j!=0)
				setSColumn(table, table.getColumnModel().getColumn((Integer) j));
			}
		}
        return table;
}

	/**
     * Crée les colonnes de la table
     * @param table 
     * @param functionColumn
     */
	public void setSColumn(JTable table,
            TableColumn sportColumn) {
           JComboBox comboBox = new JComboBox();
           comboBox.addItem("0");
           comboBox.addItem("1");
           comboBox.setSelectedIndex(0);
           sportColumn.setCellEditor(new DefaultCellEditor(comboBox));

           DefaultTableCellRenderer renderer =
           new DefaultTableCellRenderer();
           renderer.setToolTipText(Tools.getLocalizedString("CLICK_TO_SET"));
           sportColumn.setCellRenderer(renderer);
}
	
	/**
	 * Crée les nom des colonnes de la table
	 * @param value nombre de variables
	 */
	private String[] createcolumnNames(Integer value) {
		return Tools.getColonneName(value.intValue());
	}
	
	
	 /**
     * Crée et remplit la table de Karnaugh.
     * @param value nombre de variable de la fonction
     */
	private Object[][] createRowData(Integer value) {
		int lines =(int) Math.pow(2, Math.floor((double)value.intValue()/2));
		int column=(int) Math.pow(2,Math.ceil( (double)value.intValue()/2));
		Object[][] data = new Object[lines][column+1];
		for (int i=0;i<lines;i++){
			for (int j=0;j<column+1;j++){
				if(j==0) data[i][j]=Tools.getLigneName(value.intValue())[i];
				else data[i][j] ="0"; 
				}
			}
		return data;
	}

		
		/**
		 * Retourne le nombre de variable de la table
		 */
		public int getNbrVar()
		{
			Integer col = (Integer) spinner.getValue();
			return  col.intValue();
		}
		/**
		 * Retourne l'ordre des variables dans la table
		 */
		public String getVarOrder()
		{
			Integer col = (Integer) spinner.getValue();
			String ordrVar="";
			for(int i=0; i<col.intValue(); i++)
			{
				char c = (char) (65+i);
				ordrVar = ordrVar+ c;
				
			}
			return ordrVar;
			
		}
	
}

/**
 * Modèle pour l'affichage de la table de Karnaugh
 */
class KMapModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object[][] data;
	private String[] title;
  
	public KMapModel(Object[][] data,String[] title){
	  this.data = data;
	  this.title = title;
  }

    public int getColumnCount() {
        return title.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return title[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }


    public Class<? extends Object> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

 
    public boolean isCellEditable(int row, int col) {
        if (col==0) {
            return false;
        } else {
            return true;
        }
    }
    
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
    
    
    
    
    
}

