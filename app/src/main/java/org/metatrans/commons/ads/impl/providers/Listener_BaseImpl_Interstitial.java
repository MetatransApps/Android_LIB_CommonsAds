package org.metatrans.commons.ads.impl.providers;


import org.metatrans.commons.ads.impl.flow.AdLoadFlow_Interstitial;


public class Listener_BaseImpl_Interstitial {


	private AdLoadFlow_Interstitial flow;
	private Object interstitial;
	
	
	public Listener_BaseImpl_Interstitial(AdLoadFlow_Interstitial _flow, Object _interstitial) {
		flow = _flow;
		interstitial = _interstitial;
	}
	
	
	public AdLoadFlow_Interstitial getFlow() {
		return flow;
	}


	public Object getInterstitial() {
		return interstitial;
	}
}
