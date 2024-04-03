/**
 * 
 */
package toq.driver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import toq.ankete.Ankete;
import toq.ankete.impl.AnketeImpl;

/**
 * @author anton.bondarenko@gmail.com Jan 21, 2010 1:34:25 AM
 */
public class Driver32 {
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		final int noe = 127;
		final int noq = 31;
		Ankete a = new AnketeImpl(noe, noq, (double) noq);
		String line;
		OutputStream stdin = null;
		InputStream stderr = null;
		InputStream stdout = null;

		try {
			// launch EXE and grab stdin/stdout and stderr
			Process process = Runtime.getRuntime().exec("pwpc/driver.exe");
			stdin = process.getOutputStream();
			stderr = process.getErrorStream();
			stdout = process.getInputStream();

			// "write" the noe into stdin
			line = noe + "\n";
			stdin.write(line.getBytes());
			stdin.flush();

			// "write" the probabilities
			List<Double> pr = new ArrayList<Double>();
	    	for(int j = 0; j < noe; j++)
	    		pr.add(a.getProbability(j));
	    	Collections.sort(pr);
	    	line = "";
	    	for(int j = noe - 1; j >= 0; j--){
	    		line += pr.get(j).toString();
	    		line += " ";
	    	}
	    	line += "\n";
	    	stdin.write(line.getBytes() );
	    	stdin.flush();

			// "write" the costs
	    	List<Double> co = new ArrayList<Double>();
	    	int j = 0;
			while (j < noe - 1 && j < noq) 
				co.add(a.getCost(j++));
			for (j = 0; j < noe - noq - 1; j++)
				co.add(Double.MAX_VALUE);
	    	Collections.sort(co);
			line = "";
			for(j = 0; j < co.size()-1; j++) {
				line += co.get(j);
				line += " ";
			}
			line += "\n";
			stdin.write(line.getBytes());
			stdin.flush();
			stdin.close();

			// "read" the answer
			BufferedReader brCleanUp = new BufferedReader(
					new InputStreamReader(stdout));
			System.out.println("PWPC : "
					+ Double.parseDouble(brCleanUp.readLine()));

			// clean up if any output in stdout
			while ((line = brCleanUp.readLine()) != null)
				;
			brCleanUp.close();

			// clean up if any output in stderr
			brCleanUp = new BufferedReader(new InputStreamReader(stderr));
			while ((line = brCleanUp.readLine()) != null)
				System.out.println(line);
			brCleanUp.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (NullPointerException e){
			BufferedReader brCleanUp = new BufferedReader(new InputStreamReader(stderr));
			while ((line = brCleanUp.readLine()) != null)
				System.out.println(line);
			brCleanUp.close();
			e.printStackTrace();
		}

	}

}
