/**
 * 
 */
package toq.local_search.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import toq.ankete.Ankete;
import toq.ankete.impl.AnketeImpl;
import toq.functions.CFunctionType;
import toq.functions.PFunctionType;
import toq.functions.impl.CostEntropyCFImpl;
import toq.functions.impl.CostPFImpl;
import toq.functions.impl.DeltaEntropyPFImpl;
import toq.functions.impl.EntropyByCostPFImpl;
import toq.functions.impl.EntropyCFImpl;
import toq.functions.impl.N2RRatioCFImpl;
import toq.functions.impl.RQMPFImpl;
import toq.local_search.Solution;
import toq.local_search.SolutionMap;
import toq.local_search.SubtaskDescriptor;
import toq.questionnaire.QNode;
import toq.questionnaire.impl.QNodeImpl;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 11, 2010
 * 3:39:28 PM
 */
public class SolutionImpl implements Solution {
	/**
	 * Current solution root task
	 */
	SubtaskDescriptorImpl current;
	
	/**
	 * Pretender's root task
	 */
	SubtaskDescriptorImpl pretender;
	
	/**
	 * Current.cost - Pretender.cost
	 */
	double improvement;
	
	/**
	 * Pretender's position on the tree 
	 */
	QNode pretenderPosition;
	
	/**
	 * Characteristic function
	 */
	CFunctionType cf;
	
	/**
	 * Preference functions array
	 */
	PFunctionType[] pfa;
	
	/**
	 * Solution split map
	 */
	SolutionMapImpl map;

	/**
	 * Default constructor
	 */
	SolutionImpl(){}
	
	/**
	 * Ankete-based constructor
	 * @param Ankete a
	 * @param Characteristic function cf
	 * @param Preference functions array pfa
	 * @param Preference function for building of 
	 * initial solution
	 */
	public SolutionImpl(Ankete a, CFunctionType cf, PFunctionType[] pfa, int initialPf){
		this.cf = cf;
		this.pfa = pfa;
		current = new SubtaskDescriptorImpl(a, initialPf, cf.getValue(a));	
		buildQuestionnaire(current, initialPf);
		map = new SolutionMapImpl(current);
	}
	
	/* (non-Javadoc)
	 * @see toq.local_search.Solution#buildQuestionnaire(toq.local_search.SubtaskDescriptor)
	 */
	@Override
	public QNode buildQuestionnaire(SubtaskDescriptor std) {
		Ankete st[] = new AnketeImpl[2]; // TODO Verify
		QNode root = new QNodeImpl();
		std.setRoot(root);
		root.setSubtaskDescriptor(std);
		double p = 0.0;
		for(int i = 0; i < std.getAnkete().getNOE(); i++)
			p += std.getAnkete().getProbability(i);
		if(std.getAnkete().getNOE() > 1){
			int pf = map.getPreferenceFunction(std.getCFunctionValue());
			root.setRoot(pfa[pf].getRoot(std.getAnkete()));
			st = std.getAnkete().getSubanketes(root.getRoot());
			QNode leftChild = buildQuestionnaire(new SubtaskDescriptorImpl(st[0], pf, cf.getValue(st[0])), pf);
			QNode rightChild = buildQuestionnaire(new SubtaskDescriptorImpl(st[1], pf, cf.getValue(st[1])), pf);
			leftChild.setUplink(root);
			rightChild.setUplink(root);
			root.setLeftChild(leftChild);
			root.setRightChild(rightChild);
			std.setCost(std.getAnkete().getCost(root.getRoot()) * p + 
					leftChild.getSubtaskDescriptor().getCost() + 
					rightChild.getSubtaskDescriptor().getCost());
		}
		else
			std.setCost(0.0);
		return root;
	}

