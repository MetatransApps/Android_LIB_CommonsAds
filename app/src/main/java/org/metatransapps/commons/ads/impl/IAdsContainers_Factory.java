package org.metatransapps.commons.ads.impl;


import org.metatransapps.commons.ads.api.IAdsConfiguration;

import android.content.Context;


public interface IAdsContainers_Factory {
	
	public IAdsContainer createAdsContainer(int providerID, Context context, IAdsConfiguration config);
}
