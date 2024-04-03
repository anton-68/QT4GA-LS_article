/**
 * 
 */
package toq.local_search.impl;

import toq.local_search.SolutionMapCell;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 10, 2010
 * 3:56:08 AM
 */
public class SolutionMapCellImpl implements SolutionMapCell {
	/**
	 * Right border of the element
	 */
	double border;
	/**
	 * Preference function No.
	 */
	int pf;
	
	/**
	 * Default constructor
	 */
	SolutionMapCellImpl(){}
	/**
	 * @param double border
	 * @param int preference function
	 */
	SolutionMapCellImpl(double border, int pf){
		this.border = border;
		this.pf = pf;
	}
	
	/* (non-Javadoc)
	 * @see toq.local_search.SolutionMapCell#getPreferenceFunction()
	 */
	@Override
	public int getPreferenceFunction() {
		return pf;
	}
	/* (non-Javadoc)
	 * @see toq.local_search.SolutionMapCell#setPreferenceFunction(int)
	 */
	@Override
	public void setPreferenceFunction(int pf) {
		this.pf = pf;
		
	}
	/* (non-Javadoc)
	 * @see toq.local_search.SolutionMapCell#getBorder()
	 */
	@Override
	public double getBorder() {
		return border;
	}

}
