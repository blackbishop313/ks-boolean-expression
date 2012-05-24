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


/**
 * Permet d'avoir la table de Karnaugh d'une fonction<br>
 * à partir de sa table de vérité.
 * @author Mounir Hamoudi
 */



public class KarnaughTable {
	private KMapStructure kmap[][]; 
	private int Row;  
	private int Column;  
	private int ttable[];
	private int nbVar;
	
	
	/**
	 * Constructeur de KarnaughTable
	 * @param ttab la table de vérité
	 * @param nbrVar le nombre de variables
	 */
	
	public KarnaughTable(int ttab[], int nbrVar)
	{
		setNbrVar(nbrVar);
		setTruthTable(ttab);
		initialiseKMap();
		fillWithCellAdress();
		fillWithValues();
		
	}
	/**
	 * Constructeur
	 * @param ktable table de karnaugh
	 * @param nbrVar nombre de variables
	 */
	public KarnaughTable(int ktable[][], int nbrVar)
	{
		setNbrVar(nbrVar);
		initialiseKMap();
		fillWithCellAdress();
		setKmap(ktable);
	}
	
	
	
	/**
	 * set le nombre de variables
	 */
	private void setNbrVar(int nbrVar)
	{
		nbVar = nbrVar;
	}
	
	/**
	 * set la tables de Karnaught
	 */
	private void setKmap(int kmap[][])
	{
		Row=(int) Math.pow(2, Math.floor((double) nbVar/2));
		Column=(int) Math.pow(2,Math.ceil( (double) nbVar/2));
		for (int i=0; i<Row; i++)
		{
			for(int j=0; j<Column; j++)
			this.kmap[i][j].cellValue=kmap[i][j];	
		}
		
	}
	
	
	/**
	 * Initialise la table de vérité et la taille de la table de Karnaugh
	 * @param ttab la table de vérité
	 */

    private void setTruthTable(int ttab[])
    {
    	int taille = ttab.length;
    	ttable= new int[taille];
    	for(int i=0; i<taille; i++)
    	{
    		ttable[i] = ttab[i];
    	}
    	//On initialise aussi la taille de la table de Karnaugh
    	Row=(int) Math.pow(2, Math.floor((double) nbVar/2));
		Column=(int) Math.pow(2,Math.ceil( (double) nbVar/2));
    	
    }
    
    
    /**
     * Initialise la table de Karnaugh avec des objets de type KMapStructure
     */
    private void initialiseKMap()
	{
    	Row=(int) Math.pow(2, Math.floor((double) nbVar/2));
		Column=(int) Math.pow(2,Math.ceil( (double) nbVar/2));
    	kmap = new KMapStructure[Row][Column];
    	for(int i=0; i<Row; i++)
	  	   { 
	  		for(int j=0; j<Column; j++)
	  		{  
	  			kmap[i][j] = new KMapStructure();
	  		}
	  	   }
	}
    
	
	/**
	 * Remplit chaque case de la table de Karnaugh avec son adresse<br>
	 * L'adresse est déterminée en utilisant le Code Gray <br>
	 */
	
