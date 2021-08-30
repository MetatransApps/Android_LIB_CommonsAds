package org.metatrans.commons.ads.impl.stat.model;


import java.io.Serializable;


public class AdData implements Serializable {
	
	
	private static final long serialVersionUID = -9128630685104730167L;
	
	
	long requests;
	long impressions;
	long clicks;
	long time_accumulative_load; //in ms
	
	
	public AdData(int defaultAccLoadTime) {
		requests = 10;
		impressions = 10;
		clicks = 1;
		time_accumulative_load = impressions * defaultAccLoadTime;
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
