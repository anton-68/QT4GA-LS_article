/**
 * 
 */
package toq.ankete.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import toq.ankete.Ankete;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 10, 2010
 * 3:41:02 PM
 */
public class AnketeImpl implements Ankete {
	int n;
	int r;
	/**
	 * Probabilities
	 */
	ArrayList<Double> p;
	/**
	 * Costs
	 */
	ArrayList<Double> c;
	/**
	 * Ankete
	 */
	ArrayList<ArrayList<Character>> a;
	
	/**
	 * Default constructor
	 */
	AnketeImpl() {
		this.a = new ArrayList<ArrayList<Character>>();
		this.p = new ArrayList<Double>();
		this.c = new ArrayList<Double>();
	}
	
	/**
	 * File-based initialization
	 * @param Input filename in
	 * @throws FileNotFoundException 
	 */
	public AnketeImpl(String in) throws FileNotFoundException {
		Scanner s = new Scanner(new FileInputStream(new File(in)));
		String str;
		ArrayList<Character> al;
		n = s.nextInt();
		r = s.nextInt();
		p = new ArrayList<Double>();
		c = new ArrayList<Double>();
		for(int i = 0; i < n; i++)
			p.add(s.nextDouble());
		for(int i = 0; i < r; i++)
			c.add(s.nextDouble());
		a = new ArrayList<ArrayList<Character>>();
		for(int i = 0; i < r; i++){
			str = s.next();
			al = new ArrayList<Character>();
			for(int j = 0; j < n; j++)
				al.add(str.charAt(j));
			a.add(al);
		}
	}
	
	/**
	 * Pseudo-random ankete generator
	 * @param int n
	 * @param int r
	 */
	public AnketeImpl(int n, int r){
		this(n, r, (double)r);
	}
	
	/**
	 * Pseudo-random ankete generator
	 * @param n
	 * @param r
	 * @param maxC - maximal cost of questions
	 */
	public AnketeImpl(int n, int r, double maxC){
		this();
		this.n = n;
		this.r = r;
		if(n <= 32)
			intBasedGenerator(maxC);
		else {
/*			System.err.println("Too large number of events requested:");
			System.err.println("n = " + n);
			System.err.println("n <= 32 are supported only in current release");
			System.exit(1);*/
			bigintBasedGenerator(maxC);
		}
	}
	
	/**
	 * Int-based ankete generator for small anketes (n<=32)
	 * @param n
	 * @param r
	 * @param a
	 * @param p
	 * @param c
	 * @param maxC
	 */
	private void intBasedGenerator(double maxC){
		if(r < Math.ceil(Math.log((double)n)/Math.log(2.0))){
			System.err.println("Insufficient number of events for ");
			System.err.println("production of logically complete ankete: ");
			System.err.println("n = " + n + ", r = " + r);
			System.exit(1);
		}
		Set<Integer> en = new HashSet<Integer>();
		int upper = (int)Math.pow(2, r);
		Random generator = new Random();
		while(en.size() < n)
			en.add(generator.nextInt(upper));
		String se;
		char[][] ea = new char[n][r];		
		int i = 0;
		for(Integer e : en){			
			se = Integer.toBinaryString(e);
			while(se.length() < r)
				se = '0' + se;
			//System.out.println("Integer = " + e + ", string = " + se);
			for(int j = 0; j < r; j++)
				ea[i][j] = se.charAt(j);
			i++;
		}
		ArrayList<Character> cq;
		for(i = 0; i < r; i++){
			boolean senseful = false;
			while(!senseful){
				int j = 1;
				while(j < n && (ea[j][i] == ea[0][i]))j++;
				if(j == n){
					se = Integer.toBinaryString(generator.nextInt((int)Math.pow(2, n)));
					while(se.length() < n)
						se = '0' + se;
					for(int k = 0; k < n; k++)
						ea[k][i] = se.charAt(k);
				}
				else
					senseful = true;
			}
			cq = new ArrayList<Character>();
			for(int j = 0; j < n; j++)
				cq.add(ea[j][i]);
			a.add(cq);
		}	
		for(i = 0; i < n; i++)
			p.add(generator.nextDouble());
		double pSum = 0.0;
		for(i = 0; i < n; i++)
			pSum += p.get(i);
		for(i = 0; i < n; i++)
			p.set(i, p.get(i)/pSum);
		for(i = 0; i < r; i++)
			c.add(generator.nextDouble()*maxC);
}

	
	/**
	 * BigInteger-based ankete generator for large anketes
	 * @param n
	 * @param r
	 * @param a
	 * @param p
	 * @param c
	 * @param maxC
	 */
	private void bigintBasedGenerator(double maxC){
		if(r < Math.ceil(Math.log((double)n)/Math.log(2.0))){
			System.err.println("Insufficient number of events for ");
			System.err.println("production of logically complete ankete: ");
			System.err.println("n = " + n + ", r = " + r);
			System.exit(1);
		}	
		Set<BigInteger> en = new HashSet<BigInteger>();
		//int upper = (int)Math.pow(2, r);
		Random generator = new Random();
		while(en.size() < n)
			en.add(new BigInteger(r, generator));
		String se;
		char[][] ea = new char[n][r];		
		int i = 0;
		for(BigInteger e : en){			
			se = e.toString(2);
			while(se.length() < r)
				se = '0' + se;
			//System.out.println("Integer = " + e + ", string = " + se);
			for(int j = 0; j < r; j++)
				ea[i][j] = se.charAt(j);
			i++;
		}
		ArrayList<Character> cq;
		for(i = 0; i < r; i++){			
			boolean senseful = false;
			while(!senseful){
				int j = 1;
				while(j < n && (ea[j][i] == ea[0][i]))j++;
				if(j == n){
					se = (new BigInteger(n, generator)).toString(2);
					while(se.length() < n)
						se = '0' + se;
					for(int k = 0; k < n; k++)
						ea[k][i] = se.charAt(k);
				}
				else
					senseful = true;
			}		
			cq = new ArrayList<Character>();
			for(int j = 0; j < n; j++)
				cq.add(ea[j][i]);
			a.add(cq);
		}	
		for(i = 0; i < n; i++)
			p.add(generator.nextDouble());
		double pSum = 0.0;
		for(i = 0; i < n; i++)
			pSum += p.get(i);
		for(i = 0; i < n; i++)
			p.set(i, p.get(i)/pSum);
		for(i = 0; i < r; i++)
			c.add(generator.nextDouble()*maxC);
}
	
