package clustering.hierarchical;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import utils.ConfigUtils;
import utils.FileUtils;
import dataobjects.Sample;


public class HierarchicalClusteringDriver {

	public static void main(String[] args) {
		FileUtils file = new FileUtils();
		
		try {
			Properties properties = ConfigUtils.getProperties();
			
			ArrayList<Sample> samples = new ArrayList<Sample>();
			Scanner scanner = file.readFileUsingScanner(properties.getProperty("cho.filepath"));
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				String[] tokens = line.split("\\s");
				
				//number of tokens created should be 3 or above
				float[] features = new float[tokens.length-2];
				for(int i = 2; i<tokens.length; i++){
					features[i-2] = Float.parseFloat(tokens[i]);
				}
				samples.add(new Sample(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]),features));
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}