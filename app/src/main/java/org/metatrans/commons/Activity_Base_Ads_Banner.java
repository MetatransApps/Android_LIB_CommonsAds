package org.metatrans.commons;


import static org.metatrans.commons.ads.BuildConfig.DEBUG;

import org.metatrans.commons.ads.impl.flow.IAdLoadFlow;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.app.Application_Base_Ads;
import org.metatrans.commons.ui.Toast_Base;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public abstract class Activity_Base_Ads_Banner extends org.metatrans.commons.Activity_Base implements IActivityInterstitial, IActivityRewardedVideo {


	//1 minute if productive mode and smaller for testing
	private static final int INTERSTITIAL_INTERVAL 			= (DEBUG ? (5 * 1000) : (60 * 1000));

	private static final int REWARD_INTERVAL 				= 7 * INTERSTITIAL_INTERVAL;

	private static final int WAITING_TIME_FOR_REWARDED_AD 	= (int) (3.5 * 1000); //3.5 seconds

	private static volatile long timestamp_last_interstitial_ad_opening = System.currentTimeMillis() + INTERSTITIAL_INTERVAL;

	public static volatile long timestamp_last_rewarded_ad_opening;

	//Used for waiting some time to load the Rewarded Video, because if it is clicked too fast, it doesn't opened.
	private static volatile long timestamp_last_resumed;


	private IAdLoadFlow current_adLoadFlow_Banner;

	private IAdLoadFlow current_adLoadFlow_Interstitial;

	private IAdLoadFlow current_adLoadFlow_RewardedVideo;

	private boolean isBannerAttached;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		//Must be initialized statically, because otherwise if initialized on each create, it doesn't show ads in many cases.
		//timestamp_last_interstitial_ad_opening = System.currentTimeMillis();
	}


	@Override
	protected void onResume() {


		System.out.println("Activity_Base_Ads_Banner: onResume()");


		super.onResume();


		//try-catch ensures no errors, because of ads logic.
		try {

			if (showAds()) {

				attachBanner();
			}

			preloadInterstitial();

			preloadRewardedVideo();

		} catch (Throwable t) {

			t.printStackTrace();
		}

		timestamp_last_resumed = System.currentTimeMillis();
	}

	private boolean showAds() {

		return System.currentTimeMillis() >= timestamp_last_rewarded_ad_opening + REWARD_INTERVAL;
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

	protected String getRewardedVideoName() {
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

				//For example: Menu Activity, which opens Reworded Video, have null frame for banner as the activity doesn't have banner at all.
				//throw new IllegalStateException("Frame is null");

				return;
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

				if (!current_adLoadFlow_Banner.isActive()) {

					System.out.println("Activity_Base_Ads_Banner: attachBanner(): resume add");

					current_adLoadFlow_Banner.resume();

				} else {

					System.out.println("Activity_Base_Ads_Banner: attachBanner(): skipping, because Banner is ALREADY active");
				}

				isBannerAttached = true;
			}
		}
	}


	protected void detachBanner() {

		if (current_adLoadFlow_Banner != null) {

			if (current_adLoadFlow_Banner.isActive()) {

				current_adLoadFlow_Banner.pause();
			}

			current_adLoadFlow_Banner = null;
		}

		isBannerAttached = false;
	}


	private void preloadInterstitial() {

		System.out.println("Activity_Base_Ads_Banner.preloadInterstitial(): getInterstitialName()=" + getInterstitialName());

		if (getInterstitialName() != null) {

			System.out.println("Activity_Base_Ads_Banner.preloadInterstitial(): called");

			current_adLoadFlow_Interstitial = ((Application_Base_Ads)getApplication()).getAdsManager().getCachedFlow(getInterstitialName());


			if (current_adLoadFlow_Interstitial == null) {

				System.out.println("Activity_Base_Ads_Banner.preloadInterstitial(): create Interstitial");

				current_adLoadFlow_Interstitial = ((Application_Base_Ads)getApplication()).getAdsManager().createFlow_Interstitial_Mixed(getInterstitialName());
				((Application_Base_Ads)getApplication()).getAdsManager().putCachedFlow(getInterstitialName(), current_adLoadFlow_Interstitial);

			} else {

				System.out.println("Activity_Base_Ads_Banner.preloadInterstitial(): Interstitial EXISTS");

				if (current_adLoadFlow_Interstitial.isActive()) {

					current_adLoadFlow_Interstitial.pause();
				}
			}
		}
	}


	@Override
	public boolean openInterstitial() {

		System.out.println("Activity_Base_Ads_Banner.openInterstitial(): called");

		if (!showAds()) {

			System.out.println("Activity_Base_Ads_Banner.openInterstitial(): !showAds()");

			return false;
		}

		try {

			if (Application_Base.getInstance().getApp_Me().isPaid()) {

				System.out.println("Activity_Base_Ads_Banner.openInterstitial(): the app is paid - skipping");

				return false;
			}

			System.out.println("Activity_Base_Ads_Banner.openInterstitial(): timestamp_last_interstitial_ad_opening="
					+ timestamp_last_interstitial_ad_opening);
			System.out.println("Activity_Base_Ads_Banner.openInterstitial(): readable_time="
					+ TimeUtils.getReadableDateTime(timestamp_last_interstitial_ad_opening));
			System.out.println("Activity_Base_Ads_Banner.openInterstitial(): INTERSTITIAL_INTERVAL="
					+ INTERSTITIAL_INTERVAL);

			boolean success = false;

			long now = System.currentTimeMillis();

			if (now >= timestamp_last_interstitial_ad_opening + INTERSTITIAL_INTERVAL) {

				if (current_adLoadFlow_Interstitial != null) {

					if (!current_adLoadFlow_Interstitial.isActive()) {

						System.out.println("Activity_Base_Ads_Banner.openInterstitial(): RESUMED");

						current_adLoadFlow_Interstitial.resume();

					} else {

						System.out.println("Activity_Base_Ads_Banner: openInterstitial(): skipping, because Interstitial is ALREADY active");
					}

					success = true;
				}

				timestamp_last_interstitial_ad_opening = now;

			} else {

				System.out.println("Activity_Base_Ads_Banner.openInterstitial(): SKIPPED (to not show too often)");
			}

			return success;


		} catch (Throwable t) {

			t.printStackTrace();

			return false;
		}
	}


	private void preloadRewardedVideo() {

		System.out.println("Activity_Base_Ads_Banner.preloadRewardedVideo(): getRewardedVideoName()=" + getRewardedVideoName());

		if (getRewardedVideoName() != null) {

			System.out.println("Activity_Base_Ads_Banner.preloadRewardedVideo(): called");

			current_adLoadFlow_RewardedVideo = ((Application_Base_Ads)getApplication()).getAdsManager().getCachedFlow(getRewardedVideoName());


			if (current_adLoadFlow_RewardedVideo == null) {

				System.out.println("Activity_Base_Ads_Banner.preloadRewardedVideo(): create RewardedVideo");

				current_adLoadFlow_RewardedVideo = ((Application_Base_Ads)getApplication()).getAdsManager().createFlow_RewardedVideo_Mixed(getRewardedVideoName());
				((Application_Base_Ads)getApplication()).getAdsManager().putCachedFlow(getRewardedVideoName(), current_adLoadFlow_RewardedVideo);

			} else {

				System.out.println("Activity_Base_Ads_Banner.preloadRewardedVideo(): RewardedVideo EXISTS");

				if (current_adLoadFlow_RewardedVideo.isActive()) {

					current_adLoadFlow_RewardedVideo.pause();

				} else {

					System.out.println("Activity_Base_Ads_Banner.preloadRewardedVideo(): current_adLoadFlow_RewardedVideo is null");
				}
			}
		}
	}


	@Override
	public boolean openRewardedVideo() {

		System.out.println("Activity_Base_Ads_Banner.openRewardedVideo(): called");

		//If the user wants to explicitly reset the time for No ads, it must be possible.
		/*if (!showAds()) {

			System.out.println("Activity_Base_Ads_Banner.openRewardedVideo(): !showAds()");

			return false;
		}*/

		try {

			//It is currently called only when the user clicks button intentionally.
			/*if (Application_Base.getInstance().getApp_Me().isPaid()) {

				System.out.println("Activity_Base_Ads_Banner.openRewardedVideo(): the app is paid - skipping");

				return false;
			}*/

			long waiting_time = System.currentTimeMillis() - (timestamp_last_resumed + WAITING_TIME_FOR_REWARDED_AD);
			if (waiting_time < 0) {

				Toast_Base.showToast_InCenter_Short(this, (-waiting_time) + " ms");

				return false;
			}

			if (current_adLoadFlow_RewardedVideo != null) {

				if (!current_adLoadFlow_RewardedVideo.isActive()) {

					System.out.println("Activity_Base_Ads_Banner.openRewardedVideo(): RESUMED");

					current_adLoadFlow_RewardedVideo.resume();

				} else {

					System.out.println("Activity_Base_Ads_Banner: openRewardedVideo(): skipping, because RewardedVideo is ALREADY active");
				}

				return true;

			} else {

				System.out.println("Activity_Base_Ads_Banner.openRewardedVideo(): current_adLoadFlow_RewardedVideo is null");
			}

		} catch (Throwable t) {

			t.printStackTrace();
		}

		return false;
	}
}
