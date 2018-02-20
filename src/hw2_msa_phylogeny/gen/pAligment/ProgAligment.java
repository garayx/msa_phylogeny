package hw2_msa_phylogeny.gen.pAligment;


import java.util.ArrayList;
import java.util.List;

import hw2_msa_phylogeny.tree.TreeNode;
	
public class ProgAligment {
	public List<String> kekList = new ArrayList<String>();	
	public static List<String> distancesList = new ArrayList<String>();
	public TreeNode finalNode = new TreeNode();
	public double finalScore;
	/* 
	 * 1. pairwise aligment by Hirsch...
	 * 2. make matrix from score made by hirsch...
	 * 	  make distance matrix (subs(:)/length) 
	 * 3. make graph from the matrix by NJ algorithm
	 * 4. make prog algorithm guided by tree
	 * 
	 * 
	 * Things to improve:
	 * 1. print all distances
	 * 2. change arraylist-list to list-list
	 * 3. change msAligment to recursion
	 * 4. rework center star alg
	 * 
	 */
	
	public ProgAligment(List<String> strList){
		
		List<String> tempList = new ArrayList<String>();	
		tempList= editStringList(strList);
		
		
		kekList = runProgAligment(tempList);
	}
	
	
	
	
	
	
	
	
	
	private List<String> editStringList(List<String> strList) {
		for(int i=0;i<strList.size();i++){
			String temp = "";
			temp = strList.get(i);
			strList.set(i, temp.substring(1));
		}
		return strList;
	}









	private List<String> runProgAligment(List<String> strList) {
		double [][] distanceMatrix = new double[strList.size()][strList.size()];
		for(int i=0; i<strList.size(); i++){
			for(int j=i+1; j<strList.size();j++){
				Hirschberg kek = new Hirschberg(strList.get(i), strList.get(j));
				List<String> myList = new ArrayList<String>();
				myList = kek.kekList;
				String Z = myList.get(2);
				String X = myList.get(2);
				//count non-gaps, count matches
				//distance 1-match/non-gap
				double matchCount = Z.length() - Z.replace("|", "").length(); //count only |
				double subsCount = X.length() - X.replace(":", "").length();
				double nonGaps = subsCount+matchCount;
				
				distanceMatrix[i][j]=(1 - (matchCount/nonGaps));
				distanceMatrix[j][i]=(1 - (matchCount/nonGaps));
			}
		}
		ArrayList<ArrayList<String>> listOfLists = new ArrayList<ArrayList<String>>();
		
		
		listOfLists = moddedguideTree(distanceMatrix);
		
		ArrayList<String> pairsList = new ArrayList<String>();
		ArrayList<String> distanceList = new ArrayList<String>();
		distanceList = listOfLists.get(1);
		pairsList = listOfLists.get(0);
		//System.out.println(distanceList);
		List<String> msaList = new ArrayList<String>();
		msaList = msAligment(pairsList,strList,distanceList);
		return msaList;
	}






