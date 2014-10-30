package clustering.hierarchical;

import java.util.ArrayList;

import clustering.driver.Clustering;
import dataobjects.Sample;

public class HierarchicalClustering implements Clustering{
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
		ArrayList<Cluster> clusterArray = new ArrayList<Cluster>();
		for(int i = 0; i<samples.size(); i++){
			ArrayList<Sample> sampleArray = new ArrayList<Sample>();
			sampleArray.add(samples.get(i));
			clusterArray.add(new Cluster(sampleArray));
		}

		//float[][]distanceMatrix = computeDistanceMatrix(samples);
		/*for(int i = 0; i<distanceMatrix.length; i++){
			for(int j = 0; j<distanceMatrix.length; j++){
				System.out.print(distanceMatrix[i][j]+ " ");
			}
			System.out.println();
		}*/




		//to maintain the snapshot at each level
		ArrayList<ArrayList<Cluster>> snapshot = new ArrayList<ArrayList<Cluster>>(); 
		
		//perform size-1 agglomerations
		for(int i = 0; i<samples.size()-1; i++){
			snapshot.add(clusterArray);
			agglomerationUsingMin(clusterArray);
			System.out.println("hi");
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
		agglomeratedSamples.addAll(clusterArray.get(sample2index).getSamples());
		clusterArray.get(sample1index).setSamples(agglomeratedSamples);
		clusterArray.remove(sample2index);
	}
	
	
	//this method is now redundant
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
