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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;


/**
 * Boite de dialogue qui permet à l'utilisateurs d'introduire<br>
 * une nouvelle table de vérité<br>
 * @author Hamoudi, Meradi
 *
 */
public class TruthTablePanel extends JDialog implements ChangeListener {

	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JSpinner spinner = new JSpinner();
	private JScrollPane scrollPane ;
	private int truthTable[];

	/**
	 * Crée et affiche la boite de dialoque
	 */
	public TruthTablePanel(final Controller controller)  {
		super(controller.program.getFrame());
		setBounds(100, 100, 450, 465);
		this.setVisible(true);
		this.setIconImage(Program.programIcon);
		this.setTitle(Tools.getLocalizedString("TRUTH_TABLE.ShortDescription"));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
			JPanel panel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			contentPanel.add(panel, BorderLayout.NORTH);
				JLabel lblNombreDeVarib = new JLabel(Tools.getLocalizedString("NUMBER_OF_VARIABLES"));
				lblNombreDeVarib.setFont(UIManager.getFont("OptionPane.font"));
				panel.add(lblNombreDeVarib);
				spinner.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent arg0) {
						updateTable();
					}
				});
				spinner.setModel(new SpinnerNumberModel(4, 2, 8, 1));
				panel.add(spinner);
				this.table = createTable();
				Integer val = (Integer) spinner.getValue();
				val = val.intValue();
			for(int j=0; j<val+2; j++) table.getColumnModel().getColumn(j).setMaxWidth(40);
				
				this.scrollPane = new JScrollPane(this.table);
				contentPanel.add(scrollPane, BorderLayout.CENTER);
				this.contentPanel.add(scrollPane, FlowLayout.LEFT);
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						getValues();
						int i=0; boolean allZero=true;
						while(i<truthTable.length && allZero)
						{
							if(truthTable[i]==1) allZero=false;
							i++;
						}
						if(!allZero) controller.solveFromTruthTable(truthTable, getNbrVar(), getVarOrder());
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				JButton cancelButton = new JButton(Tools.getLocalizedString("ButtonCancel"));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
	}
        /**
         * Récupère les valeurs de la table de vérité intrduite
         */
		protected void getValues() {
			truthTable = new int[(int) Math.pow(2, (Integer) spinner.getValue())];
			for (int i=0;i<truthTable.length;i++){
				Integer col = (Integer) spinner.getValue();
				String comboBoxValue = (String) this.table.getValueAt(i, col.intValue()+1);
				char value = comboBoxValue.charAt(0);
				if(value=='0') truthTable[i] = 0;
				else truthTable[i] = 1;	
			}
		}
		
        /**
         * Permet de réinitialiser la table de vérité<br>
         * après redimentionnement de celle-ci<br>
         */
		public void updateTable() {
			this.table = createTable();
			Integer val = (Integer) spinner.getValue();
			val = val.intValue();
		    for(int j=0; j<val+2; j++) table.getColumnModel().getColumn(j).setMaxWidth(40);
			this.scrollPane.setViewportView(table);
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			this.getContentPane().repaint();
			}
        /**
         * Crée la table de vérité.
         * @return table 
         */
		private JTable createTable() {
			TruthTableModel model = new TruthTableModel(createRowData((Integer) spinner.getValue()),
                    createcolumnNames((Integer) spinner.getValue()));
           table = new JTable(model);
           setSColumn(table, table.getColumnModel().getColumn((Integer) spinner.getValue()+1));
		return table;
	}
         /**
          * Crée la colonne des valeurs de la fonction.
          * @param table 
          * @param functionColumn
          */
		public void setSColumn(JTable table,
	            TableColumn functionColumn) {
	       //Set up the editor for the sport cells.
	           JComboBox comboBox = new JComboBox();
	           comboBox.addItem("0");
	           comboBox.addItem("1");
	           comboBox.setSelectedIndex(0);
	           functionColumn.setCellEditor(new DefaultCellEditor(comboBox));

	           //Set up tool tips for the sport cells.
	           DefaultTableCellRenderer renderer =
	           new DefaultTableCellRenderer();
	           renderer.setToolTipText(Tools.getLocalizedString("CLICK_TO_SET"));
	           functionColumn.setCellRenderer(renderer);
	}
		
    /**
     * Crée et remplit la table de vérité.
     * @param nbrVars nombre de variable de la fonction
     */
	private Object[][] createRowData(Integer nbrVars) {
		int lines = (int) Math.pow(2, nbrVars.intValue());
		Object[][] data = new Object[lines][nbrVars.intValue() + 2];
		for (int i=0;i<lines;i++){
			for (int j=0;j<nbrVars.intValue()+2;j++){
				if(j==0)data[i][j]=i;
				else{
				if(j==nbrVars.intValue()+1) data[i][j] ="0";
				else if ((i%((int)Math.pow(2, nbrVars.intValue()-j+1)))<((int)Math.pow(2, nbrVars.intValue()-j)))
					   data[i][j] = new Integer(0);
				else data[i][j]   = new Integer(1);
			}
			}
			
		}
			
		return data;
	}
     /**
      * Crée la ligne des titre de la table.
      * @param nbrVars
      * @return
      */
	private String[] createcolumnNames(Integer nbrVars) {
		String[] columnNames = new String[nbrVars.intValue()+2];
		columnNames[0] = "index";
		for (int i=1;i<nbrVars.intValue()+1;i++){
			char c = (char) (64+i);
			columnNames[i] = String.valueOf(c);
		}
		columnNames[nbrVars.intValue()+1] = "S";
		return columnNames;
	}
     
	public void stateChanged(ChangeEvent arg0) {
		updateTable();
		
	}

	
	/**
	 * Retourne le nombre de variables.
	 */
	public int getNbrVar()
	{
		Integer col = (Integer) spinner.getValue();
		return  col.intValue();
	}
	/**
	 * Retourne l'ordre des variables dans la<br>
	 * table de vérité.
	 * @return ordrVar l'ordre des variables.
	 */
	public String getVarOrder()
	{
		Integer col = (Integer) spinner.getValue();
		String ordrVar="";
		for(int i=0; i<col.intValue(); i++)
		{
			char c = (char) (65+i);
			ordrVar = ordrVar+ String.valueOf(c);
			
		}
		return ordrVar;
		
	}
		
	}

/**
 * Modèle pour la table de vérité.
 *
 */
class TruthTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private Object[][] data;
	private String[] title;
  
	public TruthTableModel(Object[][] data,String[] title){
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
        if (col < getColumnCount()-1) {
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





