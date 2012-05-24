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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * An action which <code>actionPerformed</code> method 
 * will call a parametrizable method. 
 * @author Emmanuel Puybaret
 */
@SuppressWarnings("serial")
public class ControllerAction extends ResourceAction {
  private final Object    controller;
  private final Method    controllerMethod;
  private final Object [] parameters;

  /**
   * Créer une action dont les proriété seront lus depuis un fichier resources 
   * @param preferences    preferences les préférences de l'utilisateur pour savoir 
   *                       en quelle langue lire les proriétés
   * @param actionPrefix préfix utilisé pour retrouver les propriétés de l'action
   * @param controller   le controlleur dans lequel se trouve la méthode à appeller
   * @param method       le nom de la méthode à appeller et qui se trouve dans le controlleur
   * @param parameters la liste des paramètres de <code>method</code>
   * @throws NoSuchMethodException si <code>method</code> avec les paramètres
   *               <code>parameters</code> n'existe pas
   */
  public ControllerAction(UserPreferences preferences, 
                          Class<?> resourceClass, 
                          String actionPrefix, 
                          Object controller, 
                          String method, 
                          Object ... parameters) throws NoSuchMethodException {
    this(preferences, actionPrefix, false, controller, method, parameters);
  }

  /**
   * Créer une action dont les proriété seront lus depuis un fichier resources 
   * @param preferences   preferences les préférences de l'utilisateur pour savoir 
   *                       en quelle langue lire les proriétés
   * @param actionPrefix préfix utilisé pour retrouver les propriétés de l'action
   * @param enabled <code>true</code> si l'action doir être activé à la création .
   * @param controller    le controlleur dans lequel se trouve la méthode à appeller
   * @param method       le nom de la méthode à appeller et qui se trouve dans le controlleur
   * @param parameters la liste des paramètres de <code>method</code>
   * @throws NoSuchMethodException si <code>method</code> avec les paramètres
   *               <code>parameters</code> n'existe pas
   */
  public ControllerAction(UserPreferences preferences, 
                          String actionPrefix,
                          boolean enabled,
                          Object controller, 
                          String method, 
                          Object ... parameters) throws NoSuchMethodException {
    super(preferences, actionPrefix, enabled);
    this.controller = controller;
    this.parameters = parameters;
    Class<?> [] parametersClass = new Class [parameters.length];
    for(int i = 0; i < parameters.length; i++)
      parametersClass [i] = parameters [i].getClass();
    
    this.controllerMethod = controller.getClass().getMethod(method, parametersClass);
  }

  /**
   * Appeler la méthode qui se trouve dans le controlleur.
   */
  @Override
  public void actionPerformed(ActionEvent ev) {
    try {
      this.controllerMethod.invoke(controller, parameters);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException (ex);
    } catch (InvocationTargetException ex) {
      throw new RuntimeException (ex);
    }
  }
}
