package org.metatrans.commons.analytics;


import java.util.ArrayList;
import java.util.List;

import org.metatrans.commons.IActivityInterstitial;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.events.api.IEvent_Base;

import android.app.Activity;


public class Analytics_ActivitiesStack implements IAnalytics {
	
	
	private List<Activity> stack;
	
	
	public Analytics_ActivitiesStack() {

		stack = new ArrayList<Activity>();
	}


	@Override
	public void onActivity_Create(Activity activity) {
		System.out.println("Analytics_ActivitiesStack: onActivity_Create: " + activity);
		stack.add(activity);
	}


	@Override
	public void onActivity_Destroy(Activity activity) {

		boolean last_found = stack.remove(activity);

		if (!last_found) {

			throw new IllegalStateException();
		}

		System.out.println("Analytics_ActivitiesStack: onActivity_Destroy: " + activity);
	}


	/*@Override
	public void onActivity_Start(Activity activity) {

		System.out.println("Analytics_ActivitiesStack: onActivity_Start: " + activity);
		stack.add(activity);
	}


	@Override
	public void onActivity_Stop(Activity activity) {

		boolean last_found = stack.remove(activity);

		if (!last_found) {

			throw new IllegalStateException();
		}

		System.out.println("Analytics_ActivitiesStack: onActivity_Stop: " + activity);
	}*/


	@Override
	public Activity getCurrentActivity() {

		//System.out.println("Current activity: " + current);
		if (stack.size() == 0) {

			return null;
		}

		return stack.get(stack.size() - 1);
	}


	public Activity getInterstitialActivity() {

		for (Activity current: stack) {

			if (current instanceof IActivityInterstitial) {

				return current;
			}
		}

		return null;
	}

	
	@Override
	public void init(Application_Base app_context) {
		//Do nothing
	}


	@Override
	public void sendEvent(IEvent_Base event) {
		//Do nothing
	}
}
