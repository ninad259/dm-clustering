package clustering.hierarchical;

import java.util.ArrayList;

import clustering.driver.Clustering;
import dataobjects.Sample;

public class HierarchicalClustering implements Clustering{


	@Override
	public void clustering(ArrayList<Sample> samples) {
		
	}
	public float[][] computeDistanceMatrix(ArrayList<Sample> samples){
		int sampleSize = samples.size();
		float[][] distanceMatrix = new float[sampleSize][sampleSize];
		
		
		return distanceMatrix;
	}
}
