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
import toq.functions.impl.EntropyCFImpl;
import toq.functions.impl.nPFImpl;
import toq.local_search.Solution;
import toq.local_search.impl.SolutionImpl;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 14, 2010
 * 2:55:57 PM
 */
public class Driver01 {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Ankete a = new AnketeImpl("dat/test9.dat");
		PrintStream ps = new PrintStream(new File("res/test9.res"));
		Solution s;
		ps.println("Ankete:");
		a.print(ps);
		PFunctionType[] pfa = new PFunctionType[a.getNOQ()];
		for(int i = 0; i < a.getNOQ(); i++)
			pfa[i] = new nPFImpl(i);
		CFunctionType cf = new EntropyCFImpl();
		ps.println("Greedy methods results");
		ps.println();
		for(int i = 0; i < pfa.length; i++){
			ps.print(pfa[i].getName() + ": ");
			s = new SolutionImpl(a, cf, pfa, i);
			ps.println(s.getCost());
			//s.getQuestionnaire().printSubtree(ps);
			ps.println();
		}
		ps.println("Local search results");
		ps.println();

		ps.println(cf.getName() + ": ");
		s = new SolutionImpl(a, cf, pfa, 0);
		ps.println("Initial cost: " +  s.getCost());
		ps.println();
		for(int j = 0; j < 32; j++){
			s.searchAll(s.getQuestionnaire().getSubtaskDescriptor());
			ps.println("Iteration " + j);
			ps.println("Cost:" +  s.getCost() + ", improvement: " + s.getImprovement());
			if(s.getImprovement() > 0.0)
				s.applyPretender();				
			ps.println();
		}
		ps.println("Map:");
		s.getMap().print(ps);
	}

}
