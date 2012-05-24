
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

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.event.SwingPropertyChangeSupport;

/**
 * Une action dont les propriété sont lus depuis les fichiers resources.
 * @author Emmanuel Puybaret
 */
public class ResourceAction extends AbstractAction {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public static final String POPUP = "Popup";
  /**
   * Créer une action dont les proriété seront lus depuis un fichier resources 
   * @param actionPrefix  préfix utilisé pour retrouver les propriétés de l'action
   * @param preferences les préférences de l'utilisateur pour savoir en quelle langue lire les proriétés
   */
  public ResourceAction(UserPreferences preferences,  
                        String actionPrefix,
                        Object ... parameters) {
    this(preferences, actionPrefix, false);
  }
  
  /**
   * Créer une action dont les proriété seront lus depuis le fichier resources
   * @param preferences   les préférences de l'utilisateur pour savoir en quelle langue lire les proriétés
   * @param actionPrefix  préfix utilisé pour retrouver les propriétés de l'action
   * @param enabled <code>true</code> si l'action doir être activé à la création .
   */
  public ResourceAction(UserPreferences preferences,  
                        String actionPrefix, 
                        boolean enabled) {
	  readActionProperties(actionPrefix);    
    setEnabled(enabled);
    
    preferences.addPropertyChangeListener(new LanguageChangeListener(this,actionPrefix));
  }
    
  /**
   * Une classe pour écouter si la langue de l'application est changé ou non
   * Si la langue a changé, les propriétés de l'action seront mise à jour dans 
   * la nouvelle langue   
   */
  private static class LanguageChangeListener implements PropertyChangeListener {
    private final WeakReference<ResourceAction> resourceAction;
    private final String                        actionPrefix;

    public LanguageChangeListener(ResourceAction resourceAction,
                                  String actionPrefix) {
      this.resourceAction = new WeakReference<ResourceAction>(resourceAction);
      this.actionPrefix = actionPrefix;
    }

    public void propertyChange(PropertyChangeEvent ev) {
      ResourceAction resourceAction = this.resourceAction.get();
      if (resourceAction == null) {
        ((UserPreferences)ev.getSource()).removePropertyChangeListener("language", this);
      } else {
        resourceAction.readActionProperties(this.actionPrefix);
      }
    }
  }
  
  /**
   * Lire les proriétés de l'action
   */
  private void readActionProperties(String actionPrefix) {
    String propertyPrefix = actionPrefix + ".";
    try {
      putValue(NAME, Tools.getLocalizedLabelText(propertyPrefix + NAME));
    } catch (IllegalArgumentException ex) {
    }
    putValue(DEFAULT, getValue(NAME));
    putValue(POPUP, getOptionalString(propertyPrefix + POPUP));
    putValue(SHORT_DESCRIPTION, 
        getOptionalString(propertyPrefix + SHORT_DESCRIPTION));
    putValue(LONG_DESCRIPTION, 
        getOptionalString(propertyPrefix + LONG_DESCRIPTION));
    
    String smallIcon = getOptionalString(propertyPrefix + SMALL_ICON);
    if (smallIcon != null) {
      putValue(SMALL_ICON, new ImageIcon(Program.class.getResource(smallIcon)));
    }

    String propertyKey = propertyPrefix + ACCELERATOR_KEY;
    String acceleratorKey = getOptionalString(propertyKey + "." + System.getProperty("os.name"));
    if (acceleratorKey == null) {
      acceleratorKey = getOptionalString(propertyKey);
    }
    if (acceleratorKey !=  null) {
      putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(acceleratorKey));
    }
    
    String mnemonicKey = getOptionalString(propertyPrefix + MNEMONIC_KEY);
    if (mnemonicKey != null) {
      putValue(MNEMONIC_KEY, Integer.valueOf(KeyStroke.getKeyStroke(mnemonicKey).getKeyCode()));
    }
    }

  /**
   * Retrourner la valeur de <code>propertyKey</code>, 
   * ou <code>null</code> si la valeur n'existe pas.
   */
  private String getOptionalString(String propertyKey) {
    try {
      String localizedText = Tools.getLocalizedString(propertyKey);
      if (localizedText != null && localizedText !="") return localizedText;
       else {
        return null;
      }
    } catch (IllegalArgumentException ex) {
      return null;
    }
  }

 
  public void actionPerformed(ActionEvent ev) {
    throw new UnsupportedOperationException();
  }
  
  /**
   * Decorateur d'action.  
   */
  private static class AbstractDecoratedAction implements Action {
    private Action action;
    private SwingPropertyChangeSupport propertyChangeSupport;

    public AbstractDecoratedAction(Action action) {
      this.action = action;
      this.propertyChangeSupport = new SwingPropertyChangeSupport(this);
      action.addPropertyChangeListener(new PropertyChangeListener() {
          public void propertyChange(PropertyChangeEvent ev) {
            String propertyName = ev.getPropertyName();
            if ("enabled".equals(propertyName)) {
              propertyChangeSupport.firePropertyChange(ev);
            } else {
              Object newValue = getValue(propertyName);
              if (newValue != null) {
                propertyChangeSupport.firePropertyChange(new PropertyChangeEvent(ev.getSource(), 
                    propertyName, ev.getOldValue(), newValue));
              }
            }
          }
        });
    }

    public final void actionPerformed(ActionEvent ev) {
      this.action.actionPerformed(ev);
    }

    public final void addPropertyChangeListener(PropertyChangeListener listener) {
      this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public Object getValue(String key) {
      return this.action.getValue(key);
    }

    public final boolean isEnabled() {
      return this.action.isEnabled();
    }

    public final void putValue(String key, Object value) {
      this.action.putValue(key, value);
    }

    public final void removePropertyChangeListener(PropertyChangeListener listener) {
      this.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public final void setEnabled(boolean enabled) {
      this.action.setEnabled(enabled);
    }
    
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
      this.propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
  }
  
  /**
   * Decorateur d'action pour les sous menus.  
   */
  public static class MenuItemAction extends AbstractDecoratedAction {
    public MenuItemAction(Action action) {
      super(action);
    }

    public Object getValue(String key) {
      if (Tools.isMacOSX()
          && (key.equals(MNEMONIC_KEY)
              || key.equals(SMALL_ICON))) {
        return null;
      }
      return super.getValue(key);
    }
  }
  

  /**
   * Decorateur d'action pour les boutons de la barre d'outils  
   */
  public static class ToolBarAction extends AbstractDecoratedAction {
    public ToolBarAction(Action action) {
      super(action);
    }

    public Object getValue(String key) {
      // Ignore NAME in tool bar 
      if (key.equals(NAME)) {        
        return null;
      } 
      return super.getValue(key);
    }
  }

  /**
   *  Decorateur d'action pour les boutons.  
   */
  public static class ButtonAction extends AbstractDecoratedAction {
    public ButtonAction(Action action) {
      super(action);
    }

    public Object getValue(String key) {
      if (Tools.isMacOSX()
          && key.equals(MNEMONIC_KEY)) {
        return null;
      }
      return super.getValue(key);
    }
  }
}
