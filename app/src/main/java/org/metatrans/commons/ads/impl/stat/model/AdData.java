package org.metatrans.commons.ads.impl.stat.model;


import java.io.Serializable;


public class AdData implements Serializable {
	
	
	private static final long serialVersionUID = -9128630685104730167L;


	long created 		= System.currentTimeMillis();
	long requests 		= 100;
	long impressions 	= 10;
	long clicks 		= 1;

	long time_accumulative_load; //in ms
	
	
	public AdData(int defaultAccLoadTime) {

		time_accumulative_load = impressions * defaultAccLoadTime;
	}


	public long getImpressions() {

		return impressions;
	}


	public long getCreatedTimeInMillis() {

		//Will be 0 after the deserialization of an object from the old class version.
		if (created == 0) {

			created = System.currentTimeMillis();
		}

		return created;
	}


	public double getFillRate() {
		return impressions / (double) requests;
	}
	
	
	public double getCTR() {
		return clicks / (double) impressions;
	}
	
	
	public long getTime_AvgLoad() {
		return time_accumulative_load / impressions;
	}
	
	
	public double getRating() {
		
		double result = AdDataUtils.RATING_MULTIPLIER;
		
		result *= (1 / (double) getFillRate()) * AdDataUtils.WEIGHT_FILLRATE;
		
		result *= getCTR() * AdDataUtils.WEIGHT_CTR;
		
		//result *= (1 / (double) getTime_AvgLoad()) * AdDataUtils.WEIGHT_LOADTIME;
		
		return result;
	}


	public long getClicksCount() {
		return clicks;
	}

	
	@Override
	public String toString() {
		String result = "";
		
		result += "R=" + requests + ", I=" + impressions + ", C=" + clicks
				+ ", T=" + time_accumulative_load
				+ " > FR=" + getFillRate() + ", CTR=" + getCTR() + ", AVG=" + getTime_AvgLoad()
				+ "	>	RATING=" + getRating();
		
		return result;
	}
}
