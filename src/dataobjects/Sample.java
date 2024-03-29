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
		this.calculatedClusterId = 0;
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
	public double getEuclideanDistance(Sample sample){
		double euclideanDistance = 0;
		for(int i = 0; i<sample.getFeatures().length; i++){
			euclideanDistance+= Math.pow(Math.abs(this.features[i]-sample.getFeatures()[i]), 2);
		}
		euclideanDistance = (double) Math.sqrt(euclideanDistance);
		return euclideanDistance;
	}
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		String TAB = "\t";
		sb.append(new Integer(this.sampleId).toString()+TAB+new Integer(this.calculatedClusterId).toString()+TAB);
		for(float f : features){
			sb.append(f+TAB);
		}
		sb.append("\n");
		return sb.toString();
	}
	
}
