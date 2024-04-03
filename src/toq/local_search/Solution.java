/**
 * 
 */
package toq.local_search;

import toq.functions.CFunctionType;
import toq.functions.PFunctionType;
import toq.questionnaire.QNode;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 9, 2010
 * 8:25:55 PM
 */
public interface Solution {
	/**
	 * Implements step of local search strategy
	 * based on recursive analysis of all subtasks
	 * @param current subtask
	 */
	void searchAll(SubtaskDescriptor std);
	
	/**
	 * Implements step of local search strategy
	 * based on analysis of subtasks until the
	 * first cost improvements
	 * @param current subtask
	 * @return true if improvement found
	 */
	boolean searchFirst(SubtaskDescriptor std);
	
	/**
	 * @return double cost of current solution
	 */
	double getCost();
	
	/**
	 * @return double cost of pretender solution
	 */
	double getPretenderCost();
	
	/**
	 * @return Root node of the questonnaire
	 */
	QNode getQuestionnaire();
	
	/**
	 * @return Root node of the pretender's questionnaire
	 */
	QNode getPretenderQuestionnaire();	
	/**
	 * @return PFunction objects array
	 */
	PFunctionType[] getPfunctions();
	
	/**
	 * @param PFunction objects array
	 */
	void setPfunctions(PFunctionType[] pfa);
	
	/**
	 * @return CFunction object
	 */
	CFunctionType getCFunction();
	
	/**
	 * @return Map object
	 */
	SolutionMap getMap();
	
	/**
	 * @param CFunction object
	 */
	void setCFunction(CFunctionType cf);
	
	/**
	 * Build optimized questionnaire for the given subtask 
	 * according to the current solution map, CFunction and 
	 * PFunction array
	 * @param Given subtask STD
	 * @return root node of the tree
	 */
	QNode buildQuestionnaire(SubtaskDescriptor std);
	
	/**	 
	 * Build optimized questionnaire for the given subtask 
	 * according to the given preference function
	 * @param Given subtask STD
	 * @param initial preference function
	 * @return root node of the tree
	 */
	QNode buildQuestionnaire(SubtaskDescriptor std, int initialPf);
	
	/**
	 * Current = Pretender;
	 */
	void applyPretender();
	
	/**
	 * @return double current inprovement
	 */
	double getImprovement();
}
