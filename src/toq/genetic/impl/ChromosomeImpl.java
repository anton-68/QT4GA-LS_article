/**
 * 
 */
package toq.genetic.impl;

import java.util.Random;

import toq.functions.PFunctionType;
import toq.functions.impl.CostPFImpl;
import toq.functions.impl.DeltaEntropyPFImpl;
import toq.functions.impl.EntropyByCostPFImpl;
import toq.functions.impl.RQMPFImpl;
import toq.genetic.Chromosome;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 18, 2010
 * 2:41:34 PM
 */
public class ChromosomeImpl implements Chromosome {	
	/**
	 * Chromosome array
	 */
	private int[] c;
	
	/**
	 * Array of preference functions constituting the chromosome
	 */
	private static PFunctionType[] pfa;
	
	/**
	 * Boundaries of the characteristic function used for mapping
	 */
	private static double[] genomMap;

	/**
	 * Step of characteristic function
	 */
	private static double gridStep;
	
	/**
	 * No default constructor - no setters/getters :) 
	 * @param cl
	 * @param cf 
	 * @param genes
	 */
	public ChromosomeImpl(int cl, double cfLow, double cfHigh, PFunctionType[] pfa) {
		Random generator = new Random();
		ChromosomeImpl.pfa = pfa;
		c = new int[cl];
		for(int i = 0; i < cl; i++)
			c[i] = generator.nextInt(pfa.length);
		genomMap = new double[]{cfLow, cfHigh};	
		gridStep = (genomMap[1] - genomMap[0]) / (double) c.length;
	}
	
	/**
	 * Copy constructor
	 */
	public ChromosomeImpl(ChromosomeImpl x){
		this.c = new int[x.getChromosomeLength()];
		for(int i = 0; i < x.getChromosomeLength(); i++)
			this.c[i] = x.getGene(i);
	}
	
	/* (non-Javadoc)
	 * @see toq.genetic.Chromosome#crossover(toq.genetic.Chromosome, int)
	 */
	@Override
	public Chromosome[] crossover(Chromosome x, int xPoint) {
		Chromosome[] nc = new Chromosome[2];
		nc[0] = new ChromosomeImpl((ChromosomeImpl) x);
		nc[1] = new ChromosomeImpl(this);
		for(int i = 0; i < xPoint; i++)
			nc[0].setGene(i, this.c[i]);
		for(int i = xPoint; i < c.length; i++)
			nc[0].setGene(i, x.getGene(i));
		return nc;
	}

	/* (non-Javadoc)
	 * @see toq.genetic.Chromosome#mutate(int)
	 */
	@Override
	public void mutate(int i, int g) {
		setGene(i, g);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String s = new String();
		for(int i = 0; i < c.length; i++)
			s += pfa[c[i]].getSymbol(); 
		return s;
	}
	
	/* (non-Javadoc)
	 * @see toq.genetic.Chromosome#getGene(int)
	 */
	@Override
	public int getGene(int i){
		return (i < c.length ? c[i] : -1);
	}
	
	/* (non-Javadoc)
	 * @see toq.genetic.Chromosome#setGene(int, int)
	 */
	@Override
	public void setGene(int i, int g){
		if(i < c.length)
			c[i] = g;
	}

	/* (non-Javadoc)
	 * @see toq.genetic.Chromosome#getChromosomeLength()
	 */
	@Override
	public int getChromosomeLength() {
		return c.length;
	}

	/* (non-Javadoc)
	 * @see toq.genetic.Chromosome#getGenomMap()
	 */
	@Override
	public double[] getGenomMap() {
		return genomMap;
	}
	
	/* (non-Javadoc)
	 * @see toq.genetic.Chromosome#getNumberOfGenes()
	 */
	@Override
	public int getNumberOfGenes() {
		return pfa.length;
	}
	
	/* (non-Javadoc)
	 * @see toq.genetic.Chromosome#getGenomMap()
	 */
	@Override
	public double getGridStep() {
		return gridStep;
	}	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PFunctionType[] pfa1 = new PFunctionType[4];
		pfa1[0] = new RQMPFImpl();
		pfa1[1] = new DeltaEntropyPFImpl();
		pfa1[2] = new EntropyByCostPFImpl();
		pfa1[3] = new CostPFImpl();
		Chromosome c = new ChromosomeImpl(20, 0.0, 1.0, pfa1);
		System.out.println(c);
	}



}
