/*
# KS Boolean Expression, Copyright (c) 2012 The Authors. / ks.contrubutors@gmail.com
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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.ITextUserAgent;
import org.xhtmlrenderer.resource.XMLResource;
import org.xml.sax.InputSource;

import com.lowagie.text.DocumentException;



/**
 * Rassemble toutes les fonctions utiles qui sont<br>
 * utilisées par plusieurs autres classes.
 * @author Mounir Hamoudi, Meradi.
 *
 */
public class Tools implements View{

	/**
	   * Retourne le text localisé pour les noms de menus ainsi<br>
	   * que les raccourcis qui eux dependent du system.
	   */
	  public static String getLocalizedLabelText(String   resourceKey) {
	    String localizedString = Tools.getLocalizedString(resourceKey);
	    return localizedString;
	  }

	
	/**
	 * 
	 * Permet d'afficher la boite de dialogue pour enregistrer un fichier.
	 * @param title pour le nom de la boite de dialogue
	 * @return File le fichier sauvegrdé
	 */
	public static File showSaveDialog(String title)
	{
		final ExtensionFileFilter filter= new ExtensionFileFilter();
		JFileChooser Save = new JFileChooser();
		JFrame frame = new JFrame();
		frame.setIconImage(Program.programIcon);
		if(title.compareTo(Tools.getLocalizedString(ActionType.EXPORT.name() +".Name"))==0){
			filter.setDescription(Tools.getLocalizedString("FILE_TYPE_PDF"));
			filter.addExtension(".pdf");
			Save.setDialogTitle(Tools.getLocalizedString(ActionType.EXPORT.name() +".Name"));
			Save.setSelectedFile(new File(Tools.getLocalizedString("UNTITLED")+".pdf"));
		}
		else{
		filter.setDescription(Tools.getLocalizedString("FILE_TYPE_KS"));
		filter.addExtension(".ks");
		Save.setDialogTitle(title);
		Save.setSelectedFile(new File(Tools.getLocalizedString("UNTITLED")+".ks"));
		}
		
		Save.setFileFilter(filter);
		Save.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    File file = null;
		int result= Save.showSaveDialog(frame);

		if(result == JFileChooser.APPROVE_OPTION)
		{
		  file = Save.getSelectedFile();
		  if(file.exists()){
			  Object[] options = {Tools.getLocalizedString("YES"),Tools.getLocalizedString("NO")};
		        int n = JOptionPane.showOptionDialog(
		        		Save, Tools.getLocalizedString("FILE_ALREADY_EXIST")+" "+file.getName()+" "+
		        		Tools.getLocalizedString("FILE_ALREADY_EXIST2"),Tools.getLocalizedString("CONFIRM_SAVE_REPLACE"),
		        JOptionPane.YES_NO_OPTION, JOptionPane.YES_NO_OPTION, null, options, options[0]);
		        if(n==JOptionPane.NO_OPTION){file=null; Tools.showSaveDialog(title);}
			 }
		  }
		return file;
	}
		