	/* (non-Javadoc)
	 * @see toq.local_search.Solution#buildQuestionnaire(toq.local_search.SubtaskDescriptor, int)
	 */
	@Override
	public QNode buildQuestionnaire(SubtaskDescriptor std, int initialPf) {
		Ankete st[] = new Ankete[2];
		QNode root = new QNodeImpl();
		std.setRoot(root);
		root.setSubtaskDescriptor(std);
		double p = 0.0;
		for(int i = 0; i < std.getAnkete().getNOE(); i++)
			p += std.getAnkete().getProbability(i);
		if(std.getAnkete().getNOE() > 1){
			//System.out.println("n before calling getRoot:" + std.getAnkete().getNOE());
			root.setRoot(pfa[initialPf].getRoot(std.getAnkete()));			
			st = std.getAnkete().getSubanketes(root.getRoot());
			QNode leftChild = buildQuestionnaire(new SubtaskDescriptorImpl(st[0], initialPf, cf.getValue(st[0])), initialPf);
			QNode rightChild = buildQuestionnaire(new SubtaskDescriptorImpl(st[1], initialPf, cf.getValue(st[1])), initialPf);
			leftChild.setUplink(root);
			rightChild.setUplink(root);
			root.setLeftChild(leftChild);
			root.setRightChild(rightChild);
			std.setCost(std.getAnkete().getCost(root.getRoot()) * p + 
					leftChild.getSubtaskDescriptor().getCost() + 
					rightChild.getSubtaskDescriptor().getCost());
		}
		else
			std.setCost(0.0);
		return root;
	}

	/* (non-Javadoc)
	 * @see toq.local_search.Solution#searchAll()
	 */
	@Override
	public void searchAll(SubtaskDescriptor std) { if(std.getAnkete().getNOE() > 1) {		
/*		System.out.println("LOG:seacrhAll has been invoked");
		System.out.println("LOG:NOE = " + std.getAnkete().getNOE());
		System.out.println("LOG:NOQ = " + std.getAnkete().getNOQ());*/
		for (int i = 0; i < pfa.length; i++) if(i != std.getPFuction()) {
			SubtaskDescriptorImpl next = new SubtaskDescriptorImpl((SubtaskDescriptorImpl)std);
			map.setPreferenceFunction(map.getCell(std.getCFunctionValue()), i);
			buildQuestionnaire(next);
/*			System.out.println("LOG:seacrhAll has been invoked");
			System.out.println("LOG:next.getCost() = " + next.getCost());
			System.out.println("LOG:std.getCost() = " + std.getCost());
			System.out.println("LOG:Delta = " + (std.getCost() - next.getCost()));
			System.out.println("LOG:improvement = " + improvement);*/
			if((std.getCost() - next.getCost()) > improvement){
/*				System.out.println("LOG:Inmprovement found");
				System.out.println("LOG:Delta = " + (next.getCost() - std.getCost()));
				System.out.println("LOG:Current improvement = " + improvement);	*/		
				pretender = next;
				pretenderPosition = std.getRoot();
				improvement = std.getCost() - next.getCost();
			}
			map.setPreferenceFunction(map.getCell(std.getCFunctionValue()), std.getPFuction());
		}
		
		if(std.getRoot().getLeftChild() != null ){
			SubtaskDescriptorImpl childSTD = (SubtaskDescriptorImpl) std.getRoot().getLeftChild().getSubtaskDescriptor();
			if (childSTD != null)
				searchAll(childSTD);
		}
		if(std.getRoot().getRightChild() != null ){
			SubtaskDescriptorImpl childSTD = (SubtaskDescriptorImpl) std.getRoot().getRightChild().getSubtaskDescriptor();
			if (childSTD != null)
				searchAll(childSTD);
		}
	}}
	
