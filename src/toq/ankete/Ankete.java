/**
 * 
 */
package toq.ankete;

import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Binary ankete
 * @author anton.bondarenko@gmail.com
 * Jan 8, 2010
 * 12:47:11 AM
 */
public interface Ankete {
	//abstract String toString();
	/**
	 * Setters&Getters
	 * @return int the number of questions
	 */
	int getNOQ();
	
	/**
	 * Setters&Getters
	 * @return int the number of events
	 */
	int getNOE();
	
	/**
	 * Produces subanketes for question t 
	 * @param int the number question
	 * @return array of subanketes
	 */
	Ankete[] getSubanketes(int t);
	
	/**
	 * Setters&Getters
	 * @param int a number of question
	 * @param int a number of events
	 * @return char outcome
	 */
	char getOutcome(int t, int y);
	
	/**
	 * Setters&Getters
	 * @param int a number of events
	 * @return double probability of event y
	 */
	double getProbability(int y);
	
	/**
	 * Setters&Getters
	 * @param int a number of question
	 * @return double cost of questin t
	 */
	double getCost(int t);
	
	/**
	 * Fine print of ankete
	 * @param String output filename
	 * @throws FileNotFoundException 
	 */
	void print(String of) throws FileNotFoundException;
	
	/**
	 * Fine print of ankete
	 * @param PrintStream
	 * @throws FileNotFoundException 
	 */
	void print(PrintStream ps) throws FileNotFoundException;
	
	/**
	 * Full search
	 * @return price of optimal questionaire
	 */
	double getOptPrice();

	/**
	 * @param of
	 * @throws FileNotFoundException
	 */
	void printMDP(String of) throws FileNotFoundException;
	
	/**
	 * @param ps
	 * @throws FileNotFoundException
	 */
	void printMDP(PrintStream ps) throws FileNotFoundException;
	
}
