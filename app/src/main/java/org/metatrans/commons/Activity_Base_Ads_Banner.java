package org.metatrans.commons;


import org.metatrans.commons.ads.impl.flow.IAdLoadFlow;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.app.Application_Base_Ads;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public abstract class Activity_Base_Ads_Banner extends org.metatrans.commons.Activity_Base implements IActivityInterstitial {
	
	
	private IAdLoadFlow current_adLoadFlow;

	private boolean isBannerAttached;

	private static long timestamp_last_ad_openning;


	private IAdLoadFlow current_adLoadFlow_Interstitial;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		//Skip ads first 1 minute
		//TODO timestamp_last_ad_openning = System.nanoTime();
	}


	@Override
	protected void onResume() {

		System.out.println("Activity_Base_Ads_Banner: onResume()");

		super.onResume();

		attachBanner();

		if (getInterstitialName() != null) {

			current_adLoadFlow_Interstitial = ((Application_Base_Ads)getApplication()).getAdsManager().getCachedFlow(getInterstitialName());

			if (current_adLoadFlow_Interstitial == null) {

				System.out.println("Activity_Question create Interstitial");

				current_adLoadFlow_Interstitial = ((Application_Base_Ads)getApplication()).getAdsManager().createFlow_Interstitial_Mixed(getInterstitialName());
				((Application_Base_Ads)getApplication()).getAdsManager().putCachedFlow(getInterstitialName(), current_adLoadFlow_Interstitial);
			} else {

				System.out.println("Activity_Question Interstitial EXISTS");

				//current_adLoadFlow_Interstitial.cleanCurrent();
				current_adLoadFlow_Interstitial.pause();
			}
		}
	}


	@Override
	protected void onPause() {

		System.out.println("Activity_Base_Ads_Banner: onPause()");

		detachBanner();

		super.onPause();
	}


	protected abstract String getBannerName();

	protected String getInterstitialName() {
		return null;
	}


	protected abstract FrameLayout getFrame();
	
	
	protected int getGravity() {
		return Gravity.TOP | Gravity.CENTER_HORIZONTAL;
	}
	
	
	protected boolean isBannerAttached() {
		return isBannerAttached;
	}
	
	
	protected void detachBanner() {
		
		try {
	        
			if (current_adLoadFlow != null) {
				current_adLoadFlow.pause();
				current_adLoadFlow = null;
			}
			
			isBannerAttached = false;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	protected void attachBanner() {
		
		try {
			
	        ViewGroup frame = getFrame();
	        
	        if (Application_Base.getInstance().isTestMode()) {

	            if (frame == null) {

	        		throw new IllegalStateException("Frame is null");
	        	}
	        }
	        
	        if (frame != null) {
	        	
				//System.out.println("Activity_Base_Ads_Banner: attachBanner() frame.isShown = " + frame.isShown());
				
				if (current_adLoadFlow != null) {
					throw new IllegalStateException("current_adLoadFlow is NOT null");
				}
				
				if (getBannerName() != null /*&& DeviceUtils.isConnectedOrConnecting(this)*/) {
					current_adLoadFlow = ((Application_Base_Ads)getApplication()).getAdsManager().createFlow_Banner_Mixed(frame, getBannerName(), getGravity());
				}				
				
				if (current_adLoadFlow != null) {
					
					System.out.println("Activity_Base_Ads_Banner: attachBanner() - resume add");
					
					current_adLoadFlow.resume();
					
					isBannerAttached = true;
				}
	        }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void openInterstitial() {

		try {

			System.out.println("Activity_Question openInterstitial called");

			long now = System.currentTimeMillis();

			if (now >= timestamp_last_ad_openning + 60 * 1000) {

				if (current_adLoadFlow_Interstitial != null) {

					current_adLoadFlow_Interstitial.resume();

					System.out.println("Activity_Question openInterstitial RESUMED");
				}

				timestamp_last_ad_openning = now;

			} else {
				System.out.println("Activity_Question openInterstitial SKIPPED");
			}

		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
}
