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
 * Jan 14, 2010
 * 2:48:15 PM
 */
public class nPFImpl implements PFunctionType {
	/**
	 * Human-readable function name
	 */
	String functionName; 
	
	/**
	 * Function unique symbol
	 */
	static final char functionSymbol = 'N';
	
	/**
	 * Preferred question
	 */
	int q;
	
	/**
	 * Default constructor
	 */
	nPFImpl(){
		functionName = "Preference function linked to question 0";
	}
	
	/**
	 * Question number-based constructor
	 * @param q
	 */
	public nPFImpl(int q){
		this.q = q;
		functionName = "Preference function linked to question " + q;
	}
	
	/* (non-Javadoc)
	 * @see toq.functions.PFunctionType#getRoot(toq.ankete.Ankete)
	 */
	@Override
	public int getRoot(Ankete a) {
		if(a.getNOQ() == 1)
			return 0;
		if(a.getNOE() == 1){
			System.err.println("Number of events must be 2 or more");
			System.err.println("for calling PFs:");
			System.err.println("n = " + a.getNOQ());
			(new Exception()).printStackTrace(System.err);
			System.exit(1);
		}
		int r = q;
		while(r >= a.getNOQ())
			r = (int) Math.round((float)r/2.0);
		return r;
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
		nPFImpl pf;
		for(int i = 0; i < 3; i++){
			pf = new nPFImpl(i);
			System.out.println(pf.getName() + ": " + pf.getRoot(a));
		}			
	}

}
