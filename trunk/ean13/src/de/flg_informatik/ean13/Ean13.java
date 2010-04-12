package de.flg_informatik.ean13;

public interface Ean13 {
	final int digits=13;
	
	//this one codes 0,1,2 ... 9 ( raw (for digit #1-13 only)), code modified by alphabeth13 and specialchars: edgechar,midchar  
	final int[][] alphabeth = {{3,2,1,1},{2,2,2,1},
			{2,1,2,2},{1,4,1,1},
			{1,1,3,2},{1,2,3,1},
			{1,1,1,4},{1,3,1,2},
			{1,2,1,3},{3,1,1,2},
			{1,1,1},//midchar
			{1,1,1,1,1}}; //edgechar
	
	final int[][] alphabeth13 = {{1,1,1,1,1,1,3,3,3,3,3,3}, // for #0=0, 
	 						     {1,1,2,1,2,2,3,3,3,3,3,3},
								 {1,1,2,2,1,2,3,3,3,3,3,3},
								 {1,1,2,2,2,1,3,3,3,3,3,3},
								 {1,2,1,1,2,2,3,3,3,3,3,3},// to
								 {1,2,2,1,1,2,3,3,3,3,3,3},
								 {1,2,2,2,1,1,3,3,3,3,3,3},
								 {1,2,1,2,1,2,3,3,3,3,3,3},
								 {1,2,1,2,2,1,3,3,3,3,3,3},
								 {1,2,2,1,2,1,3,3,3,3,3,3}};// #0=9
								// 1 -> starting white "odd", 2 -> starting white "even", 3 -> starting black "odd" 

}