    private void fillWithCellAdress()
    {
    	// PREMIER CAS: le nombre de variables est égal à 1
  	  if(nbVar==1)
  	  {
  		  kmap[0][0].cellAdress=0;
  		  kmap[0][1].cellAdress=1;
  	  }
  	  else{  	  
  	  // DEUXIEME CAS: le nombre de variables est égal à 2
  	  if(nbVar==2)
  	  {
  		  kmap[0][0].cellAdress=0;
  		  kmap[0][1].cellAdress=1;
  		  kmap[1][0].cellAdress=2;
  		  kmap[1][1].cellAdress=3;
  	  }
  	   //TROISIEME CAS: le nombre de variables est 3 OU 4 
  	  else{  		  		   
  	  if(nbVar==3 || nbVar==4)
  	  {  
  	   for(int i=0; i<Row; i++)
  	   { 
  		for(int j=0; j<Column; j++)
  		{  
  			if(Row==4)
  			kmap[i][j].cellAdress= GrayEncode3_4(j%4+((i%4)*4));
  			else 
  			kmap[i][j].cellAdress=GrayEncode3_4(j%4+((i%4)*Column));
  	    } 
  	   }
  	  }
  	  
 	//QUATRIEME CAS: le nombre de variables est 5 ou 6
  	  else{  
  	if(nbVar==5 || nbVar==6)
	  {
	   for(int i=0; i<Row; i++)
	   { 
		for(int j=0; j<Column; j++)
		{
			if(i<=3 && j<=3) 
			{
			  kmap[i][j].cellAdress= GrayEncode5_6(j%4+((i%4)*4));
			}	
			else
			{
				if(Row==4)
				{
			      if(j<6) kmap[i][j].cellAdress= (kmap[i][j-4].cellAdress+6);
			      else kmap[i][j].cellAdress= (kmap[i][j-4].cellAdress+2);
				}
				else
				{
					if(i>3 && j<=3)
		 			  {
						if(i<6) kmap[i][j].cellAdress = kmap[i-4][j].cellAdress+48;
		 				else kmap[i][j].cellAdress = kmap[i-4][j].cellAdress+16;
					  }
		 			  else
		 			  {
		 				 if(j<6) kmap[i][j].cellAdress= (kmap[i][j-4].cellAdress+6);
			 			 else kmap[i][j].cellAdress= (kmap[i][j-4].cellAdress+2);
		 			  }	
				}
			 }
		 }
	   }
     } 
	  
  //CINQIEME CAS: le nombre de variables est 7 ou 8
  	else{
  	  if (nbVar==7 || nbVar==8)
  	  {
  		
  	   for(int i=0; i<Row; i++)
  	   { 
  		for(int j=0; j<Column; j++)
  		{
  			if(i<=3 && j<=3) 
  			{
  		 
  			kmap[i][j].cellAdress= GrayEncode7_8(j%4+((i%4)*4));
  			}	
  			else
  			{
  			 if(Row==8)
  			 { 
  			   if(i>3 && j<=3)
    		   {
    		    if(i<6) kmap[i][j].cellAdress = kmap[i-4][j].cellAdress+96;
    		    else kmap[i][j].cellAdress = kmap[i-4][j].cellAdress+32;
    		   }
  			 }
  			 else
 			 {
 				if(i>3 && j<=3)
 	    		 {
 					if(i<6 || (i>7 && i<10)) kmap[i][j].cellAdress= (kmap[i-4][j].cellAdress+96);
 					else{if(i==6 || i==7)  kmap[i][j].cellAdress= (kmap[i-4][j].cellAdress+32);
 					else{if(i==10 || i==11) kmap[i][j].cellAdress= (kmap[i-4][j].cellAdress+160);
 					else{if(i==12 || i==13) kmap[i][j].cellAdress= (kmap[i-4][j].cellAdress-32);
 	    		    else kmap[i][j].cellAdress = kmap[i-4][j].cellAdress-96;}}}
 	    		 }
  			  }
  			   if(j>3)
  	  			  {
  	  				if(j==4 || j==5 || j==8 || j==9) kmap[i][j].cellAdress= (kmap[i][j-4].cellAdress+6);
  	  				else{if(j==6  || j==7)  kmap[i][j].cellAdress= (kmap[i][j-4].cellAdress+2);
  	  				else{if(j==10 || j==11) kmap[i][j].cellAdress= (kmap[i][j-4].cellAdress+10);
  	  				else{if(j==12 || j==13) kmap[i][j].cellAdress= (kmap[i][j-4].cellAdress-2);
  	  				else kmap[i][j].cellAdress= (kmap[i][j-4].cellAdress-6);}}}
  	  			  } 
  			 }
  			}
  		}
  	 }
  	}}}}	
    
    
    }

    /**
     * Remplit la table de Karnaugh avec les valeurs de la fonction<br>
     * Les valeurs sont données dans la table de vérité<br>
     */
    
	private void fillWithValues()
    {
    	for(int i=0; i<Row; i++)
    	{
    		for(int j=0; j<Column; j++)
    		{
    			kmap[i][j].cellValue= ttable[kmap[i][j].cellAdress];
    		}
    	}
    }
    
	 /**
     * Donne l'adresse de chaque celulle du premier bloc 4x4<br>
     * d'une table de karnaugh de taille 2x4 ou 4x4<br>
     * @param m numéro de la celule déterminé par j%4+((i%4)*4)<br>
     * tels que i numéro de la ligue et j numéro de la colonne<br>
     * @return renvoit l'adresse de la celulle
     */


