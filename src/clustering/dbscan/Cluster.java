package clustering.dbscan;

import java.util.ArrayList;

public class Cluster {

	private int clusterId;
	private ArrayList<DBScanSample> cluster;
	public Cluster() {
		this.cluster = new ArrayList<DBScanSample>();
	}
	public int getClusterId() {
		return clusterId;
	}
	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}
	public ArrayList<DBScanSample> getCluster() {
		return cluster;
	}
	public void setCluster(ArrayList<DBScanSample> cluster) {
		this.cluster = cluster;
	}
	public boolean add(DBScanSample sample){
		return this.cluster.add(sample);
	}
}
