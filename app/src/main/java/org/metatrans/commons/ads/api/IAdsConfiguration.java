package org.metatrans.commons.ads.api;


public interface IAdsConfiguration {
	
	
	//Internal IDs
	public static final String AD_ID_BANNER1 					= "BANNER1";
	public static final String AD_ID_BANNER2 					= "BANNER2";
	public static final String AD_ID_BANNER3 					= "BANNER3";
	public static final String AD_ID_INTERSTITIAL1 				= "INTERSTITIAL1";

	public static final String AD_ID_REWARDED_VIDEO1 			= "REWARDED_VIDEO1";

	public String getContainerClass();
	
	public String[] getUnitIDs_Banners();
	
	public String[] getUnitIDs_Interstitial();

	public String[] getUnitIDs_RewardedVideo();

	public String getUnitID(String adID);

	//TODO: GAME ID
	public String getApplicationID();
}
