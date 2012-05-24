/*
# KS Boolean Expression, Copyright (c) 2012 The Authors. / ks.contrubutors@gmail.com
#
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.undo.UndoManager;

import org.xhtmlrenderer.css.style.derived.RectPropertySet;
import org.xhtmlrenderer.render.Box;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;


/**
 * Crée le panneau principal de l'application
 * @author Meradi, Hamoudi
 *
 */
public class MainPanel implements View {
	

	private JSplitPane splitPane = new JSplitPane();
	private JTree tree;
	private JScrollPane scrollPane;
    private UndoManager undo = new UndoManager();
	private Controller controller;
	private UserPreferences prefs;
	private Action action;
	private UpperCaseField textField;
	private XHTMLPanel xHTMLPanel;
	public  JLabel lblF;
	
	/**
	 * Constructeurs
	 * @param prefs
	 */
	public MainPanel(UserPreferences prefs){
		this.prefs=prefs;
	}
	
	/**
	 * Crée et retourne le panneau principal
	 * @param tree un arbre pour naviguer entre les fonction
	 * @param controller le controller de l'application
	 * @param b true pour afficher la page d'acceuil
	 * @return le panneau principal
	 */
	public JSplitPane getMainPane(JTree tree,Controller controller, boolean b){
		    this.controller = controller;
		    if (scrollPane ==null) scrollPane = new JScrollPane();
		    scrollPane.setViewportView(tree);
			scrollPane.setMinimumSize(new Dimension(100,100));
			scrollPane.setPreferredSize(new Dimension(200,200));

			splitPane.setContinuousLayout(true);
			splitPane.setOneTouchExpandable(false);
			
			splitPane.setRightComponent(getRightComponent(b));
			splitPane.setLeftComponent(scrollPane);
			return splitPane;
		}
	/**
	 * Retourne le composant de droite du panneau principal<br>
	 * Il contient un panneau pour l'affichage de text HTML<br>
	 * et un champ pour introduire les expression logiques.
	 * @param b true pour afficher la page d'acceuil
	 * @return mainPanel qui est le composant de droite
	 */
	private JPanel getRightComponent(boolean b){
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		if (textField ==null)
		textField = new UpperCaseField();
		panel.add(textField, BorderLayout.CENTER);
		textField.setColumns(10);
		textField.addKeyListener(new KeyListener(){
		
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode()==KeyEvent.VK_ENTER && textField.getText().compareTo("")!=0){
					if(prefs.detailledSolution)
			        	  controller.simplify(SolutionType.DETAILLED_SOLUTION);
			        	  else controller.simplify(SolutionType.MINIMIZED_FUNCTION);
				}
	  
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			
		});
		Document doc = textField.getDocument();
		doc.addUndoableEditListener(new MyUndoableEditListener());
		
		lblF = new JLabel(Tools.getLocalizedString("FORMULA"));
		lblF.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                                    BorderFactory.createTitledBorder(""),
                                    BorderFactory.createEmptyBorder(6,6,6,6)),
                    lblF.getBorder()));
		try {
			action = new ControllerAction(
		          prefs, this.getClass(), "LAUNCH_SIMPLIFICATION", controller, "simplify", SolutionType.DETAILLED_SOLUTION);
		    } catch (NoSuchMethodException ex) {
		      throw new RuntimeException(ex);
		    }
		JButton validateButton = new JButton(action);
		
		textField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				action.setEnabled(true);
				controller.program.setEnabled(View.ActionType.MINIMIZED_FUNCTION, true);
			    controller.program.setEnabled(View.ActionType.DETAILED_SOLUTION, true);
				
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if (textField.getText().length()==0) action.setEnabled(false);
				else action.setEnabled(true);
			}		 
		});
		panel.add(validateButton,BorderLayout.EAST);
		panel.add(lblF, BorderLayout.WEST);
		mainPanel.add(panel, BorderLayout.NORTH);
		
		  xHTMLPanel = new XHTMLPanel();
		  
		  if(b){ 
		  try {
		    	xHTMLPanel.setDocument(new File(Tools.getLocalizedString("WELCOME_PAGE")));
			} catch (Exception e) {
				System.err.println( e.getMessage() );
			}
		  }
		    
		    FSScrollPane scroll = new FSScrollPane(xHTMLPanel);
		    mainPanel.add(scroll, BorderLayout.CENTER);
		return mainPanel;
	}
	
	/**
	 * Retourne l'arbre de nivigation entre les fonctions.
	 * @return
	 */
	public JTree getJtree(){
		return this.tree;
	}
	
	/**
	 * Set l'arbre de nivigation entre les fonctions. 
	 * @param tree
	 */
	public void setJTree(JTree tree){
		this.tree = tree;
	}

    
  
    public class MyUndoableEditListener
                    implements UndoableEditListener {
        public void undoableEditHappened(UndoableEditEvent e) {
            undo.addEdit(e.getEdit());
            updateUndoState();
            updateRedoState();
        }
    }

	public void updateUndoState() {
		this.controller.updateUndoState();
	}

	public void updateRedoState() {
		this.controller.updateRedoState();
		
	}	

	
	
	
	
	  
	  /**
	     * Lire un fichier HTML à l'emplacement d'une ancre.
	     */
	    public void setDocumentRelative(String id) {
	        try{    
	    	Box box = xHTMLPanel.getSharedContext().getBoxById(id);
	            if (box != null) {
	                Point pt;
	                if (box.getStyle().isInline()) {
	                    pt = new Point(box.getAbsX(), box.getAbsY());
	                } else {
	                    RectPropertySet margin = box.getMargin(xHTMLPanel.getLayoutContext());
	                    pt = new Point(
	                            box.getAbsX() + (int)margin.left(),
	                            box.getAbsY() + (int)margin.top());
	                }
	                xHTMLPanel.scrollTo(pt);
	            }
	            
	            
	        }
	        catch(NullPointerException e){
	        	System.err.println( e.getMessage() );
	        }
	    }


public void setTextField(UpperCaseField textField) {
	this.textField = textField;
}


public UpperCaseField getTextField() {
	return textField;
}


public void setxHTMLPanel(XHTMLPanel xHTMLPanel) {
	this.xHTMLPanel = xHTMLPanel;
}

public XHTMLPanel getxHTMLPanel() {
	return xHTMLPanel;
}


public void setUndo(UndoManager undo) {
	this.undo = undo;
}


public UndoManager getUndo() {
	return undo;
}
}


/**
 * Un JTextField qui affiche le text introduit en majuscules.
 *
 */
class UpperCaseField extends JTextField {
	 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UpperCaseField(){
    	super();
    }


	public UpperCaseField(int cols) {
        super(cols);
    }

    protected Document createDefaultModel() {
        return  new UpperCaseDocument();
    }

    static class UpperCaseDocument extends PlainDocument {

		private static final long serialVersionUID = 1L;

		public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {

            if (str == null) {
                return;
            }
            char[] upper = str.toCharArray();
            for (int i = 0; i < upper.length; i++) {
                upper[i] = Character.toUpperCase(upper[i]);
            }
            super.insertString(offs, new String(upper), a);
        }
    }
}
 

	


