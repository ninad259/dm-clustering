package indexing.internal;

import java.util.ArrayList;

import dataobjects.Sample;

public class InternalIndex {

	public static double getCorrelation(ArrayList<Sample> samples){
		double result = 0;
		double[][] incidenceMatrix = (double[][]) getIncidenceMatrix(samples);
		double[][] distanceMatrix = getDistanceMatrix(samples);
		result = ( (squaredStandardDeviation(incidenceMatrix, distanceMatrix))
				/ ((Math.sqrt(squaredStandardDeviation(distanceMatrix,distanceMatrix)))*(Math.sqrt(squaredStandardDeviation(incidenceMatrix,incidenceMatrix))) ));
		return result;
	}

	public static double[][] getIncidenceMatrix(ArrayList<Sample> samples){
		double[][] result = null;
		int length = samples.size();
		if(samples!=null && length>0){
			result = new double[length][length];
			for(int i=0; i<length; i++){
				for(int j=i; j<length; j++){
					if(samples.get(i).getCalculatedClusterId() ==  samples.get(j).getCalculatedClusterId()){
						if(samples.get(i).getCalculatedClusterId()!=-1 && samples.get(j).getCalculatedClusterId()!=-1){
							result[i][j]=1;
							result[j][i]=1;	
						}
					}else{
						result[i][j]=0;
					}
				}
			}
		}
		return result;
	}

	public static double[][] getDistanceMatrix(ArrayList<Sample> samples){
		double[][] result = null;
		int length = samples.size();
		if(samples!=null && length>0){
			result = new double[length][length];
			for(int i=0; i<length; i++){
				for(int j=i; j<length; j++){
					result[i][j] = samples.get(i).getEuclideanDistance(samples.get(j));
					result[j][i] = result[i][j];
				}
			}
		}
		return result;
	}

	public double getMeanOfMatrix(int[][] matrix){
		double sum = 0;
		for(int i=0; i<matrix.length; i++){
			for(int j=0; j<matrix.length; j++){
				sum += matrix[i][j];
			}
		}
		return (double)(sum/(matrix.length*matrix.length));
	}

	public static double getMeanOfMatrix(double[][] matrix){
		double sum = 0;
		for(int i=0; i<matrix.length; i++){
			for(int j=0; j<matrix.length; j++){
				sum += matrix[i][j];
			}
		}
		return (double)(sum/(matrix.length*matrix.length));
	}

	public static double squaredStandardDeviation(double[][] m1, double[][] m2){
		double meanM1 = getMeanOfMatrix(m1);
		double meanM2 = getMeanOfMatrix(m2);
		int length = m1.length;
		double sum = 0;
		for(int i=0; i<length; i++){
			for(int j=0; j<length; j++){
				sum += (m1[i][j]-meanM1)*(m2[i][j]-meanM2);
			}
		}
		return sum;
	}
}
