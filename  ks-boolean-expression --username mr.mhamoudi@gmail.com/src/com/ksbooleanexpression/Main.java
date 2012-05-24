/*
# KS Boolean Expression, Copyright (c) 2012 The Authors. / ks_contributors@gmail.com
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
 * Classe principale 
 *
 */
public class Main implements View {
	
	
	/**
	 * Lance l'application
	 *
	 */
	public static void main(String[] args) {
	    
				try {
					Program window = new Program();
					window.getController().enableDefaultActions(window);
					if(args.length>=1 )
					{
					   window.getController().open(args[0]);
					   window.getController().simplify(SolutionType.DETAILLED_SOLUTION);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
	}
}
