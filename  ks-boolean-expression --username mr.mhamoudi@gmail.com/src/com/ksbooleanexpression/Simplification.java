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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Permet la simplification d'une fonction logique<br>
 * La fontion peut être donnée sous diverses formes: <br>
 * expression, tables de vérité, table de Karnaugh, et<br>
 * forme numérique. <br>
 * @author Rabah Meradi, Mounir Hamoudi
 *
 */

public class Simplification implements View {
	
	private String[] functions;
	private String [] operateurs = {"+","*","!","^","/","|"};
	private int nombreVars ;
	private TruthTable truthTable;
	private KarnaughTable kmap;
	private ArrayList<KarnaughNode> arrayList;
	private String minimisedExpression;
	private String ordrVar;
	private static char NOT = '!';
	private static char AND = '*';
	private static char OR  = '+';
	private static char XOR = '^';
	private static char NAND= '|';
	private static char NOR = '/';
	private String s;
	private boolean detailledSolution;
	private Controller controller;
		                           
	/**
	 * Constructeur
	 * @param strings les fonctions à simplifiées
	 * @param solutionType le type de solution
	 * @param controller controller de l'application
	 */
	public Simplification(String[] strings,SolutionType solutionType, Controller controller){
		s="";
		if (solutionType.name().compareTo("DETAILLED_SOLUTION")==0)
		this.detailledSolution = true;
		else this.detailledSolution=false;
		functions = strings;
		s +=Tools.intialize();
		s +=Tools.writeIntroducedFunctions(strings);
		this.controller = controller;
		
		
		
		}
	/**
	 * Constructeur
	 * @param ttable table de vérité de la fonction
	 * @param nbrVar le nombre de variables
	 * @param ordrVar l'ordre des variables
	 * @param controller controller de l'application
	 */
	
	public Simplification(int ttable[], int nbrVar, String ordrVar, Controller controller){
	
		this.controller = controller;
		truthTable = new TruthTable();
		truthTable.TruthTable = ttable;
		nombreVars=nbrVar;
		s="";
		s +=Tools.intialize();
		kmap = new KarnaughTable(ttable, nbrVar);
		this.ordrVar = ordrVar;
		}
	
		/**
		 * Constructeurs
		 * @param kmap la table de Karnaugh
		 * @param nbrVar le nombre de variables
		 * @param ordrVar l'ordre des variables
		 */
	public Simplification(int kmap[][], int nbrVar, String ordrVar, Controller controller){
		
		this.controller = controller;
		nombreVars=nbrVar;
		this.ordrVar = ordrVar;
		s="";
		s +=Tools.intialize();
		this.kmap = new KarnaughTable(kmap,nombreVars);
		
		}
	
	
	/**
	 * Permet de lancer la simplification
	 * @param type forme de la fonction à simplifier.
	 */
	public void launchSimplification(Type type) {
		int i=1;
		switch (type){
		case FUNCTION :
		    for(String function : functions){
			MessageType message = validate(function);
			if (message==MessageType.NO_ERREUR)
			{
				if(function.length()==1){
					this.s +=Tools.writeSolution(function, i);
					i++;
				}
		 		else{
			TruthTable truthTable = new TruthTable(function);
			nombreVars=truthTable.getOrdrVar().length();
			ordrVar = truthTable.getOrdrVar();
			kmap = new KarnaughTable(truthTable.TruthTable,nombreVars);
			solve();
			solve2();
			truthTable = new TruthTable(function);
			nombreVars=truthTable.getOrdrVar().length();
			ordrVar = truthTable.getOrdrVar();
			kmap = new KarnaughTable(truthTable.TruthTable,nombreVars);
			
			this.s +=Tools.writeSolution(minimisedExpression, i);
			if(detailledSolution && nombreVars!=1) this.s +=Tools.writeKarnaughMap(kmap, nombreVars);
			if(functions.length==1 && detailledSolution && nombreVars!=1 ) this.s +=Tools.writeTruthTable(truthTable, nombreVars,ordrVar);
			
			i++;
			}
			}
			else 
				{
				this.s+=Tools.writeError(i, message);
				i++;
				}
		    }
		    break;
		case TRUTH_TABLE :
			String function[] = new String[1];
			function[0] = getFunctionFromTruthTable(truthTable.TruthTable, nombreVars);
			controller.program.getMainPanel().getTextField().setText(function[0]);
			s += Tools.writeIntroducedFunctions(function);
			kmap = new KarnaughTable(truthTable.TruthTable,nombreVars);
			solve();
			int nbVars = nombreVars;
			String ordreVars = ordrVar;
			TruthTable t = truthTable;
			solve2();
			truthTable = t;
			nombreVars=nbVars;
			ordrVar = ordreVars;
			kmap = new KarnaughTable(truthTable.TruthTable, nombreVars);
			s +=Tools.writeSolution(minimisedExpression, 1);
			s +=Tools.writeKarnaughMap(kmap, nombreVars);
			s +=Tools.writeTruthTable(truthTable, nombreVars,ordrVar);
			break;
			
		case KARNAUGH_MAP :
			String func[] = new String[1];
			func[0] = functionFromKmap(nombreVars);
			controller.program.getMainPanel().getTextField().setText(func[0]);
			s += Tools.writeIntroducedFunctions(func);
			solve();
			int nbVar = nombreVars;
			String varOrder = ordrVar;
			KarnaughTable kt =kmap;
			solve2();
			kmap = kt;
			nombreVars = nbVar;
			ordrVar = varOrder;
			s +=Tools.writeSolution(minimisedExpression, 1);
			s +=Tools.writeKarnaughMap(kmap, nombreVars);
			break;
		}
		writeSolution();
	}
	

