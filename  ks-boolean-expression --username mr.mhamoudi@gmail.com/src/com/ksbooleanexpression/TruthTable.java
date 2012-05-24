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

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Permet d'avoir la table de vérité d'une fonction<br>
 * à partir de son expression.
 * @author Mounir Hamoudi
 *
 */


public class TruthTable {
	
	private String exprBool; 
	private ArrayList <String> variables;
	public int TruthTable[]; 
	private String ordrVar=""; 
	
	/**
	 * Constructeur
	 * @param expression l'expression de la fonction
	 */
	public TruthTable(String expression)
	    {
		this.setBooleanExpression(expression);
		addAndOperator();
		addBrackets();
		this.getTruthTable();
	    }
	 
	
	/**
	 * Constructeurs par défaut
	 */
	public TruthTable() {
	}


	/**
	 * Ajoute l'opérateur logique ET dans les emplacement<br>
	 * ou il en manque. exemple: AB--> A*B
	 */
	
	private void addAndOperator()
	{
		char ch[] = exprBool.toCharArray();
		String newExpreBool = "";
		
		for(int i=0; i<ch.length-1; i++)
		{
			
			if((ch[i]<='Z' && ch[i]>='A' && ch[i+1]<='Z' && ch[i+1]>='A') || (ch[i]<='Z' && ch[i]>='A' && ch[i+1]=='(') 
					|| (ch[i]==')' && ch[i+1]<='Z' && ch[i+1]>='A') || (ch[i]==')' && ch[i+1]=='('))
				newExpreBool = newExpreBool+ ch[i]+'*';
			
			else newExpreBool = newExpreBool+ ch[i];	
			
		}
		newExpreBool = newExpreBool+ch[ch.length-1];
		this.exprBool = newExpreBool;
	}
	
	
	/**
	 * Ajoutes de parenthèses à une expression qui contient<br>
	 * l'opérateurs NON. Exemple: A*!B --> A*(!B).<br>
	 * Sinon l'evaleur risques de donnez de faux résultats
	 */
	private void addBrackets()
	{
		char ch[] = exprBool.toCharArray();
		String newExpreBool = "";
		int pos=-1;
		for(int i=0; i<ch.length-1; i++)
		{
			if(i!=pos){
			if(ch[i]=='!' && ch[i+1]!='(')
			{
				newExpreBool = newExpreBool+'('+ch[i]+ch[i+1]+')';
				pos =i+1;
			}
			else newExpreBool = newExpreBool+ch[i];
				
				}
		}
		if(pos!=ch.length-1)
		newExpreBool = newExpreBool+ch[ch.length-1];
		this.exprBool = newExpreBool;
		
	}
	
	
	
	
	/**
	 * Donne la table de vérité après évaluation de l'éxpression booléenne<br>
	 * La table de vérité est donné sous forme d'un vecteur à 2 puissance le nombre<br>
	 * de variables.<br>
	 * La case 0 correspond à l'état ou toutes les variables sont à 0 (Faux), et<br>
	 * ainsi de suite... <br>
	 */
	private void getTruthTable()
	    {
	     variables= new ArrayList<String>();
	     Evalueur ev= new Evalueur();
	     //on extrait toutes les variables de la fonction
	     getVariables();
	     triVariables();
	     
	     //on initialise la table de vérité
	     TruthTable= new int[(int) Math.pow(2, variables.size())];
	     byte lesBit[]= new byte[variables.size()];
	     for(int i=0; i<lesBit.length; i++)
			{
	    	 lesBit[i]=0;
			}
	     //on remplit la table de vérité
	     for(int j=0; j<TruthTable.length; j++)
	     {
	    	 //on donne les valeurs aux variables et on les ajoute à l'evalueur
	    	 //par exemple pour j=0, on obtiendra pour 3 var: lesBit={0, 0, 0}
	    	 decimalToBinary(j, lesBit, variables.size());
	    	 int k=0;
	    	 for(int i=0; i<variables.size(); i++)
		     {
		    	ev.addVar(variables.get(i), (double)lesBit[k]);
		    	k++;
		     }
	    	 
	    	 double r=ev.evalue(exprBool); //on evalue l'expression avec ces valeurs
	    	 if(r>=1) TruthTable[j]=1;
	    	 else     TruthTable[j]=0;       //on met le resultat dans la table de vérité
	     }
	    }
	     
	     
		 
	/**
	 * Permet d'éxtraire les variables d'une expression booléenne<br>
	 * donnée.<br>
	 * Les variables sont ensuite placée dans la table des variables<br>
	 * On garde leur ordre dans une chaine de type String.<br>	 
	 */
	
	private void getVariables()
	{
		 if(variables==null) variables= new ArrayList<String>();
		 for(int i=0 ; i<exprBool.length(); i++)
		 {
			 String var = "";
			 var= var+ exprBool.charAt(i);
			
			if((var.compareTo("(")!=0) && (var.compareTo(")")!=0 ) && 
				(var.compareTo("*")!=0) && (var.compareTo("+")!=0) &&
				(var.compareTo("|")!=0) && (var.compareTo("/")!=0) &&
				(var.compareTo("^")!=0) && (var.compareTo("!")!=0))
			 {
				if(!alreadyGot(var)) {variables.add(var); ordrVar= ordrVar+var;} 
			 }
			 }	 
	}
		 

    /**
     * Permet de verifier si une variable est déjà extraite<br>
     * @param var la variable dont on veut vérifier l'existence dans <br>
     * le tableau des variables<br>
     * @return true si elle a été déjà extraite<br>
     */
	private boolean alreadyGot (String var)
	{
		return variables.contains(var);
	}
	
	
	
	
	/**
	 * Permet de convertir un nombre du décimal au binaire<br>
	 * Le résultat est donné sous forme d'un tableau de byte<br>
	 * @param n le nombre à convertir vers la binaire<br>
	 * @param lesBit tableau qui contiendera les bit du nombre obtenu<br>
	 * @param nbrVar le nombre de bit sur lesquel doit être écrit le résultat<br>
	 */
	private void decimalToBinary(int n, byte lesBit[], int nbrVar)
	{
      	if(n!=0){
		while(n!=0)
		{
			lesBit[nbrVar-1]=(byte) (n % 2);
			n= n/2;
			nbrVar--;
		}
      	}	
	}
	
	
	/**
	 * Initialise l'éxpression bolléenne à évaluer<br>
	 * @param exprBool l'éxpression booléenne, de type String<br>
	 */
	
	private void setBooleanExpression(String exprBool)
	{
		this.exprBool=exprBool;
	}
	
	
	/**
	 * Retourne l'ordre des variables avec lequel a été <br>
	 * évalée l'éxpression boolénne<br>
	 * @return ordrVar l'ordre des variables, de type String<br>
	 */
	public String getOrdrVar()
	{
		return this.ordrVar;
	}
	
	/**
	 * Tri les variables extraites de l'expression booléenne<br>
	 * comme ça l'expression sera evalué en suivant l'ordre<br>
	 * alphabetique des variables.<br>
	 */
	public void triVariables()
	{
		String ch[] = new String[variables.size()];
		for(int i=0;i<variables.size();i++){
			ch[i]=variables.get(i);
		}
		Arrays.sort(ch);
		variables = null;
		ordrVar = "";
		variables = new ArrayList<String>();
		for(int i=0;i<ch.length;i++){
			variables.add(ch[i]);
			ordrVar = ordrVar+ch[i];
		}
		
	}

}



