/**
 * 
 */
package toq.genetic.impl;

import java.io.PrintStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import toq.ankete.Ankete;
import toq.ankete.impl.AnketeImpl;
import toq.functions.CFunctionType;
import toq.functions.PFunctionType;
import toq.functions.impl.CostPFImpl;
import toq.functions.impl.DeltaEntropyPFImpl;
import toq.functions.impl.EntropyByCostPFImpl;
import toq.functions.impl.EntropyCFImpl;
import toq.functions.impl.RQMPFImpl;
import toq.functions.impl.nPFImpl;
import toq.genetic.Chromosome;
import toq.genetic.Individual;
import toq.genetic.Population;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 18, 2010
 * 1:13:43 AM
 */
public class MixedPopulationImpl implements Population {
	
	private class SelectionMap {
		private double map[];
		SelectionMap(){
			map = new double[size];
			updateMap();
		}
		int selectIndividual(double f){
			int left = 0,
		    right = map.length - 1;
			while(right - left > 1)
				if(f <= map[(int) Math.ceil((right + left)/2.0)])
					right = (int) Math.ceil((right + left)/2.0); 
				else
					left = (int) Math.ceil((right + left)/2.0); 
			return right;
		}
		void updateMap(){
			//map = new double[size];
			double mSum = 0.0;
			for(int i = 0; i < size; i++) {
				map[i] = 1.0 / p.get(i).getFitness() - 1.0/getFitness() /*+
				         (1.0/p.getLast().getFitness() - 1.0/getFitness())/(double)p.size()*/;
				mSum += map[i];
			}
			map[0] /= mSum;
			for(int i = 1; i < size; i++){
				map[i] /= mSum;
				map[i] = map[i-1] + map[i];
			}
		}
	}
	
	/**
	 * The population
	 */
	private LinkedList<Individual> p;
	
	/**
	 * Map for selecting individuals for mating
	 */
	private SelectionMap sMap;
	
	/**
	 * The ankete
	 */
	private AnketeImpl a;	
	
	/**
	 * Target size of population
	 */
	private int size;
	
	/**
	 * Size of each new generation / size of population
	 */
	private double matingRate;
	
	/**
	 * Characteristic function
	 */
	private CFunctionType cf;	
	
	/**
	 * Array of preference functions used as genes
	 */
	private PFunctionType[] pfa;
	
	/**
	 * Length of chromosome
	 */
	private int cl;
	
	/**
	 * Array of gene symbols
	 */
	private static char[] genes ;
	
	/**
	 * Lower bound of the characteristic function
	 */
	private static double cfLow;
	
	/**
	 * Upper bound of the characteristic function
	 */
	private static double cfHigh;
	
	/**
	 * Probability of mutation
	 */
	private static double mutationRate;
	
/*	*//**
	 * Low boundary f the solution cost used for scaling
	 *//*
	private static double scalingValue = 0.0;*/
	
	/**
	 * Parameterized constructor: no default constructor - no headache
	 * with setters/getters
	 * @param a
	 * @param size
	 * @param cf
	 * @param pfa
	 * @param cl
	 */
	public MixedPopulationImpl(AnketeImpl a, 
						int size,
						double matingRate,
						CFunctionType cf,
						PFunctionType[] pfa,
						int cl,
						double mutationRate){
		this.a = a;
		this.size = size;
		this.matingRate = matingRate;
		this.cf = cf;
		this.pfa = pfa;
		this.cl = cl;
		this.setMutationRate(mutationRate);
		MixedPopulationImpl.cfLow = this.cf.getBoundaries(this.a.getNOE(), this.a.getNOQ())[0]; 
		MixedPopulationImpl.cfHigh = this.cf.getBoundaries(this.a.getNOE(), this.a.getNOQ())[1]; 		
		genes = new char[pfa.length];
		for(int i = 0; i < pfa.length; i++)
			MixedPopulationImpl.genes[i] = pfa[i].getSymbol();		
		p = new LinkedList<Individual>();
		while(p.size() < this.size) 
			p.add(new IndividualImpl(new ChromosomeImpl(this.cl, cfLow, cfHigh, this.pfa), this));		
		Collections.sort(p);
		sMap = new SelectionMap();
	}

/*	public MixedPopulationImpl(AnketeImpl a, 
			int size,
			double newGenerationRatio,
			CFunctionType cf,
			PFunctionType[] pfa,
			int cl,
			double scalingValue){
		this(a, size, newGenerationRatio, cf, pfa, cl);
		MixedPopulationImpl.scalingValue = scalingValue;
	}*/
	
	/* (non-Javadoc)
	 * @see toq.genetic.Population#produceNextGeneration()
	 */
	@Override
	public void produceNextGeneration() {
		Random generator = new Random();
		Individual[] np;
		for(int i = 0; i < size*matingRate; i++){
			Individual male = p.get(sMap.selectIndividual(generator.nextDouble()));
			Individual female = p.get(sMap.selectIndividual(generator.nextDouble()));
			np = male.mate(female); 
			for(int j = 0; j < np.length; j++)
				p.add(np[j]);
		}
		Collections.sort(p);
		p = new LinkedList<Individual> (p.subList(0, size));
		sMap.updateMap();
	}