	/* (non-Javadoc)
	 * @see toq.local_search.ankete.Ankete#getCost(int)
	 */
	@Override
	public double getCost(int t) {
		return c.get(t);
	}

	/* (non-Javadoc)
	 * @see toq.local_search.ankete.Ankete#getNOE()
	 */
	@Override
	public int getNOE() {
		return n;
	}

	/* (non-Javadoc)
	 * @see toq.local_search.ankete.Ankete#getNOQ()
	 */
	@Override
	public int getNOQ() {
		return r;
	}

	/* (non-Javadoc)
	 * @see toq.local_search.ankete.Ankete#getOutcome(int, int)
	 */
	@Override
	public char getOutcome(int t, int y) {
		return a.get(t).get(y);
	}

	/* (non-Javadoc)
	 * @see toq.local_search.ankete.Ankete#getProbability(int)
	 */
	@Override
	public double getProbability(int y) {
		return p.get(y);
	}

	/* (non-Javadoc)
	 * @see toq.local_search.ankete.Ankete#getSubanketes(int)
	 */
	@Override
	public Ankete[] getSubanketes(int t) {
		//char oc[] = {'0','1'};
		char tc;
		boolean found, senseless0, senseless1;
		ArrayList<Character> tmpQ0, tmpQ1, tmpQ0inv, tmpQ1inv;
		AnketeImpl[] sa = new AnketeImpl[2];
		sa[0] = new AnketeImpl();
		sa[1] = new AnketeImpl();
		for(int i = 0; i < r; i++){	if(i != t){
// 1. build questions and their inverses for subanketes
			tmpQ0 = new ArrayList<Character>();
			tmpQ1 = new ArrayList<Character>();
			tmpQ0inv = new ArrayList<Character>();
			tmpQ1inv = new ArrayList<Character>();
			senseless0 = senseless1 = true;
			for(int j = 0; j < n; j++) {
				tc = a.get(i).get(j);
				if(a.get(t).get(j) == '0') {
					tmpQ0.add(tc);
					if(tmpQ0.get(0) != tc)
						senseless0 = false;
					if(tc == '0')
						tmpQ0inv.add('1');
					else
						tmpQ0inv.add('0');
				}	
				else {
					tmpQ1.add(a.get(i).get(j));
					if(tmpQ1.get(0) != tc)
						senseless1 = false;
					if(tc == '0')
						tmpQ1inv.add('1');
					else
						tmpQ1inv.add('0');
				}
			}
// 2. check if there is equivalent question and choose cheaper one / cleanup / add
			if(!senseless0) {
				found = false;
				for(int j = 0; j < sa[0].a.size(); j++){
					if(sa[0].a.get(j).equals(tmpQ0) || sa[0].a.get(j).equals(tmpQ0inv)){
						if(sa[0].c.get(j) > this.c.get(i))
							sa[0].c.set(j, this.c.get(i)); //add->set
						found = true;
					}
				}
				if(!found){
					sa[0].a.add(tmpQ0);
					sa[0].c.add(this.c.get(i)); 
				}	
			}
			if(!senseless1) {
				found = false;
				for(int j = 0; j < sa[1].a.size(); j++){
					if(sa[1].a.get(j).equals(tmpQ1) || sa[1].a.get(j).equals(tmpQ1inv)){
						if(sa[1].c.get(j) > this.c.get(i))
							sa[1].c.set(j, this.c.get(i));//add->set
						found = true;
					}
				}
				if(!found){
					sa[1].a.add(tmpQ1);
					sa[1].c.add(this.c.get(i)); 
				}			
			}	
		}}
// 3. Copy probabilities
		for(int j = 0; j < n; j++) {
			if(a.get(t).get(j) == '0')
				sa[0].p.add(this.p.get(j));
			else
				sa[1].p.add(this.p.get(j));
		}
// 3. Set sizes
		sa[0].n = sa[0].p.size();
		sa[0].r = sa[0].c.size();
		sa[1].n = sa[1].p.size();
		sa[1].r = sa[1].c.size();
		return sa;
	}
	