	/* (non-Javadoc)
	 * @see toq.local_search.Solution#searchFirst(toq.local_search.SubtaskDescriptor)
	 */
	@Override
	public boolean searchFirst(SubtaskDescriptor std) {if(std.getAnkete().getNOE() > 1) {
		pretender = new SubtaskDescriptorImpl((SubtaskDescriptorImpl)std);
		for (int i = 0; i < pfa.length; i++) if(i != std.getPFuction()) {
			SubtaskDescriptorImpl next = new SubtaskDescriptorImpl((SubtaskDescriptorImpl)std);
			map.setPreferenceFunction(map.getPreferenceFunction(std.getCFunctionValue()), i);
			buildQuestionnaire(next);
			if(next.getCost() <= pretender.getCost())
				pretender = next;
			map.setPreferenceFunction(map.getPreferenceFunction(std.getCFunctionValue()), std.getPFuction());
		}
		if(pretender.getCost() < std.getCost())
			return true;
		else {
			QNode leftChild = std.getRoot().getLeftChild();
			QNode rightChild = std.getRoot().getRightChild();
			SubtaskDescriptorImpl leftSTD = null, rightSTD = null;
			if(leftChild != null)
				leftSTD = (SubtaskDescriptorImpl) std.getRoot().getLeftChild().getSubtaskDescriptor();
			if(rightChild != null)
				rightSTD = (SubtaskDescriptorImpl) std.getRoot().getRightChild().getSubtaskDescriptor();
			if(leftChild != null && leftSTD != null && searchFirst(leftSTD))
				return true;
			else
				if(rightChild != null && rightSTD != null)
					return searchFirst(rightSTD);
		}}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see toq.local_search.Solution#applyPretender()
	 */
	@Override
	public void applyPretender() { if(pretender != null) {
		if(pretenderPosition.getUplink() == null) 
			current = pretender;
		else {
			if(pretenderPosition.getUplink().getLeftChild() == pretenderPosition)
				pretenderPosition.getUplink().setLeftChild(pretender.getRoot());
			else
				pretenderPosition.getUplink().setRightChild(pretender.getRoot());
			pretender.getRoot().setUplink(pretenderPosition.getUplink());
		}
		current.updateCosts();
		map = new SolutionMapImpl(current);
		pretender = null;
		improvement = 0.0;
		pretenderPosition = null;
	}}
	
	/* (non-Javadoc)
	 * @see toq.local_search.Solution#getPretenderQuestionnaire()
	 */
	@Override
	public QNode getPretenderQuestionnaire() {
		return pretender.getRoot();
	}

	/* (non-Javadoc)
	 * @see toq.local_search.Solution#getImprovement()
	 */
	@Override
	public double getImprovement() {
		return improvement;
	}
	
	/* (non-Javadoc)
	 * @see toq.local_search.Solution#getCFunction()
	 */
	@Override
	public CFunctionType getCFunction() {
		return this.cf;
	}

	/* (non-Javadoc)
	 * @see toq.local_search.Solution#getCost()
	 */
	@Override
	public double getCost() {
		return current.getCost();
	}
	
	/* (non-Javadoc)
	 * @see toq.local_search.Solution#getPretenderCost()
	 */
	@Override
	public double getPretenderCost() {
		return ( pretender != null ? pretender.getCost() : 0.0);
	}


	@Override
	public SolutionMap getMap() {
		return map;
	}
	
	/* (non-Javadoc)
	 * @see toq.local_search.Solution#getPfunctions()
	 */
	@Override
	public PFunctionType[] getPfunctions() {
		return pfa;
	}

	/* (non-Javadoc)
	 * @see toq.local_search.Solution#getQuestionnaire()
	 */
	@Override
	public QNode getQuestionnaire() {
		return current.getRoot();
	}

	/* (non-Javadoc)
	 * @see toq.local_search.Solution#setCFunction(toq.functions.CFunctionType)
	 */
	@Override
	public void setCFunction(CFunctionType cf) {
		this.cf = cf;
	}

	/* (non-Javadoc)
	 * @see toq.local_search.Solution#setPfunctions(toq.functions.PFunctionType[])
	 */
	@Override
	public void setPfunctions(PFunctionType[] pfa) {
		this.pfa = pfa;
	}

	/**
	 * Unit test
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Ankete a = new AnketeImpl("dat/test0.dat");
		PrintStream ps = new PrintStream(new File("res/test0.res"));
		ps.println("Ankete:");
		a.print(ps);		
		PFunctionType[] pfa = new PFunctionType[4];
		pfa[0] = new CostPFImpl();
		pfa[1] = new DeltaEntropyPFImpl();
		pfa[2] = new EntropyByCostPFImpl();
		pfa[3] = new RQMPFImpl();
		CFunctionType[] cfa = new CFunctionType[3];
		cfa[0] = new EntropyCFImpl();
		cfa[1] = new CostEntropyCFImpl();
		cfa[2] = new N2RRatioCFImpl();
		ps.println("Greedy methods results");
		for(int i = 0; i < pfa.length; i++){
			ps.print(pfa[i].getName() + ": ");
			ps.println((new SolutionImpl(a, cfa[0], pfa, i)).getCost());
		}
		ps.println("Local search results");
		for(int i = 0; i < cfa.length; i++){
			ps.print(cfa[i].getName() + ": ");
			Solution s = new SolutionImpl(a, cfa[0], pfa, i);
			ps.println("Initial cost :" +  s.getCost());
			for(int j = 0; j < 5; j++){
				s.searchAll(s.getQuestionnaire().getSubtaskDescriptor());
				ps.println("Iteration " + j + ": " + s.getCost());
			}
		}
	}

}
