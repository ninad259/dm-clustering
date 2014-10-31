package clustering.driver;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import clustering.hierarchical.HierarchicalClustering;
import utils.FileUtils;
import dataobjects.Sample;


public class ClusteringDriver {

	public static void main(String[] args) throws IOException {
		FileUtils file = new FileUtils();
		
		try {
			ArrayList<Sample> samples = new ArrayList<Sample>();
			Scanner scanner = file.readFileUsingScanner("/iyer.txt");
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				String[] tokens = line.split("\\s");
				
				//number of tokens created should be 3 or above
				if(tokens.length<=2){
					throw new IOException("Invalid Input File");
				}
				float[] features = new float[tokens.length-2];
				for(int i = 2; i<tokens.length; i++){
					features[i-2] = Float.parseFloat(tokens[i]);
				}
				samples.add(new Sample(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]),features));
			}
			scanner.close();

			HierarchicalClustering HC = new HierarchicalClustering();
			HC.clustering(samples);
			
			
		} catch (FileNotFoundException e) {
			System.err.println(e.toString());;
		}
		
	}

}
