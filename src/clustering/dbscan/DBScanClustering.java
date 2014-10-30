/**
 * 
 */
package clustering.dbscan;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import utils.FileUtils;
import clustering.driver.Clustering;
import dataobjects.Sample;

/**
 * @author Ninad
 *
 */
public class DBScanClustering implements Clustering{

	private double epsilon;
	private int minPoints;

	/**
	 * @param epsilon
	 * @param minPoints
	 */
	public DBScanClustering(double epsilon, int minPoints) {
		super();
		this.epsilon = epsilon;
		this.minPoints = minPoints;
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	public int getMinPoints() {
		return minPoints;
	}

	public void setMinPoints(int minPoints) {
		this.minPoints = minPoints;
	}

	@Override
	public void clustering(ArrayList<Sample> samples) {
		ArrayList<DBScanSample> dbSamples = new ArrayList<DBScanSample>();
		for(Sample point : samples){
			dbSamples.add(new DBScanSample(point));
		}
		ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		Cluster noise = new Cluster();
		noise.setClusterId(-1);
		clusters.add(noise);
		int clusterCount = 1;
		ArrayList<DBScanSample> neighborPts;
		for(DBScanSample point : dbSamples){
			point.setVisited(true);
			neighborPts = regionQuery(point, dbSamples);
			if(neighborPts.size()<this.minPoints){
				noise.add(point);
				point.setCalculatedClusterId(-1);
			}else {
				Cluster cluster = new Cluster();
				cluster.setClusterId(clusterCount++);
				clusters.add(cluster);
				expandCluster(point, neighborPts, cluster, dbSamples);
			}
		}
		if(clusters.size()>5 && clusters.size()<12)
			System.out.print("# Clusters: "+ clusters.size()+" ");
		for(int i=0; i<samples.size(); i++){
			DBScanSample dbSample = dbSamples.get(i);
			Sample sample = samples.get(i);
			sample.setCalculatedClusterId(dbSample.getCalculatedClusterId());
		}
	}

	public void expandCluster(DBScanSample point, ArrayList<DBScanSample> neighbors, Cluster cluster, ArrayList<DBScanSample> samples){
		cluster.add(point);
		point.setCalculatedClusterId(cluster.getClusterId());
		for(int i=0; i<neighbors.size(); i++){
			DBScanSample pt = neighbors.get(i);
			if(!pt.isVisited()){
				pt.setVisited(true);
				ArrayList<DBScanSample> nextNeighbors = regionQuery(pt, samples);
				if(nextNeighbors.size()>=this.minPoints){
					neighbors.addAll(nextNeighbors);
				}
			}
			if(pt.getCalculatedClusterId()==0){
				cluster.add(pt);
				pt.setCalculatedClusterId(cluster.getClusterId());
			}
		}
	}

	public ArrayList<DBScanSample> regionQuery(DBScanSample point, ArrayList<DBScanSample> samples){
		ArrayList<DBScanSample> neighborhood = new ArrayList<DBScanSample>();
		for(DBScanSample sample : samples){
			if(point!=sample){
				if(point.getEuclideanDistance(sample) < this.epsilon){
					neighborhood.add(sample);
				}
			}
		}
		return neighborhood;
	}

	public static void initVisited(ArrayList<Boolean> visited, int size){
		for(int i=0; i<size; i++){
			visited.add(false);
		}
	}

	public static void main(String args[]){
		try {
			FileUtils fileUtils = new FileUtils();
			BufferedReader reader = fileUtils.readFileUsingBufferedReader("/cho.txt");
			System.out.println(reader.readLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// testing commit and push
	}
}
