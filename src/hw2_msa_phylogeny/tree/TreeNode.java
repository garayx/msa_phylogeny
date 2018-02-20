package hw2_msa_phylogeny.tree;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    final String name;
    final List<TreeNode> children;

    public TreeNode(){
    	this.name = null;
        this.children = null;
    };
    
    public TreeNode(String name, List<TreeNode> children) {
        this.name = name;
        this.children = children;
    }
    public TreeNode(TreeNode node){
    	this.name = node.name;
        this.children = node.children;
    };
    
    public void print() {
       print("", true);
    }
    public String getName() {
        return this.name;
    }

    
    private void print( String prefix, boolean isTail){
        //System.out.println(prefix + (isTail ? "└───" : "├───") + " "+name);
        StringBuilder sb = new StringBuilder();
    	sb.append(prefix);
        if(isTail){
        	//System.out.print("└───");
        	sb.append("└───");
        }else{
        	//System.out.print("├───");
        	sb.append("├───");
        }if(isInteger(name)){
        	//System.out.println(" "+name);
        	sb.append(" "+name+"");
        }else{
        	//System.out.println(name);
        	sb.append(name+"");
        }
        try(PrintWriter out = new PrintWriter( new FileWriter("tree.txt", true))){
            out.println(sb.toString());
        } catch (IOException e) {
			e.printStackTrace(); 
		}
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).print(prefix + (isTail ? "    " : "│   "), false);
        }
        if (children.size() > 0) {
            children.get(children.size() - 1)
                    .print(prefix + (isTail ?"    " : "│   "), true);
        }
        
    }
    
    
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
    
    
    
}