	/**
	 * Permet d'afficher la boite de dialogue pour ouvrir un fichier	
	 * @return File le fichier séléctionné
	 */
	public static File showOpenDialog(String title)
	{
		final ExtensionFileFilter filter= new ExtensionFileFilter();
		JFileChooser Open = new JFileChooser();
		if(title.compareTo("Importer")==0){
			filter.setDescription(Tools.getLocalizedString("FILE_TYPE_TXT"));
			filter.addExtension(".txt");
			Open.setFileFilter(filter);
			Open.setDialogTitle(Tools.getLocalizedString(ActionType.IMPORT.name()+".Name"));
		}
		
		else{
		filter.setDescription(Tools.getLocalizedString("FILE_TYPE_KS"));
		filter.addExtension(".ks");
		Open.setFileFilter(filter);
		Open.setDialogTitle(title);
		}
		Open.setFileSelectionMode(JFileChooser.FILES_ONLY);
		JFrame frame = new JFrame();
		frame.setIconImage(Program.programIcon);
		File file = null;
		int result= Open.showOpenDialog(frame);
		
	   	
		if(result == JFileChooser.APPROVE_OPTION)
		{
		
		try
		{
			file = Open.getSelectedFile();
				      		
		} catch(Exception err){
			System.err.println( err.getMessage() );
		}
		
	}
		return file;
	}
	/**
	 * Affiche une boite de dialogue lors de la fermuture de l'application
	 * Elle est affichée dans le seul cas ou des modifications n'ont pas été 
	 * enregitrées 
	 * @return int
	 */
	public static int showExitDialog()
	{
		Object[] options = {Tools.getLocalizedString("YES"),Tools.getLocalizedString("NO"), Tools.getLocalizedString("ButtonCancel")};
		JFrame frame = new JFrame();
		frame.setIconImage(Program.programIcon);
        int n = JOptionPane.showOptionDialog(
        frame, Tools.getLocalizedString("EXIT_DIALOGUE"),"KS Boolean Expression",
        JOptionPane.YES_NO_OPTION, JOptionPane.YES_NO_CANCEL_OPTION, null, options, options[0]);
        
        return n;
	}

