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

import java.util.HashMap;

/**
 * Evalueur d'expressions logiques:<br>
 * Permet d'évaluer des expressions Booléennes. Les variables doivent être écrites en majuscule.<br>
 * Gere les opérateurs de base ainsi que les opérateurs Universel: NAND, NOR et XOR.<br>
 * <br>
 * La Syntax:<br>
 * 
 * NOT--> \b!
 * AND--> \b*
 * OR--> \b+
 * NAND--> \b|
 * NOR--> \b/
 * XOR--> \b^
 *
 * <br>
 * Pour l'ajout de nouvelle variable on utilise addVar.<br>
 * Si l'éxpression à évaluer ne comporte que les opérateurs de bases qui sont: AND, OR, NOT<br>
 * on a le choix d'utiliser ou non des parenthèses.<br>
 * Si l'éxpression comporte des opérateurs tels NAND, NOR, ou bien XOR alors on sera contraint<br>
 * d'utliser des parenthèses car il n'existe pas de priorité entre tous ces opérateur réunis.<br>
 * EXEMPLE:<br>
 * on a le choix d'écrire A+B*C ou bien A+(B*C)<br>
 * Mais si on a A^B+C|B======>Erreur, il faut écrire:<br>
 * par exemple: (A^B)+(C|B), A^((B+C)|B)...<br>
 * 
 * Le source est inspiré du \b MINI-EVALUATEUR-EXPRESSION-ARITHMETIQUES de \b Guillaume Bouchon<br>
 * disponible sur le site http://www.javafr.com/.<br>
 *  
 * 
 * @author Hamoudi
 * 
 */
 public class Evalueur
{
    
    public static final byte ERROR_NO=0;
    public static final byte ERROR_BAD_SYNTAX=1;
    public static final byte ERROR_UNKNOW_VAR=2;
    public static final byte ERROR_UNKNOW_FUNCTION=3;
    private String  expr;
    private int     pos=-1;
    private char    lu;
    private HashMap<String,Double>  vars = new  HashMap<String, Double>();
    private byte     erreur=ERROR_NO;
    private double stack[]=new double[100];
    private Fonction    stackF[]=new    Fonction[100];
    private int stack_pos=stack.length-1;
    private int stackF_pos=stackF.length-1;
    
    /**
     * Constructeur
     */
    public Evalueur()
    {
        
    }
    
    /**
     * empile une foction (empile au sommet)
     */
    private void    empileF(Fonction v)
    {
        if (stackF_pos==0)   {erreur=ERROR_BAD_SYNTAX; return;}
        stackF[stackF_pos]=v;
        stackF_pos--;
    }
    
    
    /**
     * empile une valeur (empile au sommet)
     */
    private void    empile(double v)
    {
        if (stack_pos==0)   {erreur=ERROR_BAD_SYNTAX; return;}
        stack[stack_pos]=v;
        stack_pos--;
    }
    
    /**
     * depile une valeur de la pile (donne la valeur du sommet de la pile)
     */
    private double     depile()
    {
        if (stack_pos==stack.length-1)   {erreur=ERROR_BAD_SYNTAX; return 0;}
        stack_pos++;
        return  stack[stack_pos];
    }
    
    /**
     * evalue une expression
     * @param expr l'expression a évaluer (sans espaces)
     * @return le resultat de l'expression (vérifier que getError() ne renvoi pas une erreur)
     */
    public double  evalue(String expr)
    {
        erreur=ERROR_NO;
        this.expr=expr;
        //se remplace en haut de la pile
        stack_pos=stack.length-1;
        stackF_pos=stackF.length-1;
        stack[stack.length-1]=0;
        stackF[stack.length-1]=null;
        
        //au debut de l'expression
        pos=-1;
        avance();
        
        //lance l'automate
        Sprime();
        
        //execute sur la pile de fonctions
        
        for(int i=stackF.length-1;i>stackF_pos && stackF[i]!=null;i--)
        {
           
            stackF[i].evalue();
        }
        
        //le sommet de la pile contient le resultat (si pas d'erreur!)
        return  stack[stack.length-1];  
    }
    
    /**
     * reévalu l'expression en cours (permet d'optimiser car ne regenere pas toute l'expression)<br>
     * prends en compte les changements de valeurs des variables<br>
     * Utilisation :<br>
     * on genere l'expression avec evalue la 1ere fois<br>
     * ensuite on peut utiliser reevalue si l'expression n'a pas changé (les variables peuvent avoir changés)<br>
     */
    public  double  reevalue()
    {
        //se remplace en haut de la pile
        stack_pos=stack.length-1;
        stack[stack.length-1]=0;
        
         //execute sur la pile de fonctions
        
        for(int i=stackF.length-1;i>stackF_pos && stackF[i]!=null;i--)
        {
            
            stackF[i].evalue();
        }
        
        //le sommet de la pile contient le resultat (si pas d'erreur!)
        return  stack[stack.length-1];  
    }
    
    /**
     * avance dans l'expression
     */
    private void    avance()
    {
        lu=' ';
        while(lu==' ')  //enleve tous les espaces
        {
            pos++;
            if (pos>=expr.length()) 
                lu=0;
            else
                lu=expr.charAt(pos);
        }
    }
  
    /**
     * S'->E
     */
    private void Sprime()
    {
        if (erreur!=ERROR_NO)   return;
        E();
        if (lu!=0)  //pas bon
            erreur=ERROR_BAD_SYNTAX;
    }
    
