/**
 * 
 */
package toq.local_search;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.TreeMap;

/**
 * Solution map array
 * @author anton.bondarenko@gmail.com
 * Jan 10, 2010
 * 2:30:04 AM
 */
public interface SolutionMap {
	/**
	 * Setters&Getters
	 * @param int cell No.
	 * @return preference function No.
	 */
	int getPreferenceFunction(int c);
	
	/**
	 * Setters&Getters
	 * @param int cell No.
	 * @param preference function No.
	 */
	void setPreferenceFunction(int c, int pf);
	
	/**
	 * @param double characteristic function value
	 * @return int preference function for the given 
	 * characteristic function value
	 */
	int getPreferenceFunction(double cfv);
	
	/**
	 * @param double characteristic function value
	 * @return int cell number for the given 
	 * characteristic function value
	 */
	int getCell(double cfv);
	
	/**
	 * Collects characteristic function values 
	 * for all subanketes of std
	 * @param Subtask std
	 */
	void collectCFValues(SubtaskDescriptor std, TreeMap<Double, Integer> ds);
	
	/**
	 * Fine print of map
	 * @param String output filename
	 * @throws FileNotFoundException 
	 */
	void print(String of) throws FileNotFoundException;
	
	/**
	 * Fine print of map
	 * @param PrintStream
	 * @throws FileNotFoundException 
	 */
	void print(PrintStream ps) throws FileNotFoundException;
}
