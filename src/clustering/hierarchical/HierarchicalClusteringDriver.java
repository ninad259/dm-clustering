package clustering.hierarchical;
import indexing.external.ExternalIndex;
import indexing.internal.InternalIndex;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import utils.ConfigUtils;
import utils.FileUtils;
import clustering.dbscan.DBScanClusteringDriver;
import dataobjects.Sample;


public class HierarchicalClusteringDriver {

	public static void main(String[] args) throws IOException {
		FileUtils file = new FileUtils();
		Properties prop = ConfigUtils.getProperties();
		try {
			ArrayList<Sample> samples = new ArrayList<Sample>();
			//Scanner scanner = file.readFileUsingScanner("/cho.txt");
			Scanner scanner = file.readFileUsingScanner("/"+prop.getProperty("file.name"));
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
			if(prop.getProperty("normalized").equals("true")){
				DBScanClusteringDriver.normalizeData(samples);	
			}
			
			HC.clustering(samples);
			System.out.println("Correlation: "+InternalIndex.getCorrelation(samples));
			System.out.println("Jaccard: "+ExternalIndex.getJaccardCoeff(samples));
			File f = new File(prop.getProperty("resources.folder")+"/"+prop.getProperty("outputfile.name"));
			if(!f.exists()){
				f.createNewFile();
			}
			FileWriter fw = new FileWriter(f.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for(Sample s : samples){
				bw.write(s.toString());
			}
			bw.close();
			
		} catch (FileNotFoundException e) {
			System.err.println(e.toString());;
		}
		
	}

}
