package clustering.dbscan;

import indexing.external.ExternalIndex;
import indexing.internal.InternalIndex;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import utils.FileUtils;
import clustering.driver.Clustering;
import dataobjects.Sample;


public class DBScanClusteringDriver {
	
	static double bestEpsilon=0.0, bestMinPoints=0.0, bestAccuracy=0.0;
	
	public static void main(String[] args) throws IOException {
		FileUtils file = new FileUtils();

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
//			for(int i=1; i<100; i++){
//				for(int j=4; j<6; j++){
//					double epsilon = 0.11+i*0.001;
			double epsilon = 0.257;
					int minPoints = 5;
					ArrayList<Sample> newSamples = new ArrayList<Sample>();
					newSamples.addAll(samples);
					normalizeData(newSamples);
					Clustering clusterTech = new DBScanClustering(epsilon, minPoints);
					clusterTech.clustering(newSamples);
					
					evaluateClusteringTech(epsilon, minPoints, newSamples);
					System.out.println("Correlation: "+InternalIndex.getCorrelation(samples));
					System.out.println("Jaccard: "+ExternalIndex.getJaccardCoeff(samples));
//				}
//			}
		} catch (FileNotFoundException e) {
			System.err.println(e.toString());;
		}
	}

	public static void evaluateClusteringTech(double epsilon, int minPoints, ArrayList<Sample> samples){
		int count = 0;
		for(Sample s : samples){
			if(s.getCalculatedClusterId() == s.getGroundTruthClusterId()){
				count++;
			}
		}
		double accuracy = ((double)count/(double)samples.size());
		
//		if(accuracy*10000>5.0){
//			if(accuracy > bestAccuracy){
				bestAccuracy = accuracy;
				bestEpsilon = epsilon;
				bestMinPoints = minPoints;
				System.out.print("Epsilon: "+bestEpsilon+" minPoints: "+bestMinPoints+" ");
				System.out.println("Accuracy: "+bestAccuracy*100);
				System.out.println();
//			}
//		}
	}
	
	public static void normalizeData(ArrayList<Sample> samples){
		ArrayList<Double> min = new ArrayList<Double>();
		ArrayList<Double> max = new ArrayList<Double>();
		if(samples!=null){
			Sample sample = samples.get(0);
			for(int i=0; i<sample.getFeatures().length; i++){
				min.add(Double.MAX_VALUE);
				max.add(Double.MIN_VALUE);
			}
		}
		for(Sample point : samples){
			float[] features = point.getFeatures();
			for(int i=0; i<min.size(); i++){
				if(features[i]<min.get(i)){
					min.set(i, new Double(features[i]));
				}else if(features[i]>max.get(i)){
					max.set(i, new Double(features[i]));
				}
			}
		}
		for(Sample point : samples){
			float[] features = point.getFeatures();
			for(int i=0; i<min.size(); i++){
				if((max.get(i)-min.get(i)) > 0)
					features[i] = ((float)(features[i]-min.get(i)))/((float)(max.get(i)-min.get(i)));
			}
		}
	}
}
