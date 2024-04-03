/**
 * 
 */
package toq.local_search;

import toq.ankete.Ankete;
import toq.questionnaire.QNode;

/**
 * Subtask related information - subankete, subtree cost, PFunction, etc.
 * @author anton.bondarenko@gmail.com
 * Jan 10, 2010
 * 2:41:23 AM
 */
public interface SubtaskDescriptor {
	/**
	 * Setters&Getters
	 * @return Ankete
	 */
	Ankete getAnkete();
	
	/**
	 * Setters&Getters
	 * @param Ankete
	 */
	void setAnkete(Ankete a);
	
	/**
	 * Setters&Getters
	 * @return double cost
	 */
	double getCost();
	
	/**
	 * Setters&Getters
	 * @param double cost
	 */
	void setCost(double c);	
	
	/**
	 * Setters&Getters
	 * @return preference function object
	 */
	int getPFuction();
	
	/**
	 * Setters&Getters
	 * @param preference function object
	 */
	void setPFuction(int pf);
	
	/**
	 * Setters&Getters
	 * @return double CFunction value
	 */
	double getCFunctionValue();
	
	/**
	 * Setters&Getters
	 * @param double CFunction value
	 */
	void setCFunctionValue(double cf);
	
	/**
	 * @param root QNode
	 */
	void setRoot(QNode root);
	
	/**
	 * @return root QNode
	 */
	QNode getRoot();

	/**
	 * Refreshes costs of all subtrees after tree modifications
	 * @return updated double cost of the subtree
	 */
	double updateCosts();

}
