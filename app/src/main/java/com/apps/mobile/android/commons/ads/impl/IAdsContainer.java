package com.apps.mobile.android.commons.ads.impl;


import android.content.Context;

import com.apps.mobile.android.commons.ads.impl.flow.AdLoadFlow_Banner;
import com.apps.mobile.android.commons.ads.impl.flow.AdLoadFlow_Interstitial;


public interface IAdsContainer {
	
	public int getProviderID();
	public void onCreate_Container(Context _app_context);
	
	public void attach(AdLoadFlow_Banner flow);
	public void detach(AdLoadFlow_Banner flow);
	
	public void initInterstitial(AdLoadFlow_Interstitial flow);
	public void requestInterstitial(AdLoadFlow_Interstitial flow);
	public void removeInterstitial(String adID, AdLoadFlow_Interstitial flow);
}
