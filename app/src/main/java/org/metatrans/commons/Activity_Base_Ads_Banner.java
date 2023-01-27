package org.metatrans.commons;


import org.metatrans.commons.ads.impl.flow.IAdLoadFlow;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.app.Application_Base_Ads;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public abstract class Activity_Base_Ads_Banner extends org.metatrans.commons.Activity_Base implements IActivityInterstitial {


	private static volatile long timestamp_last_interstitial_ad_openning;


	private IAdLoadFlow current_adLoadFlow_Banner;

	private IAdLoadFlow current_adLoadFlow_Interstitial;

	private boolean isBannerAttached;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		//Skip ads first 1 minute
		timestamp_last_interstitial_ad_openning = System.currentTimeMillis();
	}


	@Override
	protected void onResume() {


		System.out.println("Activity_Base_Ads_Banner: onResume()");


		super.onResume();


		//try-catch ensures no errors, because of ads logic.
		try {

			attachBanner();

			preloadInterstitial();

		} catch(Throwable t) {

			t.printStackTrace();
		}
	}


	@Override
	protected void onPause() {


		System.out.println("Activity_Base_Ads_Banner: onPause()");


		//try-catch ensures no errors, because of ads logic.
		try {

			detachBanner();

		} catch(Throwable t) {

			t.printStackTrace();
		}


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


	protected void attachBanner() {
			
		ViewGroup frame = getFrame();

		if (Application_Base.getInstance().isTestMode()) {

			if (frame == null) {

				throw new IllegalStateException("Frame is null");
			}
		}

		if (frame != null) {

			//System.out.println("Activity_Base_Ads_Banner: attachBanner() frame.isShown = " + frame.isShown());

			if (current_adLoadFlow_Banner != null) {
				throw new IllegalStateException("current_adLoadFlow is NOT null");
			}

			if (getBannerName() != null /*&& DeviceUtils.isConnectedOrConnecting(this)*/) {
				current_adLoadFlow_Banner = ((Application_Base_Ads)getApplication()).getAdsManager().createFlow_Banner_Mixed(frame, getBannerName(), getGravity());
			}

			if (current_adLoadFlow_Banner != null) {

				System.out.println("Activity_Base_Ads_Banner: attachBanner() - resume add");

				current_adLoadFlow_Banner.resume();

				isBannerAttached = true;
			}
		}
	}


	protected void detachBanner() {

		if (current_adLoadFlow_Banner != null) {

			current_adLoadFlow_Banner.pause();

			current_adLoadFlow_Banner = null;
		}

		isBannerAttached = false;
	}


	private void preloadInterstitial() {


		if (getInterstitialName() != null) {


			current_adLoadFlow_Interstitial = ((Application_Base_Ads)getApplication()).getAdsManager().getCachedFlow(getInterstitialName());


			if (current_adLoadFlow_Interstitial == null) {

				System.out.println("Activity_Question create Interstitial");

				current_adLoadFlow_Interstitial = ((Application_Base_Ads)getApplication()).getAdsManager().createFlow_Interstitial_Mixed(getInterstitialName());
				((Application_Base_Ads)getApplication()).getAdsManager().putCachedFlow(getInterstitialName(), current_adLoadFlow_Interstitial);

			} else {

				System.out.println("Activity_Question Interstitial EXISTS");

				if (current_adLoadFlow_Interstitial.isActive()) {

					//current_adLoadFlow_Interstitial.cleanCurrent();
					current_adLoadFlow_Interstitial.pause();
				}
			}
		}
	}


	@Override
	public boolean openInterstitial() {

		System.out.println("Activity_Base_Ads_Banner.openInterstitial(): called");

		boolean success = false;

		try {

			if (Application_Base.getInstance().getApp_Me().isPaid()) {

				return success;
			}

			long now = System.currentTimeMillis();

			if (now >= timestamp_last_interstitial_ad_openning + 60 * 1000) {

				if (current_adLoadFlow_Interstitial != null) {

					current_adLoadFlow_Interstitial.resume();

					System.out.println("Activity_Base_Ads_Banner.openInterstitial(): RESUMED");

					success = true;
				}

				timestamp_last_interstitial_ad_openning = now;

			} else {

				System.out.println("Activity_Base_Ads_Banner.openInterstitial(): SKIPPED (to not show too often)");
			}

		} catch(Throwable t) {

			t.printStackTrace();
		}

		return success;
	}
}
