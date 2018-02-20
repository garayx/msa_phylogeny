package hw2_msa_phylogeny.gui;


import hw2_msa_phylogeny.gen.pAligment.ProgAligment;
import hw2_msa_phylogeny.gen.CenterStarAligment.CenterStar;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.JRadioButton;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.Font;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import javax.swing.JFileChooser;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.beans.PropertyChangeEvent;
import java.awt.SystemColor;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class Gui {

	public JFrame frmHwGenomics;
	private JTextField textField_MSAScore;
	private static boolean msaIsSelected=false;
	private static boolean errorMsg=false;
	private static boolean fileSelected=true;
	private static List<String> strList = new ArrayList<String>();
	private static List<String> resultList = new ArrayList<String>();
	private static List<String> distancesList = new ArrayList<String>();
	private final Action action = new SwingAction();
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private static JTextField textField_CS_Score;

	private static char[][] matrix = new char[24][3];
	private String file="";
	private final Action action_1 = new SwingAction_1();
	//private double score;
	
	
	
	/**
	 * @wbp.nonvisual location=342,-31
	 */
	private final static JTextArea textArea_treePrint = new JTextArea();
	private final Action action_2 = new SwingAction_2();
	private final Action action_3 = new SwingAction_3();
	
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 */
	public Gui() {
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
		
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		textArea_treePrint.setFont(new Font("Courier New", Font.PLAIN, 12));
		action.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
			}
		});
		frmHwGenomics = new JFrame();
		frmHwGenomics.setResizable(false);
		frmHwGenomics.setTitle("HW2 Genomics");
		frmHwGenomics.setBounds(100, 100, 800, 600);
		frmHwGenomics.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLayeredPane layeredPane_1 = new JLayeredPane();
		JLabel lblMSAScore = new JLabel("MSA Score:");
		lblMSAScore.setToolTipText("MSA Score:");
		JLabel lblCS_Score = new JLabel("CenterStar Score:");
		lblCS_Score.setToolTipText("CenterStar Score:");
		JButton btnLoadMatrix = new JButton("Load Matrix");
		btnLoadMatrix.setAction(action_1);
		JLabel lblSubMatrix = new JLabel("Substitution Matrix:");
		lblSubMatrix.setToolTipText("Substitution Matrix:");
		lblMSAScore.setBounds(10, 20, 86, 14);
		layeredPane_1.add(lblMSAScore);
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Select Method", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		JButton btnShowTree = new JButton("Show Tree");
		
		
		JLabel lblShowDistance = new JLabel("Show Pairs:");
		lblShowDistance.setToolTipText("Show pairs and distance between them");
		lblShowDistance.setEnabled(false);
		lblShowDistance.setBounds(270, 20, 86, 14);
		layeredPane_1.add(lblShowDistance);
		
		JButton btnShowDistance = new JButton("Show Pairs");
		btnShowDistance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//showPairsDistanceDialog(frmHwGenomics);
			}
		});
		btnShowDistance.setAction(action_3);
		btnShowDistance.setToolTipText("shows pairs and their distance");
		btnShowDistance.setEnabled(false);
		btnShowDistance.setBounds(270, 49, 90, 50);
		layeredPane_1.add(btnShowDistance);
		
		JLabel lblShowTree = new JLabel("Show Tree:");
		lblShowTree.setToolTipText("Show Tree:");
		lblShowTree.setEnabled(false);
		lblShowTree.setBounds(140, 20, 86, 14);
		layeredPane_1.add(lblShowTree);
		
		JRadioButton rdbtnCenterStar = new JRadioButton("Center Star");
		rdbtnCenterStar.setToolTipText("Center Star Algorithm");
		rdbtnCenterStar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblMSAScore.setEnabled(false);
				textField_MSAScore.setEnabled(false);
				textField_CS_Score.setEnabled(true);
				lblCS_Score.setEnabled(true);
				lblSubMatrix.setEnabled(true);
				btnLoadMatrix.setEnabled(true);
				btnShowTree.setEnabled(false);
			}
		});
		buttonGroup.add(rdbtnCenterStar);
		rdbtnCenterStar.setBounds(10, 30, 109, 23);
		layeredPane.add(rdbtnCenterStar);
		
		
		
		btnLoadMatrix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fs = new JFileChooser(new File("c:\\"));
				fs.setDialogTitle("Open a File");	
				fs.showOpenDialog(null);
				File fi =fs.getSelectedFile();
				try {
					BufferedReader br = new BufferedReader(new FileReader(fi.getPath()));					
					String line ="";					
					while((line = br.readLine())!= null){
						file+=line;
					}
					if(br !=null)
						br.close();	
					
				} catch (Exception e1) {
					fileSelected=false;
					//JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				//initMatrix();
			}
		});
		
		
		
		
		JRadioButton rdbtnMsa = new JRadioButton("Progressive Aligment");
		rdbtnMsa.setToolTipText("Progressive Aligment Algorithm");
		rdbtnMsa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblMSAScore.setEnabled(true);
				textField_MSAScore.setEnabled(true);
				textField_CS_Score.setEnabled(false);
				lblCS_Score.setEnabled(false);
				lblSubMatrix.setEnabled(false);
				btnLoadMatrix.setEnabled(false);
				btnShowTree.setEnabled(false);
			}
		});
		buttonGroup.add(rdbtnMsa);
		rdbtnMsa.setBounds(10, 60, 125, 23);
		layeredPane.add(rdbtnMsa);
		
		//JLayeredPane layeredPane_1 = new JLayeredPane();
		layeredPane_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), " ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		JLayeredPane layeredPane_2 = new JLayeredPane();
		layeredPane_2.setToolTipText("Please enter sequences starting with '>' symbol");
		layeredPane_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Sequences", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		
		textField_MSAScore = new JTextField();
		textField_MSAScore.setToolTipText("MSA Algorithm Score");
		textField_MSAScore.setText("0");
		textField_MSAScore.setFont(new Font("Tahoma", Font.PLAIN, 27));
		textField_MSAScore.setBounds(10, 49, 90, 50);
		layeredPane_1.add(textField_MSAScore);
		textField_MSAScore.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setToolTipText("");
		scrollPane.setViewportBorder(null);
		layeredPane_2.setLayer(scrollPane, 0);
		scrollPane.setBounds(10, 21, 774, 370);
		layeredPane_2.add(scrollPane);
		
		JTextArea txtrSequences = new JTextArea();
		txtrSequences.setText("Please enter sequences starting with '>' symbol");
		txtrSequences.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(txtrSequences.getText().trim().equals("Please enter sequences starting with '>' symbol"))
					txtrSequences.setText("");
				else{}
			    //do nothing
			}
			@Override
			public void focusLost(FocusEvent e) {
				 if(txtrSequences.getText().trim().equals("")){
					 txtrSequences.setText("Please enter sequences starting with '>' symbol");
				 }
			}
		});
		txtrSequences.setToolTipText("");
		scrollPane.setViewportView(txtrSequences);
		txtrSequences.setWrapStyleWord(true);
		txtrSequences.setLineWrap(true);
		//textArea.setDropMode(DropMode.ON);
		layeredPane_2.setLayer(txtrSequences, 1);
		txtrSequences.setFont(new Font("Courier New", Font.PLAIN, 12));
		layeredPane_2.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{txtrSequences}));
		GroupLayout groupLayout = new GroupLayout(frmHwGenomics.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(layeredPane_1))
						.addComponent(layeredPane_2, GroupLayout.PREFERRED_SIZE, 794, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(layeredPane_1)
						.addComponent(layeredPane, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
					.addGap(18)
					.addComponent(layeredPane_2, GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE))
		);
		
		
		lblCS_Score.setBounds(400, 20, 100, 14);
		layeredPane_1.add(lblCS_Score);
		
		textField_CS_Score = new JTextField();
		textField_CS_Score.setToolTipText("Center Star algorithm score");
		textField_CS_Score.setText("0");
		textField_CS_Score.setFont(new Font("Tahoma", Font.PLAIN, 27));
		textField_CS_Score.setColumns(10);
		textField_CS_Score.setBounds(400, 49, 90, 50);
		layeredPane_1.add(textField_CS_Score);
		
		JButton btnRun = new JButton("Run");
		btnRun.setAction(action);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				distancesList.clear();
				if(buttonGroup.getSelection() == rdbtnMsa.getModel()){
					msaIsSelected = true;
					String data = txtrSequences.getText().trim();
					int lineNum = countLines(data);
					
					if(!(lineNum < 2)){
						for (String line : txtrSequences.getText().split("\\n")) strList.add(line);
						ProgAligment temp = new ProgAligment(strList);
						resultList = temp.kekList;
						distancesList = temp.distancesList;
						textField_MSAScore.setText(String.valueOf(temp.finalScore));
						try(PrintWriter out = new PrintWriter("tree.txt")){
							 
				        } catch (IOException e1) {
							e1.printStackTrace(); 
						}
						temp.finalNode.print();
						btnShowTree.setEnabled(true);
						btnShowDistance.setEnabled(true);
						lblShowTree.setEnabled(true);
						lblShowDistance.setEnabled(true);
                    } else{
                    	JOptionPane.showMessageDialog(frmHwGenomics, "Please enter sequences!", "No Sequences!", JOptionPane.ERROR_MESSAGE);
                    	errorMsg=true;
                    }                    
					
					
					
				}else if(buttonGroup.getSelection() == rdbtnCenterStar.getModel()){
					msaIsSelected = false;
					if(file == ""){
						errorMsg=true;
						JOptionPane.showMessageDialog(frmHwGenomics,"File needs to be choose first!", "No File Selected", JOptionPane.ERROR_MESSAGE);
					}
					else{
						String data = txtrSequences.getText().trim();
						int lineNum = countLines(data);
						
						if(!(lineNum < 2)){
							for (String line : txtrSequences.getText().split("\\n")) strList.add(line);
							for (int i=0;i<strList.size();i++){
								CenterStar.sequence[i]=strList.get(i);
							}
						}else{
	                    	JOptionPane.showMessageDialog(frmHwGenomics, "Please enter sequences!", "No Sequences!", JOptionPane.ERROR_MESSAGE);
	                    	errorMsg=true;
						}
					}
				} else{
                	JOptionPane.showMessageDialog(frmHwGenomics, "Please select method!", "No Method", JOptionPane.ERROR_MESSAGE);
                	errorMsg=true;
				}
				
			}
		});
		btnRun.setBounds(364, 402, 89, 23);
		layeredPane_2.add(btnRun);
		
		JLabel lblAbout = new JLabel("About");
		lblAbout.setForeground(SystemColor.controlHighlight);
		lblAbout.setBounds(745, 426, 39, 14);
		layeredPane_2.add(lblAbout);
		lblAbout.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		       // you can open a new frame here as
		       // i have assumed you have declared "frame" as instance variable
		    JOptionPane.showMessageDialog(frmHwGenomics, "garay made it","very important message",JOptionPane.INFORMATION_MESSAGE);
		       

		    }  
		}); 
		
		
		
		
		
		
		
		
		btnLoadMatrix.setBounds(530, 49, 90, 50);
		layeredPane_1.add(btnLoadMatrix);
		

		lblSubMatrix.setBounds(530, 20, 100, 14);
		layeredPane_1.add(lblSubMatrix);
		
		
		btnShowTree.setAction(action_2);
		btnShowTree.setEnabled(false);
		btnShowTree.setBounds(140, 49, 90, 50);
		layeredPane_1.add(btnShowTree);
		
	
		
		
		
		
		
		
		//Set all disabled at start
				lblMSAScore.setEnabled(false);
				textField_MSAScore.setEnabled(false);
				textField_CS_Score.setEnabled(false);
				lblCS_Score.setEnabled(false);
				lblSubMatrix.setEnabled(false);
				btnLoadMatrix.setEnabled(false);
				btnShowTree.setEnabled(false);
				lblShowTree.setEnabled(false);
				btnShowDistance.setEnabled(false);
				lblShowDistance.setEnabled(false);
				
				
		
		
		frmHwGenomics.getContentPane().setLayout(groupLayout);
		

	}
	@SuppressWarnings("serial")
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Run");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			if(msaIsSelected){
				if(!errorMsg){
					showMSAResultsDialog(frmHwGenomics);
					//CLEAR THE LISTS
					resultList.clear();
					strList.clear();
					
				}
				errorMsg=false;
			} else {
				if(!errorMsg){
					//run center star alg
					showCenterStarResultsDialog(frmHwGenomics);
					//CLEAR THE LISTS
					resultList.clear();
					strList.clear();
				}
				errorMsg=false;
			}
		}
	}
	
	
	public static void showMSAResultsDialog(JFrame parent) {
		// create and configure a text area - fill it with exception text.
		final JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
		textArea.setEditable(false);
		for(int i=0; i<resultList.size();i++){
			textArea.append(resultList.get(i)+"\n");
		}
		// stuff it in a scrollpane with a controlled size.
		JScrollPane scrollPane = new JScrollPane(textArea);		
		scrollPane.setPreferredSize(new Dimension(800, 600));
		// pass the scrollpane to the joptionpane.				
		JOptionPane.showMessageDialog(parent, scrollPane, "Result", JOptionPane.INFORMATION_MESSAGE);
		//msaTextArea.setText("");
	}
	public static void showCenterStarResultsDialog(JFrame parent) {
		// create and configure a text area - fill it with exception text.
		final JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
		textArea.setEditable(false);
		textArea.setText(CenterStar.cumpute(matrix,strList.size()));
		textField_CS_Score.setText(String.valueOf(CenterStar.totalScore));
		// stuff it in a scrollpane with a controlled size.
		JScrollPane scrollPane = new JScrollPane(textArea);		
		scrollPane.setPreferredSize(new Dimension(800, 600));
		// pass the scrollpane to the joptionpane.				
		JOptionPane.showMessageDialog(parent, scrollPane, "Result", JOptionPane.INFORMATION_MESSAGE);
		//msaTextArea.setText("");
	}
	private void initMatrix(){
		file=file.replace(" ", "");
		for(int i=0;i<24;i++){
			matrix[i] = file.substring(i*3, i*3+3).toCharArray();		
		}
	}
	public int countLines(String str){
		String[] lines = str.split("\r\n|\r|\n");
		return  lines.length;
		}
	
	public static void showPairsDistanceDialog(JFrame parent) {
		// create and configure a text area - fill it with exception text.
		final JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
		textArea.setEditable(false);
		for(int i=0; i<distancesList.size();i++){
			textArea.append(distancesList.get(i)+"\n");
		}
		// stuff it in a scrollpane with a controlled size.
		JScrollPane scrollPane = new JScrollPane(textArea);		
		scrollPane.setPreferredSize(new Dimension(800, 600));
		// pass the scrollpane to the joptionpane.				
		JOptionPane.showMessageDialog(parent, scrollPane, "Result", JOptionPane.INFORMATION_MESSAGE);
		//msaTextArea.setText("");
	}
	
	
	public static void showGuideTree(JFrame parent) {
		// create and configure a text area - fill it with exception text.
		//final JTextArea textArea = new JTextArea();
		textArea_treePrint.setFont(new Font("Courier New", Font.PLAIN, 12));
		textArea_treePrint.setEditable(false);
		
		//FileReader reader = new FileReader("tree.txt");
		try {
			FileReader reader = new FileReader("tree.txt");
			textArea_treePrint.read(reader,"tree.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//textArea_treePrint.append();
		// stuff it in a scrollpane with a controlled size.
		JScrollPane scrollPane = new JScrollPane(textArea_treePrint);		
		scrollPane.setPreferredSize(new Dimension(800, 600));
		// pass the scrollpane to the joptionpane.				
		JOptionPane.showMessageDialog(parent, scrollPane, "Result", JOptionPane.INFORMATION_MESSAGE);
		//msaTextArea.setText("");
	}
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "Load Matrix");
			putValue(SHORT_DESCRIPTION, "Load Matrix");
		}
		public void actionPerformed(ActionEvent e) {
			if(fileSelected){
				initMatrix();
			}else{
				JOptionPane.showMessageDialog(frmHwGenomics, "Please select file!", "No File Selected", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	private class SwingAction_2 extends AbstractAction {
		public SwingAction_2() {
			putValue(NAME, "Show Tree");
			putValue(SHORT_DESCRIPTION, "Enabled after running MSA");
		}
		public void actionPerformed(ActionEvent e) {
			showGuideTree(frmHwGenomics);
		}
	}
	private class SwingAction_3 extends AbstractAction {
		public SwingAction_3() {
			putValue(NAME, "Show Pairs");
			putValue(SHORT_DESCRIPTION, "Enabled after running MSA");
		}
		public void actionPerformed(ActionEvent e) {
			showPairsDistanceDialog(frmHwGenomics);
		}
	}
}
