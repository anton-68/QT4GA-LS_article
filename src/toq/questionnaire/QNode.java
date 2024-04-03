/**
 * 
 */
package toq.questionnaire;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import toq.local_search.SubtaskDescriptor;

/**
 * Questionnaire node 
 * @author anton.bondarenko@gmail.com
 * Jan 9, 2010
 * 8:31:37 PM
 */
public interface QNode {
	/**
	 * Setters&Getters
	 * @return SubtaskDescriptor
	 */
	SubtaskDescriptor getSubtaskDescriptor();
	
	/**
	 * Setters&Getters
	 * @param SubtaskDescriptor
	 */
	void setSubtaskDescriptor(SubtaskDescriptor STD);
	
	/**
	 * Setters&Getters
	 * @return int root question No.
	 */
	int getRoot();
	
	/**
	 * Setters&Getters
	 * @param int root question No.
	 */
	void setRoot(int t);
	
	/**
	 * Setters&Getters
	 * @return parent QNode
	 */
	QNode getUplink();
	
	/**
	 * Setters&Getters
	 * @param parent QNode
	 */
	void setUplink(QNode qn);
	
	/**
	 * Setters&Getters
	 * @return left child QNode
	 */
	QNode getLeftChild();
	
	/**
	 * Setters&Getters
	 * @param left child QNode
	 */
	void setLeftChild(QNode qn );
	
	/**
	 * Setters&Getters
	 * @return right child QNode
	 */
	QNode getRightChild();
	
	/**
	 * Setters&Getters
	 * @param right child QNode
	 */
	void setRightChild(QNode qn);
	
	/**
	 * @return boolean 
	 */
	boolean isLeaf();
	
	/**
	 * @param PrintStream ps
	 * @throws FileNotFoundException 
	 */
	void printSubtree(PrintStream ps) throws FileNotFoundException;
}
