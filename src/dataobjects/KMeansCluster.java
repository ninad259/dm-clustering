package dataobjects;

import java.util.ArrayList;

public class KMeansCluster {
	
	ArrayList<Integer> sampleID = new ArrayList<Integer>();
	public float[] centroid;
	
	
	public KMeansCluster(int length){
		centroid = new float[length];
	}


	public ArrayList<Integer> getSampleID() {
		return sampleID;
	}


	public void setSampleID(ArrayList<Integer> sampleID) {
		this.sampleID = sampleID;
	}


	public float[] getCentroid() {
		return centroid;
	}


	public void setCentroid(float[] centroid) {
		this.centroid = centroid;
	}

}
