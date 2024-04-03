/**
 * 
 */
package toq.functions;

import toq.ankete.Ankete;

/**
 * Characteristic function interface
 * @author anton.bondarenko@gmail.com
 * Jan 11, 2010
 * 2:01:16 PM
 */
public interface CFunctionType extends FunctionType{
	/**
	 * Calculates value of the characteristic 
	 * function for ankete a
	 * @param Ankete a
	 * @return double CFunction value
	 */
	double getValue(Ankete a);
	
	/**
	 * @return lower and upper boundaries of function value
	 */
	double[] getBoundaries(int n, int r);

}