    /**
     * E->TE'
     */
    private void    E()
    {
        if (erreur!=ERROR_NO)   return;
        
        if (lu=='(' || (lu>='A' && lu<='Z'))
        {
            T(); Eprime();
        }else
        	{
        	  if ( lu=='!') Tprime();
        	  else
        	  erreur=ERROR_BAD_SYNTAX;
        	}
    }
    
    /**
     * E'->+TE' 
     */
    private void Eprime()
    {
        if (erreur!=ERROR_NO)   return;
        
        if (lu=='+')
        {
            avance(); T(); empileF(new FonctionOr()); Eprime();
            
        }
        else
        {
        	if(lu=='|')
        		{
        			avance(); T(); empileF(new FonctionNand()); Eprime();
        		}
        	else
        	{
        	if(lu=='/')
        	{
        		avance(); T(); empileF(new FonctionNor()); Eprime();
        	}
        	}
        }
    }
    
    
    /**
     * T->FT'
     */
    private void T()
    {
        if (erreur!=ERROR_NO)   return;
        
        if (lu=='(' || (lu>='A' && lu<='Z'))
        {
            F(); Tprime();
        }else{
      	  if ( lu=='!') Tprime();
      	  else
      	  erreur=ERROR_BAD_SYNTAX;
      	}
    }
    
    
    
    /**
     * T'->*FT'
     */
    private void Tprime()
    {
        if (erreur!=ERROR_NO)   return;
        if (lu=='*')
        {
            avance(); if(lu=='!') Tprime(); F();
            empileF(new FonctionAnd()); Tprime();
            
        }
        else
        {
        		if(lu=='^')
        	{
        		avance(); F(); empileF(new FonctionXor()); Tprime();
        	}
				
        		else
        		{
        		if(lu=='!')
        		{
        		avance(); F(); empileF(new FonctionNot()); Tprime();
        		}
        	}
        }
                
    }
    
    
    
    /**
     * F->id | (E)
     */
    @SuppressWarnings("static-access")
	private void F()
    {
        if (erreur!=ERROR_NO)   return;
       if ((lu>='A' && lu<='Z'))  //une variable
        {
            String nomv=""+lu;
            avance();
            while((lu>='A' && lu<='Z'))
            {
                nomv=nomv+lu;
                avance();
            }
            if (lu!='(')    //c'est une variable
            {
                //cherche la variable
                Double var=vars.get(nomv);
                if (var==null)
                    erreur=this.ERROR_UNKNOW_VAR;
                else
                    empileF(new FonctionVar(nomv));
            }   
        }
        else 
        	if (lu=='(')
        {
            avance();
            E(); 
            if (lu==')')
                avance();   
            	else erreur=ERROR_BAD_SYNTAX;        
             }else erreur=ERROR_BAD_SYNTAX;        
    }
    

    
    
    /**
     * donne l'erreur
     */
    public byte getErreur()
    {
        return erreur;
    }
    
    /**
     * ajout d'une variable ou modification de sa valeur
     */
    public  void    addVar(String nom,double value)
    {
        vars.put(nom,value);
    }

    /**
     * donne les variables
     */
    public HashMap<String, Double> getVars()
    {
        return vars;
    }

    /**
     * defini les variables
     */
    public void setVars(HashMap<String, Double> vars)
    {
        this.vars = vars;
    }
    
    /**
     * une fonction dans la pile
     */
    private abstract class Fonction
    {
        public abstract void evalue();
    }
    
       
    /**
     * variable
     */
    private class FonctionVar extends Fonction
    {
        private String  var;
        
        public  FonctionVar(String var)
        {
            this.var=var;
        }
        public void evalue()
        {
            empile(vars.get(var));  //valeur de la variable
        }
        
        public String toString() { return var;}
    }
    
    /**
     * Fonction OR
     */
    private class FonctionOr extends Fonction
    {
         public void evalue()
        {
            empile(depile()+depile());
        }
         public String toString() { return "+";}
    }
    
        
    /**
     * Fonction AND
     */
    private class FonctionAnd extends Fonction
    {
         public void evalue()
        {
            double a=depile(),b=depile();
            empile(b*a);
        }
         
         public String toString() { return "*";}
    }
    
    /**
     * Fonction NAND
     */
    private class FonctionNand extends Fonction
    {
    	public void evalue()
    	{
    		double a=depile(), b=depile();
    		if((a*b)>=1) empile(0);
    		else empile(1);    	
    	}
    	
       public String toString() {return "|";}
    }
    
    /**
     * Fonction NOR
     */
    private class FonctionNor extends Fonction
    {
    	public void evalue()
    	{
    		double a=depile(), b=depile();
    		if((a+b)>=1) empile(0);
    		else empile(1);
    	}
    	public String toString() {return "/";}
    	
    }
    
    /**
     * Fonction XOR
     */
    
    private class FonctionXor extends Fonction
    {
    	public void evalue()
    	{
    		double a=depile(), b=depile();
    		if(a>=1 && b>=1 || (a<1 && b<1)) empile(0);
    		else
    		{
    			if(a>=1 && b<1 || (a<1 && b>=1)) empile(1);
    		}
    	}
    	public String toString() {return "^";}
    }
    
    /**
     * Fonction NOT
     */
    private class FonctionNot extends Fonction
    {
    	public void evalue()
    	{
    		double a = depile();
    		if(a>=1) empile(0);
    		else {if (a<1) empile(1);}
    	}
    	public String toString() {return "!";}
    }
    
    
 }