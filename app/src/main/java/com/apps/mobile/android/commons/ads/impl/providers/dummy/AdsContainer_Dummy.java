package com.apps.mobile.android.commons.ads.impl.providers.dummy;


import android.content.Context;

import com.apps.mobile.android.commons.ads.impl.IAdsContainer;
import com.apps.mobile.android.commons.ads.api.IAdsProviders;
import com.apps.mobile.android.commons.ads.impl.flow.AdLoadFlow_Banner;
import com.apps.mobile.android.commons.ads.impl.flow.AdLoadFlow_Interstitial;


public class AdsContainer_Dummy implements IAdsContainer {
	
	
	@Override
	public int getProviderID() {
		return IAdsProviders.ID_DUMMY;
	}
	
	
	@Override
	public void onCreate_Container(Context _app_context) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void removeInterstitial(String adID, AdLoadFlow_Interstitial flow) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void attach(AdLoadFlow_Banner flow) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void detach(AdLoadFlow_Banner flow) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void requestInterstitial(AdLoadFlow_Interstitial flow) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void initInterstitial(AdLoadFlow_Interstitial flow) {
		// TODO Auto-generated method stub
		
	}
}