	private List<String> msAligment(ArrayList<String> pairsList, List<String> strList, List<String> distanceList) {
		List<List<String>> listOfAligmentsA = new ArrayList<List<String>>();
		List<List<String>> listOfAligmentsB = new ArrayList<List<String>>();
		
		List<TreeNode> nodeListA = new ArrayList<TreeNode>();
		List<TreeNode> nodeListB = new ArrayList<TreeNode>();
		
		List<String> listOfAligments = new ArrayList<String>();
		List<String> caseList = new ArrayList<String>();
		int pairsCounter=0;
		int tempCount = 0;
		while(pairsList.size()>0){
			String pairString = new String();
			pairString = pairsList.get(pairsList.size()-1);
			List<String> newpairsList = new ArrayList<String>();
			newpairsList=getPairs(pairString);
			boolean isFirst=true;
			for(int i=0;i<newpairsList.size();i++){
				String pair = newpairsList.get(i);
				if(pair.contains(",")){
					if(isFirst){
						int a; 
						int b;
						//seq-seq aligment
						String[] number = pair.split(",");
						a = Integer.parseInt(number[0]);
						b = Integer.parseInt(number[1]);
						//System.out.println(a+" "+b);
						TreeNode nodeA = new TreeNode(number[0], new ArrayList<TreeNode>());
						TreeNode nodeB = new TreeNode(number[1], new ArrayList<TreeNode>());
						Hirschberg kek = new Hirschberg(strList.get(a), strList.get(b));
						List<String> tempAligmentList = new ArrayList<String>();
						tempAligmentList.add(kek.kekList.get(0));
						tempAligmentList.add(kek.kekList.get(1));
						
						
						
						if(pairsCounter==0){
							listOfAligmentsA.add(tempAligmentList);
							
							List<TreeNode> nodeList = new ArrayList<TreeNode>();
							nodeList.add(nodeA);
							nodeList.add(nodeB);
							//TreeNode nodeFP = new TreeNode(number[0]+""+number[1], nodeList);
							TreeNode nodeFP = new TreeNode("┐", nodeList);
							nodeListA.add(nodeFP);
						} else{
							listOfAligmentsB.add(tempAligmentList);
							
							List<TreeNode> nodeList = new ArrayList<TreeNode>();
							nodeList.add(nodeA);
							nodeList.add(nodeB);
							TreeNode nodeFP = new TreeNode("┐", nodeList);
							nodeListB.add(nodeFP);
						}
						isFirst=false;
						caseList.add("FP");
						String temp="";
						temp = "D("+a+", "+b+") = "+"("+distanceList.get(tempCount)+", "+distanceList.get(tempCount+1)+")";
						distancesList.add(temp);
						tempCount+=2;
					} else {
						/*
						 * Hirschenberg and then make prof-prof
						 * */
						
						
						//pairwise aligment first
						int a; 
						int b;
						String[] number = pair.split(",");
						a = Integer.parseInt(number[0]);
						b = Integer.parseInt(number[1]);
						
						TreeNode nodeA = new TreeNode(number[0],new ArrayList<TreeNode>());
						TreeNode nodeB = new TreeNode(number[1],new ArrayList<TreeNode>());
						List<TreeNode> nodeList = new ArrayList<TreeNode>();
						nodeList.add(nodeA);
						nodeList.add(nodeB);
						TreeNode nodeFP = new TreeNode("┐", nodeList);
						Hirschberg kek = new Hirschberg(strList.get(a), strList.get(b));
						List<String> tempPairwiseAligmentList = new ArrayList<String>();
						//List<String> mySequencesList = new ArrayList<String>();
						tempPairwiseAligmentList.add(kek.kekList.get(0));
						tempPairwiseAligmentList.add(kek.kekList.get(1));
						String temp="";
						temp = "D("+a+", "+b+") = "+"("+distanceList.get(tempCount)+", "+distanceList.get(tempCount+1)+")";
						distancesList.add(temp);
						tempCount+=2;
						
						//prof-prof now:
						List<String> myprofprofAligmentList = new ArrayList<String>();
						if(pairsCounter==0){
							ProfileAligment kekek = new ProfileAligment(
									listOfAligmentsA.get(i-1),
									tempPairwiseAligmentList);
							myprofprofAligmentList.addAll(kekek.kekList);
							listOfAligmentsA.add(myprofprofAligmentList);
							
							List<TreeNode> nodeList2 = new ArrayList<TreeNode>();
							nodeList2.add(nodeFP);
							nodeList2.add(nodeListA.get(i-1));
							TreeNode nodePP = new TreeNode("┐", nodeList2);
							nodeListA.add(nodePP);
						} else{
							ProfileAligment kekek = new ProfileAligment(
									listOfAligmentsB.get(i-1),
									tempPairwiseAligmentList);
							myprofprofAligmentList.addAll(kekek.kekList);
							listOfAligmentsB.add(myprofprofAligmentList);
							
							List<TreeNode> nodeList2 = new ArrayList<TreeNode>();
							nodeList2.add(nodeFP);
							nodeList2.add(nodeListB.get(i-1));
							TreeNode nodePP = new TreeNode("┐", nodeList2);
							nodeListB.add(nodePP);
							
							
							
						}
						caseList.add("PP");
					}
				} else {
					//case that we got in second part of pair only one number like [[0], [[1],[2]]]
					if(isFirst){
						int a; 
						a = Integer.parseInt(pair);
						//GUIDE TREE MAY BE PRINTED HERE
						//System.out.println(a);
						List<String> myList = new ArrayList<String>();
						myList.add(strList.get(a));
						listOfAligmentsB.add(myList);
						
						TreeNode node = new TreeNode(pair,new ArrayList<TreeNode>());
						nodeListB.add(node);
						isFirst=false;
					} else {
						//always prof-seq
						int a; 
						List<String> myprofseqAligmentList = new ArrayList<String>();
						a = Integer.parseInt(pair);
						//GUIDE TREE MAY BE PRINTED HERE
					//	System.out.println(a);
						List<String> myList = new ArrayList<String>();
						myList.add(strList.get(a));
						//prof-seq
						if(pairsCounter==0){
							ProfileAligment kekek = new ProfileAligment(
									listOfAligmentsA.get(i-1),
									myList);
							myprofseqAligmentList.addAll(kekek.kekList);
							listOfAligmentsA.add(myprofseqAligmentList);
							TreeNode node = new TreeNode(pair,new ArrayList<TreeNode>()); 
							List<TreeNode> nodeList = new ArrayList<TreeNode>();
							nodeList.add(node);
							nodeList.add(nodeListA.get(i-1));
							TreeNode nodePP = new TreeNode("┐", nodeList);
							nodeListA.add(nodePP);
							
							
							
							
							
							
						} else{
							ProfileAligment kekek = new ProfileAligment(
									listOfAligmentsB.get(i-1),
									myList);
							myprofseqAligmentList.addAll(kekek.kekList);
							listOfAligmentsB.add(myprofseqAligmentList);
							
							TreeNode node = new TreeNode(pair,new ArrayList<TreeNode>()); 
							List<TreeNode> nodeList = new ArrayList<TreeNode>();
							nodeList.add(node);
							nodeList.add(nodeListB.get(i-1));
							TreeNode nodePP = new TreeNode("┐", nodeList);
							nodeListB.add(nodePP);
						}
						caseList.add("PS");
					}
				}
			}
			pairsList.remove(pairsList.size()-1);
			pairsCounter++;
		}
		//Need to align the two aligments A and B
		//prof-prof aligment:
		List<TreeNode> nodeList = new ArrayList<TreeNode>();
/*		System.out.println(nodeListB);
		System.out.println(listOfAligmentsA.size());
		System.out.println(listOfAligmentsB);
		System.out.println(listOfAligmentsB.size());*/
		if(listOfAligmentsA.size()>=1 && listOfAligmentsB.size()>=1){
			ProfileAligment kekek = new ProfileAligment(
					listOfAligmentsA.get(listOfAligmentsA.size()-1),
					listOfAligmentsB.get(listOfAligmentsB.size()-1));
			listOfAligments.addAll(kekek.kekList);
			finalScore = kekek.finalScore;
			nodeList.add(nodeListA.get(nodeListA.size()-1));
			nodeList.add(nodeListB.get(nodeListB.size()-1));
		} else{ //if only two sequences given
			ProfileAligment kekek = new ProfileAligment(
					listOfAligmentsB.get(0),
					listOfAligmentsB.get(1));
			listOfAligments.addAll(kekek.kekList);
			
			finalScore = kekek.finalScore;
			
			nodeList.add(nodeListB.get(0));
			nodeList.add(nodeListB.get(1));
		}
		finalNode = new TreeNode("┐", nodeList);
		return listOfAligments;
	}
	
	
	
