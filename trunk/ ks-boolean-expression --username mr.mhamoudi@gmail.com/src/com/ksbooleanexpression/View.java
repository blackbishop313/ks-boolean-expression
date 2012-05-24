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

/**
 * Contient une liste d'ennumerations<br>
 * pour les nom des constantes.
 * @author Meradi
 *
 */
public interface View {
	
	/**
	 * Noms des Actions des différents menus
	 *
	 */
	public enum MenuActionType {FILE_MENU, EDIT_MENU, ADD_MENU, SIMPLIFY_MENU, HELP_MENU,
                                NEW_TABLE_MENU, NEW_FUNCTION_MENU}
	/**
	 * Noms des Actions des éléments des menus et boutons.
	 */
    public enum ActionType {NEW, OPEN,SAVE,SAVE_AS,PREFERENCES,PRINT,EXIT,CLOSE, UNDO, REDO,
                        CUT,COPY,PASTE,SELECT_ALL,DELETE, TRUTH_TABLE, KARNAUGH_MAP,
                        NEW_FUNCTION,ALGEBRIC_FORM,NUMERIC_FORM,MINIMIZED_FUNCTION,
                        DETAILED_SOLUTION,HELP,ABOUT,AND, OR, NOT, NAND, NOR, XOR, IMPORT,EXPORT}
    /**
     * Noms des messages d'erreurs
     */
    public enum MessageType {NO_ERREUR,START_OR_END_WITH_AN_OPERATOR, FALSE_CHARACTER,
                             TOO_MUCH_VARIABLES,MISSING_BRACKETS, SAVE_CHANGES, OPERATOR_CONFLICT}
    /**
     * Constantes pour le type de solution
     */
    public enum SolutionType {DETAILLED_SOLUTION,MINIMIZED_FUNCTION}
    /**
     * Constantes pour désigner une expression, une table<br>
     * de vérité ou bien une table de Karnaugh.
     */
    
    public enum Type {FUNCTION,TRUTH_TABLE,KARNAUGH_MAP}
}
