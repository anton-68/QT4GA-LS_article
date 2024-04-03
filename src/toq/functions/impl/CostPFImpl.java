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
 * 12:18:33 PM
 */
public class CostPFImpl implements PFunctionType {
	/**
	 * Human-readable function name
	 */
	static final String functionName = "Cost preference function";
	
	/**
	 * Function unique symbol
	 */
	static final char functionSymbol = 'C';
	
	/* (non-Javadoc)
	 * @see toq.functions.PFunctionType#getRoot(toq.ankete.Ankete)
	 */
	@Override
	public int getRoot(Ankete a) {
		int q = 0;
		double minC = a.getCost(0);
		for(int j = 1; j < a.getNOQ(); j++)
			if(a.getCost(j) < minC){
				minC = a.getCost(j);
				q = j;
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
		CostPFImpl pf = new CostPFImpl();
		System.out.println(pf.getName() + ": " + pf.getRoot(a));
	}

}