	private List<String> getPairs(String pairString) {
		List<String> numsList = new ArrayList<String>();
		List<String> tempnumsList = new ArrayList<String>();
		List<String> pairsList = new ArrayList<String>();
		String number ="";
		int numberCounter = 0;
		int bracketCounter = 0;
		char[] arr = pairString.toCharArray();
		int strStart=0;
		int strEnd=0;
		for(int i=0;i<arr.length;i++){
			if(arr[i]=='['){
				bracketCounter++;
				if(bracketCounter == 1){
					strStart=i;
				}
			}
			else if(arr[i]==']'){
				bracketCounter--;
				if(bracketCounter == 1){
					strEnd=i;
					pairsList.add(pairString.substring(strStart+1, strEnd+1));
					strStart=strEnd+2;
				}
			}
			else if(arr[i]==' ')
				continue;
			else if(arr[i]==',')
				continue;
			else{
				String tempNumber ="";
				int k=i;
				int counter=0;
				while(arr[k] != ']'){
					tempNumber+=arr[k];
					k++;
					counter++;
				}
				i+=(counter-1);
				numberCounter++;
				tempnumsList.add(tempNumber);
			}
		}
		if(numberCounter>2){
			while(pairsList.size()>0){
			tempnumsList = getPairs(pairsList.get(pairsList.size()-1));
			//not get 0 get by size
			for(int i=0; i<tempnumsList.size(); i++){
				numsList.add(tempnumsList.get(i));
			}
			pairsList.remove(pairsList.size()-1);
			}
		} else {
			if(!tempnumsList.isEmpty()){
				if(tempnumsList.size()>1){
					number = tempnumsList.get(0)+","+tempnumsList.get(1);
				} else {
					number = tempnumsList.get(0);
				}
			}
			numsList.add(number);
		}
		return numsList;
	}
	
	
	//modded to use Nj alg
	private ArrayList<ArrayList<String>> moddedguideTree(double [][] arr){
		//NEW TREE BRANCH
		ArrayList<ArrayList<String>> listOfLists = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> listOfDistanceLists = new ArrayList<ArrayList<String>>();
		int n = arr.length;
		for(int i=0; i< arr.length; i++){
			ArrayList<String> myList = new ArrayList<String>();
			myList.add(Integer.toString(i));
			listOfLists.add(myList);
		}
		for(int i=0; i< arr.length; i++){
			ArrayList<String> myList = new ArrayList<String>();
			myList.add(Integer.toString(0));
			listOfDistanceLists.add(myList);
		}
		ArrayList<String> DistanceList = new ArrayList<String>();
		while(arr.length>1){
			List<Double> avgDistance = new ArrayList<Double>();
			avgDistance = calcAvgDistance(arr,n);
			
			
			List<Integer> treeBranch = new ArrayList<Integer>();
			treeBranch = moddedgetMinIndex(arr,n);
			ArrayList<String> myList = new ArrayList<String>();
			myList.add(0, String.valueOf(listOfLists.get(treeBranch.get(0))));
			myList.add(1, String.valueOf(listOfLists.get(treeBranch.get(1))));
			ArrayList<String> myList2 = new ArrayList<String>();
			ArrayList<String> iDistList = new ArrayList<String>();
			ArrayList<String> jDistList = new ArrayList<String>();
			double iDistance = 0.5*(arr[treeBranch.get(0)][treeBranch.get(1)]+(avgDistance.get(treeBranch.get(0))-avgDistance.get(treeBranch.get(1))));
			double jDistance = 0.5*(arr[treeBranch.get(0)][treeBranch.get(1)]+(avgDistance.get(treeBranch.get(1))-avgDistance.get(treeBranch.get(0))));
			iDistList.add(String.valueOf(iDistance));
			jDistList.add(String.valueOf(jDistance));
			listOfDistanceLists.set(treeBranch.get(0), iDistList);
			listOfDistanceLists.set(treeBranch.get(1), jDistList);
			myList2.add(0, String.valueOf(listOfDistanceLists.get(treeBranch.get(0))));
			myList2.add(1, String.valueOf(listOfDistanceLists.get(treeBranch.get(1))));
			DistanceList.add(String.valueOf(jDistance));
			DistanceList.add(String.valueOf(iDistance));
			
			listOfDistanceLists.remove((int)treeBranch.get(0));
			listOfDistanceLists.remove((int)treeBranch.get(1)-1);
			listOfDistanceLists.add(myList2);

			listOfLists.remove((int)treeBranch.get(0));
			listOfLists.remove((int)treeBranch.get(1)-1);
			listOfLists.add(myList);
			
			arr = moddedmodArray(arr,treeBranch.get(0),treeBranch.get(1));
			
			
		}
		listOfLists.add(DistanceList);
		return listOfLists;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//modded to use Nj alg
	private double[][] moddedmodArray(double [][] arr, int iDel,int jDel){
		double[][] tempArr = new double[arr.length-1][arr.length-1];
		int p=0;
		for(int i=0; i<arr.length; ++i){
			 if (i == iDel || i == jDel)		//delete rows & columns
				 continue;
			 int q=0;
			 for(int j = 0; j<arr.length; ++j){
				 if (j == jDel || j == iDel)	//delete rows & columns
					 continue;
				 tempArr[p][q]=arr[i][j];
				 ++q;
			 }
			 ++p;
		}
		int q=0;
		for(int k=0; k<arr.length; k++){
			if(k!= iDel && k!=jDel){
				tempArr[q][tempArr.length-1] = (arr[k][iDel] + arr[k][jDel] - arr[iDel][jDel]) / 2;
				q++;
			}
		}
		return tempArr;
	}
	
	
	
	
	
	
	
	private List<Integer> moddedgetMinIndex(double [][] arr,int n){
		List<Integer> indList = new ArrayList<Integer>();
		indList.add(0);
		indList.add(1);
		List<Double> avgDistance = new ArrayList<Double>();
		avgDistance = calcAvgDistance(arr,n);
		//int iminAvg=0;int jminAvg=1;
		double minAvg = arr[0][1]-avgDistance.get(0)-avgDistance.get(1);
		for(int i=0;i<arr.length;i++){
			for(int j=i+1;j<arr.length;j++){
				double temp = arr[i][j]-avgDistance.get(i)-avgDistance.get(j);
				if(temp<minAvg){
					minAvg=temp;
					indList.removeAll(indList);
					indList.add(i);
					indList.add(j);
				}
			}
		}
		return indList;
	}
	
	
	private List<Double> calcAvgDistance(double [][] arr,int n){
		List<Double> avgDistance = new ArrayList<Double>();
		for(int i=0; i<arr.length;i++)
			avgDistance.add(i, 0.0);
		for(int i=0;i<arr.length;i++){
			for(int j=0;j<arr.length;j++){
				if(i!=j){
					double temp = avgDistance.get(i);
					temp +=arr[i][j]/(n);
					avgDistance.set(i, temp);
				}
			}
		}
		return avgDistance;
	}
	
	
}