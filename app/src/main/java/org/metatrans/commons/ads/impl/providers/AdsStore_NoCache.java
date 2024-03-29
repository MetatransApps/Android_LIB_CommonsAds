package org.metatrans.commons.ads.impl.providers;


import java.util.HashMap;
import java.util.Map;

import org.metatrans.commons.DeviceUtils;
import org.metatrans.commons.ads.impl.flow.AdLoadFlow_Banner;
import org.metatrans.commons.ads.impl.flow.AdLoadFlow_Interstitial;
import org.metatrans.commons.ads.impl.flow.AdLoadFlow_RewardedVideo;
import org.metatrans.commons.ads.utils.BannerUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class AdsStore_NoCache {
	
	
	private AdsContainer_Base adsContainer;
	private Map<String, Object> ads;
	
	
	AdsStore_NoCache(AdsContainer_Base _adsContainer) {
		
		adsContainer = _adsContainer;
		
		ads = new HashMap<String, Object>();
	}
	
	
	LinearLayout getBanner(AdLoadFlow_Banner flow) {
		View adview = adsContainer.createBanner(flow);
		return getBanner_internal(flow.getAdID(), adview, flow.getGravity());
	}
	
	
	private LinearLayout getBanner_internal(String adID, View adview, int gravity) {
		
		if (ads.containsKey(adID)) {
			//When there is new MainActivity started with new Intent, we have to override the entry.
			returnBanner(adID);
			//throw new IllegalStateException();
		}

		ads.put(adID, adview);

		System.out.println("AdsStore_NoCache: getBanner " + adID + " = " + adview);
		
		return (LinearLayout) adview;
	}
	
	
	void returnBanner(String adID) {
		
		Object bannerAd = ads.remove(adID);
		
		if (bannerAd == null) {
			//TODO - Enable exception throwing in debug mode
			//In Some cases remove is called 2 times in onPause of activity and in Listener.onFaild
			//This throwing of exception should be disabled in productive mode
			//throw new IllegalStateException("Not found in returnBanner with adID=" + adID);
			return;
		}
		
		View bannerContainer = (View)bannerAd;
		if (bannerContainer.getParent() != null) {
			((ViewGroup)bannerContainer.getParent()).removeView(bannerContainer);
			System.out.println("AdsStore_NoCache: remove view from parent " + bannerContainer);
		}
		
		View banner = bannerContainer.findViewById(BannerUtils.AD_BANNER_VIEW_ID);
		
		adsContainer.destroyBanner(banner);
		
		System.out.println("AdsStore_NoCache: returnBanner " + adID + " = " + banner);
		//(new Exception()).printStackTrace();
	}
	
	
	Object getInterstitial(AdLoadFlow_Interstitial flow) {
		
		/*if (ads.containsKey(flow.getAdID())) {
			throw new IllegalStateException("adID=" + flow.getAdID() + " already exists.");
		}*/
		
		//Created at first call only and than cached
		Object interstitialAd = ads.get(flow.getAdID());
		if (interstitialAd == null) {
			interstitialAd = adsContainer.createInterstitial(flow);
			ads.put(flow.getAdID(), interstitialAd);
		}
		
		System.out.println("AdsStore_NoCache: getInterstitial " + flow.getAdID() + " = " + interstitialAd);
		
		return interstitialAd;
	}
	
	
	void returnInterstitial(String adID, AdLoadFlow_Interstitial flow) {
		
		System.out.println("AdsStore_NoCache: returnInterstitial " + adID);
		
		Object interstitialAd = ads.remove(adID);

		if (interstitialAd != null) {

			System.out.println("AdsStore_NoCache: returned OBJ = " + interstitialAd);

			adsContainer.destroyInterstitial(interstitialAd);
		}

		//Re-create the ad in order to load it upfront and now wait when it is necessary to show.
		//!!! This should be already done in the Activity_Base_Ads_Banner.onResume(...)
		/*interstitialAd = adsContainer.createInterstitial(flow);

		System.out.println("AdsStore_NoCache.returnInterstitial(...): Pre-load Interstitial, interstitialAd=" + interstitialAd);

		ads.put(flow.getAdID(), interstitialAd);
		*/
	}

	Object getRewardedVideo(AdLoadFlow_RewardedVideo flow) {

		/*if (ads.containsKey(flow.getAdID())) {
			throw new IllegalStateException("adID=" + flow.getAdID() + " already exists.");
		}*/

		//Created at first call only and than cached
		Object rewardedAd = ads.get(flow.getAdID());
		if (rewardedAd == null) {
			rewardedAd = adsContainer.createRewardedVideo(flow);
			ads.put(flow.getAdID(), rewardedAd);
		}

		System.out.println("AdsStore_NoCache: getRewardedVideo " + flow.getAdID() + " = " + rewardedAd);

		return rewardedAd;
	}


	void returnRewardedVideo(String adID, AdLoadFlow_RewardedVideo flow) {

		System.out.println("AdsStore_NoCache: returnRewardedVideo " + adID);

		Object rewardedAd = ads.remove(adID);

		if (rewardedAd != null) {

			System.out.println("AdsStore_NoCache: returned OBJ = " + rewardedAd);

			adsContainer.destroyRewardedVideo(rewardedAd);
		}

		//Pre load only if connected
		if (DeviceUtils.isConnected()) {

			rewardedAd = adsContainer.createRewardedVideo(flow);

			System.out.println("AdsStore_NoCache.returnRewardedVideo(...): Pre-load RewardedVideo, rewardedAd=" + rewardedAd);

			ads.put(flow.getAdID(), rewardedAd);
		}
	}
}
