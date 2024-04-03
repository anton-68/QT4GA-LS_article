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
 * 3:18:04 PM
 */
public class N2RRatioCFImpl implements CFunctionType {
	/**
	 * Human-readable function name
	 */
	static final String functionName = "Compacteness characteristic function";
	
	/* (non-Javadoc)
	 * @see toq.functions.CFunctionType#getValue(toq.ankete.Ankete)
	 */
	@Override
	public double getValue(Ankete a) {
		return new Double(a.getNOE())/new Double(a.getNOQ());
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
		 double b[] = new double[]{0.0, 1.0};
		 b[0] = 2.0 / (double)r;
		 b[1] = (double)n / Math.ceil(Math.log(r) / Math.log(2));
		 return b;
	}

	/**
	 * Unit test
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Ankete a = new AnketeImpl("test0.dat");
		N2RRatioCFImpl pf = new N2RRatioCFImpl();
		System.out.println(pf.getName() + ": " + pf.getValue(a));
	}

}
