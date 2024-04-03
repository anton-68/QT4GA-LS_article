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
 * 11:58:20 AM
 */
public class EntropyByCostPFImpl implements PFunctionType {
	/**
	 * Human-readable function name
	 */
	static final String functionName = "Delta entropy by Cost preference function"; 
		
	/**
	 * Function unique symbol
	 */
	static final char functionSymbol = 'E';
	
	/* (non-Javadoc)
	 * @see toq.functions.PFunctionType#getRoot(toq.ankete.Ankete)
	 */
	@Override
	public int getRoot(Ankete a) {
		// Init		
		double minEbyCost = Double.MAX_VALUE;
		int q = 0;
		Ankete as[] = new Ankete[2];
		// Entropy of a
/*		double apSum = 0.0, aEntropy = 0.0;
		for(int j = 0; j < a.getNOE(); j++)
			apSum += a.getProbability(j);
		double normedPa[] = new double[a.getNOE()];
		for(int j = 0; j < a.getNOE(); j++)
			normedPa[j] = a.getProbability(j)/apSum;
		for(int j = 0; j < a.getNOE(); j++)
			aEntropy -= normedPa[j]*Math.log(normedPa[j])/Math.log(2.0);
		System.out.println("aEntropy = " + aEntropy);*/
		// For all questions
		for(int i = 0; i < a.getNOQ(); i++){	
			as = a.getSubanketes(i);
			double a0pSum = 0.0, a1pSum = 0.0, a0Entropy = 0.0, a1Entropy = 0.0;
			// Calculate E for the left subankete
			for(int j = 0; j < as[0].getNOE(); j++)
				a0pSum += as[0].getProbability(j);
			double normedPa0[] = new double[as[0].getNOE()];
			for(int j = 0; j < as[0].getNOE(); j++)
				normedPa0[j] = as[0].getProbability(j)/a0pSum;
			for(int j = 0; j < as[0].getNOE(); j++)
				a0Entropy -= normedPa0[j]*Math.log(normedPa0[j])/Math.log(2.0);						
			// Calculate E for the right subankete
			for(int j = 0; j < as[1].getNOE(); j++)
				a1pSum += as[1].getProbability(j);
			double normedPa1[] = new double[as[1].getNOE()];
			for(int j = 0; j < as[1].getNOE(); j++)
				normedPa1[j] = as[1].getProbability(j)/a1pSum;
			for(int j = 0; j < as[1].getNOE(); j++)
				a1Entropy -= normedPa1[j]*Math.log(normedPa1[j])/Math.log(2.0);	
			// Compare sum
			/*System.out.println("Question t" + i);
			System.out.println("a0Entropy = " + a0Entropy);
			System.out.println("a1Entropy = " + a1Entropy);
			System.out.println("Sum entropy = " + (a0Entropy + a1Entropy));
			System.out.println("Delta entropy by cost = " + ((a0Entropy + a1Entropy)*a.getCost(i)));*/
			if(((a0Entropy + a1Entropy)*a.getCost(i)) < minEbyCost){
				minEbyCost = (a0Entropy + a1Entropy)*a.getCost(i);
				q = i;
			}		
		}
		//System.out.println("The optimal is question t" + q);
		return q;
	}
	/* (non-Javadoc)
	 * @see toq.functions.PFunctionType#getValue(int, toq.ankete.Ankete)
	 */
	@Override
	public double getValue(int t, Ankete a) {
		// TODO Auto-generated method stub
		return 0.0;
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
		EntropyByCostPFImpl pf = new EntropyByCostPFImpl();
		System.out.println(pf.getName() + ": " + pf.getRoot(a));
	}

}
