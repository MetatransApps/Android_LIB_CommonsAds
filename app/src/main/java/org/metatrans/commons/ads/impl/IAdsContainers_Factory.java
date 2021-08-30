package org.metatrans.commons.ads.impl;


import org.metatrans.commons.ads.api.IAdsConfiguration;

import android.content.Context;


public interface IAdsContainers_Factory {
	
	public IAdsContainer createAdsContainer(int providerID, Context context, IAdsConfiguration config);
}
