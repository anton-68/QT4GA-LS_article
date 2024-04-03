/**
 * 
 */
package toq.driver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
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
import toq.genetic.impl.IndividualImpl;
import toq.genetic.impl.MixedPopulationImpl;
import toq.local_search.Solution;
import toq.local_search.impl.SolutionImpl;

/**
 * @author anton.bondarenko@gmail.com Jan 20, 2010 10:25:41 PM
 */
public class Driver31 {
	static boolean PRINT_INDIVIDUAL_RESULTS = true;
	static boolean CALCULATE_EXACT_OPTIMUM = true;
	static boolean MEASURE_EXECUTION_TIME = true;
	static boolean PRINT_POPULATION = false;

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		String subdirName = "Driver79";
		if(PRINT_INDIVIDUAL_RESULTS) {
			File dir = new File("dat/" + subdirName);
			if(!dir.exists())
				dir.mkdir();
			else
				if(dir.list().length != 0){
					System.err.println("Requested directory already exists and is not empty");
					System.exit(1);
				}
		}
		File dir = new File("res/" + subdirName);
		if(!dir.exists())
			dir.mkdir();
		else
			if(dir.list().length != 0){
				System.err.println("Requested directory already exists and is not empty");
				System.exit(1);
			}

		final int noe = 17;
		final int noq = 11;
		final int not = 32;

		final double matingRate = 1.0;
		final double mutationRate = 0.1;
		final int pSize = noe * noq * noe;
		final int chLength = noe * noq;
		final int noGWOI = 32;
		final int maxNOG = 4096;

		PrintStream rs = null, ls, ds, csvs;
		ls = new PrintStream(new File("res/" + subdirName + "/test.log"));
		csvs = new PrintStream(new File("res/" + subdirName + "/test.csv"));
		Ankete a;
		Solution s;

		PFunctionType[] pfa0 = new PFunctionType[noq];
		for (int j = 0; j < noq; j++)
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
		for (int j = 4; j < (4 + noq); j++)
			pfa2[j] = new nPFImpl(j);

		CFunctionType cf = new EntropyCFImpl();

		double imp;
		double minC = 0.0;
		int iter, gen;
		long t0 = 0;

