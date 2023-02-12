package org.metatrans.commons.app;


import android.app.Activity;

import org.metatrans.commons.IActivityInterstitial;
import org.metatrans.commons.ads.api.IAdsConfigurations;
import org.metatrans.commons.ads.impl.AdsConfigurations_DynamicImpl;
import org.metatrans.commons.ads.impl.AdsManager;
import org.metatrans.commons.model.GameData_Base;

import java.util.List;


public abstract class Application_Base_Ads extends Application_Base {

	
	private AdsManager adsmanager;

	private IAdsConfigurations adsConfigurations;


	@Override
	public void onCreate() {

		super.onCreate();
		//Called when the application is starting, before any other application objects have been created.
		
		System.out.println("Application_Base_Ads: onCreate called " + System.currentTimeMillis());

		adsConfigurations = new AdsConfigurations_DynamicImpl();
	}


	public IAdsConfigurations getAdsConfigurations() {

		return adsConfigurations;
	}


	public static Application_Base_Ads getInstance() {

		return (Application_Base_Ads) Application_Base.getInstance();
	}


	public boolean openInterstitial() {

		Object activity = getInterstitialActivity();

		if (activity instanceof IActivityInterstitial) {

			return ((IActivityInterstitial) activity).openInterstitial();

		} else {

			if (isTestMode()) {

				throw new IllegalStateException("Not in IActivityInterstitial");
			}
		}

		return false;
	}


	protected Activity getInterstitialActivity() {

		List<Activity> stack = getActivitiesStack().getActivitiesStack();

		for (Activity current: stack) {

			if (current instanceof IActivityInterstitial) {

				return current;
			}
		}

		return null;

		//TODO: Use standard Android approach (if there is any without additional permissions)
		/*ActivityManager m = (ActivityManager) getSystemService( ACTIVITY_SERVICE );

		List<ActivityManager.RunningTaskInfo> runningTaskInfoList =  m.getRAppTasks();

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