		/**
		   * Met à jour les resources bundles de Swing quand<br>
		   * on chnage la langue de l'application.
		   */
		  public static void updateSwingResourceLanguage() {
		    updateSwingResourceBundle("com.sun.swing.internal.plaf.metal.resources.metal");
		    updateSwingResourceBundle("com.sun.swing.internal.plaf.basic.resources.basic");
		    if (UIManager.getLookAndFeel().getClass().getName().equals("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
		      updateSwingResourceBundle("com.sun.java.swing.plaf.gtk.resources.gtk");
		    } else if (UIManager.getLookAndFeel().getClass().getName().equals("com.sun.java.swing.plaf.motif.MotifLookAndFeel")) {
		      updateSwingResourceBundle("com.sun.java.swing.plaf.motif.resources.motif");
		    } 
		  }

		  /**
		   * Met à jour les resources bundles de Swing quand<br>
		   * on chnage la langue de l'application.
		   */
		  private static void updateSwingResourceBundle(String swingResource) {
		    ResourceBundle resource;
		    try {
		      resource = ResourceBundle.getBundle(swingResource);
		    } catch (MissingResourceException ex) {
		      resource = ResourceBundle.getBundle(swingResource, Locale.ENGLISH);
		    }
		    for (Enumeration<?> it = resource.getKeys(); it.hasMoreElements(); ) {
		      String property = (String)it.nextElement();
		      UIManager.put(property, resource.getString(property));
		    }
		  }
		  
		  
		  /**
		   * Reourne <code>true</code> si le système courant est un Mac OS
		   */
		  public static boolean isMacOSX() {
		    return System.getProperty("os.name").startsWith("Mac OS X");
		  }
		  
		  /**
		   * Ecrit la table de vérité en HTML d'une fonction donnée.
		   * @param tb la table de vérité de la fonction
		   * @param nombreVars nombre de variables de la fonction
		   * @param ordrVars l'ordre des variables dans la tables de vérité
		   */
		  public static String writeTruthTable(TruthTable tb, int nombreVars,String ordrVars){
			  StringBuffer s = new StringBuffer("");
			  int width = nombreVars*50;
			  s.append("<div id=\"cent\" align=\"center\">");
			    s.append("<h3 class =\"subsubTitle2\">"+Tools.getLocalizedString("TRUTH_TABLE.Name")+" :"+"</h3>");
				s.append("<table width=\""+width+"\" id=\"table1\" rules=\"all\">");
				s.append("<tr>");
				for (int i=0;i<nombreVars;i++){
					s.append("<th>"+ordrVars.charAt(i)+"</th>");
				}
				s.append("<th>S</th>");
				s.append("</tr>");
				for (int i=0; i<tb.TruthTable.length;i++){
					s.append("<tr>");
					for(int j=0;j<nombreVars+1;j++){
							if(j==nombreVars){
								if(tb.TruthTable[i]==1) s.append("<td class=\"ones\">"+tb.TruthTable[i]+"</td>");
								else s.append("<td>"+tb.TruthTable[i]+"</td>");
							}
							else {
								if ((i%((int)Math.pow(2, nombreVars-j)))<((int)Math.pow(2, nombreVars-j-1)))
								s.append("<td>0</td>");
							else s.append("<td>1</td>");
							}
					}
					s.append("</tr>");
				}
				s.append("</table>");
				s.append("</div>");
				return s.toString();
			}
		  /**
		   * Initialise le contenu du fichier HTML<br>
		   * Ecrit l'entete du fichier.
		   */
		  public static String intialize(){
				String s = "";
				    s+="<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>";
					s +="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
							"<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
							"<head>";
					s += "<link rel=\"stylesheet\" href=\"Style.css\" />";
					
					s += "</head>";
					s+="<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />";
					s+="<body>";
			  return s;
		  }
		  
		  /**
		   * Crée une image de la fonction écrite avec LaTeX.
		   * @param function la fonction donnée
		   * @param i numéro de la fonction
		   * @param name nom de l'image à créer
		   */
		  private static void createImageForFunction(String function, int i, String name){ 
			  TeXFormula formula = new TeXFormula(getLatexForm(function,i));
					TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
					icon.setInsets(new Insets(5, 5, 5, 5));
					
					BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics2D g2 = image.createGraphics();
					g2.setColor(Color.white);
					g2.fillRect(0,0,icon.getIconWidth(),icon.getIconHeight());
					JLabel jl = new JLabel();
					jl.setForeground(new Color(0, 0, 0));
					icon.paintIcon(jl, g2, 0, 0);
					File file = new File(Tools.getApplicationFolder()+"temp/"+name);
					file.deleteOnExit();
					try {
					    ImageIO.write(image, "png", file.getAbsoluteFile());
					} catch (IOException ex) { 
						System.err.println( ex.getMessage() );
					}
		  }
		  
		  /**
		   * Ecrit les fonction introduite en HTML.
		   * @param functions les fonctions itroduites
		   */
		  public static String writeIntroducedFunctions(String [] functions){
			  StringBuffer s = new StringBuffer("");
			  
				  s.append("<h2 class=\"Title\">"+Tools.getLocalizedString("INTRODUCED_FUNCTION")+"</h2>");
				  for (int i=0; i<functions.length;i++){
					  try{
					      String name = "Function" + i + ".png";
				          createImageForFunction(functions[i],i+1,name);
				          s.append("<img src=\"function"+i+".png"+"\" /><br />");
				           s.append("\n",0,1);
				  
			           }catch(Exception e){
			        	   System.err.println( e.getMessage() );
			           }
				  
		             }
				  s.append("<h2 class=\"Title\">"+Tools.getLocalizedString("MINIMISED_FUNCTION")+"</h2>");
			  
			  return s.toString();
		  }
		 
		  /**
		   * Ecrit la solution en HTML après la simplification.
		   * @param minimisedExpression l'expression réduite.
		   * @param i numéro de la fonction
		   */
		  public static String writeSolution(String minimisedExpression, int i){
			  StringBuffer s =new StringBuffer("");
			  String name = "MinimisedForm" + i + ".png";
			  createImageForFunction(minimisedExpression,i,name);
			  s.append("<a id=\"function"+i+"\"></a>");
			  s.append("<h3 class =\"subTitle\" >"+Tools.getLocalizedString("FUNCTION")+" F"+i+" :</h3>");
			  s.append("<img src=\"MinimisedForm"+i+".png"+"\" /><br />");
			   
			  return s.toString();
		  }

		  /**
		   * Ecrit la table de Karnaugh en HTML d'une fonction donnée.
		   * @param kmap la table de Karnaugh de la fonction
		   * @param nbrVars nombre de variables de la fonction.
		   */
		public static String writeKarnaughMap(KarnaughTable kmap, int nbrVars) {
			int width = nbrVars*50;
			String s="";
			
				s+="<div id=\"cent\" align=\"center\">";
			
				s+="<h3 class =\"subsubTitle2\">"+Tools.getLocalizedString("KARNAUGH_MAP.Name")+":"+"</h3>";
				
				s+="<table width=\""+width+"\"><tr>";
				
			
			 String[] colomNames = getColonneName(nbrVars);
				String[] linesNames = getLigneName(nbrVars);
				for (int i=0;i<colomNames.length;i++){
					s +="<th>"+colomNames[i] + "</th>";
				}
				s +="</tr><tr>";
				for (int i=0;i<kmap.getRow();i++){
					for(int j=0;j<kmap.getColumn()+1;j++){
						if(j==0) s+="<th>" + linesNames[i] +"</th>";
						else{
							if(kmap.getCellValue(i, j-1)==1)
							s += "<td class=\"ones\">" + kmap.getCellValue(i, j-1)+ "</td>";
							else s += "<td>" + kmap.getCellValue(i, j-1)+ "</td>";
							}
					}
					s += "</tr><tr>";
				}
				s = s.substring(0, s.length()-4);
				s +="</table>";
				s+="</div>";
				return s;
				
				
		}
		/**
		 * Retourne le repertoire où se trouve l'application.
		 */
		
		public static String getApplicationFolder(){
			String chemin = Tools.class.getResource("../../temp/style.css").toString();
			if(chemin.equals("null")) return null;
			else{
				chemin = chemin.substring(6,chemin.length()-14);
				chemin = Tools.formatURL(chemin);
				return chemin;
			}
			
		}
		
		/**
		 * Remplacent les <code>%20</code> dans <code>url</code> par des espaces.
		 * @param chemin
		 */
		public static String formatURL(String url) {
			String s ="";
			int index =0;
			while((index = url.indexOf("%20")) !=-1)
			{
				s +=url.substring(0, index)+" ";
				url = url.substring(index+3, url.length());
			}
			s +=url;
			return s;
		}
		
		/**
		 * Crée une fichier pdf à partir d'un fichier HTML.
		 * @param url chemin du fichier HTML
		 * @param pdf extension pdf
		 * @throws IOException
		 * @throws DocumentException
		 */
		public static void createPDF(String url, String pdf)
	            throws IOException, DocumentException {
	        FileOutputStream os = null;
	        try {
	            os = new FileOutputStream(pdf);
	            ITextRenderer renderer = new ITextRenderer();
	            ResourceLoaderUserAgent callback = new ResourceLoaderUserAgent(renderer.getOutputDevice());
	            callback.setSharedContext(renderer.getSharedContext());
	            renderer.getSharedContext ().setUserAgentCallback(callback);

	            Document doc = XMLResource.load(new InputSource(url)).getDocument();

	            renderer.setDocument(doc, url);
	            renderer.layout();
	            renderer.createPDF(os);

	            os.close();
	            os = null;
	        } finally {
	            if (os != null) {
	                try {
	                    os.close();
	                } catch (IOException e) {
	                	System.err.println( e.getMessage() );
	                }
	            }
	        }
	}
		
		   /**
		    * Converti l'écriture d'une fonction vers son écriture en LaTeX.
		    * @param function expression à convertir
		    * @return expression écrite en LaTeX.
		    */
			
			private static String convertToLatex(String function)
			{
				int i=0;
				String convertedForm="";
				while(i<function.length())
				{
					if(function.charAt(i)!='*' && function.charAt(i)!='/' && function.charAt(i)!='|' && function.charAt(i)!='^' && function.charAt(i)!='!')
					{
						convertedForm = convertedForm+ function.charAt(i);
						i++;
					}
					else{
						
					if(function.charAt(i)=='*'){
					convertedForm = convertedForm+ ". "; i++;}
					
					if(function.charAt(i)=='/'){
					convertedForm = convertedForm+ "\\downarrow "; i++;}
					
					if(function.charAt(i)=='|'){
					convertedForm = convertedForm+ "\\uparrow  "; i++;}
					
					if(function.charAt(i)=='^'){
					convertedForm = convertedForm+ "\\oplus  "; i++;}
					
					if(function.charAt(i)=='!')
					{
						if(i==function.length()-1) {convertedForm+="!"; i++;}
						else{
						if(function.charAt(i+1)!='('){
						convertedForm = convertedForm+ "\\overline  "; i++;}
						else if(function.charAt(i+1)=='(')
						{
							convertedForm = convertedForm+ "\\overline  {(" +convertToLatex(function.substring(i+2, getEndOfBrackets(function, i+1)))+")} " ;
							i=getEndOfBrackets(function, i+1)+1;
						}
						}
					}
					
					}
					
				}
				return convertedForm;
			}
			
			/**
			 * Retourne la position ou se ferme une parenthèse dans une fonctions.
			 * @param function l'expression de la fonction
			 * @param posActuelle la position actuelle dans la fonction.
			 * @return position ou se ferme la prenthèse ouverte.
			 */
			public static int getEndOfBrackets(String function, int posActuelle)
			{
				int nbrBracketsOpened = 0; 
				int nbrBracketsClosed = 0;
				int endPosition = 0;
				for(int j=posActuelle; j<function.length(); j++)
				{
					
					if(function.charAt(j)=='(') nbrBracketsOpened++;
					else if(function.charAt(j)==')') nbrBracketsClosed++;
					
					if(nbrBracketsOpened==nbrBracketsClosed)
					{
					endPosition = j;
					break;
					}
						
					
				}
				return endPosition;
			}
			
			/**
			 * Donne la forme finale en LaTeX d'une expression.
			 * @param function fonction à convertir
			 * @param numFunction numéro de la fonction.
			 * @return expression écrite en LaTeX
			 */
			public static String getLatexForm(String function, int numFunction)
			{
				return "$\\textbf{F}_{"+numFunction+"} = " + convertToLatex(function)+ "$";
			}
			  

			
			/**
			 * Donne les nom des colonne d'une table de Karnaugh<br>
			 * pour une fonctions donnée.
			 * @param nbrVar nombre de variables
			 * @return tableau qui contient les noms des colonnes.
			 */
			public static String[] getColonneName(int nbrVar)
			{
				String[] names =null;
				switch (nbrVar)
				{
				case 1:
				case 2: 
				{
					String columnNane[]={"","0", "1"};
					names = columnNane;
					break;
				}
				
				case 3: 
				{
					String columnNane[]={"","00", "01", "11", "10"};
					names = columnNane;
					break;
				}
				
				case 4: 
				{
					String columnNane[]={"","00", "01", "11", "10"};
					names = columnNane;
					break;
				}
				
				case 5: 
				{
					String columnNane[]={"","000", "001", "011", "010", "110", "111", "101","100"};
					names = columnNane;
					break;
				}
				
				case 6: 
				{
					String columnNane[]={"","000", "001", "011", "010", "110", "111", "101","100"};
					names = columnNane;
					break;
				}
				
				case 7: 
				{
					String columnNane[]={"","0000", "0001", "0011", "0010", "0110", "0111","0101",
							"0100","1100","1101","1111","1110","1010","1011","1001","1000"};
					names = columnNane;
					break;
				}
				
				case 8: 
				{
					String columnNane[]={"","0000", "0001", "0011", "0010", "0110", "0111","0101",
							"0100","1100","1101","1111","1110","1010","1011","1001","1000"};
					names = columnNane;
					break;
				}
				
				}
				return names;
					
			}
		
		
			/**
			 * Donne les noms des lignes d'une table de Karnaugh<br>
			 * pour une fonction donnée.
			 * @param nbrVar nombre de variables
			 * @return tableau qui contient les noms des lignes.
			 */
			public static String[] getLigneName(int nbrVar)
			{
				String names[] = null;
				switch (nbrVar)
				{
				case 2: 
				{
					String ligneName[]={"0", "1"};
					names = ligneName;
					break;
				}
				
				case 3: 
				{
					String ligneName[]={"0", "1"};
					names= new String[2];
					names = ligneName;
					break;
				}
				
				case 4: 
				{
					String ligneName[]={"00", "01", "11", "10"};
					names = ligneName;
					break;
				}
				
				case 5: 
				{
					String ligneName[]={"00", "01", "11", "10"};
					names = ligneName;
					break;
				}
				
				case 6: 
				{
					String ligneName[]={"000", "001", "011", "010", "110", "111", "101","100"};
					names = ligneName;
					break;
				}
				
				case 7: 
				{
					String ligneName[]={"000", "001", "011", "010", "110", "111", "101","100"};
					names = ligneName;
					break;
				}
				
				case 8: 
				{
					String ligneName[]={"0000", "0001", "0011", "0010", "0110", "0111","0101",
							"0100","1100","1101","1111","1110","1010","1011","1001","1000"};
					names = ligneName;
					break;
				}
				
				}
				return names;
					
			}
			
			
			
			
			/**
			   * Returns the string matching <code>resourceKey</code> for the given resource bundle.
			   */
			  public static String getLocalizedString(String resourceKey) {
			    try {
			    	ResourceBundle resource = ResourceBundle.getBundle("translations//lpackage");
			      String localizedString = resource.getString(resourceKey); 
			      return localizedString;
			    } catch (MissingResourceException ex) {
			      throw new IllegalArgumentException("Unknown key " + resourceKey);
			    }
			  }
			
			
			/**
			 * En cas d'erreur dans une fonctions introduite, alors cette<br>
			 * fonction écrit l'erreur dans le fichier HTML contenant la solution.
			 * @param i numéro de la fonction
			 * @param message type d'erreur comise.
			 */
			public static String writeError(int i,MessageType message) {
				 StringBuffer s =new StringBuffer("");
				 s.append("<h3 class =\"subTitle\" id='function"+i+"'>"+Tools.getLocalizedString("FUNCTION")+" F"+i+" :</h3>");
				 s.append("<h3>"+Tools.getLocalizedString("SYNTAX_ERROR")+" </h3>");
				  s.append(Tools.getLocalizedString(message.name()));
				  return s.toString();
			}
			
			private static class ResourceLoaderUserAgent extends ITextUserAgent
			{
			    public ResourceLoaderUserAgent(ITextOutputDevice outputDevice) {
			        super(outputDevice);
			    }

			    protected InputStream resolveAndOpenStream(String uri) {
			        InputStream is = super.resolveAndOpenStream(uri);
			        return is;
			    }
			}
}



    /**
	 * Filtre pour les extentions de fichier<br>
	 * @author Mounir Hamoudi
	 *
	 */
class ExtensionFileFilter extends FileFilter
	{
	
	private String description = "";
	private ArrayList<String> extensions = new ArrayList<String>();
	 
	public void addExtension(String extension)
	 {
	 if (!extension.startsWith("."))
	 extension = "." + extension;
	 extensions.add(extension.toLowerCase());
	 }

	 public void setDescription(String aDescription)
	 {
	 description = aDescription;
	 }

	 public String getDescription()
	 {
	 return description;
	 }

	 public boolean accept(File f)
	 {
	 if (f.isDirectory()) return true;
	 String name = f.getName().toLowerCase();

	 for (int i = 0; i < extensions.size(); i++)
	 if (name.endsWith((String)extensions.get(i)))
	 return true;
	 return false;
	 }
	 
	}