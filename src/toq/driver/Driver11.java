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
import toq.local_search.Solution;
import toq.local_search.impl.SolutionImpl;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 16, 2010
 * 1:22:31 PM
 */
public class Driver11 {
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
	String subdirName = "Driver19";
	final int noe = 31;
	final int noq = 5;
	final int not = 1024;
	
	// TODO We need to ensure subdir
	PrintStream rs, ls, ds, csvs;
	ls = new PrintStream(new File("res/" + subdirName + "/test.log"));
	csvs = new PrintStream(new File("res/" + subdirName + "/test.csv"));
	Ankete a;
	Solution s;	
	
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
	
	double imp;
	double minC;//TODO Opt
	int iter;
	long t0 = System.currentTimeMillis();

	for (int i = 0; i < not; i++){
		a = new AnketeImpl(noe, noq, (double) noq);
		ds = new PrintStream(new File("dat/" + subdirName + "/test" + i + ".dat"));
		a.print(ds);
		rs = new PrintStream(new File("res/" + subdirName + "/test" + i + ".res"));
		ls.print("Test No. " + i);
		minC = a.getOptPrice();//TODO Opt
		rs.println("Price of optimal questionnaire: " + minC); //TODO Opt
		rs.println();			
		ls.print(" :: Min price: " + minC);//TODO Opt
		csvs.print(i + ", ");
		csvs.print(minC + ", "); //TODO Opt
		
		rs.println("Greedy methods results");
		rs.println();
/*			rs.println("Question-linked PFs:");
		rs.println();			
		for(int j = 0; j < pfa0.length; j++){
			rs.print(pfa0[j].getName() + ": ");
			s = new SolutionImpl(a, cf, pfa0, j);
			rs.println(s.getCost());
			//ls.print("PF" + j + ": c = " + s.getCost()+ " - ");
			rs.println();
		}*/
		rs.println("Heuristic PFs:");
		rs.println();			
		for(int j = 0; j < pfa1.length; j++){
			rs.print(pfa1[j].getName() + ": ");
			s = new SolutionImpl(a, cf, pfa1, j);
			rs.println(s.getCost());
			if(j == 0){
				ls.print(" :: RQMPFImpl(): " + s.getCost());
				csvs.print(s.getCost() + ", ");
			}
			rs.println();
		}					
		rs.println("Local search results");
		rs.println();			
/*			rs.println("Question-linked PFs:");
		rs.println();
		s = new SolutionImpl(a, cf, pfa0, 0);
		rs.println("Initial cost: " +  s.getCost());
		rs.println();
		imp = 0;
		iter = 0;
		do {
			s.searchAll(s.getQuestionnaire().getSubtaskDescriptor());
			rs.println("Iteration " + iter);
			imp = s.getImprovement();
			rs.println("Cost:" +  s.getCost() + ", improvement: " + imp);
			if(s.getImprovement() > 0.0)
				s.applyPretender();				
			rs.println();
			iter++;
		} while(imp != 0.0);
		ls.print(" :: Question-linked: " + s.getCost());
		csvs.print(s.getCost() + ", ");*/
		
		rs.println("Heuristic PFs:");
		rs.println();
		s = new SolutionImpl(a, cf, pfa1, 0);
		rs.println("Initial cost: " +  s.getCost());
		rs.println();
		imp = 0;
		iter = 0;
		do {
			s.searchAll(s.getQuestionnaire().getSubtaskDescriptor());
			rs.println("Iteration " + iter);
			imp = s.getImprovement();
			rs.println("Cost:" +  s.getCost() + ", improvement: " + imp);
			if(s.getImprovement() > 0.0)
				s.applyPretender();				
			rs.println();
			iter++;
		} while(imp != 0.0);			
		ls.print(" :: Heuristic: " + s.getCost());
		csvs.print(s.getCost() + ", ");
		
		rs.println("Mixed PFs:");
		rs.println();
		s = new SolutionImpl(a, cf, pfa2, 0);
		rs.println("Initial cost: " +  s.getCost());
		rs.println();
		imp = 0;
		iter = 0;
		do {
			s.searchAll(s.getQuestionnaire().getSubtaskDescriptor());
			rs.println("Iteration " + iter);
			imp = s.getImprovement();
			rs.println("Cost:" +  s.getCost() + ", improvement: " + imp);
			if(s.getImprovement() > 0.0)
				s.applyPretender();				
			rs.println();
			iter++;
		} while(imp != 0.0);			
		ls.println(" :: Mixed: " + s.getCost());
		csvs.println(s.getCost());
		
		rs.println("Map:");
		s.getMap().print(rs);	
	}
	long t1 = System.currentTimeMillis();
	ls.println("Computation time: " + (t1-t0));				
}

}
