
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
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.simple.XHTMLPrintable;

import com.lowagie.text.DocumentException;


/**
 * Un controller pour l'application<br>
 *@author Meradi, Hamoudi
 *
 */

public class Controller implements View{
	public Program program;
	public UserPreferences preferences;
	private File fileName; 
	private boolean isChanged;
	
	public Controller(Program program, UserPreferences prefs){
		this.program = program;
		preferences = prefs;
	}
	
    
	
	/**
     * Quitte l'application. En cas de modification</br>
     * du projet il sera demandé à l'utilisateur de </br>
     * sauvegarder son travail avant de quitter.
     */
	public void exit() 
	{
		
		if (isChanged){
			int s= Tools.showExitDialog();
			if(s==JOptionPane.YES_OPTION) {save(); System.exit(0);}
			if(s==JOptionPane.NO_OPTION) System.exit(0);
		}
		else System.exit(0);
            
	}
      
	
	  /**
	   * 
	   * Active les fonctionnalités de l'application à l'initialisation. 
	   */
	public void enableDefaultActions(Program program) {
	
	    program.setEnabled(View.ActionType.NEW, true);
	    program.setEnabled(View.ActionType.OPEN, true);
	    program.setEnabled(View.ActionType.PREFERENCES, true);
	    program.setEnabled(View.ActionType.EXIT, true);
	    program.setEnabled(ActionType.CUT, true);
	    program.setEnabled(ActionType.COPY, true);
	    program.setEnabled(ActionType.PASTE, true);
	    program.setEnabled(View.ActionType.KARNAUGH_MAP, true);
	    program.setEnabled(View.ActionType.TRUTH_TABLE, true);
	    program.setEnabled(View.ActionType.ALGEBRIC_FORM, true);
	    program.setEnabled(View.ActionType.NUMERIC_FORM, true);
	    program.setEnabled(View.ActionType.HELP, true);
	    program.setEnabled(View.ActionType.ABOUT, true);
	    program.setEnabled(View.ActionType.AND, true);
	    program.setEnabled(View.ActionType.OR, true);
	    program.setEnabled(View.ActionType.NOT, true);
	    program.setEnabled(View.ActionType.NAND, true);
	    program.setEnabled(View.ActionType.XOR, true);
	    program.setEnabled(View.ActionType.NOR, true);
	    program.setEnabled(View.ActionType.CLOSE, true);
	    program.setEnabled(View.ActionType.DELETE, true);
	    program.setEnabled(View.ActionType.IMPORT, true);
	  }
	
	
	/**
	 * Permet de créer un nouveau projet, en cas</br>
	 * de modification du projet en cours un message sera</br>
	 * affiché à l'utilisateur lui demandant d'enregistrer </br>
	 * son travail.
	 * @throws IOException 
	 */
	public void newProject() throws IOException{
		boolean canceled=false;
		if(isChanged)
		{
			Object[] options = {Tools.getLocalizedString("YES"),Tools.getLocalizedString("NO")};
			JFrame frame = new JFrame();
	        int n = JOptionPane.showOptionDialog(
	        frame, Tools.getLocalizedString("EXIT_DIALOGUE"),"KS Boolean Expression",
	        JOptionPane.YES_NO_OPTION, JOptionPane.YES_NO_OPTION, null, options, options[0]);
	        if(n==JOptionPane.YES_OPTION){ save();
	        if(isChanged) canceled=true;}
		}
		if(!canceled)
			{algebricForm();
			fileName = null;
			isChanged = false;
			
			}
		
	}
	
	
	/**
	 * Permet d'ouvrir un projet déjà enregistré</br>
	 * Un message d'erreur est prévue en cas d'une</br>
	 * extension non prise en charge par l'application. 
	 */
	public void open()
	{
		
		File f = Tools.showOpenDialog(Tools.getLocalizedString(ActionType.OPEN.name()+".Name"));
		open(f.getAbsolutePath());
		
		}
		
