/**
 * 
 */
package toq.genetic;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 18, 2010
 * 12:14:50 AM
 */
public interface Individual extends Comparable<Individual>{
	/**
	 * @param x
	 * @return
	 */
	Individual[] mate(Individual x);
	
	/**
	 * @return fitness of individual of this chromosome
	 */
	double getFitness();

	/**
	 * @return
	 */
	Chromosome getChromosome();

}
