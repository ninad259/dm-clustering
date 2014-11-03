package clustering.dbscan;

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
import dataobjects.Sample;


public class DBScanClusteringDriver {
	
	static double bestEpsilon=0.0, bestMinPoints=0.0, bestCorrelation=0.0, bestJaccard=0.0;
	private static Properties prop ;
	
	public static void main(String[] args) throws IOException {
		prop = ConfigUtils.getProperties();
		try {
			ArrayList<Sample> samples = new ArrayList<Sample>();
			Scanner scanner = getScannerForFile();
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
//			for(int i=1; i<=100; i++){
//				for(int j=2; j<5; j++){
//					double epsilon = 0.027+i*0.001;
//					int minPoints = j;
					double epsilon = getEpsilon();
					int minPoints = getMinPoints();
					ArrayList<Sample> newSamples = new ArrayList<Sample>();
					newSamples.addAll(samples);
					if(isNormalized())
						normalizeData(newSamples);
					DBScanClustering clusterTech = new DBScanClustering(epsilon, minPoints);
					clusterTech.clustering(newSamples);

					ArrayList<Cluster> clusters = clusterTech.getClusters();
					System.out.print("# Total Clusters: "+ (clusters.size()-1)+" -> ");
					for(Cluster c : clusters){
						System.out.print("<"+c.getClusterId()+","+c.getCluster().size()+"> ");
					}
					System.out.println();
					double correlation = InternalIndex.getCorrelation(newSamples);
					double jaccard = ExternalIndex.getJaccardCoeff(newSamples);
//					evaluateClusteringTech(epsilon, minPoints, correlation, jaccard, currentClusters);
					System.out.println("eps: "+epsilon+" minPts: "+minPoints+" "+" correlation: "+correlation+" Jaccard: "+jaccard);
					File f = new File(prop.getProperty("resources.folder")+"/"+prop.getProperty("outputfile.name"));
					if(!f.exists()){
						f.createNewFile();
					}
					FileWriter fw = new FileWriter(f.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					for(Sample s : newSamples){
						bw.write(s.toString());
					}
					bw.close();
//				}
//			}
		} catch (FileNotFoundException e) {
			System.err.println(e.toString());;
		}
	}

	public static void evaluateClusteringTech(double epsilon, int minPoints, double correlation, double jaccard, ArrayList<Cluster> clusters){
		if(jaccard>bestJaccard || correlation>bestCorrelation){
			if(correlation>bestCorrelation)
				bestCorrelation = correlation;
			if(jaccard>bestJaccard)
				bestJaccard = jaccard;
			bestEpsilon = epsilon;
			bestMinPoints = minPoints;
//			System.out.println("eps: "+bestEpsilon+" minPts: "+bestMinPoints+" "+" correlation: "+bestCorrelation+" Jaccard: "+bestJaccard);
			System.out.print("# Total Clusters: "+ (clusters.size()-1)+" -> ");
			for(Cluster c : clusters){
				System.out.print("<"+c.getClusterId()+","+c.getCluster().size()+"> ");
			}
			System.out.println();
		}
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

	public static double getEpsilon(){
		Double result = Double.parseDouble((String)prop.get("epsilon"));
		return result;
	}

	public static int getMinPoints(){
		Integer result = Integer.parseInt((String)prop.get("min.points"));
		return result;
	}
	
	public static boolean isNormalized(){
		Boolean result = Boolean.parseBoolean((String)prop.get("normalized"));
		return result;
	}
	
	public static Scanner getScannerForFile(){
		String filePath = prop.getProperty("file.name");
		FileUtils fileUtils = new FileUtils();
		Scanner sc;
		try {
			sc = fileUtils.readFileUsingScanner("/"+filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return sc;
	}
}