		if (MEASURE_EXECUTION_TIME)
			t0 = System.currentTimeMillis();
		for (int i = 0; i < not; i++) {
			a = new AnketeImpl(noe, noq, (double) noq);
			if (PRINT_INDIVIDUAL_RESULTS) {
				ds = new PrintStream(new File("dat/" + subdirName + "/test" + i
						+ ".dat"));
				a.print(ds);
				ds.println("Mating rate: " + matingRate);
				ds.println("Mutation rate: " + mutationRate);
				ds.println("Size of population: " + pSize);
				ds.println("Length of chromosome: " + chLength);
				ds.println("No. of generation without improvement before stop: " + noGWOI);
				ds.println("Total max No. of generation: " + maxNOG);
				rs = new PrintStream(new File("res/" + subdirName + "/test" + i
						+ ".res"));
			}
			ls.print("Test No. " + i);
			if (CALCULATE_EXACT_OPTIMUM)
				minC = a.getOptPrice();
			if (PRINT_INDIVIDUAL_RESULTS) {
				rs.println("Price of optimal questionnaire: "
						+ (CALCULATE_EXACT_OPTIMUM ? minC : "N/A"));
				rs.println();
			}
			ls.print(" :: Min price: "
					+ (CALCULATE_EXACT_OPTIMUM ? minC : "N/A"));// 
			csvs.print(i + ", ");
			csvs.print((CALCULATE_EXACT_OPTIMUM ? minC : "N/A") + ", ");
			if (PRINT_INDIVIDUAL_RESULTS) {
				rs.println("Greedy methods results");
				rs.println();
				rs.println("Question-linked PFs:");
				rs.println();
			}
			for (int j = 0; j < pfa0.length; j++) {
				if (PRINT_INDIVIDUAL_RESULTS)
					rs.print(pfa0[j].getName() + ": ");
				s = new SolutionImpl(a, cf, pfa0, j);
				if (PRINT_INDIVIDUAL_RESULTS) {
					rs.println(s.getCost());
					rs.println();
				}
				/*
				 * ls.print(" :: PF(" + j + "): " + s.getCost());
				 * csvs.print(s.getCost() + ", ");
				 */
			}
			if (PRINT_INDIVIDUAL_RESULTS) {
				rs.println("Heuristic PFs:");
				rs.println();
			}
			for (int j = 0; j < pfa1.length; j++) {
				if (PRINT_INDIVIDUAL_RESULTS)
					rs.print(pfa1[j].getName() + ": ");
				s = new SolutionImpl(a, cf, pfa1, j);
				if (PRINT_INDIVIDUAL_RESULTS) {
					rs.println(s.getCost());
					rs.println();
				}
				if (j == 0) {
					ls.print(" :: RQMPFImpl(): " + s.getCost());
					csvs.print(s.getCost() + ", ");
				}
			}
			if (PRINT_INDIVIDUAL_RESULTS) {
				rs.println("Local search results");
				rs.println();
				rs.println("Question-linked PFs:");
				rs.println();
			}
			s = new SolutionImpl(a, cf, pfa0, 0);
			if (PRINT_INDIVIDUAL_RESULTS) {
				rs.println("Initial cost: " + s.getCost());
				rs.println();
			}
			imp = 0;
			iter = 0;
			do {
				s.searchAll(s.getQuestionnaire().getSubtaskDescriptor());
				if (PRINT_INDIVIDUAL_RESULTS)
					rs.println("Iteration " + iter);
				imp = s.getImprovement();
				if (PRINT_INDIVIDUAL_RESULTS)
					rs.println("Cost:" + s.getCost() + ", improvement: " + imp);
				if (s.getImprovement() > 0.0)
					s.applyPretender();
				if (PRINT_INDIVIDUAL_RESULTS)
					rs.println();
				iter++;
			} while (imp != 0.0);
			ls.print(" :: Question-linked: " + s.getCost());
			csvs.print(s.getCost() + ", ");
			if (PRINT_INDIVIDUAL_RESULTS) {
				rs.println("Heuristic PFs:");
				rs.println();
			}
			s = new SolutionImpl(a, cf, pfa1, 0);
			if (PRINT_INDIVIDUAL_RESULTS) {
				rs.println("Initial cost: " + s.getCost());
				rs.println();
			}
			imp = 0;
			iter = 0;
			do {
				s.searchAll(s.getQuestionnaire().getSubtaskDescriptor());
				if (PRINT_INDIVIDUAL_RESULTS)
					rs.println("Iteration " + iter);
				imp = s.getImprovement();
				if (PRINT_INDIVIDUAL_RESULTS)
					rs.println("Cost:" + s.getCost() + ", improvement: " + imp);
				if (s.getImprovement() > 0.0)
					s.applyPretender();
				if (PRINT_INDIVIDUAL_RESULTS)
					rs.println();
				iter++;
			} while (imp != 0.0);
			ls.print(" :: Heuristic: " + s.getCost());
			csvs.print(s.getCost() + ", ");
			if (PRINT_INDIVIDUAL_RESULTS) {
				rs.println("Mixed PFs:");
				rs.println();
			}
			s = new SolutionImpl(a, cf, pfa2, 0);
			if (PRINT_INDIVIDUAL_RESULTS) {
				rs.println("Initial cost: " + s.getCost());
				rs.println();
			}
			imp = 0;
			iter = 0;
			do {
				s.searchAll(s.getQuestionnaire().getSubtaskDescriptor());
				if (PRINT_INDIVIDUAL_RESULTS)
					rs.println("Iteration " + iter);
				imp = s.getImprovement();
				if (PRINT_INDIVIDUAL_RESULTS)
					rs.println("Cost:" + s.getCost() + ", improvement: " + imp);
				if (s.getImprovement() > 0.0)
					s.applyPretender();
				if (PRINT_INDIVIDUAL_RESULTS)
					rs.println();
				iter++;
			} while (imp != 0.0);
			ls.print(" :: Mixed: " + s.getCost());
			csvs.print(s.getCost() + ", ");
			/*
			 * if(PRINT_INDIVIDUAL_RESULTS) { rs.println("Map:");
			 * s.getMap().print(rs); }
			 */
			if (PRINT_INDIVIDUAL_RESULTS) {
				rs.println("Genetic algorithm results");
				rs.println();
			}

			MixedPopulationImpl p = new MixedPopulationImpl((AnketeImpl) a,
					pSize, matingRate, cf, pfa2, chLength, mutationRate);
			if (PRINT_INDIVIDUAL_RESULTS){
				rs.println("Fitness of the original population: "
						+ p.getFitness());
				rs.println("Chromosome of the fittest individual:");
				rs.println(p.getBestChromosome());
				rs.println();
			}
			iter = 0;
			gen = 0;
			long tGA0 = 0, tGA1 = 0;
			if (PRINT_POPULATION){
				rs.println("Original population:");
				p.printPopulation(rs);
			}
			do {
				imp = p.getFitness();
				if (MEASURE_EXECUTION_TIME)
					tGA0 = System.currentTimeMillis();
				if (PRINT_POPULATION) 
					p.produceNextGeneration(rs);
				else
					p.produceNextGeneration();
				if (MEASURE_EXECUTION_TIME)
					tGA1 = System.currentTimeMillis();
				gen++;
				if (PRINT_INDIVIDUAL_RESULTS) {
					rs.println("Population # " + gen + " : best - "
							+ p.getFitness() + ", worst: " + 
							(new IndividualImpl(p.getWorstChromosome(), p)).getFitness());
					rs.println("Chromosome of the fittest individual:");
					rs.println(p.getBestChromosome());
					rs.println("Chromosome of the weakest individual:");
					rs.println(p.getWorstChromosome());
					rs.println("Generation computation time: " + (tGA1 - tGA0));
					rs.println();
					if (PRINT_POPULATION){
						rs.println("Population # " + gen + ":");
						p.printPopulation(rs);
					}
				}
				imp -= p.getFitness();
				if (imp == 0.0)
					iter++;
				else
					iter = 0;
			} while (iter < noGWOI && gen < maxNOG && (p.getFitness() - minC ) > 1.0E-10);
			if (PRINT_INDIVIDUAL_RESULTS){
				rs.println("Final population:");
				p.printPopulation(rs);
			}
			ls.print(" :: GA best: " + p.getFitness());
			csvs.print(p.getFitness() + ", ");
			double wf = (new IndividualImpl(p.getWorstChromosome(), p)).getFitness();
			ls.print(" :: GA worst: " + wf);
			csvs.print(wf);
			ls.println(" :: GA gen: " + gen);
			csvs.println(gen);
		}
		if (MEASURE_EXECUTION_TIME) {
			long t1 = System.currentTimeMillis();
			ls.println("Computation time: " + (t1 - t0));
		}
	}

}
