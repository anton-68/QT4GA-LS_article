/**
 * 
 */
package toq.genetic.impl;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;

import toq.ankete.Ankete;
import toq.genetic.Chromosome;
import toq.genetic.Individual;
import toq.genetic.Population;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 18, 2010
 * 1:19:59 AM
 */
public class IndividualImpl implements Individual {
	/**
	 * Chromosome
	 */
	ChromosomeImpl c;
	
	/**
	 * Population ref
	 */
	Population p;
	
	/**
	 * 
	 */
	double fitness;

	/**
	 * Default constructor
	 */
	public IndividualImpl() {}
	
	/**
	 * @param Chromosome-based constructor
	 */
	public IndividualImpl(Chromosome c, Population p) {
		this.c = (ChromosomeImpl) c;
		this.p = p;
		this.fitness = calculateFitness(p.getAnkete());
	}
	
	private double calculateFitness(Ankete a) {
		if(a.getNOE() == 1)
			return 0.0;
		else
			return cost(a);
	}
	
	private double cost(Ankete a) {
		if(a.getNOE() == 1)
			return 0.0;
		else {
			double sumP = 0.0;
			for(int i = 0; i < a.getNOE(); i++)
				sumP += a.getProbability(i);
			int cPosition = (int) Math.floor(p.getCFunction().getValue(a) / c.getGridStep());
/*			System.out.println("cPosition = " + cPosition);
			System.out.println("c.getGridStep() = " + c.getGridStep());
			System.out.println("Grid size = " + c.getChromosomeLength());
			System.out.println("c.getGenomMap()[0] = " + c.getGenomMap()[0]);
			System.out.println("c.getGenomMap()[1] = " + c.getGenomMap()[1]);
			System.out.println("p.getCFunction().getValue(a) = " + p.getCFunction().getValue(a));
			System.out.println("n = " + a.getNOE() + ", r = " + a.getNOQ());*/
			int r = p.getPfunctions()[c.getGene(cPosition)].getRoot(a);			
			Ankete sa[] = a.getSubanketes(r);
			sumP *= a.getCost(r);
			a = null;
			return sumP + cost(sa[0]) + cost(sa[1]);
		}
	}

	public void print(Ankete a, PrintStream ps) throws FileNotFoundException {
		if(a.getNOE() == 1)
			return;
		else {
			double sumP = 0.0;
			for(int i = 0; i < a.getNOE(); i++)
				sumP += a.getProbability(i);
			int cPosition = (int) Math.floor(p.getCFunction().getValue(a) / c.getGridStep());
			ps.println("Ankete:");
			a.print(ps);
			int r = p.getPfunctions()[c.getGene(cPosition)].getRoot(a);
			ps.println("Root: " + r);
			sumP *= a.getCost(r);
			Ankete sa[] = a.getSubanketes(r);
			a = null;
			print(sa[0], ps);
			print(sa[1], ps);
		}
	}
	
	/* (non-Javadoc)
	 * @see toq.genetic.Individual#getFitness()
	 */
	@Override
	public double getFitness() {
		return fitness;
	}

	/* (non-Javadoc)
	 * @see toq.genetic.Individual#reproduce(toq.genetic.Individual)
	 */
	@Override
	public Individual[] mate(Individual x){
		Random generator = new Random();
		int xPoint = generator.nextInt(c.getChromosomeLength());
		Chromosome[] ng = this.getChromosome().crossover(x.getChromosome(), xPoint);
		Individual[] ni;
		if(generator.nextDouble() < p.getMutationRate())
			ni = new Individual[4];
		else
			ni = new Individual[2];
		for(int i = 0; i < ni.length; i++)
			ni[i] = new IndividualImpl(ng[i%2], p);
		for(int i = 2; i < ni.length; i++){
			for(int j = 0; j < c.getChromosomeLength()/p.getAnkete().getNOE(); j++){
				int mutaGen = generator.nextInt(c.getChromosomeLength());
				int geneValue = generator.nextInt(c.getNumberOfGenes());
				ng[0].mutate(mutaGen, geneValue);
			}
		}		
		return ni;
	}
	
	/* (non-Javadoc)
	 * @see toq.genetic.Individual#getChromosome()
	 */
	@Override
	public Chromosome getChromosome() {
		return c;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Individual x) {
		if (this.fitness < x.getFitness())
			return -1;
		else if (this.fitness > x.getFitness())
			return 1;
		else return 0;
	}

}
