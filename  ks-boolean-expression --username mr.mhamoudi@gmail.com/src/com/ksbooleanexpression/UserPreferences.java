package com.ksbooleanexpression;

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


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;



/**
 * Une classe pour sauvegarder les préférences de l'utilisateur
 * @author Meradi
 *
 */
public class UserPreferences {
	private static final String LANGUAGE                              = "language";
	private static final String LOOK_AND_FEEL                         = "lookAndFeel";
	private static final String SOLUTION_TYPE                         = "solutionType";
	private static final String HEIGHT                                = "height";
	private static final String WIDTH                                 = "width";
	private static final String MAXIMAZED                             = "maximazed";
	private PropertyChangeSupport          propertyChangeSupport;
	public String language;
	public String lookAndFeel;
	public boolean detailledSolution;
	public boolean maximazed;
	public int height;
	public int width;
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	public static Preferences preferences = Preferences.userNodeForPackage(UserPreferences.class);
	
	
	public UserPreferences(){
		 readPreferences();
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
	}
	
	/**
	 * Lire les préférences de l'utilisateur
	 */
	public void readPreferences(){
		language = preferences.get("language", "en");
		setLookAndFeel(preferences.get("lookAndFeel","com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"));
		detailledSolution = (preferences.getBoolean("solutionType", true));
		maximazed = preferences.getBoolean("maximazed", false);
		height = preferences.getInt("height", 0);
		width = preferences.getInt("width", 0);
		Locale locale = new Locale(language);
		Locale.setDefault(locale);
		Tools.updateSwingResourceLanguage();
	}
	
	
	
	/**
	 * Mettre à jour Locale lors de changement de la langue
	 */
	private void updateDefaultLocale() {
	      Locale.setDefault(new Locale(this.language));
	  }


	/**
	 * Affecter le look and feel
	 * @param string
	 */
	public void setLookAndFeel(String newLookAndFeel) {
			      this.lookAndFeel = newLookAndFeel;      	
		
	}


	/**
	 * Affecter le langue et avertir les écouteurs s'il y a 
	 * un changement de langue
	 * @param string
	 */
	public void setLanguage(String newLanguage) {
		if(this.language !=null){
			if (!newLanguage.equals(this.language)) {
			      String oldLanguage = this.language;
			      this.language = newLanguage;      
			      updateDefaultLocale();
			      Tools.updateSwingResourceLanguage();
			      this.pcs.firePropertyChange("language", oldLanguage, newLanguage);
			    }
		}
		else this.language = newLanguage;
		    
		  }
		

    /**
     * 
     * @return le look and feel utilisé par l'application
     */
	public String getLookAndFeel(){
		UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
		for (LookAndFeelInfo laf : lafs){
			if(laf.getName() == lookAndFeel) return laf.getClassName();
		}
		return lookAndFeel;
		
	}
	 /**
	  * 
	  * @return la liste des langues disponible dans cette application
	  */
	public String [] getSupportedLanguages(){
		ResourceBundle resource = ResourceBundle.getBundle("translations//UserPreferences");
		
		String [] displayLanguages = resource.getString("supportedLanguages").split("\\s");
		return displayLanguages;
	}
	
	/**
	 * @return le nom de langue à partir de son code ISO
	 */
	public String getLanguageName(){
		Locale locale = new Locale(language);
		return Character.toUpperCase(locale.getDisplayLanguage().charAt(0)) + locale.getDisplayLanguage().substring(1);
	}
	
	
	/**
	 * @return Le langue en cours d'utilisation 
	 */
	public String getLanguage(){
		return this.language;
	}



	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}



	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}



	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}



	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}



	/**
	 * @param maximazed the maximazed to set
	 */
	public void setMaximazed(boolean maximazed) {
		this.maximazed = maximazed;
	}



	/**
	 * @return the maximazed
	 */
	public boolean isMaximazed() {
		return maximazed;
	}



	/**
	 * Sauvegarder les préférences de l'utilisateur
	 * @throws Exception si les préférences ne peuvent pas être sauvegarder 
	 * 
	 */
	public void savePreferences() throws Exception {
		preferences.put(LANGUAGE, this.language);
		preferences.put(LOOK_AND_FEEL,this.lookAndFeel);
	    preferences.putBoolean(SOLUTION_TYPE, this.detailledSolution);
	    preferences.putBoolean(MAXIMAZED, maximazed);
	    preferences.putInt(HEIGHT, height);
	    preferences.putInt(WIDTH, width);
	    try {
	        // Write preferences 
	        preferences.flush();
	      } catch (BackingStoreException ex) {
	        throw new Exception("Couldn't write preferences", ex);
	      }
	      }




	  public void addPropertyChangeListener(String language2, 
	                                        PropertyChangeListener listener) {
	    this.propertyChangeSupport.addPropertyChangeListener("language", listener);
	  }



	  /**
	   * Supprimer un écouteur
	   */
	  public void removePropertyChangeListener(String language2, 
	                                           PropertyChangeListener listener) {
	    this.propertyChangeSupport.removePropertyChangeListener("language", listener);
	  }


    /**
     * Retourner le nom de look and feel
     * @param lookAndFeel le look and feel
     * @return le nom de look and feel
     */
	public String getLookAndFeelClassName(String lookAndFeel) {
		UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
		for (LookAndFeelInfo laf : lafs){
			if(laf.getName() == lookAndFeel) return laf.getClassName();
		}
		return null;
	}
	
	
	 /**
	   * Ajouter un écouteur pour notifier les autres classe si la langue change
	   */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

	
	 /**
	   * Supprimer un écouteur
	   */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
}
