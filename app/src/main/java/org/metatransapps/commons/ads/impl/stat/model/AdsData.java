package org.metatransapps.commons.ads.impl.stat.model;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class AdsData implements Serializable {
	
	
	private static final long serialVersionUID = -5931713058377001365L;
	
	
	private Map<Integer, AdData> adsData;
	int defaultAccLoadTime; //in ms
	
	
	public AdsData(int _defaultAccLoadTime) {
		adsData = new HashMap<Integer, AdData>();
		defaultAccLoadTime = _defaultAccLoadTime;
	}
	
	
	public synchronized AdData getAdData(int providerID) {
		AdData data = adsData.get(providerID);
		if (data == null) {
			data = new AdData(defaultAccLoadTime);
			adsData.put(providerID, data);
		}
		return data;
	}
	
	
	@Override
	public String toString() {
		String result = "\r\n";
		
		for (Integer key: adsData.keySet()) {
			AdData data = adsData.get(key);
			result += "ProviderID=" + key + " -> " + data.toString() + "\r\n";
		}
		
		return result;
	}
}
