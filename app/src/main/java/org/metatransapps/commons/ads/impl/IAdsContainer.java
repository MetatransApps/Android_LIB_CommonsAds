package org.metatransapps.commons.ads.impl;


import org.metatransapps.commons.ads.impl.flow.AdLoadFlow_Banner;
import org.metatransapps.commons.ads.impl.flow.AdLoadFlow_Interstitial;

import android.content.Context;


public interface IAdsContainer {
	
	public int getProviderID();
	public void onCreate_Container(Context _app_context);
	
	public void attach(AdLoadFlow_Banner flow);
	public void detach(AdLoadFlow_Banner flow);
	
	public void initInterstitial(AdLoadFlow_Interstitial flow);
	public void requestInterstitial(AdLoadFlow_Interstitial flow);
	public void removeInterstitial(String adID, AdLoadFlow_Interstitial flow);
}
