package indexing.external;

import java.util.ArrayList;

import dataobjects.Sample;

public class ExternalIndex {

	public static double getJaccardCoeff(ArrayList<Sample> samples){
		double result = 0;
		double[][] m1 = getIncidenceMatrixForGroundTruth(samples);
		double[][] m2 = getIncidenceMatrixForCalculatedLabel(samples);
		double ss = 0, sd = 0, ds = 0;
		for(int i=0; i<samples.size(); i++){
			for(int j=0; j<samples.size(); j++){
				if(m1[i][j]==1 && m2[i][j]==1){
					ss++;
				}
				if(m1[i][j]==1 && m2[i][j]==0){
					sd++;
				}
				if(m1[i][j]==0 && m2[i][j]==1){
					ds++;
				}
			}
		}
		result = ss / (ss+sd+ds);
		return result;
	}
	
	public static double[][] getIncidenceMatrixForGroundTruth(ArrayList<Sample> samples){
		double[][] result = new double[samples.size()][samples.size()];
		for(int i=0; i<samples.size(); i++){
			for(int j=i; j<samples.size(); j++){
				if(samples.get(i).getGroundTruthClusterId() == samples.get(j).getGroundTruthClusterId()){
					result[i][j]=1;
					result[j][i]=1;
				}
				else{
					result[i][j]=0;
				}
			}
		}
		return result;
	}
	
	public static double[][] getIncidenceMatrixForCalculatedLabel(ArrayList<Sample> samples){
		double[][] result = new double[samples.size()][samples.size()];
		for(int i=0; i<samples.size(); i++){
			for(int j=i; j<samples.size(); j++){
				if(samples.get(i).getCalculatedClusterId() == samples.get(j).getCalculatedClusterId()){
					result[i][j]=1;
					result[j][i]=1;
				}
				else{
					result[i][j]=0;
				}
			}
		}
		return result;
	}
}
