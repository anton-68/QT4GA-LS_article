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
import toq.functions.impl.CostEntropyCFImpl;
import toq.functions.impl.CostPFImpl;
import toq.functions.impl.DeltaEntropyPFImpl;
import toq.functions.impl.EntropyByCostPFImpl;
import toq.functions.impl.EntropyCFImpl;
import toq.functions.impl.N2RRatioCFImpl;
import toq.functions.impl.RQMPFImpl;
import toq.functions.impl.nPFImpl;
import toq.local_search.Solution;
import toq.local_search.impl.SolutionImpl;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 13, 2010
 * 4:04:26 PM
 */
public class Driver00 {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Ankete a = new AnketeImpl("dat/Driver80/test0.dat");
		PrintStream ps = new PrintStream(new File("res/Driver80/test0.res"));
		Solution s;
		ps.println("Ankete:");
		a.print(ps);	
		int noq = a.getNOE();
		PFunctionType[] pfa = new PFunctionType[4];
		pfa[0] = new CostPFImpl();
		pfa[1] = new DeltaEntropyPFImpl();
		pfa[2] = new EntropyByCostPFImpl();
		pfa[3] = new RQMPFImpl();
		PFunctionType[] pfa2 = new PFunctionType[4 + noq];
		pfa2[0] = new RQMPFImpl();
		pfa2[1] = new DeltaEntropyPFImpl();
		pfa2[2] = new EntropyByCostPFImpl();
		pfa2[3] = new CostPFImpl();
		for (int j = 4; j < (4 + noq); j++)
			pfa2[j] = new nPFImpl(j);
		CFunctionType[] cfa = new CFunctionType[3];
		cfa[0] = new EntropyCFImpl();
		cfa[1] = new CostEntropyCFImpl();
		cfa[2] = new N2RRatioCFImpl();
		ps.println("Price of optimal questionnaire: " + a.getOptPrice());
		ps.println();
		ps.println("Greedy methods results");
		ps.println();
		for(int i = 0; i < pfa.length; i++){
			ps.print(pfa[i].getName() + ": ");
			s = new SolutionImpl(a, cfa[0], pfa, i);
			ps.println(s.getCost());
			s.getQuestionnaire().printSubtree(ps);
			ps.println();
		}
		ps.println("Local search results");
		ps.println();
		for(int i = 0; i < cfa.length; i++){
			ps.println(cfa[i].getName() + ": ");
			s = new SolutionImpl(a, cfa[i], pfa, 0);
			ps.println("Initial cost: " +  s.getCost());
			ps.println();
			for(int j = 0; j < 5; j++){
				s.searchAll(s.getQuestionnaire().getSubtaskDescriptor());
				ps.println("Iteration " + j);
				ps.println("Cost:" +  s.getCost() + ", improvement: " + s.getImprovement());
				if(s.getImprovement() > 0.0)
					s.applyPretender();				
				ps.println();
			}
		}
	}

}
