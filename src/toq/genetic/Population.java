/**
 * 
 */
package toq.genetic;

import java.io.PrintStream;

import toq.ankete.Ankete;
import toq.functions.CFunctionType;
import toq.functions.PFunctionType;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 17, 2010
 * 10:28:29 PM
 */
public interface Population {
	
	/**
	 * Produce new generation
	 */
	void produceNextGeneration();
	
	/**
	 * @return double fitness of the best chromosome
	 */
	double getFitness();
	
	/**
	 * @return best Chromosome
	 */
	Chromosome getBestChromosome();
	
	/**
	 * @return guess what...
	 */
	Chromosome getWorstChromosome();	
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
	 * @param CFunction object
	 */
	void setCFunction(CFunctionType cf);
	
	/**
	 * @return
	 */
	Ankete getAnkete();
	
	/**
	 * @param ls
	 */
	void printPopulation(PrintStream ls);

	/**
	 * @return
	 */
	double getMutationRate();

	/**
	 * @param mutationRate
	 */
	void setMutationRate(double mutationRate);

}
