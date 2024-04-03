/**
 * 
 */
package toq.functions.impl;

import java.io.FileNotFoundException;

import toq.ankete.Ankete;
import toq.ankete.impl.AnketeImpl;
import toq.functions.PFunctionType;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 13, 2010
 * 3:43:22 PM
 */
public class RQMPFImpl implements PFunctionType {
	/**
	 * Human-readable function name
	 */
	static final String functionName = "Root question method preference function"; 
	
	/**
	 * Function unique symbol
	 */
	static final char functionSymbol = 'R';
	
	/* (non-Javadoc)
	 * @see toq.functions.PFunctionType#getRoot(toq.ankete.Ankete)
	 */
	@Override
	public int getRoot(Ankete a) {
		double apSum = 0.0;
		for(int j = 0; j < a.getNOE(); j++)
			apSum += a.getProbability(j);
		double normedPa[] = new double[a.getNOE()];
		for(int j = 0; j < a.getNOE(); j++)
			normedPa[j] = a.getProbability(j)/apSum;
		int q = 0;
		double minPF = Double.MAX_VALUE;
		for(int i = 0; i < a.getNOQ(); i++){
			double api0Sum = 0.0, 
			       api1Sum = 0.0;
			for(int j = 0; j < a.getNOE(); j++)
				if(a.getOutcome(i, j) == '0')
					api0Sum += a.getProbability(j);
				else
					api1Sum += a.getProbability(j);
			double pf = a.getCost(i)/api0Sum + a.getCost(i)/api1Sum;
			if (pf < minPF){
				minPF = pf;
				q = i;
			}
			//System.out.println("Q" + i + ": P0 = " + api0Sum + ", P1 = " + api1Sum + ", Ci = " + a.getCost(i) + ", RQM_PF = " + pf);
		}
		return q;
	}

	/* (non-Javadoc)
	 * @see toq.functions.PFunctionType#getValue(int, toq.ankete.Ankete)
	 */
	@Override
	public double getValue(int t, Ankete a) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see toq.functions.FunctionType#getName()
	 */
	@Override
	public String getName() {
		return functionName;
	}
	
	/* (non-Javadoc)
	 * @see toq.functions.PFunctionType#getSymbol()
	 */
	@Override
	public char getSymbol() {
		return functionSymbol;
	}
	
	/**
	 * Unit test
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Ankete a = new AnketeImpl("test0.dat");
		RQMPFImpl pf = new RQMPFImpl();
		System.out.println(pf.getName() + ": " + pf.getRoot(a));
	}
	
}
