/**
 * 
 */
package toq.questionnaire.impl;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import toq.local_search.SubtaskDescriptor;
import toq.questionnaire.QNode;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 10, 2010
 * 2:50:09 AM
 */
public class QNodeImpl implements QNode{
	
	/**
	 * Uplink
	 */
	QNode upLink;
	/**
	 * Left child
	 */
	QNode leftChild;
	/**
	 * Right child
	 */
	QNode rightChild;
	/**
	 * Root question
	 */
	int root;
	/**
	 * Subtask descriptor
	 */
	SubtaskDescriptor std;
	
	/**
	 * Default constructor
	 */
	public QNodeImpl() {}
	
	/**
	 * Constructor parameterized with subtask descriptor ref
	 * @param SubtaskDescriptor
	 */
	QNodeImpl(SubtaskDescriptor std) {
		this.std = std;
	}

	/* (non-Javadoc)
	 * @see toq.local_search.questionnaire.QNode#getLeftChild()
	 */
	@Override
	public QNode getLeftChild() {
		return leftChild;
	}

	/* (non-Javadoc)
	 * @see toq.questionnaire.QNode#getRightChild()
	 */
	@Override
	public QNode getRightChild() {
		return rightChild;
	}

	/* (non-Javadoc)
	 * @see toq.questionnaire.QNode#getRoot()
	 */
	@Override
	public int getRoot() {
		return root;
	}

	/* (non-Javadoc)
	 * @see toq.questionnaire.QNode#getSubtaskDescriptor()
	 */
	@Override
	public SubtaskDescriptor getSubtaskDescriptor() {
		return std;
	}

	/* (non-Javadoc)
	 * @see toq.questionnaire.QNode#getUplink()
	 */
	@Override
	public QNode getUplink() {
		return upLink;
	}

	/* (non-Javadoc)
	 * @see toq.questionnaire.QNode#isLeaf()
	 */
	@Override
	public boolean isLeaf() {
		if(rightChild == null)
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see toq.questionnaire.QNode#setRoot(int)
	 */
	@Override
	public void setRoot(int t) {
		root = t;
	}

	/* (non-Javadoc)
	 * @see toq.questionnaire.QNode#setLeftChild(toq.questionnaire.QNode)
	 */
	@Override
	public void setLeftChild(QNode qn) {
		leftChild = qn;
	}

	/* (non-Javadoc)
	 * @see toq.questionnaire.QNode#setRightChild(toq.questionnaire.QNode)
	 */
	@Override
	public void setRightChild(QNode qn) {
		rightChild = qn;
	}

	/* (non-Javadoc)
	 * @see toq.questionnaire.QNode#setSubtaskDescriptor(toq.local_search.SubtaskDescriptor)
	 */
	@Override
	public void setSubtaskDescriptor(SubtaskDescriptor std) {
		this.std = std;	
	}

	/* (non-Javadoc)
	 * @see toq.questionnaire.QNode#setUplink(toq.questionnaire.QNode)
	 */
	@Override
	public void setUplink(QNode qn) {
		upLink = qn;	
	}

	/* (non-Javadoc)
	 * @see toq.questionnaire.QNode#printSubtree(java.io.PrintStream)
	 */
	@Override
	public void printSubtree(PrintStream ps) throws FileNotFoundException {
		ps.println("Ankete:");
		this.getSubtaskDescriptor().getAnkete().print(ps);	
		ps.println("Root: " + this.getRoot());
		if(!this.isLeaf()) {
			leftChild.printSubtree(ps);
			rightChild.printSubtree(ps);
		}
	}

}
