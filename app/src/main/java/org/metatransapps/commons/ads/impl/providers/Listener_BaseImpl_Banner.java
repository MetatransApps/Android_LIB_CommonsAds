package org.metatransapps.commons.ads.impl.providers;


import org.metatransapps.commons.ads.impl.flow.AdLoadFlow_Banner;


public class Listener_BaseImpl_Banner {


	private AdLoadFlow_Banner flow;
	
	
	public Listener_BaseImpl_Banner(AdLoadFlow_Banner _flow) {
		flow = _flow;
	}
	
	
	public AdLoadFlow_Banner getFlow() {
		return flow;
	}
}
