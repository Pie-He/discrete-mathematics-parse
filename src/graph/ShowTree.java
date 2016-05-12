package graph;

/*
 * [The "BSD license"]
 * Copyright (c) 2011, abego Software GmbH, Germany (http://www.abego.org)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the abego Software GmbH nor the names of its 
 *    contributors may be used to endorse or promote products derived from this 
 *    software without specific prior written permission.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */

import java.awt.Container;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;

import parse.Parse.Node;
public class ShowTree {
	static int i;
	private static void showInFrame(JComponent panel) {
		JFrame frame = new JFrame(i+"");
		Container contentPane = frame.getContentPane();
		((JComponent) contentPane).setBorder(BorderFactory.createEmptyBorder(
				10, 10, 10, 10));
		//Frame.pack();
		//contentPane.add(panel);
		JScrollPane jsp=new JScrollPane();
		jsp.getViewport().add(panel);
		contentPane.add(jsp);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setSize(600, 300);
		frame.setVisible(true);
		
		//jsp.add(panel);
		//contentPane.add(jsp);
		//frame.getContentPane().add(new JScrollPane());
		i++;
	}

	private static TreeForTreeLayout<Node> getTree(LinkedList<Node> nodes) {
		DefaultTreeForTreeLayout<Node> tree = new DefaultTreeForTreeLayout<Node>(
				nodes.get(0));
		for (int i = 0; i < nodes.size(); i++) {
			if(nodes.get(i).left!=null){
				tree.addChild(nodes.get(i), nodes.get(i).left);
			}
			if(nodes.get(i).right!=null){
				tree.addChild(nodes.get(i), nodes.get(i).right);
			}
		}
		return tree;
	}
	public static void show(LinkedList<Node> nodes) {
		// get the sample tree
		TreeForTreeLayout<Node> tree = getTree(nodes);

		// setup the tree layout configuration
		double gapBetweenLevels = 50;
		double gapBetweenNodes = 10;
		DefaultConfiguration<Node> configuration = new DefaultConfiguration<Node>(
				gapBetweenLevels, gapBetweenNodes);

		// create the NodeExtentProvider for TextInBox nodes
		TreeNodeNodeExtentProvider nodeExtentProvider = new TreeNodeNodeExtentProvider();

		// create the layout
		TreeLayout<Node> treeLayout = new TreeLayout<Node>(tree,
				nodeExtentProvider, configuration);

		// Create a panel that draws the nodes and edges and show the panel
		TreePane panel = new TreePane(treeLayout);
		showInFrame(panel);
	}
}
