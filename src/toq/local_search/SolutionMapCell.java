/**
 * 
 */
package toq.local_search;

/**
 * Solution map cell structure - element of the solution map array
 * @author anton.bondarenko@gmail.com
 * Jan 10, 2010
 * 2:30:31 AM
 */
public interface SolutionMapCell {
	/**
	 * Setters&Getters
	 * @return preference function No.
	 */
	int getPreferenceFunction();
	/**
	 * Setters&Getters
	 * @param preference function No.
	 */
	void setPreferenceFunction(int pf);
	/**
	 * @return double right interval border
	 */
	double getBorder();

}
