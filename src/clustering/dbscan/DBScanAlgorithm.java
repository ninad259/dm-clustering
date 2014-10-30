/**
 * 
 */
package clustering.dbscan;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import utils.FileUtils;

/**
 * @author Ninad
 *
 */
public class DBScanAlgorithm {

	public static void main(String args[]){
		try {
			FileUtils fileUtils = new FileUtils();
			BufferedReader reader = fileUtils.readFileUsingBufferedReader("/cho.txt");
			System.out.println(reader.readLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// testing commit and push
	}
}
