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
 * 2:50:21 PM
 */
public class CostEntropyCFImpl implements CFunctionType {
	/**
	 * Human-readable function name
	 */
	static final String functionName = "Cost entropy characteristic function"; 
	
	/* (non-Javadoc)
	 * @see toq.functions.CFunctionType#getValue(toq.ankete.Ankete)
	 */
	@Override
	public double getValue(Ankete a) {
		double acSum = 0.0, aCEntropy = 0.0;
		for(int j = 0; j < a.getNOQ(); j++)
			acSum += a.getCost(j);
		double normedCa[] = new double[a.getNOQ()];
		for(int j = 0; j < a.getNOQ(); j++)
			normedCa[j] = a.getCost(j)/acSum;
		for(int j = 0; j < a.getNOQ(); j++)
			aCEntropy -= normedCa[j]*Math.log(normedCa[j])/Math.log(2.0);
		return aCEntropy;
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
		Ankete a = new AnketeImpl("test0.dat");
		CostEntropyCFImpl pf = new CostEntropyCFImpl();
		System.out.println(pf.getName() + ": " + pf.getValue(a));
	}



}
