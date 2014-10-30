package clustering.dbscan;

import dataobjects.Sample;

public class DBScanSample extends Sample {

	private boolean visited;
	
	public DBScanSample(int sampleId, int groundTruthClusterId, float[] features) {
		super(sampleId, groundTruthClusterId, features);
	}
	
	public DBScanSample(Sample sample) {
		super(sample.getSampleId(), sample.getGroundTruthClusterId(), sample.getFeatures());
		this.visited = false;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	
}
