package hw2_msa_phylogeny.gen.pAligment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Hirschberg {
	final int insertion    = -2;
	final int deletion     = -2;
	final int substitution = -1;
	final int match        =  2;
	List<String> kekList = new ArrayList<String>();
	/*
	 * returns list with first word, results, second word
	 */
	public Hirschberg(String x,String y){
		kekList = runHirschberg(x,y);
	}
	
	private List<String> runHirschberg(String x, String y){
		String row = "";
		String column = "";
		String middle = "";
		List<String> myList = new ArrayList<String>();
		if(x.length() == 0 || y.length() == 0){
			if(x.length() == 0){
				char[] chars = new char[y.length()];
				Arrays.fill(chars, '-');
				column += new String(chars);
				//row = y;
				row += y;
				chars = new char[y.length()];
				Arrays.fill(chars, 'x');
				middle += new String(chars);
			} else {
				column += x;
				char[] chars = new char[x.length()];
				Arrays.fill(chars, '-');
				row += new String(chars);
				chars = new char[x.length()];
				Arrays.fill(chars, 'x');
				middle += new String(chars);
			}
		}
		else if(x.length() == 1 || y.length() == 1){
			myList = NeedlemanWunsch(x,y);
			
			row += myList.get(1);
			column += myList.get(0);
			middle += myList.get(2);
		}
		else{
			int xlen = x.length();
			int xmid = xlen/2;
			int[] scoreL = lastLine(x.substring(0, xmid),y);
			String tmp = new StringBuilder(x.substring(xmid)).reverse().toString();
			String tmp2 = new StringBuilder(y).reverse().toString();
			int[] scoreR = lastLine(tmp,tmp2);
			int ymid = partition(scoreL,scoreR);
			List<String> myList_l = new ArrayList<String>();
			List<String> myList_r = new ArrayList<String>();
			myList_l = runHirschberg(x.substring(0, xmid),y.substring(0, ymid));
			myList_r = runHirschberg(x.substring(xmid),y.substring(ymid));
			row = myList_l.get(1) + myList_r.get(1);
			column = myList_l.get(0) + myList_r.get(0);
			middle = myList_l.get(2) + myList_r.get(2);
			
		}
		
		myList.add(column);
		myList.add(row);
		myList.add(middle);
		return myList;
	}
	
	private int[] lastLine(String x, String y){
		char[] row = y.toCharArray();
		char[] column = x.toCharArray();
		int minLen = y.length();
		int prev[] = new int[minLen+1];
		int current[] = new int[minLen+1];
		for(int i=1; i<minLen+1;i++){
			prev[i] = prev[i-1] + insertion;
		}
		current[0] = 0;
		for(int j=1; j<column.length + 1;j++){
			current[0] += deletion;
			for(int i=1; i<minLen+1;i++){
				if(row[i-1] == column[j-1]){
					current[i] = Math.max(Math.max((current[i-1] + insertion), (prev[i-1] + match)), (prev[i] + deletion));
				} else
					current[i] = Math.max(Math.max(current[i-1] + insertion, prev[i-1] + substitution), prev[i] + deletion);
			}
			//prev = current;
			System.arraycopy( current, 0, prev, 0, current.length );
		}
		
		return current;
	}

	/*
	 * M = array of score
	 * Path = array of path information
	 * d Diagonal
	 * u Up
	 * l Left
	 * 
	 * 
	 * SET THE STRING TO CHAR ARRAY!!!!
	 */
	private List<String> NeedlemanWunsch(String a, String b){
		List<String> myList = new ArrayList<String>();
		char[] x = a.toCharArray();
		char[] y = b.toCharArray();
		int[][] M = new int[x.length+1][y.length+1];
		String[][] Path = new String[x.length+1][y.length+1];
		
		
		for(int i=1; i<y.length + 1;i++){
			M[0][i] = M[0][i-1] + insertion;
			Path[0][i] = "l";
		}
		for(int j=1; j<x.length + 1;j++){
			M[j][0] = M[j-1][0] + deletion;
			Path[j][0] = "u";
		}
		for(int i=1; i<x.length + 1;i++){
			for(int j=1; j<y.length + 1;j++){
				if(x[i-1] == y[j-1]){
					M[i][j] = Math.max(Math.max(M[i-1][j-1] + match, M[i-1][j] + insertion), M[i][j-1] + deletion);
					if(M[i][j] == M[i-1][j-1] + match)
						Path[i][j] =  "d";
					else if(M[i][j] == M[i-1][j] + insertion)//not sure about elif????
						Path[i][j] = "u";
					else
						Path[i][j] = "l";
				} else {
					M[i][j] = Math.max(Math.max(M[i-1][j-1] + substitution, M[i-1][j] + insertion), M[i][j-1] + deletion);
					if(M[i][j] == M[i-1][j-1] + substitution)
						Path[i][j] =  "d";
					else if(M[i][j] == M[i-1][j] + insertion)
						Path[i][j] = "u";
					else
					 	Path[i][j] = "l";
				}
			}
		}
		/*
		 * FIX
		 */
		List<Character> row = new ArrayList<Character>();
		List<Character> column = new ArrayList<Character>();
		List<Character> middle = new ArrayList<Character>();
		int i = x.length;
		int j = y.length;
		while(Path[i][j] != null){
			if(Path[i][j] == "d"){
				row.add(0, y[j-1]);
				column.add(0, x[i-1]);
				if(x[i-1] == y[j-1])
					middle.add(0, '|');
				else
					middle.add(0, ':');
				i--;j--;
			}
			else if(Path[i][j] == "u"){
				row.add(0, '-');
				column.add(0, x[i-1]);
				middle.add(0, 'x');
				i--;
			}
			else if(Path[i][j] == "l"){
				row.add(0, y[j-1]);
				column.add(0, '-');
				middle.add(0, 'x');
				j--;
			}
		}
		myList.add(column.stream().map(e->e.toString()).collect(Collectors.joining()));
		myList.add(row.stream().map(e->e.toString()).collect(Collectors.joining()));
		myList.add(middle.stream().map(e->e.toString()).collect(Collectors.joining()));
		return myList;
	}
	
	private int partition(int[] scoreL, int[] scoreR){
		int max_index = 0;

		for(int i = 0; i < scoreR.length / 2; i++)
		{
		    int temp = scoreR[i];
		    scoreR[i] = scoreR[scoreR.length - i - 1];
		    scoreR[scoreR.length - i - 1] = temp;
		}
		int max_sum = scoreL[0]+scoreR[0];
		for(int i=1;i<scoreR.length;i++){
			if(scoreL[i]+scoreR[i] > max_sum){
				max_sum = scoreL[i]+scoreR[i];
				max_index = i;
			}
		}
		return max_index;
	}
}
