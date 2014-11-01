package clustering.kMeans;
import java.util.ArrayList;

import clustering.driver.Clustering;
import dataobjects.Sample;
import dataobjects.KMeansCluster;
import indexing.external.*;

public class KMeansClustering {
	
	//Step 1:Declare Variables
	int groundTruthLabel=0;
	int numberOfClusters=0;
	int lengthOfFeatureVector;
	int featureCounter=0;
	int clusterCounter=0;
	int sampleCounter =0;
	KMeansCluster[] clusters;
	boolean recalculateCentroid = false;
	double jaccardCoefficient=0;
	
	
	public float calculateEucledianDistance(Sample s ,float[] centroid){
		int featureCounter;
		float Euclediandistance=0;
		
		for(featureCounter=0;featureCounter < lengthOfFeatureVector ; featureCounter++){
			Euclediandistance = Euclediandistance + (float)Math.pow((s.getFeatures()[featureCounter] - centroid[featureCounter]),2);
		}
		
		return (float)Math.sqrt(Euclediandistance);
	}
		
	public void assignCluster(Sample s , int sampleCounter){		
		int clusterCounter=0;
		float minimumEucledianDistance=Float.MAX_VALUE;
		float eucledianDistance;
		int clusterID=0;
		
		for(clusterCounter=0;clusterCounter <numberOfClusters;clusterCounter++){
			eucledianDistance = calculateEucledianDistance(s,clusters[clusterCounter].getCentroid());
			if(minimumEucledianDistance > eucledianDistance){
				minimumEucledianDistance = eucledianDistance;
				clusterID = clusterCounter + 1;
			}
		}
		
		//Set the clusterId in Sample
		if(s.getCalculatedClusterId()!= clusterID){
		   recalculateCentroid = true;
		   s.setCalculatedClusterId(clusterID);
		   clusters[clusterID -1].getSampleID().add(sampleCounter);
		}
		
		
	}
		
	public void clustering(ArrayList<Sample> samples,int numberOfClusters,int[] centroidID,int iterations,double sse ,boolean iterationFlag) {
		
		int sampleNumber=0;
		float centroidValue=0;
		int numberOfSamplesInCluster=0;
		int totalError=0;
				
		//Step 2:Initialize Variables
		lengthOfFeatureVector = samples.get(0).getFeatures().length;
        this.numberOfClusters=numberOfClusters;				
				
		//Check if samples is null
		if(samples==null || samples.size()==0){
			return;
		}				
		
		//Initialize clusters;
		clusters = new KMeansCluster[numberOfClusters];  //clusters is an array if size numberOfClusters
		for(clusterCounter = 0;clusterCounter<numberOfClusters;clusterCounter++){
			clusters[clusterCounter] = new KMeansCluster(lengthOfFeatureVector);
		}												
		
		//Initialize centroids for 1st pass					
		for(featureCounter=0;featureCounter<lengthOfFeatureVector;featureCounter++){														
		   for(clusterCounter=0;clusterCounter<numberOfClusters;clusterCounter++){
		      clusters[clusterCounter].getCentroid()[featureCounter] = samples.get(centroidID[clusterCounter]).getFeatures()[featureCounter];								
		   }
		}			
														
		//Assign each sample a cluster ID
		for(sampleCounter=0;sampleCounter<samples.size();sampleCounter++){			
			assignCluster(samples.get(sampleCounter),sampleCounter);
		}
		
		
		while(recalculateCentroid){			
		   recalculateCentroid=false;
			
		   //Recalculate centroids
		   for(clusterCounter=0;clusterCounter<numberOfClusters;clusterCounter++){			
			  //calculate number of samples in cluster
			  numberOfSamplesInCluster = clusters[clusterCounter].getSampleID().size();
			  for(featureCounter=0;featureCounter<lengthOfFeatureVector;featureCounter++){
			  	 centroidValue=0;
				 //for each sample compute the centroid of a particular feature
				 for(sampleCounter=0;sampleCounter<numberOfSamplesInCluster;sampleCounter++){
					sampleNumber = clusters[clusterCounter].getSampleID().get(sampleCounter);
					centroidValue = centroidValue + samples.get(sampleNumber).getFeatures()[featureCounter];
				 }				
				 centroidValue = centroidValue/numberOfSamplesInCluster;
				 clusters[clusterCounter].getCentroid()[featureCounter] = centroidValue;												
			  }			
		   }
		
		   //Assign each sample a cluster ID
		   for(sampleCounter=0;sampleCounter<samples.size();sampleCounter++){			
		      assignCluster(samples.get(sampleCounter),sampleCounter);
		   }								
	    }
		
		//Calculate error
		for(Sample s : samples){
			if(s.getCalculatedClusterId()!=s.getGroundTruthClusterId()){
				totalError++;
			}
		}
		
		System.out.println("Total error=" + totalError + "Error percent=" + (totalError*1.0)/(samples.size()));
		
		//Calculate Jaccard Coefficient
		jaccardCoefficient = ExternalIndex.getJaccardCoeff(samples);
		System.out.println("Jaccard Coefficient=" + jaccardCoefficient);
		
	}			
}