	/**
	 * Ouvre le fichier dont le chemin est path.
	 * @param path chemin
	 */
	public void open(String path) {
		File f = new File(path);
		String functions ="";
		if(f!=null){
			String ext = f.getName().substring(f.getName().length()-2, f.getName().length());
			if(ext.compareTo("ks")!=0) 
				JOptionPane.showMessageDialog(null, Tools.getLocalizedString("ERROR_OPENING")+" "+f.getName()+
						" "+Tools.getLocalizedString("ERROR_OPENING2"),
			             Tools.getLocalizedString("ERROR"), JOptionPane.ERROR_MESSAGE);
			else{
			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				functions = br.readLine();
				isChanged = false;
			} catch (FileNotFoundException e1) {
			} catch (IOException e1) {
			}
			catch(Exception e){
				System.err.println( e.getMessage() );
			}
			}
			}
			program.getMainPanel().getTextField().setText(functions);
			simplify(SolutionType.DETAILLED_SOLUTION);
			isChanged = false;
		
	}



	/**
	 * Permet de sauvegarder le projet en cours.
	 */
	public void save()
	{
		String title = Tools.getLocalizedString(ActionType.SAVE.name() +".Name");    
		String fonctions = program.getMainPanel().getTextField().getText();
		try {
			if(fileName==null)
			{
				String file  = Tools.showSaveDialog(title).getAbsolutePath();
				if(!file .endsWith(".ks")) file +=".ks";
				fileName = new File(file );
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			bw.append(fonctions);
			bw.newLine();
			bw.close();
		isChanged = false;
	    program.setEnabled(ActionType.SAVE, false);
	    program.setEnabled(ActionType.SAVE_AS, true);
	    } 
	catch (FileNotFoundException e) {
		System.err.println( e.getMessage() );
	}
	catch (IOException e) {
		System.err.println( e.getMessage() );
	}
	catch(Exception e){
		System.err.println( e.getMessage() );
	}
		
	}
	
	/**
	 * Permet d'exporter le projet en cours vers un fichier PDF
	 */
	public void export()
	{		
			File f =Tools.showSaveDialog(Tools.getLocalizedString(ActionType.EXPORT.name() +".Name"));
			if(f!=null){
			try {
				String url = Tools.getApplicationFolder()+"temp/temp.html";
				File file = new File(url);
	            if (file.exists()) {
	                url = file.toURI().toURL().toString();
	            }
	            String chemin=f.getAbsolutePath();
	            if(!chemin.endsWith(".pdf")) chemin+=".pdf";
				Tools.createPDF(url, chemin);
			}	
		    catch (IOException e) {
		    	System.err.println( e.getMessage() );
		    }
		   catch (DocumentException e) {
			   System.err.println( e.getMessage() );
		   }
			}
		}
		
	
	
	/**
	 * Permet de sauvegarder le projet en cours.</br>
	 * 
	 */
	public void saveAs()
	{
		try {
			String chemin= Tools.showSaveDialog(Tools.getLocalizedString(ActionType.SAVE_AS.name() +".Name")).getAbsolutePath();
			if(!chemin.endsWith(".ks")) chemin+=".ks";
			fileName = new File(chemin);
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			bw.append(program.getMainPanel().getTextField().getText());
			bw.close();
		isChanged = false;
	} 
	catch (FileNotFoundException e) {
		System.err.println( e.getMessage() );
	}
	catch (IOException e) {
		System.err.println( e.getMessage() );
	}
	catch(Exception e){
		System.err.println( e.getMessage() );
	}
		
	}
		
	
	/**
	 * Permet d'importer des fonctions à partir</br>
	 * d'un fichier text. Des message d'erreur sont</br>
	 * prévus en cas d'extension différente .txt ou bien</br>
	 * le fichier selectionné est vide.
	 */
	public void importFunctions()
	{
		File f=Tools.showOpenDialog("Importer");
		if(f!=null){
		String ext = f.getName().substring(f.getName().length()-3, f.getName().length());
		if(ext.compareTo("txt")!=0) 
			JOptionPane.showMessageDialog(null, Tools.getLocalizedString("ERROR_IMPORT")+" "+
		                            f.getName()+" "+Tools.getLocalizedString("ERROR_IMPORT2"),
		                           Tools.getLocalizedString("ERROR"), JOptionPane.ERROR_MESSAGE);
		
		else{
		String functions = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = br.readLine();
			while(line!=null){
				functions +=line+";";
				line = br.readLine();
			}
			isChanged = false;
			br.close();
			functions = functions.substring(0, functions.length()-1);
			
		} catch (FileNotFoundException e) {
			System.err.println( e.getMessage() );
		} catch (IOException e) {
			System.err.println( e.getMessage() );
		}
		catch(Exception e){
			System.err.println( e.getMessage() );
		}
		
		if(functions.length()!=0)
		{
			program.getMainPanel().getTextField().setText(functions);
			simplify(SolutionType.DETAILLED_SOLUTION);
		}
		else
		{
			JOptionPane.showMessageDialog(null, Tools.getLocalizedString("ERROR_IMPORT")+" "+
                    f.getName()+" "+Tools.getLocalizedString("ERROR_IMPORT3"),
                   Tools.getLocalizedString("ERROR"), JOptionPane.ERROR_MESSAGE);
		}
		}
		}
	}
	

     
			
		
	/**
	 * 	Ouvre la boite de dialogue Preferences</br>
	 */
	
	public void preferences(){
		PreferencePanel dialog = new PreferencePanel(this.program.getUserPreferences(),this);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
		//program.setEtatInitial();
		
	}
	
	
	
	/**
	 * Permet d'imprimer le projet en cours.
	 */
	
	public void print()
	{
		final PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(new XHTMLPrintable(program.getMainPanel().getxHTMLPanel()));

        if (printJob.printDialog()) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        printJob.print();
                    } catch (PrinterException ex) {
                    	System.err.println( ex.getMessage() );
                    }
                }
            }).start();
        }
}
	
	/**
	 * Fonction annuler
	 */
	public void undo(){
		if (program.getMainPanel().getUndo().canUndo())
			program.getMainPanel().getUndo().undo();
		updateUndoState();
		updateRedoState();
		
	}
	
	/**
	 * Function updateUndoState
	 */
	public void updateUndoState() {
		if (program.getMainPanel().getUndo().canUndo()) {
        	program.setEnabled(ActionType.UNDO, true);
        } else {
            program.setEnabled(ActionType.UNDO, false);
       
        }
    }
    
	/**
	 * Fonction Rétablir
	 */
	public void redo(){
		if (program.getMainPanel().getUndo().canRedo())
			program.getMainPanel().getUndo().redo();
		updateRedoState();
		updateUndoState();
		
	}
	/**
	 * Function updateRedoState
	 */
	public void updateRedoState() {
		if (program.getMainPanel().getUndo().canRedo()) {
        	program.setEnabled(ActionType.REDO, true);
        } else {
            program.setEnabled(ActionType.REDO, false);
        }
		
	}
	
	/**
	 * Fonction supprimer. Pour supprimer le</br>
	 * le text selectionné.
	 */
	public void delete(){
		StringBuffer s = new StringBuffer(program.getMainPanel().getTextField().getText());
		int begin = program.getMainPanel().getTextField().getText().indexOf(program.getMainPanel().getTextField().getSelectedText());
		int end = begin + program.getMainPanel().getTextField().getSelectedText().length();
		s.delete(begin, end);
		program.getMainPanel().getTextField().setText(s.toString());
	}
	
	/**
	 * Ouvre la boite de dialogue qui permet d'ajouter</br>
	 * une nouvelle table de Karnaugh.
	 */
	public void newKarnaughMap(){
		@SuppressWarnings("unused")
		KarnaughMapPanel kmap = new KarnaughMapPanel(this);
		
	}
	
	/**
	 * Ouvre la boite de dialogue qui permet d'ajouter</br>
	 * une nouvelle table de vérité.
	 */
	public void newTruthTable(){
		@SuppressWarnings("unused")
		TruthTablePanel table = new TruthTablePanel(this);
		
	}
	
	/**
	 * Lance la simplification à partir d'une table</br>
	 * de vérité.
	 * @param ttable la table de vérité
	 * @param nbrVar le nombre de variables
	 * @param varOrder l'ordre des variables
	 */
	public void solveFromTruthTable(int ttable[], int nbrVar, String varOrder)
	{
		Simplification simplify = new Simplification(ttable, nbrVar, varOrder, this);
		simplify.launchSimplification(Type.TRUTH_TABLE);
		program.createTree(1);
		showSolution();
		
	}
	
	/**
	 * Lance la simplification à partir d'une table</br>
	 * de Karnaugh.
	 * @param kmap la table de Karnaugh
	 * @param nbrVar le nombre de variables
	 * @param varOrder l'ordre des variables
	 */
	public void solveFromKMap(int kmap[][], int nbrVar, String varOrder)
	{
		Simplification simplify = new Simplification(kmap, nbrVar, varOrder, this);
		simplify.launchSimplification(Type.KARNAUGH_MAP);
		program.createTree(1);
		showSolution();


	}
	
	
	/**
	 * Permet d'afficher le résultat de la simplifiication</br>
	 * 
	 */
	private void showSolution() {
		program.getTabbedPane().remove(0);
		program.addTab(false);
			try {
				program.getMainPanel().getxHTMLPanel().setDocument(new File(Tools.getApplicationFolder()+"temp/temp.html"));
				
				 program.setEnabled(View.ActionType.SAVE, true);
				 program.setEnabled(View.ActionType.SAVE_AS, true);
				 program.setEnabled(View.ActionType.PRINT, true);
				 program.setEnabled(View.ActionType.EXPORT, true);
				 isChanged = true;
			} catch (Exception e) {
				System.err.println( e.getMessage() );
			}
		
	}

	/**
	 * Permet d'ajouter une nouvelle fonction de</br>
	 * de forme algébrique.
	 */
	public void algebricForm(){
		program.getMainPanel().getTextField().setText("");
		program.addTab(false);
		program.setEnabled(View.ActionType.PRINT, false);
		program.setEnabled(View.ActionType.SAVE, false);
		program.setEnabled(View.ActionType.SAVE_AS, false);
		program.setEnabled(View.ActionType.EXPORT, false);
		program.setEnabled(View.ActionType.MINIMIZED_FUNCTION, false);
		program.setEnabled(View.ActionType.DETAILED_SOLUTION, false);
		
	}
	
	/**
	 * Permet d'ajouter une nouvelle fonction de</br>
	 * de forme numérique.
	 */
	public void numericForm(){
		@SuppressWarnings("unused")
		NumericFunctionPanel digitalForm = new NumericFunctionPanel(this);
		
		
	}
	
	/**
	 * Donne le résultat de la simplification sous</br>
	 * forme d'une fonction réduite uniquement (sans</br>
	 * la table de vérité ni la table de Karnaugh).
	 */
	public void getSimpleSolution(){
		simplify(SolutionType.MINIMIZED_FUNCTION);
	}
	
	/**
	 * Donne le résultat de la simplification sous</br>
	 * forme d'une fonction + sa table de karnaugh</br>
	 * en cas d'une seul fonction à simplifiée on affichera</br>
	 * aussi sa table de vérité.
	 */
	public void getDetailedSolution(){
		simplify(SolutionType.DETAILLED_SOLUTION);
	}
	
	/**
	 * Affiche l'index (Aide)
	 */
	public void help(){
		
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			frame.setTitle(Tools.getLocalizedString("HELP_DIALOG_TITLE"));
			frame.setIconImage(Program.programIcon);
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setSize((int)((0.65)*d.width),(int)((0.65)*d.height));
			JPanel contentPane = new JPanel();
			contentPane.setLayout(new BorderLayout(0, 0));
			frame.setContentPane(contentPane);
			
			JPanel panel = new JPanel();
			contentPane.add(panel, BorderLayout.CENTER);
			panel.setLayout(new BorderLayout(0, 0));
			String url = "help/" + preferences.getLanguage() +"/index.htm";
		    XHTMLPanel xHTMLPanel = new XHTMLPanel();	
			FSScrollPane scroll = new FSScrollPane(xHTMLPanel);
			panel.add(scroll, BorderLayout.CENTER);
			try {
		    	xHTMLPanel.setDocument(new File(url));
		    	frame.setVisible(true);
			} catch (Exception e1) {
				unavailableHelp();	}
		}

	
	/**
	 * Prévient l'utilisateur par 'intermédiaire d'une boite<br>
	 * de dialogue lorsque l'aide n'est pas disponible pour une<br>
	 * une langue donnée.
	 */
	public void unavailableHelp()
	{
	    JEditorPane messagePane = new JEditorPane("text/html",
	    		Tools.getLocalizedString("UNAVAILABLE_HELP"));
	    messagePane.setOpaque(false);
	    messagePane.setEditable(false);
	    messagePane.addHyperlinkListener(new HyperlinkListener() {
	         public void hyperlinkUpdate(HyperlinkEvent ev) {
	            if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED) { 

	                Desktop desktop = Desktop.getDesktop();

	                try {
	                    desktop.browse( ev.getURL().toURI() );
	                }
	                catch ( Exception e ) {

	                    System.err.println( e.getMessage() );
	                }
	                }    
	         }
	    });
	    JOptionPane.showMessageDialog(program.getFrame(), messagePane, 
	    		Tools.getLocalizedString("HELP_DIALOG_TITLE"),  
	            JOptionPane.OK_OPTION, null);
	}
	
	/**
	 * Ouvre la boite de dialogue A propos (About)
	 * @throws FileNotFoundException
	 */
	public void about() throws FileNotFoundException{
		program.showAboutDialog();
		
	}
	

     /**
      * Lance la simplification à partir d'une expression</br>
      * introduite dans le JTextField.
      * @param solutionType type de la solution (simple ou détaillée)
      */
	public void simplify(SolutionType solutionType){
		Simplification s = new Simplification(
				program.getMainPanel().getTextField().getText().split(";"),solutionType, this);
		s.launchSimplification(Type.FUNCTION);
		program.createTree(program.getMainPanel().getTextField().getText().split(";").length);
		showSolution();
	}
	
	public void cut(){
		program.getMainPanel().getTextField().cut();
	}
	
	public void copy(){
		program.getMainPanel().getTextField().copy();
	}
	
	public void paste(){
		program.getMainPanel().getTextField().paste();
	}
	
	public void selectAll(){
		program.getMainPanel().getTextField().selectAll();
	}
	
	/**
	 * Permet d'écrire le symbole voulu en pressant sur les</br>
	 * différents bouttons de la bar des symbole.
	 * @param type
	 */
	public void printSymbol(Integer type){
		String string = program.getMainPanel().getTextField().getText();
		string = string.substring(0, program.getMainPanel().getTextField().getCaretPosition());
		switch(type){
		case 0: string = string + "*" ;break;
		case 1: string = string + "+" ;break;
		case 2: string = string + "!" ;;break;
		case 3: string = string + "|" ;;break;
		case 4: string = string + "/" ;;break;
		case 5: string = string + "^" ;;break;
		}
		string = string + program.getMainPanel().getTextField().getText().substring(string.length()-1, program.getMainPanel().getTextField().getText().length());
		program.getMainPanel().getTextField().setText(string);
		program.getMainPanel().getTextField().requestFocus();
	}
	
	/**
	 * Simplifie une fonction donnée sous frome numérique
	 * @param sets0
	 * @param sets1
	 */
	public void simplifyNumericFunction(String[] sets0, String[] sets1, int nbrVar, String varOrder) {
		
		int table[] = new int[(int) Math.pow(2,nbrVar)];
		if(sets1==null && sets0 ==null) JOptionPane.showMessageDialog(null, Tools.getLocalizedString("ERROR_NUMFORM"),
				                        Tools.getLocalizedString("ERROR"), JOptionPane.ERROR_MESSAGE);
		else{
		if(sets1!=null){		
		for(int j=0; j<table.length; j++)
			table[j]=0;

		for(int i=0; i<sets1.length; i++){
		table[Integer.valueOf(sets1[i])]=1;
		}
	     }  
		else if (sets0 !=null){
				for(int j=0; j<table.length; j++)
					table[j]=0;

				for(int i=0; i<sets0.length; i++){
				table[Integer.valueOf(sets0[i])]=1;
				}
		}
		solveFromTruthTable(table,nbrVar,varOrder);
		}
		
		
	
	}


    /**
     * Retourne la version de l'application
     * @return
     */
	public String getVersion() {
		return "1.0";
	}
	
	
	
}