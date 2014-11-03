package clustering.driver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import utils.FileUtils;
import clustering.kMeans.KMeansClustering;
import dataobjects.Sample;


public class ClusteringDriver {

	public static void main(String[] args) throws IOException {
		FileUtils file = new FileUtils();
		int numberOfClusters;
		int iterations;
		int counter;
		String[] centroidIDString;
		int[] centroidID;
		
		
		try {
			ArrayList<Sample> samples = new ArrayList<Sample>();
			Scanner scanner = file.readFileUsingScanner("/cho.txt");
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

			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			//Get no. of clusters
			System.out.println("Enter the number of clusters");
			numberOfClusters = Integer.parseInt(br.readLine());
			
			//Get centroid ID
			System.out.println("Enter the centroid ID");
			centroidIDString = br.readLine().split("\\s");
			centroidID = new int[numberOfClusters];			
			for(counter = 0;counter<centroidIDString.length;counter++){
				centroidID[counter] = Integer.parseInt(centroidIDString[counter]);
			}
			
			//Get iterations
			System.out.println("Enter the number of iterations");
			iterations = Integer.parseInt(br.readLine());
			
			//Call Kmeans 
			KMeansClustering KC = new KMeansClustering();
			KC.clustering(samples,numberOfClusters,centroidID,iterations,0,false);
			
			
		} catch (Exception e) {
			System.err.println(e.toString());;
		}
		
	}

}
