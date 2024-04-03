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
import toq.local_search.Solution;
import toq.local_search.impl.SolutionImpl;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 14, 2010
 * 4:53:08 PM
 */
public class Driver02 {
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		String subdirName = "0";
		// Do we need to ensure subdir?
		PrintStream ds, rs, ls;
		ls = new PrintStream(new File("res/" + subdirName + "/test.log"));
		Ankete a;
		Solution s;
		PFunctionType[] pfa = new PFunctionType[4];
		pfa[0] = new CostPFImpl();
		pfa[1] = new DeltaEntropyPFImpl();
		pfa[2] = new EntropyByCostPFImpl();
		pfa[3] = new RQMPFImpl();
		CFunctionType cf = new EntropyCFImpl();
		// For each of 32 anketes:
		for (int i = 0; i < 32; i++){
			a = new AnketeImpl(32, 20, 100.0);
			ds = new PrintStream(new File("dat/" + subdirName + "/test" + i + ".dat"));
			a.print(ds);
			rs = new PrintStream(new File("res/" + subdirName + "/test" + i + ".res"));
			rs.println("Greedy methods results");
			rs.println();
			ls.println("Test No. " + i);
			for(int j = 0; j < pfa.length; j++){
				rs.print(pfa[j].getName() + ": ");
				s = new SolutionImpl(a, cf, pfa, j);
				rs.println(s.getCost());
				ls.print("PF" + j + ": c = " + s.getCost()+ " - ");
				rs.println();
			}
			rs.println("Local search results");
			rs.println();
			rs.println(cf.getName() + ": ");
			s = new SolutionImpl(a, cf, pfa, 0);
			rs.println("Initial cost: " +  s.getCost());
			rs.println();
			for(int j = 0; j < 7; j++){
				s.searchAll(s.getQuestionnaire().getSubtaskDescriptor());
				rs.println("Iteration " + j);
				rs.println("Cost:" +  s.getCost() + ", improvement: " + s.getImprovement());
				if(s.getImprovement() > 0.0)
					s.applyPretender();				
				rs.println();
			}
			ls.print("Result c = " + s.getCost());
			ls.println();
			rs.println("Map:");
			s.getMap().print(rs);			
		}				
	}

}

