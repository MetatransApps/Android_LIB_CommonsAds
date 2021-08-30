package org.metatrans.commons.ads.impl.stat.model;


public class AdDataUtils {
	
	
	public static final int RATING_MULTIPLIER 		= 10000;
	
	public static final double WEIGHT_LOADTIME		= 0.0;
	public static final double WEIGHT_FILLRATE		= 0.5;
	public static final double WEIGHT_CTR			= 0.5;
	
	
	public static void addSuccess(AdData data, long load_time) {
		
		data.requests++;
		data.impressions++;
		data.time_accumulative_load += load_time;
		
		//System.out.println("AdData: added success " + load_time);
	}
	
	
	public static void addFail(AdData data) {
		data.requests++;
		
		//System.out.println("AdData: added failure");
	}
	
	
	public static void addClick(AdData data) {
		data.clicks++;
		
		//System.out.println("AdData: added click");
	}
}
