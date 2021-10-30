package org.metatrans.commons.app;


import android.app.Activity;

import org.metatrans.commons.IActivityInterstitial;
import org.metatrans.commons.ads.api.IAdsConfigurations;
import org.metatrans.commons.ads.impl.AdsManager;
import org.metatrans.commons.analytics.Analytics_ActivitiesStack;
import org.metatrans.commons.analytics.IAnalytics;
import org.metatrans.commons.model.GameData_Base;


public abstract class Application_Base_Ads extends Application_Base {

	
	private AdsManager adsmanager;

	private IAnalytics acitvities_stack = new Analytics_ActivitiesStack();


	@Override
	public void onCreate() {
		
		super.onCreate();
		//Called when the application is starting, before any other application objects have been created.
		
		System.out.println("Application_EC: onCreate called " + System.currentTimeMillis());
	}


	public abstract IAdsConfigurations getAdsConfigurations();


	public static Application_Base_Ads getInstance() {

		return (Application_Base_Ads) Application_Base.getInstance();
	}


	public void openInterstitial() {

		Object activity = getInterstitialActivity();

		if (activity instanceof IActivityInterstitial) {

			((IActivityInterstitial) activity).openInterstitial();

		} else {

			if (isTestMode()) {

				throw new IllegalStateException("Not in IActivityInterstitial");
			}
		}
	}


	protected Activity getInterstitialActivity() {

		return ((Analytics_ActivitiesStack) getAnalytics()).getInterstitialActivity();

		/* TODO: Use standard Android approach
		ActivityManager m = (ActivityManager) getSystemService( ACTIVITY_SERVICE );

		List<ActivityManager.RunningTaskInfo> runningTaskInfoList =  m.getRunningTasks(10);

		Iterator<ActivityManager.RunningTaskInfo> iterator = runningTaskInfoList.iterator();

		while (iterator.hasNext()) {

			ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) iterator.next();

			int id = runningTaskInfo.id;
			CharSequence desc= runningTaskInfo.description;
			int numOfActivities = runningTaskInfo.numActivities;
			String topActivity = runningTaskInfo.topActivity.getShortClassName();
		}*/
	}


	@Override
	public IAnalytics getAnalytics() {
		return acitvities_stack;
	}


	@Override
	public GameData_Base createGameDataObject() {
		throw new UnsupportedOperationException();
	}
	
	
	public AdsManager getAdsManager() {

		if (adsmanager == null) {

			adsmanager = AdsManager.getSingleton(this);
		}

		return adsmanager;
	}
}