	public void produceNextGeneration(PrintStream ls) {
		Random generator = new Random();
		Individual[] np;
		for(int i = 0; i < size*matingRate; i++){
			Individual male = p.get(sMap.selectIndividual(generator.nextDouble()));
			ls.printf("Selected father fitness: %1$14.8f", male.getFitness());
			ls.println(",      father chromosome: " + male.getChromosome());
			Individual female = p.get(sMap.selectIndividual(generator.nextDouble()));
			ls.printf("Selected mother fitness: %1$14.8f", female.getFitness());
			ls.println(",      mother chromosome: " + female.getChromosome());
			np = male.mate(female); 
			p.add(np[0]);
			ls.printf("Sibling # 1 fitness:     %1$14.8f", np[0].getFitness()); 
			ls.println(", Sibling # 1 chromosome: " + np[0].getChromosome());
			p.add(np[1]);
			ls.printf("Sibling # 2 fitness:     %1$14.8f", np[1].getFitness());
			ls.println(", Sibling # 2 chromosome: " + np[1].getChromosome());
		}
		Collections.sort(p);
		p = new LinkedList<Individual> (p.subList(0, size));
		sMap.updateMap();
	}
	
	/* (non-Javadoc)
	 * @see toq.genetic.Population#getCFunction()
	 */
	@Override
	public CFunctionType getCFunction() {
		return cf;
	}

	/* (non-Javadoc)
	 * @see toq.genetic.Population#getChromosome()
	 */
	@Override
	public Chromosome getBestChromosome() {
		return p.getFirst().getChromosome();
	}

	/* (non-Javadoc)
	 * @see toq.genetic.Population#getChromosome()
	 */
	@Override
	public Chromosome getWorstChromosome() { 
		return p.getLast().getChromosome();
	}
	
	/* (non-Javadoc)
	 * @see toq.genetic.Population#getFitness()
	 */
	@Override
	public double getFitness() {
		return p.getFirst().getFitness();
	}

	/* (non-Javadoc)
	 * @see toq.genetic.Population#getPfunctions()
	 */
	@Override
	public PFunctionType[] getPfunctions() {
		return pfa;
	}

	/* (non-Javadoc)
	 * @see toq.genetic.Population#setCFunction(toq.functions.CFunctionType)
	 */
	@Override
	public void setCFunction(CFunctionType cf) {
		this.cf = cf;
	}

	/* (non-Javadoc)
	 * @see toq.genetic.Population#setPfunctions(toq.functions.PFunctionType[])
	 */
	@Override
	public void setPfunctions(PFunctionType[] pfa) {
		this.pfa = pfa;
	}	

	/* (non-Javadoc)
	 * @see toq.genetic.Population#getMutationRate()
	 */
	@Override
	public double getMutationRate() {
		return mutationRate;
	}

	/* (non-Javadoc)
	 * @see toq.genetic.Population#setMutationRate(double)
	 */
	@Override
	public void setMutationRate(double mutationRate) {
		MixedPopulationImpl.mutationRate = mutationRate;
		
	}

	/* (non-Javadoc)
	 * @see toq.genetic.Population#getAnkete()
	 */
	@Override
	public Ankete getAnkete() {
		return a;
	}	
	

	/* (non-Javadoc)
	 * @see toq.genetic.Population#printPopulation(java.io.PrintStream)
	 */
	@Override
	public void printPopulation(PrintStream ls) {
		for(int i = 0; i < p.size(); i++){
			ls.printf("Individual %1$4d fitness: %2$14.8f", i, p.get(i).getFitness());
			ls.println(",             chromosome: " + p.get(i).getChromosome());
		}
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		final int noe = 7;
		final int noq = 3;
		final int not = 1;
		final double ngRatio = 1.0;
		
		final int pSize = noe * noq;		
		final int chLength = noe * noq;
		
		AnketeImpl a = new AnketeImpl(noe, noq, (double) noq);
				
		PFunctionType[] pfa0 = new PFunctionType[noq];
		for(int j = 0; j < noq; j++)
			pfa0[j] = new nPFImpl(j);		
		
		PFunctionType[] pfa1 = new PFunctionType[4];
		pfa1[0] = new RQMPFImpl();
		pfa1[1] = new DeltaEntropyPFImpl();
		pfa1[2] = new EntropyByCostPFImpl();
		pfa1[3] = new CostPFImpl();
		
		PFunctionType[] pfa2 = new PFunctionType[4 + noq];
		pfa2[0] = new RQMPFImpl();
		pfa2[1] = new DeltaEntropyPFImpl();
		pfa2[2] = new EntropyByCostPFImpl();
		pfa2[3] = new CostPFImpl();		
		for(int j = 4; j < (4 + noq); j++)
			pfa2[j] = new nPFImpl(j);
		
		CFunctionType cf = new EntropyCFImpl();
		
		MixedPopulationImpl p = new MixedPopulationImpl (a, pSize, ngRatio, cf, pfa2, chLength, 0.0);
		
		PrintStream ls = System.out;
		
/*		try {
			ls = new PrintStream(new File("test14.res"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
		
		ls.println("Fitness of the original population: " + p.getFitness());
		ls.println("Cost of the optimal questionnaire: " + a.getOptPrice());
		
		for(int i = 0; i < not; i++){
			p.produceNextGeneration(ls);
			ls.println("Fitness of population # " + i + " = " + p.getFitness());
			ls.println("Chromosome of the fittest individual :" + p.getBestChromosome());
			ls.println("Chromosome of the least fit individual :" + p.getWorstChromosome());
		}
		
	}

}
