package clustering.hierarchical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import utils.ConfigUtils;
import clustering.driver.Clustering;
import dataobjects.Sample;

public class HierarchicalClustering implements Clustering{

	//to maintain the snapshot at each level
	ArrayList<ArrayList<Cluster>> snapshot = new ArrayList<ArrayList<Cluster>>(); 


	private class Cluster{
		private ArrayList<Sample> samples;
		public Cluster(ArrayList<Sample> samples){
			this.samples = samples;
		}
		public ArrayList<Sample> getSamples() {
			return samples;
		}
		public void setSamples(ArrayList<Sample> samples) {
			this.samples = samples;
		}
	}

	@Override
	public void clustering(ArrayList<Sample> samples) {
		int noOfClusters = Integer.parseInt(ConfigUtils.getProperties().getProperty("cluster.count"));

		ArrayList<Cluster> clusterArray = new ArrayList<Cluster>();
		for(int i = 0; i<samples.size(); i++){
			ArrayList<Sample> sampleArray = new ArrayList<Sample>();
			//initially each is its own cluster
			samples.get(i).setCalculatedClusterId(i);
			sampleArray.add(samples.get(i));
			clusterArray.add(new Cluster(sampleArray));
		}

		//initial snapshot
		snapshot.add(new ArrayList<Cluster>(clusterArray));

		//perform size-1 agglomerations
		for(int i = 0; i<samples.size()-1-noOfClusters+1; i++){
			agglomerationUsingMin(clusterArray);
			snapshot.add(new ArrayList<Cluster>(clusterArray));
		}
		/*for(Cluster c : snapshot.get(snapshot.size()-1)){
			for(Sample s: c.getSamples()){
				System.out.println(s.getCalculatedClusterId());
			}
		}*/
		int clusterCount = 0;
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for(Sample s : samples){
			if(!map.containsKey(s.getCalculatedClusterId())){
				map.put(s.getCalculatedClusterId(), ++clusterCount);
			}
		}
		for(Sample s : samples){
			s.setCalculatedClusterId(map.get(s.getCalculatedClusterId()));
		}
	}

	public static void agglomerationUsingMin(ArrayList<Cluster> clusterArray){
		int sample1index = 0;
		int sample2index = 0;
		double minEuclideanDistance = Double.MAX_VALUE; 
		for(int i = 0; i<clusterArray.size(); i++){
			for(int j = i+1; j<clusterArray.size(); j++){
				for(Sample s1 : clusterArray.get(i).getSamples()){
					for(Sample s2 : clusterArray.get(j).getSamples()){
						if (s1.getEuclideanDistance(s2)<minEuclideanDistance){
							sample1index = i;
							sample2index = j;
							minEuclideanDistance = s1.getEuclideanDistance(s2);
						}
					}
				}
			}
		}
		//merge the two min indices into one index
		ArrayList<Sample> agglomeratedSamples = new ArrayList<Sample>();
		agglomeratedSamples.addAll(clusterArray.get(sample1index).getSamples());


		//printing dendrogram-ish output
		System.out.print("Merging cluster ");
		for(Sample s : clusterArray.get(sample1index).getSamples()){
			System.out.print(s.getSampleId() + " ");
		}
		System.out.print(" with ");
		for(Sample s : clusterArray.get(sample2index).getSamples()){
			System.out.print(s.getSampleId() + " ");
		}
		System.out.println();
		agglomeratedSamples.addAll(clusterArray.get(sample2index).getSamples());

		//change calculatedClusterId to new Id
		for(Sample s : agglomeratedSamples){
			s.setCalculatedClusterId(sample1index);
		}
		clusterArray.get(sample1index).setSamples(agglomeratedSamples);
		clusterArray.remove(sample2index);

	}


	//this method is now redundant, delete/make it usable
	public double[][] computeDistanceMatrix(ArrayList<Sample> samples){
		int sampleSize = samples.size();
		double[][] distanceMatrix = new double[sampleSize][sampleSize];
		for(int i = 0; i<sampleSize; i++){
			for(int j = i; j<sampleSize; j++){
				if(i==j){
					distanceMatrix[i][j] = 0;
				}else{
					distanceMatrix[i][j] = samples.get(i).getEuclideanDistance(samples.get(j));
					distanceMatrix[j][i] = distanceMatrix[i][j];
				}
			}
		}

		return distanceMatrix;
	}
}