    private int GrayEncode3_4( int m)
	  {
		int CodeGray=0;
	    switch(m)
		{
			case 0:  {CodeGray =  0; break;} 
			case 1:  {CodeGray =  1; break;}
			case 2:  {CodeGray =  3; break;}
			case 3:  {CodeGray =  2; break;}
			case 4:  {CodeGray =  4; break;}
			case 5:  {CodeGray =  5; break;}
			case 6:  {CodeGray =  7; break;}
			case 7:  {CodeGray =  6; break;}
			case 8:  {CodeGray =  12; break;}
			case 9:  {CodeGray =  13; break;}
			case 10: {CodeGray =  15; break;}
			case 11: {CodeGray =  14; break;}
			case 12: {CodeGray =  8; break;}
			case 13: {CodeGray =  9; break;}
			case 14: {CodeGray =  11; break;}
			case 15: {CodeGray =  10; break;}
		}
		return CodeGray;
	}
	
    
    /**
     * Donne l'adresse de chaque celulle du premier bloc 4x4<br>
     * d'une table de karnaugh de taille 4x8 ou 8x8<br>
     * @param m numéro de la celule déterminé par j%4+((i%4)*4)<br>
     * tels que i numéro de la ligue et j numéro de la colonne<br>
     * @return renvoit l'adresse de la celulle
     */
    
    private int GrayEncode5_6(int m)
    {
	int CodeGray=0;
	switch(m)
	{
	case 0:  {CodeGray =  0;  break;}
	case 1:  {CodeGray =  1; break;}
	case 2:  {CodeGray =  3; break;}
	case 3:  {CodeGray =  2; break;}
	case 4:  {CodeGray =  8; break;}
	case 5:  {CodeGray =  9; break;}
	case 6:  {CodeGray =  11; break;}
	case 7:  {CodeGray =  10; break;}
	case 8:  {CodeGray =  24; break;}
	case 9:  {CodeGray =  25; break;}
	case 10: {CodeGray =  27; break;}
	case 11: {CodeGray =  26; break;}
	case 12: {CodeGray =  16; break;}
	case 13: {CodeGray =  17; break;}
	case 14: {CodeGray =  19; break;}
	case 15: {CodeGray =  18; break;}
	}
	return CodeGray;
    }

    /**
     * Donne l'adresse de chaque celulle du premier bloc 4x4<br>
     * d'une table de karnaugh de taille 8x16 ou 16x16<br>
     * @param m numéro de la celule déterminé par j%4+((i%4)*4)<br>
     * tels que i numéro de la ligue et j numéro de la colonne<br>
     * @return renvoit l'adresse de la celulle
     */
    
    
    private int GrayEncode7_8(int m)
    {
	int CodeGray=0;
	switch(m)
	{
	case 0:  {CodeGray =  0;  break;}
	case 1:  {CodeGray =  1; break;}
	case 2:  {CodeGray =  3; break;}
	case 3:  {CodeGray =  2; break;}
	case 4:  {CodeGray =  16; break;}
	case 5:  {CodeGray =  17; break;}
	case 6:  {CodeGray =  19; break;}
	case 7:  {CodeGray =  18; break;}
	case 8:  {CodeGray =  48; break;}
	case 9:  {CodeGray =  49; break;}
	case 10: {CodeGray =  51; break;}
	case 11: {CodeGray =  50; break;}
	case 12: {CodeGray =  32; break;}
	case 13: {CodeGray =  33; break;}
	case 14: {CodeGray =  35; break;}
	case 15: {CodeGray =  34; break;}
	}
	return CodeGray;
    }



/**
 * Structure des cellules de la tables de Karnaugh<br>
 * Chaque cellule contient une valeurs (0 ou 1)<br>
 * et une adresse (Code Gray de la cellule).
 * @author Mounir Hamoudi
 *
 */

private class KMapStructure{
	public int cellValue;
	public int cellAdress;
}


/**
 * Renvoit le nombre de lignes de table de Karnaugh
 * @return Row nombre de lignes de la table
 */

public int getRow()
{
	return Row;
}


/**
 * Renvoit le nombre de colonnes de table de Karnaugh
 * @return Column nombre de colonnes de la table
 */

public int getColumn()
{
	return Column;
}

/**
 * Retourne la valaur que contient la cellule [i][j]<br>
 * @param i indice de la ligne dans la table de Karnaugh<br>
 * @param j indice de la colonne dans la table de Karnaugh
 * @return la valeur que contient cette cellule
 */

public int getCellValue(int i, int j)
{
	return kmap[i][j].cellValue;
}


/**
 * Retourne l'adresse (Code Gray) de la cellule [i][j]<br>
 * @param i indice de la ligne dans la table de Karnaugh
 * @param j indice de la colonne dans la table de Karnaugh
 * @return l'adresse de la cellule
 */

public int getCellAdress(int i, int j)
{
	return kmap[i][j].cellAdress;
}
	


}


  
	
	  
	    


