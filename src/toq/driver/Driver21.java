/**
 * 
 */
package toq.driver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import toq.ankete.Ankete;
import toq.ankete.impl.AnketeImpl;

/**
 * @author anton.bondarenko@gmail.com
 * Jan 19, 2010
 * 1:28:17 PM
 */
public class Driver21 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Ankete a = null;
		try {
			a = new AnketeImpl("dat/Driver21/dat/test1.dat");
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		PrintStream ds = null;
		try {
			ds = new PrintStream(new File("dat/Driver21/dat/test2mdp.dat"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			a.printMDP(ds);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
