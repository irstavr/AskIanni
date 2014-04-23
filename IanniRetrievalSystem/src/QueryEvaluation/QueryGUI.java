package QueryEvaluation;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;


public class QueryGUI {

	private JFrame frame;
	private JTextField queryField;
	private JTextArea textArea;
	int retrievalModel = 0;
	private static HashMap<String, MutableInt> qMap;
	private static int qMaxFreq;
	/**
	 * Launch the application.
	 */
	public static void main(final HashMap<String,VocabularyEntry> vocabulary) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QueryGUI window = new QueryGUI(vocabulary);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public QueryGUI(HashMap<String,VocabularyEntry> vocabulary) {
		initialize(vocabulary);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(final HashMap<String,VocabularyEntry> vocabulary) {
		qMap = new HashMap<String, MutableInt>();
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setBackground(Color.DARK_GRAY);
		frame.setBounds(100, 100, 883, 404);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		queryField= new JTextField();
		queryField.setText("I'm searching for...");
		queryField.setColumns(10);
		
		JButton SearchButton = new JButton("Search");
		SearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					SearchButtonActionPerformed(evt,vocabulary);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
		JRadioButton NoneModelButton= new JRadioButton("None");
		NoneModelButton.setForeground(Color.LIGHT_GRAY);
		NoneModelButton.setBackground(Color.DARK_GRAY);
		 NoneModelButton.addActionListener(new ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                NoneModelButtonActionPerformed(evt);
	            }
	        });
		buttonGroup.add(NoneModelButton);

		JRadioButton VectorButton = new JRadioButton("Vector Space Model");
		VectorButton.setForeground(Color.LIGHT_GRAY);
		VectorButton.setBackground(Color.DARK_GRAY);
		VectorButton.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VectorButtonActionPerformed(evt);
            }
        });
		buttonGroup.add(VectorButton);

		JRadioButton OKAPIButton = new JRadioButton("OKAPIBM25");
		OKAPIButton.setForeground(Color.LIGHT_GRAY);
		OKAPIButton.setBackground(Color.DARK_GRAY);
		OKAPIButton.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKAPIButtonActionPerformed(evt);
            }
        });
		buttonGroup.add(OKAPIButton);

		JProgressBar progressBar = new JProgressBar();
		
		JLabel lblChooseRetrievalModel = new JLabel("Choose retrieval model:");
		lblChooseRetrievalModel.setForeground(Color.LIGHT_GRAY);
		
		setTextArea(new JTextArea());
		getTextArea().setEnabled(false);
		getTextArea().setEditable(false);
		getTextArea().setForeground(Color.LIGHT_GRAY);
		getTextArea().setBackground(Color.DARK_GRAY);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 853, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(getTextArea(), GroupLayout.DEFAULT_SIZE, 853, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(queryField, GroupLayout.PREFERRED_SIZE, 537, GroupLayout.PREFERRED_SIZE)
								.addComponent(SearchButton))
							.addGap(84)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(NoneModelButton)
								.addComponent(lblChooseRetrievalModel)
								.addComponent(VectorButton)
								.addComponent(OKAPIButton))
							.addGap(55))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(28)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblChooseRetrievalModel)
							.addGap(31)
							.addComponent(NoneModelButton)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(VectorButton)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(OKAPIButton)
							.addGap(72))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(queryField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
							.addComponent(SearchButton)
							.addGap(124)))
					.addComponent(getTextArea(), GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		frame.getContentPane().setLayout(groupLayout);
	}
		

	protected void OKAPIButtonActionPerformed(ActionEvent evt) {
		System.out.println("OKAPI Retrieval MOdel");
		retrievalModel = 2;
		
	}

	protected void VectorButtonActionPerformed(ActionEvent evt) {
		System.out.println("Vector Retrieval MOdel");
		retrievalModel = 1;
	}

	protected void NoneModelButtonActionPerformed(ActionEvent evt) {
		System.out.println("None Retrieval MOdel");
		retrievalModel = 0;
	}

	protected void SearchButtonActionPerformed(ActionEvent evt, HashMap<String,VocabularyEntry> vocabulary) throws IOException {
		//Vocabulary voc = new Vocabulary();
		//HashMap<String,VocabularyEntry> vocabulary = new HashMap<String,VocabularyEntry>();
        RetrievalModel model = null;

        /* Start counting time */
        long start, stop;
        start = System.currentTimeMillis();
        System.out.println("QueryEvaluator starts!");

        // MOVED TO QUERYEVALUATOR TO PREVENT OF RE-CREATING
        // VOCABULARY EACH TIME
//        /* Load Vocabulary from File into memory */
//        voc.setVocabulary("VocabularyFile.txt");
//        vocabulary = voc.getVocabulary();        

        /* According to the selected Retrieval model by user, instantiate it */
        model = chooseRetrievalModel(retrievalModel, vocabulary);

        /* Get the query results */
        System.out.println(">Query: "+ queryField.getText());
        QueryResults res = new QueryResults(this, vocabulary, model, queryField.getText());
        res.createResults();

        getTextArea().setEnabled(true);
                
        /* Stop counting time */
        stop = System.currentTimeMillis();

        /* Print Statistics */
        printStatistics(stop - start, res);
        System.out.println("QueryEvaluator ends!");
	}


	/* Get user selected Retrieval Model (0, 1, 2) and return the new instance of it */
	public static RetrievalModel chooseRetrievalModel(int opt, HashMap<String,VocabularyEntry> voc) throws IOException {
        switch (opt) {
            case 0:
            	return new NoneRetrievalModel();
            case 1:
                return new VectorSpaceModel(voc);
            case 2:
                return new OKAPIBM25();
            default:
                return null;
        }
    }

	/* Print statistics of the program time and size of results data */
	private static void printStatistics(long total_time, Object total_results) {		// TO CHANGE: set res type to long
		System.out.println("Time: " + ((double)total_time/1000)%60 );
	}

	/* used on QueryResults in order to print straightly the results on the GUI */
	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	public static HashMap<String, MutableInt> getqMap() {
		return qMap;
	}

	public static int getqMapTF(String word) {
		return qMap.get(word).get();
	}
	public void setqMap(HashMap<String, MutableInt> qMap) {
		QueryGUI.qMap = qMap;
	}
	
	public static void addToQmap(String word) {
		MutableInt count = qMap.get(word);
		
		if(count == null)
		{
			qMap.put(word, new MutableInt());
		}
		else
		{
			qMap.get(word).increment();
		}
		//int maxValueInMap=(Collections.max(qMap.values()));
		qMaxFreq = Collections.max(qMap.values(),new MyComparator()).get();
		
	}

	protected static class MyComparator implements Comparator<MutableInt>
	{

		@Override
		public int compare(MutableInt o1, MutableInt o2) {
			// TODO Auto-generated method stub
			if(o1.get() > o2.get())
			{
				return 1;
			}
			else if (o1.get()<o2.get())
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
		
	}
	public static float getqMaxFreq() {
		return qMaxFreq;
	}

	public static void setqMaxFreq(int qMaxFreq) {
		QueryGUI.qMaxFreq = qMaxFreq;
	}
	
	
}