	/**
	 * Refait la simplification de la la première<br>
	 * forme réduite obtenue pour optimiser la solution.
	 */
	private void solve2() {
		String function = minimisedExpression;
		truthTable = new TruthTable(minimisedExpression);
		nombreVars=truthTable.getOrdrVar().length();
		ordrVar = truthTable.getOrdrVar();
		kmap = new KarnaughTable(truthTable.TruthTable,nombreVars);
		solve();
		if(function.compareTo(minimisedExpression)!=0)
			solve2();
		
	}
	/**
	 * Ecrit la solution en HTML dans un fichier temporaire.
	 */

	private void writeSolution() {
		File file = new File(Tools.getApplicationFolder()+"temp/temp.html");
		file.deleteOnExit();
		s +="</body></html>";
		try {
			FileWriter fg = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fg);
			bw.write(s);
			bw.close();
		} catch (IOException e) {
			System.err.println( e.getMessage() );}	
		
	}
	
	/**
	 * Permet de récupérer l'expression de la fonction<br>
	 * à partir de la table de vérité de celle-ci.
	 * @return l'expression de la fonction.
	 */
	private String getFunctionFromTruthTable(int truthT[], int nbrVar) {
		String function ="";
		int k = 65;
		for (int i=0;i<truthT.length;i++){
			k =65;
			if (truthT[i] ==1){
			for (int j=0;j<nbrVar;j++){
                if ((i%((int)Math.pow(2, nbrVar-j)))<((int)Math.pow(2, nbrVar-j-1)))
					   function += "!" +String.valueOf((char) k)+"*";
				else   function += String.valueOf((char) k)+"*"; 
                k++;
			}
			function = function.substring(0,function.length()-1);
            function += "+";
			}
		}
		function = function.substring(0,function.length()-1);	
		return function;
	}
	
	
	/**
	 * Permet de récupérer l'expression de la fonction<br>
	 * à partir de la table de Karnaugh de celle-ci.
	 * @param nbrVar le nombre de variables
	 * @return l'expression de la fonction
	 */
	
	public String functionFromKmap(int nbrVar)
	{
		int truthTable[] = new int [kmap.getRow()*kmap.getColumn()];
		for(int i=0; i<kmap.getRow(); i++)
		{
			for(int j=0; j<kmap.getColumn(); j++)
				truthTable[kmap.getCellAdress(i, j)]=kmap.getCellValue(i, j);
		}
		String func = getFunctionFromTruthTable(truthTable, nbrVar);
		return func;
		
	}
	
	
	/**
	 * Permet de valider l'expression introduite<br>
	 * en cas d'erreur elle retourne le type d'erreur.
	 * @param function l'expression de la fonction
	 */
	public MessageType  validate(String function) {
		for (String s : operateurs){
			if (function.endsWith(s)){
				return MessageType.START_OR_END_WITH_AN_OPERATOR;
			}
		}
		for (String s : operateurs){
			if(s=="!") continue;
			if (function.startsWith(s)){
				return MessageType.START_OR_END_WITH_AN_OPERATOR;
			}
		}
		for(int i=0; i<function.length()-1;i++)
		{  			
			      if   (function.charAt(i) == AND ||
						function.charAt(i) == NOR ||
						function.charAt(i) == NAND ||
						function.charAt(i) == OR ||
						function.charAt(i) == XOR ||
						function.charAt(i) == NOT ){
			    	      if   (function.charAt(i+1) == AND ||
								function.charAt(i+1) == NOR ||
								function.charAt(i+1) == NAND ||
								function.charAt(i+1) == OR ||
								function.charAt(i+1) == XOR)
					return MessageType.OPERATOR_CONFLICT;
				 }

		}
		
		char [] chars = function.toCharArray();
		ArrayList<Character> al = new ArrayList<Character>();
		int n1=0; int n2=0;
		for (char s : chars){
			if (!((s>= 'A' && s<='Z') || ( 
					s == NOT || 
					s == AND || 
					s==  OR  || 
					s== XOR  || 
					s== NAND || 
					s== NOR  || 
					s=='('   || 
					s==')'))) {
				return MessageType.FALSE_CHARACTER;	
			}
			if(s=='(') n1++;
			if (s==')')n2++;
			if ((s>= 'A' && s<='Z') && !(al.contains(s))) {
				al.add(s);
			}
			if (al.size() > 8) {
				return MessageType.TOO_MUCH_VARIABLES;
			}
			
		}
		if(n1!=n2) return MessageType.MISSING_BRACKETS;			
			return MessageType.NO_ERREUR;
			
			
		}
	
	
	/**
	 * Supprime les espaces dans la fonction introduite.
	 * @param s l'expression de la fonction
	 */
	public String removeSpaces(String s){
		char [] data = s.toCharArray();
		s = "";
		for (char c : data){
			if (c != ' ') s = s + String.valueOf(c); 
		}
		return s;
	}
	
	
	
	/**
	 * Fonction qui simplifie l'expression booléenne introduite.
	 */
	
	private void solve()
	{
			if(isAllOnes()){
				minimisedExpression = "1";
			}
			else{
				if(isAllZero()) minimisedExpression = "0";
					
				else{
			arrayList = new ArrayList<KarnaughNode>();
			for (int i=0;i<kmap.getRow();i++)
			{
				for(int j=0;j<kmap.getColumn();j++)
				{
					if(kmap.getCellValue(i, j)==1)
					{
						KarnaughNode n = new KarnaughNode();
						n.flag=false;
						n.numberOfItems=1;
						n.nodes=binaryCode(kmap.getCellAdress(i, j),nombreVars);
						n.addCellAdress(n.nodes);
						arrayList.add(n); 
					}
				}
				
			}
			regroupe();
			removeNeedLessBlocks();
			minimisedExpression();			
		}
			}
	}
		
	/**
	 * Supprime les bloc qui sont utiliser plusier fois<br>
	 * pour former d'autre blocs. Elle permet ainsi d'optimier<br>
	 * la simplification.
	 */
	private void removeNeedLessBlocks() {
		for(int i=0;i<arrayList.size();i++){
			KarnaughNode node = arrayList.get(i);
			if(node.numberOfItems==2){
				if(joinedMoreThanTwice(node.cellesAdress.get(0)) && joinedMoreThanTwice(node.cellesAdress.get(1))){
					arrayList.remove(i);
					i--;
				}
			}
		}
		
	}
	/**
	 * Renvoit <code>true</code> si la cellule dont l'adresse est <code>adress</code><br>
	 * a participé à plus de deux regroupements
	 * @param adress l'adresse de la cellule
	 */
	private boolean joinedMoreThanTwice(String adress) {
		int i=0;
		for(int j=0;j<arrayList.size();j++){
			if(i==2) return true;
			KarnaughNode node = arrayList.get(j);
			if(node.numberOfItems>1){
				for (int k=0;k<node.cellesAdress.size();k++){
					if (node.cellesAdress.get(k).compareTo(adress)==0) i++;
				}
			}
		}
		if(i==2) return true;
		return false;
	}
	/**
	 * Parcourt la liste des groupement de 1 et créee tout le groupement<br>
	 * possible<br>
	 * un groupement peut contenir 2, 4, 8, 16,... éléments (celulles)<br>
	 */
	
	@SuppressWarnings("unchecked")
	private void regroupe() 
	{
			int x = (int)log2(kmap.getColumn()*kmap.getRow());
			ArrayList<KarnaughNode> array = new ArrayList<KarnaughNode>();
			for (int i=1; i<x+1;i++)
			{
				for(int j=0;j<arrayList.size();j++)
				{
					for(int k=0;k<arrayList.size();k++)
					{
						if (arrayList.get(j).numberOfItems== (int) Math.pow(2, i-1) && 
								arrayList.get(k).numberOfItems== (int) Math.pow(2, i-1))
						{
							int y =IsJoinable(arrayList.get(j),arrayList.get(k));
							if(y!=-1)
							{
								String s = getNewBinaryAdress(arrayList.get(j).nodes,
											IsJoinable(arrayList.get(j),arrayList.get(k)));
								KarnaughNode n = new KarnaughNode(s,false,arrayList.get(j).numberOfItems*2);
								n.cellesAdress = getCellesAdress(arrayList.get(j),arrayList.get(k));

								arrayList.get(j).flag=true;
								arrayList.get(k).flag=true;
								if(!alreadyExist(n))
								arrayList.add(n);
								
							}
						}
					}
				}
				
				for (int s=0;s<arrayList.size();s++){
				if (arrayList.get(s).numberOfItems== (int) Math.pow(2, i-1)){
					array.add(arrayList.get(s));
					arrayList.remove(s);
					s--;
				}
				}
			}
			arrayList =  (ArrayList<KarnaughNode>) array.clone();
			deleteUsedBlocs();
	}
		
		
		/**
		 * Combine entre les cellule participants à une regroupement.
		 * @param karnaughNode le premier regroupement.
		 * @param karnaughNode2 le second regroupement.
		 */
	   private ArrayList<String> getCellesAdress(KarnaughNode karnaughNode,
			KarnaughNode karnaughNode2) {
		ArrayList<String> arrayList = new ArrayList<String>();
		for(int i=0;i<karnaughNode.cellesAdress.size();i++){
			arrayList.add(karnaughNode.cellesAdress.get(i));
		}
		for(int i=0;i<karnaughNode2.cellesAdress.size();i++){
			arrayList.add(karnaughNode2.cellesAdress.get(i));
		}
		return arrayList;
	}
	   
	   /**
	    * Verifie si un regroupement existe déjà dans la liste des regroupements<br>
	    * @param n le regoupement dont on veut vérifier l'existence<br>
	    * @return true s'il existe déjà.
	    */
	   private boolean alreadyExist(KarnaughNode n) {
		boolean exist = false;
		   for (int i=0;i<arrayList.size();i++){
			if(arrayList.get(i).nodes.compareTo(n.nodes)==0)
			{
				exist = true; break; 
			}
		}
		return exist;
	}

	   /**
	    * Calcul le log2 d'un nombre donné x<br>
	    * @param x de type double
	    * @return log1(x)
	    */
	    private double log2(double x){
			return Math.log(x)/Math.log(2);
		}
		
		
		
		/**
		 * Retourne l'adresse en binaire d'une celulle
		 * @param cellAdress l'adresse de la celulle en décimal
		 * @param nbrVar le nombre de variable
		 * @return binaryAdress l'adresse en binaire sous forme de chaine
		 */
		private String binaryCode(int cellAdress, int nbrVar)
		{
	      	byte bt[] = new byte[nbrVar];
	      	String binaryAdress = "";
	      	for(int i=0; i<nbrVar; i++) bt[i]=0;
			if(cellAdress!=0){
			while(cellAdress!=0)
			{
				bt[nbrVar-1]=(byte) (cellAdress % 2);
				cellAdress= cellAdress/2;
				nbrVar--;
			}
	      	}
			for(int i=0; i<bt.length; i++) binaryAdress = binaryAdress +bt[i];
			return binaryAdress;
			
		}
		
		
		
		/**
		 * Si on trouve deux regroupement qui peuvent être regroupés<br>
		 * alors on utlise cette fonction pour obtenir la nouvelle adresse<br>
		 * le bit qui change sera remplacé par le chiffre 2.  
		 * @param binAdress1 l'adresse de l'un des deux regroupement
		 * @param pos la position du bit qui diffère. 
		 * @return la nouvelle adresse sous forme de chaine
		 */
		private String getNewBinaryAdress(String binAdress1, int pos)
		{
			String newAdress = "";
			for(int i=0; i<binAdress1.length(); i++) 
			{
				if(i==pos) {newAdress= newAdress+"2"; continue;}
				newAdress = newAdress+binAdress1.charAt(i);
			}
					
			return newAdress;	
		}
		
		
		
		/**
		 * Verifie si deux regoupement peuvent être regoupés pour former<br>
		 * un noueau groupement
		 * @param karnaughNode le regoupement 1
		 * @param karnaughNode2 le regoupement 2
		 * @return reourne true si oui
		 */
		private int IsJoinable(KarnaughNode karnaughNode,
				KarnaughNode karnaughNode2) {
			
		return isOneBitDeferent(karnaughNode.nodes, karnaughNode2.nodes);
		}
		
		/**
		 * Verifie si deux adresse ne contient qu'un seul bit qui est différent<br>
		 * @param binAdress1  l'adresse de la celulle 1
		 * @param binAdress2  l'adresse de la celulle 2
		 * @return retourne -1 si non, et la position ou il diffèrent si oui
		 */
		private int isOneBitDeferent(String binAdress1, String binAdress2 )
		{
			
			int pos=-1; int bitDefer=0;
			
			for(int i=0; i<binAdress1.length(); i++)
			{
				if(binAdress1.charAt(i)!= binAdress2.charAt(i))
				{
					pos = i;
					bitDefer++;
				}
			}
			if(bitDefer!=1) pos=-1;
			return pos;
			
		}
		
		/**
		 * Supprime les regoupement de 1 qui sont utilisés pour former<br>
		 * d'autre regroupement plus grand.
		 */
		
		private void deleteUsedBlocs()
		{
			for(int i=0; i<arrayList.size(); i++)
			{
				if(arrayList.get(i).flag) 
					
					{arrayList.remove(i); i--;}
			}
		}
		
		
		/**
		 * Verifie si toutes les celulle de la table de Karnaugh sont à zéro
		 * @return retourne true si oui, false sinon
		 */
		private boolean isAllZero()
		{
			boolean isAllZero= true;
			for(int i=0; i<this.kmap.getRow(); i++)
			{
				for(int j=0; j<kmap.getColumn(); j++)
				{
					if(kmap.getCellValue(i, j)==1) {isAllZero=false; break;}	
				}
			}
			return isAllZero;	
		}
		
		/**
		 * Verifie si toutes les celulle de la table de Karnaugh sont à un
		 * @return retourne true si oui, false sinon
		 */
		
		private boolean isAllOnes()
		{
			boolean isAllOnes=true;
			for(int i=0; i<this.kmap.getRow(); i++)
			{
				for(int j=0; j<kmap.getColumn(); j++)
				{
					if(kmap.getCellValue(i, j)==0) {isAllOnes=false; break;}	
				}
			}
			return isAllOnes;
		}
		
		/**
		 * Parcourt la liste des regoupement pour écrire l'expression simplifié<br>
		 * sous forme de chaine (String).
		 */
		
		private void minimisedExpression()
		{
			String ch = "";
			for(int i=0; i<arrayList.size(); i++)
			{
				char tabChar[] = arrayList.get(i).nodes.toCharArray();
				for(int j=0; j<tabChar.length; j++)
				{
					if(tabChar[j]=='0') {ch=ch+"!"+ordrVar.charAt(j);
					if(!endOfTerms(arrayList.get(i).nodes, j)) ch=ch+"*";
					}
					else{
						if(tabChar[j]=='1'){ ch=ch+ordrVar.charAt(j);
						if(!endOfTerms(arrayList.get(i).nodes, j)) ch=ch+"*";}
					}
					
				}
				if(i!=arrayList.size()-1) ch=ch+"+";
			}
			minimisedExpression = ch;
		}
		
		/**
		 * Verifie si on est à la fin des terms d'un regroupement
		 * @param ch adresse du regroupement
		 * @param i la position actuelle dans l'adresse du groupement
		 * @return true si oui, false sinon
		 */
		
		private boolean endOfTerms(String ch, int i)
		{
			boolean end=true;
			char tabChar[] = ch.toCharArray();
			for(int j=i+1; j<ch.length(); j++)
			{
				if(tabChar[j]=='1' || tabChar[j]=='0')
				{
					end = false;
					break;
				}
			}
			return end;	
		}
		
		/**
		 * Retourne la fonction (l'expression) simplifiée
		 * @return minimisedExpression l'expression simplifiée (String)
		 */
		
		public String getMinimisedExpression()
		{
			return minimisedExpression;
		}
		
		
		
		
		/**
		 * KarnaughNode Structure<br>
		 * Structure d'un regroupement de 1<br>
		 * Elle contient:<br>
		 * le nombre de term participant au regroupement
		 * l'adresse du regoupement
		 * un booléen qui indique si le groupement est utilisé dans
		 * un autre regroupement pls important
		 * l'adresse des cellule participants à ce regroupement
		 * @author Hamoudi, Meradi
		 *
		 */
		private class KarnaughNode{
			public ArrayList<String> cellesAdress;
			public int numberOfItems;
			public String nodes;
			public boolean flag;
			public KarnaughNode(String nodes,boolean flag,int numberOfItems)
			{
				this.nodes=nodes;
				this.flag=flag;
				this.numberOfItems=numberOfItems;
			}
			
			public void addCellAdress(String adress){
				if(cellesAdress ==null) cellesAdress=new ArrayList<String>();
				cellesAdress.add(adress);
			}
			
			public KarnaughNode() {
				
			}
		}		
	
	}
	

