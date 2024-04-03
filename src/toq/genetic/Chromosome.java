/**
 * 
 */
package toq.genetic;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 17, 2010
 * 11:09:53 PM
 */
public interface Chromosome {

	/**
	 * @param x - mate chromosome
	 * @param xPoint - int crossover point
	 * @return array of two produced chromosomes
	 */
	Chromosome[] crossover(Chromosome x, int xPoint);
	
	/**
	 * @param i
	 * @param g
	 */
	void mutate(int i, int g);
	
	/**
	 * @return
	 */
	public String toString();
	
	/**
	 * @param i
	 * @return
	 */
	public int getGene(int i);
	
	/**
	 * @param i
	 * @param g
	 */
	public void setGene(int i, int g);
	
	/**
	 * @return
	 */
	public int getChromosomeLength();
	
	/**
	 * @return
	 */
	public double[] getGenomMap();
	
	/**
	 * @return
	 */
	public int getNumberOfGenes();

	/**
	 * @return
	 */
	double getGridStep();
	
}
