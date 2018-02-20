package hw2_msa_phylogeny;

import java.awt.EventQueue;

import hw2_msa_phylogeny.gui.Gui;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
		
		runMain();
	}
	private static void runMain(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frmHwGenomics.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
				//.q(q)
				//.build();
	}
	
}
