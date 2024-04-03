/**
 * 
 */
package toq.functions.impl;

import java.io.FileNotFoundException;

import toq.ankete.Ankete;
import toq.ankete.impl.AnketeImpl;
import toq.functions.CFunctionType;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 13, 2010
 * 1:56:57 PM
 */
public class EntropyCFImpl implements CFunctionType {
	/**
	 * Human-readable function name
	 */
	static final String functionName = "Entropy characteristic function"; 
	
	/* (non-Javadoc)
	 * @see toq.functions.CFunctionType#getValue(toq.ankete.Ankete)
	 */
	@Override
	public double getValue(Ankete a) {
		double apSum = 0.0, aEntropy = 0.0;
		for(int j = 0; j < a.getNOE(); j++)
			apSum += a.getProbability(j);
		double normedPa[] = new double[a.getNOE()];
		for(int j = 0; j < a.getNOE(); j++)
			normedPa[j] = a.getProbability(j)/apSum;
		for(int j = 0; j < a.getNOE(); j++)
			aEntropy -= normedPa[j]*(Math.log(normedPa[j])/Math.log(2.0));
		return aEntropy;
	}

	/* (non-Javadoc)
	 * @see toq.functions.FunctionType#getName()
	 */
	@Override
	public String getName() {
		return functionName;
	}

	/* (non-Javadoc)
	 * @see toq.functions.CFunctionType#getBunaries(int, int)
	 */
	@Override
	public double[] getBoundaries(int n, int r) {
		return new double[]{0.0, Math.log((double)n)/Math.log(2.0)};
	}
	
	/**
	 * Unit test
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Ankete a = new AnketeImpl("test12.dat");
		EntropyCFImpl pf = new EntropyCFImpl();
		System.out.println(pf.getName() + ": " + pf.getValue(a));
	}

}
