/**
 * @name        Simple Java NotePad
 * @package     ph.notepad
 * @file        UI.java
 * @author      SORIA Pierre-Henry
 * @email       pierrehs@hotmail.com
 * @link        http://github.com/pH-7
 * @copyright   Copyright Pierre-Henry SORIA, All Rights Reserved.
 * @license     Apache (http://www.apache.org/licenses/LICENSE-2.0)
 * @create      2012-05-04
 * @update      2015-09-4
 *
 *
 * @modifiedby  Achintha Gunasekara
 * @modweb      http://www.achinthagunasekara.com
 * @modemail    contact@achinthagunasekara.com
 */

package simplejavatexteditor;

import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.concurrent.*;
import java.util.*;
public class Find extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    int startIndex=0;
        int select_start=-1;
    JLabel lab1, lab2;
    JTextField textF, textR;
    JButton findBtn, findNext, replace, replaceAll, cancel,findAll;
    private JTextArea txt;
    int numThreads=4;

    public Find(JTextArea text) {
        this.txt = text;

        lab1 = new JLabel("Find:");
        lab2 = new JLabel("Replace:");
        textF = new JTextField(30);
        textR = new JTextField(30);
        findBtn = new JButton("Find");
        findNext = new JButton("Find Next");
        replace = new JButton("Replace");
        replaceAll = new JButton("Replace All");
        findAll=new JButton("Find All");
        cancel = new JButton("Cancel");

        // Set Layout NULL
        setLayout(null);

        // Set the width and height of the label
        int labWidth = 80;
        int labHeight = 20;

        // Adding labels
        lab1.setBounds(10,10, labWidth, labHeight);
        add(lab1);
        textF.setBounds(10+labWidth, 10, 120, 20);
        add(textF);
        lab2.setBounds(10, 10+labHeight+10, labWidth, labHeight);
        add(lab2);
        textR.setBounds(10+labWidth, 10+labHeight+10, 120, 20);
        add(textR);

        // Adding buttons
        findBtn.setBounds(225, 6, 115, 20);
        add(findBtn);
        findBtn.addActionListener(this);

        findNext.setBounds(225, 28, 115, 20);
        add(findNext);
        findNext.addActionListener(this);

        findAll.setBounds(225, 50, 115, 20);
        add(findAll);
        findAll.addActionListener(this);

        replace.setBounds(225, 72, 115, 20);
        add(replace);
        replace.addActionListener(this);
        
        replaceAll.setBounds(225, 94, 115, 20);
        add(replaceAll);
        replaceAll.addActionListener(this);
        
        cancel.setBounds(225,116, 115, 20);
        add(cancel);
        cancel.addActionListener(this);


        // Set the width and height of the window
        int width = 360;
        int height = 200;

        // Set size window
        setSize(width,height);

        // Set window position
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        setLocation(center.x-width/2, center.y-height/2);
        setVisible(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            	//JOptionPane.showMessageDialog(null, "yo");
            	removeHighlights();
                }
            });
        
}

    public void find() {
        select_start = txt.getText().toLowerCase().indexOf(textF.getText().toLowerCase());
        if(select_start == -1)
        {
            startIndex = 0;
            JOptionPane.showMessageDialog(null, "Could not find \"" + textF.getText() + "\"!");
            return;
        }
        if(select_start == txt.getText().toLowerCase().lastIndexOf(textF.getText().toLowerCase()))
        {
            startIndex = 0;
        }
        int select_end = select_start + textF.getText().length();
        txt.select(select_start, select_end);
    }
    public Set<SearchParallel> splitString(String text,String search)
	{
		Set<SearchParallel> mySet=new HashSet<SearchParallel>();
		int length=text.length();
		for(int i=0;i<numThreads;i++)
		{
			
			int startIndex=i*length/numThreads;
			int endIndex=(i+1)*length/numThreads;
			String temp=text.substring(startIndex,endIndex);
			//System.out.println("TEMP: "+temp);
			if(endIndex<text.length())
			{
				String concatTemp= temp.substring(temp.length()-search.length())+text.substring(endIndex,endIndex+search.length());
				mySet.add(new SearchParallel(endIndex-search.length(),concatTemp,search));
				//System.out.println("CONCAT TEMP:"+concatTemp+ (endIndex-search.length()));
			}
			mySet.add(new SearchParallel(startIndex,temp,search));
			
		}
		return mySet;
	}   
    public void removeHighlights()
    {
    	txt.getHighlighter().removeAllHighlights();
    }
    public void findAll()
    {
    	//JOptionPane.showMessageDialog(null, "yo");
    	boolean found=false;
    	ExecutorService ex= Executors.newFixedThreadPool(numThreads);
    	List<Future<ArrayList<Integer>>> futures;
    	removeHighlights();
    	int count=0;
		try
		{
			long start=System.currentTimeMillis();
			Set<SearchParallel> t=splitString(txt.getText(),textF.getText());
			futures=ex.invokeAll(t);
			long end=System.currentTimeMillis();
			System.out.println("Time :"+(end-start));
			Highlighter h=txt.getHighlighter();
			for(Future<ArrayList<Integer>> f:futures)
			{
				List<Integer> temp=f.get();
				if(temp.size()!=0)				
				{
					for(int occ:temp)
					{
						h.addHighlight(occ, occ+textF.getText().length(), DefaultHighlighter.DefaultPainter);
						count++;
					}
					System.out.println(temp);
					found=true;
					
				}
			}
			if(!found)
			{
				JOptionPane.showMessageDialog(null, "Could not find \"" + textF.getText() + "\"!");
			}
			System.out.println(count);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		ex.shutdown();			
	
}
    
    public void findNext() {
        String selection = txt.getSelectedText();
        try
        {
            selection.equals("");
        }
        catch(NullPointerException e)
        {
            selection = textF.getText();
            try
            {
                selection.equals("");
            }
            catch(NullPointerException e2)
            {
                selection = JOptionPane.showInputDialog("Find:");
                textF.setText(selection);
            }
        }
        try
        {
            int select_start = txt.getText().toLowerCase().indexOf(selection.toLowerCase(), startIndex);
            int select_end = select_start+selection.length();
            txt.select(select_start, select_end);
            startIndex = select_end+1;

            if(select_start == txt.getText().toLowerCase().lastIndexOf(selection.toLowerCase()))
            {
                startIndex = 0;
            }
        }
        catch(NullPointerException e)
        {}
    }

    public void replace() {
        try
        {
            find();
            if (select_start != -1)
            txt.replaceSelection(textR.getText());
        }
        catch(NullPointerException e)
        {
            System.out.print("Null Pointer Exception: "+e);
        }
    }

    public void replaceAll() {
        txt.setText(txt.getText().toLowerCase().replaceAll(textF.getText().toLowerCase(), textR.getText()));
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == findBtn)
        {
           find();
        }
        else if(e.getSource() == findNext)
        {
           findNext();
        }
        else if(e.getSource() == replace)
        {
            replace();
        }
        else if(e.getSource() == replaceAll)
        {
           replaceAll();
        }
        else if(e.getSource() == findAll)
        {
           findAll();
        }
        else if(e.getSource() == cancel)
        {
           removeHighlights();
           this.setVisible(false);
        }
   }

}