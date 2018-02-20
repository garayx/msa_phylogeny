package hw2_msa_phylogeny.gen.pAligment;

import java.util.ArrayList;
import java.util.List;

public class ProfileAligment {
	private final int match = 2;
	private final int mismatch = -1;
	//private final int mismatch = 0;
	private final int gapPenalty = -2;
	private final char gapMarker = '-';
	private final String gapString = "-";
	List<String> kekList = new ArrayList<String>();
	public double finalScore=0;

	public ProfileAligment( 
			List<String> AligmentListA, 
			List<String> AligmentListB
			){
		
		kekList = runProfileAligment(
				AligmentListA,
				AligmentListB
				);
	}
	private List<String> runProfileAligment(
			List<String> AligmentListA,
			List<String> AligmentListB
			){
		int flag=0; //copy colBlist flag
		List<String> resultAList = new ArrayList<String>();
		List<String> resultBList = new ArrayList<String>();
		List<String> resultsList = new ArrayList<String>();
		List<String> colAList = new ArrayList<String>();
		List<String> colBList = new ArrayList<String>();
		for(int i=0;i<AligmentListA.size();i++){
			resultAList.add("");
		}
		for(int i=0;i<AligmentListB.size();i++){
			resultBList.add("");
		}
		
		
		int[][] dynamicTable = new int[AligmentListA.get(0).length()+1][AligmentListB.get(0).length()+1];
		//init table with gaps scores
		for(int i=1; i<AligmentListA.get(0).length()+1;i++)
			dynamicTable[i][0] = dynamicTable[i-1][0]+gapPenalty;
		for(int i=1; i<AligmentListB.get(0).length()+1;i++)
			dynamicTable[0][i] = dynamicTable[0][i-1]+gapPenalty;
		
		
		String[][] dynamicPathTable = new String[AligmentListA.get(0).length()+1][AligmentListB.get(0).length()+1];
		for(int i=0; i< AligmentListA.get(0).length(); i++){
			String colA = "";
			for(int j=0; j< AligmentListA.size(); j++){
				colA += AligmentListA.get(j).charAt(i);
			}
			colAList.add(colA);
			for(int k=0; k<AligmentListB.get(0).length();k++){
				String colB ="";
				for(int j=0; j< AligmentListB.size(); j++){
					colB += AligmentListB.get(j).charAt(k);
				}
				if(flag==0)
					colBList.add(colB);
				List<String> AnswerList = new ArrayList<String>();
				AnswerList = getMax(dynamicTable,i,k, colA, colB);
				finalScore += getScore(dynamicTable,i,k, colA, colB);
				dynamicTable[i+1][k+1] = Integer.parseInt(AnswerList.get(0));
				dynamicPathTable[i+1][k+1] = AnswerList.get(1);
			}
			flag=1;
		}

		//Traceback
		int i = AligmentListA.get(0).length();
		int j = AligmentListB.get(0).length();
		while(dynamicPathTable[i][j] != null){
			if(dynamicPathTable[i][j] == "d"){
				
				for(int k=0; k < resultAList.size();k++){
					String temp="";
					temp += colAList.get(i-1).charAt(k);
					temp += resultAList.get(k);
					resultAList.set(k, temp);
					
				}
				for(int k=0; k < resultBList.size();k++){
					String temp="";
					temp += colBList.get(j-1).charAt(k);
					temp += resultBList.get(k);
					resultBList.set(k, temp);
					
				}
				i--;j--;
			}
			else if(dynamicPathTable[i][j] == "v"){
				for(int k=0; k < resultAList.size();k++){
					String temp="";
					temp += colAList.get(i-1).charAt(k);
					temp += resultAList.get(k);
					resultAList.set(k, temp);
				}
				for(int k=0; k < resultBList.size();k++){
					String temp="";
					temp += gapString;
					temp += resultBList.get(k);
					resultBList.set(k, temp);
				}
				i--;
			}
			else if(dynamicPathTable[i][j] == "h"){
				for(int k=0; k < resultBList.size();k++){
					String temp="";
					temp += colBList.get(j-1).charAt(k);
					temp += resultBList.get(k);
					resultBList.set(k, temp);
				}
				for(int k=0; k < resultAList.size();k++){
					String temp="";
					temp += gapString;
					temp += resultAList.get(k);
					resultAList.set(k, temp);
				}
				j--;
			}
		}
		if(i>0){
			while(i>0){
				for(int k=0; k < resultAList.size();k++){
					String temp="";
					temp += colAList.get(i-1).charAt(k);
					temp += resultAList.get(k);
					resultAList.set(k, temp);
				}
				for(int k=0; k < resultBList.size();k++){
					String temp="";
					temp += gapString;
					temp += resultBList.get(k);
					resultBList.set(k, temp);
				}
				i--;
			}
		} else if(j>0){
			while(j>0){
				for(int k=0; k < resultBList.size();k++){
					String temp="";
					temp += colBList.get(j-1).charAt(k);
					temp += resultBList.get(k);
					resultBList.set(k, temp);
				}
				for(int k=0; k < resultAList.size();k++){
					String temp="";
					temp += gapString;
					temp += resultAList.get(k);
					resultAList.set(k, temp);
				}
				j--;
			}
		}
		resultsList.addAll(resultAList);
		resultsList.addAll(resultBList);
		return resultsList;
	}

	private double getScore(int[][] dynamicTable, int i, int k, String colA, String colB) {
		// TODO Auto-generated method stub
		int denum = colA.length()*colB.length();
		int num = PSP(colA,colB);
		double score = num/denum;
		return score;
	}
	
	
	private List<String> getMax(int[][] dynamicTable, int i, int k, String colA, String colB) {
		List<String> AnswerList = new ArrayList<String>();
		int max = dynamicTable[i][k] + PSP(colA,colB);
		//came diaganally
		String symbol = "d";
		if(dynamicTable[i+1][k] + colA.length()*gapPenalty > max){
			max = dynamicTable[i+1][k] + colA.length()*gapPenalty;
			//max = dynamicTable[i+1][k] + gapPenalty;
			symbol = "h";
		} else if (dynamicTable[i][k+1] + colB.length()*gapPenalty > max){
			max = dynamicTable[i][k+1] + colB.length()*gapPenalty;
			//max = dynamicTable[i][k+1] + gapPenalty;
			symbol = "v";
		}
		AnswerList.add(Integer.toString(max));
		AnswerList.add(symbol);
		return AnswerList;
	}


	private int PSP(String colA, String colB) {
		
		int colAsize = colA.length();
		int colBsize = colB.length();
		int score=0;
		for(int i=0;i<colAsize;i++){
			for(int j=0;j<colBsize;j++){
				if(colA.charAt(i) == colB.charAt(j)){
					if(colA.charAt(i) == gapMarker && colB.charAt(j)==gapMarker)
						score += gapPenalty;
					else
						score += match;
				}else if(colA.charAt(i) != colB.charAt(j)){
						score += mismatch;
					}
				}
			}
		return score;
		}
		
		
		
		
		
	
}
