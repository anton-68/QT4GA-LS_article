/**
 * 
 */
package toq.functions;

import toq.ankete.Ankete;

/**
 * Preference function object for choosing root question
 * @author anton.bondarenko@gmail.com
 * Jan 10, 2010
 * 2:42:04 PM 
 */
public interface PFunctionType extends FunctionType{
	/**
	 * Calculates value of the preference function for 
	 * question q in the ankete a
	 * @param Ankete a
	 * @param int question No.
	 * @return double PFunction value
	 */
	double getValue(int t, Ankete a);
	
	/**
	 * @param Ankete a
	 * @return root question No.
	 */
	int getRoot(Ankete a);
	
	/**
	 * @return char function unique symbol
	 */
	char getSymbol();

}
