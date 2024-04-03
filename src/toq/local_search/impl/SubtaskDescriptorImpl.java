/**
 * 
 */
package toq.local_search.impl;

import toq.ankete.Ankete;
import toq.local_search.SubtaskDescriptor;
import toq.questionnaire.QNode;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 10, 2010
 * 3:16:31 PM
 */
public class SubtaskDescriptorImpl implements SubtaskDescriptor {
	/**
	 * Root node of the tree
	 */
	QNode root;
	
	/**
	 * Ankete
	 */
	Ankete a;
	
	/**
	 * Preference function object
	 */
	int pf;
	
	/**
	 * Characteristic function value
	 */
	double cfv;
	
	/**
	 * Subtree cost
	 */
	double cost;

	/**
	 * Default constructor
	 */
	SubtaskDescriptorImpl() {}
	
	/**
	 * Cloning constructor ommiting root pointer
	 * @param subtask descriptor
	 */
	SubtaskDescriptorImpl(SubtaskDescriptorImpl std) {
		this.a = std.a;
		this.pf = std.pf;
		this.cfv = std.cfv;
		this.cost = std.cost;
	}
	
	/**
	 * Parameterized constructor
	 * @param Ankete a
	 * @param PFunctionType pf
	 * @param double cfv
	 */
	SubtaskDescriptorImpl(Ankete a, int pf, double cfv) {
		this.a = a;
		this.pf = pf;
		this.cfv = cfv;
	}
	
	/* (non-Javadoc)
	 * @see toq.local_search.SubtaskDescriptor#getAnkete()
	 */
	@Override
	public Ankete getAnkete() {
		return a;
	}

	/* (non-Javadoc)
	 * @see toq.local_search.SubtaskDescriptor#getCFunctionValue()
	 */
	@Override
	public double getCFunctionValue() {
		return cfv;
	}

	/* (non-Javadoc)
	 * @see toq.local_search.SubtaskDescriptor#getCost()
	 */
	@Override
	public double getCost() {
		return cost;
	}

	/* (non-Javadoc)
	 * @see toq.local_search.SubtaskDescriptor#getPFuction()
	 */
	@Override
	public int getPFuction() {
		return pf;
	}

	/* (non-Javadoc)
	 * @see toq.local_search.SubtaskDescriptor#setAnkete(toq.local_search.ankete.Ankete)
	 */
	@Override
	public void setAnkete(Ankete a) {
		this.a = a;
	}

	/* (non-Javadoc)
	 * @see toq.local_search.SubtaskDescriptor#setCFunctionValue(double)
	 */
	@Override
	public void setCFunctionValue(double cfv) {
		this.cfv = cfv;
	}

	/* (non-Javadoc)
	 * @see toq.local_search.SubtaskDescriptor#setCost(double)
	 */
	@Override
	public void setCost(double cost) {
		this.cost = cost;
	}

	/* (non-Javadoc)
	 * @see toq.local_search.SubtaskDescriptor#setPFuction(toq.local_search.pfunction.PFunctionType)
	 */
	@Override
	public void setPFuction(int pf) {
		this.pf = pf;
	}

	/* (non-Javadoc)
	 * @see toq.local_search.SubtaskDescriptor#getRoot()
	 */
	@Override
	public QNode getRoot() {
		return this.root;
	}

	/* (non-Javadoc)
	 * @see toq.local_search.SubtaskDescriptor#setRoot(toq.questionnaire.QNode)
	 */
	@Override
	public void setRoot(QNode root) {
		this.root = root;	
	}

	/* (non-Javadoc)
	 * @see toq.local_search.SubtaskDescriptor#updateCosts()
	 */
	@Override
	public double updateCosts() { 
/*		System.out.println("LOG:updateCosts has been invoked");
		System.out.println("LOG:NOE = " + a.getNOE());
		System.out.println("LOG:NOQ = " + a.getNOQ());
		System.out.println("LOG:root = " + root);
		System.out.println("LOG:root = " + root.getRoot());*/
		if(a.getNOE() > 1) {
			double p = 0.0;
			for(int i = 0; i < a.getNOE(); i++)
				p += a.getProbability(i);	
			cost = a.getCost(root.getRoot()) * p + 
					root.getLeftChild().getSubtaskDescriptor().updateCosts() + 
					root.getRightChild().getSubtaskDescriptor().updateCosts();
		}
		else
			cost = 0.0;
		return cost;		
	}

}
