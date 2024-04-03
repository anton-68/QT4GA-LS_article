/**
 * 
 */
package toq.local_search.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import toq.local_search.SolutionMap;
import toq.local_search.SolutionMapCell;
import toq.local_search.SubtaskDescriptor;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 10, 2010
 * 3:58:38 AM
 */
public class SolutionMapImpl implements SolutionMap {
	/**
	 * Solution map array
	 */
	SolutionMapCell map[];     
	
	/**
	 * Default constructor
	 */
	SolutionMapImpl(){}
	
	/**
	 * Solution-based constructor 
	 * For zero-question ankete the behavior is not predictable
	 * @param SubtaskDescriptor std
	 */
	SolutionMapImpl(SubtaskDescriptor std){
		TreeMap<Double, Integer> ds = new TreeMap<Double, Integer>();
		collectCFValues(std, ds);
		map = new SolutionMapCell[ds.size()];
		TreeSet<Double> ts = new TreeSet<Double>(ds.keySet());
		Iterator<Double> it = ts.iterator();
		double lower = it.next();
		for(int i = 0; i < ds.size() - 1; i++){
			double upper = it.next();
			map[i] = new SolutionMapCellImpl((lower + upper)/2, ds.get(lower));
			lower = upper;
		}
		map[ds.size() - 1] = new SolutionMapCellImpl(Double.MAX_VALUE, ds.get(lower));
	}

	/* (non-Javadoc)
	 * @see toq.local_search.SolutionMap#getPreferenceFunction(double)
	 */
	@Override
	public int getPreferenceFunction(double cfv) {
		int left = 0,
		    right = map.length - 1;
		while(right - left > 1)
			if(cfv <= map[(int) Math.ceil((right + left)/2.0)].getBorder())
				right = (int) Math.ceil((right + left)/2.0); 
			else
				left = (int) Math.ceil((right + left)/2.0); 
		return getPreferenceFunction(right);
	}

	/* (non-Javadoc)
	 * @see toq.local_search.SolutionMap#getCell(double)
	 */
	@Override
	public int getCell(double cfv) {
		int left = 0,
		    right = map.length - 1;
		while(right - left > 1)
			if(cfv <= map[(int) Math.ceil((right + left)/2.0)].getBorder())
				right = (int) Math.ceil((right + left)/2.0); 
			else
				left = (int) Math.ceil((right + left)/2.0); 
		return right;
	}
	
	/* (non-Javadoc)
	 * @see toq.local_search.SolutionMap#getPreferenceFunction(int)
	 */
	@Override
	public int getPreferenceFunction(int i) {
		return map[i].getPreferenceFunction();
	}

	/* (non-Javadoc)
	 * @see toq.local_search.SolutionMap#setPreferenceFunction(int, int)
	 */
	@Override
	public void setPreferenceFunction(int i, int pf) {
		map[i].setPreferenceFunction(pf);	
	}

	/* (non-Javadoc)
	 * @see toq.local_search.SolutionMap#collectCFValues(toq.local_search.SubtaskDescriptor, java.util.TreeMap)
	 */
	@Override
	public void collectCFValues(SubtaskDescriptor std, TreeMap<Double, Integer> ds) {
		ds.put(std.getCFunctionValue(), std.getPFuction());
		if(std.getRoot().getLeftChild().getSubtaskDescriptor().getAnkete().getNOQ() > 0)
			collectCFValues(std.getRoot().getLeftChild().getSubtaskDescriptor(), ds);
		if(std.getRoot().getRightChild().getSubtaskDescriptor().getAnkete().getNOQ() > 0)
			collectCFValues(std.getRoot().getRightChild().getSubtaskDescriptor(), ds);
	}

	/* (non-Javadoc)
	 * @see toq.local_search.SolutionMap#print(java.lang.String)
	 */
	@Override
	public void print(String of) throws FileNotFoundException {
		print(new PrintStream(new File(of)));
	}

	/* (non-Javadoc)
	 * @see toq.local_search.SolutionMap#print(java.io.PrintStream)
	 */
	@Override
	public void print(PrintStream ps) throws FileNotFoundException {
		for(int i = 0; i < map.length; i++)
			ps.println("Cell No.: " + i + ", border: " + map[i].getBorder() + ", PF No.: " + map[i].getPreferenceFunction());
	}

}
