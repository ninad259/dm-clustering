package clustering.kMeans;
import indexing.external.ExternalIndex;
import indexing.internal.InternalIndex;

import java.util.ArrayList;

import dataobjects.KMeansCluster;
import dataobjects.Sample;

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
	double correlation =0;
	int numberOfIterations=1;
	float eucledianDistance;
	float minimumEucledianDistance;
	
	
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
		
		//Add the sample in the respective cluster
		clusters[clusterID -1].getSampleID().add(sampleCounter);
		
		//Set recalculateCentroid
		if(s.getCalculatedClusterId()!= clusterID){
		   recalculateCentroid = true;
		   s.setCalculatedClusterId(clusterID);		   
		}
		
		
	}
		
	public void clustering(ArrayList<Sample> samples,int numberOfClusters,int[] centroidID,int iterations,double sse ,boolean iterationFlag) {
		
		int sampleNumber=0;
		float centroidValue=0;
		int numberOfSamplesInCluster=0;
//		int totalError=0;
				
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
		      clusters[clusterCounter].getCentroid()[featureCounter] = samples.get(centroidID[clusterCounter]-1).getFeatures()[featureCounter];								
		   }
		}			
														
		//Assign each sample a cluster ID
		for(sampleCounter=0;sampleCounter<samples.size();sampleCounter++){			
			assignCluster(samples.get(sampleCounter),sampleCounter);
		}
		
		
		while(recalculateCentroid  && numberOfIterations<iterations){			
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
		   
		   //Increment iterations
		   numberOfIterations++;
		   
		   //Remove the assigned samples to cluster
		   for(clusterCounter=0;clusterCounter<numberOfClusters;clusterCounter++){
			   clusters[clusterCounter].getSampleID().clear(); 
		   }
		
		   //Assign each sample a cluster ID
		   for(sampleCounter=0;sampleCounter<samples.size();sampleCounter++){			
		      assignCluster(samples.get(sampleCounter),sampleCounter);
		   }								
	    }
		/*
		for(clusterCounter=0;clusterCounter<numberOfClusters;clusterCounter++){
			System.out.println("clusterCounter:" + clusterCounter + " Size:" + clusters[clusterCounter].getSampleID().size());
		}
		
		//Find the sample closest to each centroid
		
		//Refresh the clusters
		for(clusterCounter=0;clusterCounter<numberOfClusters;clusterCounter++){
			   clusters[clusterCounter].getSampleID().clear(); 
		}
		
		//
		for(clusterCounter=0;clusterCounter<numberOfClusters;clusterCounter++){	
			minimumEucledianDistance = Float.MAX_VALUE;
			for(sampleCounter=0;sampleCounter<samples.size();sampleCounter++){
				eucledianDistance = calculateEucledianDistance(samples.get(sampleCounter),clusters[clusterCounter].getCentroid());
				if(minimumEucledianDistance > eucledianDistance){
					minimumEucledianDistance = eucledianDistance;
					clusters[clusterCounter].getSampleID().clear();
					clusters[clusterCounter].getSampleID().add(sampleCounter);					
				}				
			}
		}
		
		//Print the sample in each cluster
		for(clusterCounter=0;clusterCounter<numberOfClusters;clusterCounter++){
			System.out.println("cluster counter sample ID :" + clusters[clusterCounter].getSampleID());
//			System.out.println("Final Centroid =" + Arrays.toString(clusters[clusterCounter].getCentroid()));
		}
		*/
		//Calculate error
/*		for(Sample s : samples){
			if(s.getCalculatedClusterId()!=s.getGroundTruthClusterId()){
				totalError++;
			}
		}*/
		
		//System.out.println("Total error=" + totalError + "Error percent=" + (totalError*1.0)/(samples.size()));
		
		//Calculate Jaccard Coefficient
		jaccardCoefficient = ExternalIndex.getJaccardCoeff(samples);
		System.out.println("Jaccard Coefficient=" + jaccardCoefficient);
		
		correlation = InternalIndex.getCorrelation(samples);
		System.out.println("Correlation=" + correlation);
		System.out.println("number of iterations=" + numberOfIterations);
		//System.out.println(clusters[0].getSampleID().size());
		
		
	}			
}