	/* (non-Javadoc)
	 * @see toq.local_search.ankete.Ankete#print(java.lang.String)
	 */
	@Override
	public void print(String of) throws FileNotFoundException {
		PrintStream ps = new PrintStream(new File(of));
		this.print(ps);
		
/*		ps.println(n);
		ps.println(r);
		for(int i = 0; i < n-1; i++)
			ps.print(p.get(i) + " ");
		ps.println(p.get(n-1));
		if(n > 1) {
			for(int i = 0; i < r-1; i++)
				ps.print(c.get(i) + " ");
			ps.println(c.get(r-1));
			for(int i = 0; i < r; i++){
				for(int j = 0; j < n; j++)
					ps.print(a.get(i).get(j) + " ");
				ps.println();
			}
		}*/
	}
	
	/* (non-Javadoc)
	 * @see toq.ankete.Ankete#print(java.io.PrintStream)
	 */
	@Override
	public void print(PrintStream ps) throws FileNotFoundException {
		ps.println(n);
		ps.println(r);
		for(int i = 0; i < n-1; i++)
			ps.print(p.get(i) + " ");
		ps.println(p.get(n-1));
		if(n > 1) {
			for(int i = 0; i < r-1; i++)
				ps.print(c.get(i) + " ");
			ps.println(c.get(r-1));
			for(int i = 0; i < r; i++){
				for(int j = 0; j < n; j++)
					ps.print(a.get(i).get(j)/* + " "*/);
				ps.println();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see toq.ankete.Ankete#print(java.io.PrintStream)
	 */
	@Override
	public void printMDP(PrintStream ps) throws FileNotFoundException {
		ps.println(r + " " + n);
		for(int i = 0; i < r; i++){
			for(int j = 0; j < n-1; j++)
				ps.print(a.get(i).get(j) + " ");
			ps.print(a.get(i).get(n-1));
			ps.println();
		}
		for(int i = 0; i < n-1; i++)
			ps.print(p.get(i) + " ");
		ps.println(p.get(n-1));
		for(int i = 0; i < r-1; i++)
			ps.print(c.get(i) + " ");
		ps.println(c.get(r-1));
	}
	
	/* (non-Javadoc)
	 * @see toq.ankete.Ankete#getOptPrice()
	 */
	@Override
	public double getOptPrice() {
		if(n == 1)
			return 0.0;
		else
			return minPrice(this);
	}
	
	private double minPrice(Ankete a) {
		//System.out.println("LOG:minPrice has been invoked");
		//System.out.println("LOG:n = " + a.getNOE());
		if( a.getNOE() == 1){		
			//System.out.println("LOG:0 returned");
			return 0.0;
		}
		else {
			double sumP = 0.0;
			for(int i = 0; i < a.getNOE(); i++)
				sumP += a.getProbability(i);
			double minC = Double.MAX_VALUE;
			double minCa0, minCa1;
			Ankete sa[] = new Ankete[2];
			//System.out.println("LOG:Opt is calculated");
			for(int i = 0; i < a.getNOQ(); i++){
				sa = a.getSubanketes(i);
				minCa0 = minPrice(sa[0]);
				minCa1 = minPrice(sa[1]);
				if(a.getCost(i) * sumP + minCa0 + minCa1 < minC)
					minC = a.getCost(i) * sumP + minCa0 + minCa1;
			}
			//System.out.println("LOG:minC = " + minC);
			return minC;
		}
	}
	
	/**
	 * @param of
	 * @throws FileNotFoundException
	 */
	@Override
	public void printMDP(String of) throws FileNotFoundException {
		PrintStream ps = new PrintStream(new File(of));
		this.printMDP(ps);	
	}
		
	/**
	 * Unit test
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		PrintStream ps = new PrintStream(new File("dat/test18.dat"));
		Ankete a;
		a = new AnketeImpl(15, 7, 100.0);
		a.print(ps);
/*		long t0 = System.currentTimeMillis();
		ps.println("Price of optimal questionnaire: " + a.getOptPrice());
		long t1 = System.currentTimeMillis();
		ps.println("Computation time: " + (t1-t0));*/
	}


}
