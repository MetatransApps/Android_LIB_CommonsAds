package com.apps.mobile.android.commons.ads.impl;


import android.content.Context;

import com.apps.mobile.android.commons.ads.api.IAdsConfiguration;


public interface IAdsContainers_Factory {
	
	public IAdsContainer createAdsContainer(int providerID, Context context, IAdsConfiguration config);
}
