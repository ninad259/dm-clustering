package clustering.driver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import utils.ConfigUtils;
import utils.FileUtils;
import clustering.kMeans.KMeansClustering;
import dataobjects.Sample;


public class ClusteringDriver {

	static Properties props;
	public static void main(String[] args) throws IOException {
		int numberOfClusters;
		int iterations;
		int counter;
		String[] centroidIDString;
		int[] centroidID;
		FileUtils fileUtils = new FileUtils();
		
		try {
			ArrayList<Sample> samples = new ArrayList<Sample>();
			props = ConfigUtils.getProperties();
			Scanner scanner = fileUtils.readFileUsingScanner("/"+props.getProperty("file.name")); 
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
			if(isNormalized()){
				normalizeData(samples);
			}
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
			
			File f = new File(props.getProperty("resources.folder")+"/"+props.getProperty("outputfile.name"));
			if(!f.exists()){
				f.createNewFile();
			}
			FileWriter fw = new FileWriter(f.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for(Sample s : samples){
				bw.write(s.toString());
			}
			bw.close();
		} catch (Exception e) {
			System.err.println(e.toString());;
		}
		
	}

	public static boolean isNormalized(){
		props = ConfigUtils.getProperties();
		Boolean result = Boolean.parseBoolean((String)props.get("normalized"));
		return result;
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
