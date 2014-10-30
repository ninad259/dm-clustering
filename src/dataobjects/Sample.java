package dataobjects;
public class Sample {
	private int sampleId;
	private int groundTruthClusterId;
	private float[] features;
	private int calculatedClusterId;
	public Sample(int sampleId, int groundTruthClusterId, float[] features){
		this.sampleId = sampleId;
		this.groundTruthClusterId = groundTruthClusterId;
		this.features = features;
	}
	public int getSampleId() {
		return sampleId;
	}
	public void setSampleId(int sampleId) {
		this.sampleId = sampleId;
	}
	public int getGroundTruthClusterId() {
		return groundTruthClusterId;
	}
	public void setGroundTruthClusterId(int groundTruthClusterId) {
		this.groundTruthClusterId = groundTruthClusterId;
	}
	public float[] getFeatures() {
		return features;
	}
	public void setFeatures(float[] features) {
		this.features = features;
	}
	public int getCalculatedClusterId() {
		return calculatedClusterId;
	}
	public void setCalculatedClusterId(int calculatedClusterId) {
		this.calculatedClusterId = calculatedClusterId;
	}
	
	
}
