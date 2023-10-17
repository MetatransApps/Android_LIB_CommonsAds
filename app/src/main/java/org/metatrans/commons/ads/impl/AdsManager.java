package org.metatrans.commons.ads.impl;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.metatrans.commons.ads.api.IAdsConfiguration;
import org.metatrans.commons.ads.api.IAdsConfigurations;
import org.metatrans.commons.ads.api.IAdsProviders;
import org.metatrans.commons.ads.impl.flow.AdLoadFlow_Banner;
import org.metatrans.commons.ads.impl.flow.AdLoadFlow_Interstitial;
import org.metatrans.commons.ads.impl.flow.AdLoadFlow_RewardedVideo;
import org.metatrans.commons.ads.impl.flow.IAdLoadFlow;
import org.metatrans.commons.ads.impl.sequence.AdsContainerSequence_Cycle;
import org.metatrans.commons.ads.impl.sequence.AdsContainerSequence_PermanentSingleton;
import org.metatrans.commons.ads.impl.sequence.IAdsContainerSequence;
import org.metatrans.commons.ads.impl.stat.AdStorageUtils;
import org.metatrans.commons.ads.impl.stat.model.AdData;
import org.metatrans.commons.ads.impl.stat.model.AdsData;
import org.metatrans.commons.app.Application_Base_Ads;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.ViewGroup;


public class AdsManager {
	
	protected static volatile AdsManager singleton;
	
	private Handler uiHandler = new Handler(Looper.getMainLooper());
	private ExecutorService executor = Executors.newCachedThreadPool();
	
	private Map<Integer, IAdsContainer> providersContainers;
	private AdsContainerSequence_Cycle providersContainers_Banners;
	private AdsContainerSequence_Cycle providersContainers_Interstitials;
	private AdsContainerSequence_Cycle providersContainers_RewardedVideos;
	
	private Map<String, IAdLoadFlow> cachedFlows;
	
	private AdsData adsData_banner;
	private AdsData adsData_interstitial;

	//TODO: Currently we use adsData_interstitial as adsData_rewarded_videos
	//private AdsData adsData_rewarded_videos;

	private Context context;
	
	private IAdsConfigurations adsConfigs;
	
	private boolean testMode;
	
	
	public static AdsManager getSingleton(Application _root_context) {

		synchronized (AdsManager.class) {
			
			if (singleton == null) {

				singleton = new AdsManager(_root_context, ((Application_Base_Ads)_root_context).getAdsConfigurations(),
						((Application_Base_Ads)_root_context).isTestMode());
			}
			
			return singleton;
		}
	}
	
	
	//Singleton shoud be initialize in supper class
	public static AdsManager getSingleton() {
		return singleton;
	}
	
	
	protected AdsManager(Context _context, IAdsConfigurations _adsConfigs, boolean _testMode) {
		
		
		try {

			context = _context;
			adsConfigs = _adsConfigs;
			testMode = _testMode;
			
			providersContainers_Banners = new AdsContainerSequence_Cycle();
			providersContainers_Interstitials = new AdsContainerSequence_Cycle();
			providersContainers_RewardedVideos = new AdsContainerSequence_Cycle();

			cachedFlows = new HashMap<String, IAdLoadFlow>();
			
			providersContainers = new HashMap<Integer, IAdsContainer>();
			
			Map<Integer, IAdsContainer> duplicationTest = new HashMap<Integer, IAdsContainer>();
			int[] providers_banners = adsConfigs.getProvidersOfBanners();
			for (int i=0; i<providers_banners.length; i++) {
				
				int providerID = providers_banners[i];

				if (duplicationTest.containsKey(providerID)) {
					throw new IllegalStateException("Duplicated banner provider: " + providerID);
				}


				IAdsConfiguration config = adsConfigs.getProviderConfiguration(providerID);

				String container_class = config.getContainerClass();

				IAdsContainer adsContainer = null;

				try {

					adsContainer = (IAdsContainer) createObjectByClassName_Constructor1(
							container_class,
							context
					);

				} catch (NoSuchMethodException e) {

					adsContainer = (IAdsContainer) createObjectByClassName_Constructor2(
							container_class,
							context,
							config
					);
				}


				adsContainer.onCreate_Container(context);
				
				if (providerID != adsContainer.getProviderID()) {
					throw new IllegalStateException("Ads: providerID=" + providerID + ", adsContainer.getProviderID()=" + adsContainer.getProviderID());
				}
				
				providersContainers.put(providerID, adsContainer);
				providersContainers_Banners.addContainer(adsContainer);
				
				duplicationTest.put(providerID, adsContainer);
			}
			
			duplicationTest.clear();
			
			int[] providers_interstitial = adsConfigs.getProvidersOfInterstitials();
			for (int i=0; i<providers_interstitial.length; i++) {
				
				int providerID = providers_interstitial[i];
				if (duplicationTest.containsKey(providerID)) {
					throw new IllegalStateException("Duplicated interstitial provider: " + providerID);
				}
				
				IAdsContainer adsContainer = null;

				if (providersContainers.containsKey(providerID)) {

					adsContainer = providersContainers.get(providerID);

				} else {


					IAdsConfiguration config = adsConfigs.getProviderConfiguration(providerID);

					String container_class = config.getContainerClass();

					try {

						adsContainer = (IAdsContainer) createObjectByClassName_Constructor1(
								container_class,
								context
						);

					} catch (NoSuchMethodException e) {

						adsContainer = (IAdsContainer) createObjectByClassName_Constructor2(
								container_class,
								context,
								config
						);
					}


					adsContainer.onCreate_Container(context);
					
					if (providerID != adsContainer.getProviderID()) {
						throw new IllegalStateException("Ads: providerID=" + providerID + ", adsContainer.getProviderID()=" + adsContainer.getProviderID());
					}
					
					providersContainers.put(providerID, adsContainer);					
				}
				providersContainers_Interstitials.addContainer(adsContainer);

				duplicationTest.put(providerID, adsContainer);
			}


			duplicationTest.clear();

			int[] providers_rewarded_videos = adsConfigs.getProvidersOfRewardedVideos();
			for (int i=0; i<providers_rewarded_videos.length; i++) {

				int providerID = providers_rewarded_videos[i];
				if (duplicationTest.containsKey(providerID)) {
					throw new IllegalStateException("Duplicated interstitial provider: " + providerID);
				}

				IAdsContainer adsContainer = null;

				if (providersContainers.containsKey(providerID)) {

					adsContainer = providersContainers.get(providerID);

				} else {


					IAdsConfiguration config = adsConfigs.getProviderConfiguration(providerID);

					String container_class = config.getContainerClass();

					try {

						adsContainer = (IAdsContainer) createObjectByClassName_Constructor1(
								container_class,
								context
						);

					} catch (NoSuchMethodException e) {

						adsContainer = (IAdsContainer) createObjectByClassName_Constructor2(
								container_class,
								context,
								config
						);
					}


					adsContainer.onCreate_Container(context);

					if (providerID != adsContainer.getProviderID()) {
						throw new IllegalStateException("Ads: providerID=" + providerID + ", adsContainer.getProviderID()=" + adsContainer.getProviderID());
					}

					providersContainers.put(providerID, adsContainer);
				}

				providersContainers_RewardedVideos.addContainer(adsContainer);

				duplicationTest.put(providerID, adsContainer);
			}


			Object[] adsDataArr = AdStorageUtils.readStorage(context);
			
			adsData_banner = (AdsData) adsDataArr[0];

			if (adsData_banner == null) {

				adsData_banner = new AdsData(237);
			}

			adsData_interstitial = (AdsData) adsDataArr[1];

			if (adsData_interstitial == null) {

				adsData_interstitial = new AdsData(537);
			}
			
		} catch (Exception e) {

			// Failure

			e.printStackTrace();

			throw new RuntimeException(e);

		}


		System.out.println("AdsManager singleton created!");
	}


	public void requestConsentInfoUpdate(Activity first_activity) {

		Set keys = providersContainers.keySet();

		for (Object key: keys) {

			providersContainers.get(key).requestConsentInfoUpdate(first_activity);
		}
	}


	public boolean isTestMode() {

		return testMode;
	}


	public Handler getUiHandler() {

		return uiHandler;
	}


	public IAdLoadFlow getCachedFlow(String key) {

		return cachedFlows.get(key);
	}


	public void putCachedFlow(String key, IAdLoadFlow flow) {

		cachedFlows.put(key, flow);
	}


	public void storeAdsData() {

		try {

			AdStorageUtils.writeStore(context, adsData_banner, adsData_interstitial);

			System.out.println("ADS DATA (BANNER)" + adsData_banner);
			System.out.println("ADS DATA (INTERSTITIAL)" + adsData_interstitial);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	
	/*public IAdLoadFlow createFlow_Banner(int adsProviderID, ViewGroup frame, String adID) {
		return createFlow_Banner(frame, adID, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, new AdsContainerSequence_PermanentSingleton(providersContainers.get(adsProviderID)));
	}
	
	
	public IAdLoadFlow createFlow_Banner_Random(ViewGroup frame, String adID) {
		return createFlow_Banner_Random(frame, adID, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
	}
	*/
	
	/*public IAdLoadFlow createFlow_Banner_Random(ViewGroup frame, String adID, int gravity) {
		
		List<IAdsContainer> adsContainers = new ArrayList<IAdsContainer>();
		adsContainers.addAll(providersContainers_Banners.getAdsContainers());
		Collections.shuffle(adsContainers);
		
		return createFlow_Banner(frame, adID, gravity, new AdsContainerSequence_Cycle(adsContainers));
	}
	
	
	public IAdLoadFlow createFlow_Banner_Rating(ViewGroup frame, String adID) {
		return createFlow_Banner_Rating(frame, adID, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 1);
	}*/


	/*ublic IAdLoadFlow createFlow_Banner_Mixed(ViewGroup frame, String adID) {
    	return createFlow_Banner_Mixed(frame, adID, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
	}*/




	/*private double getHomeAdsProbability() {

		//if (true) return true;

		//double[] probs_for_show_homeAD = new double[] {0.90, 0.77, 0.63, 0.50, 0.37, 0.23, 0.10};
		double[] probs_for_show_homeAD = new double[] {0.50, 0.35, 0.25, 0.10};

		EventsData_Base eventsData = Application_Base.getInstance().getEventsManager().getEventsData(Application_Base.getInstance());

		if (eventsData != null) {

			long time_since_install_ms = System.currentTimeMillis() - eventsData.installation_time;
			int time_since_install_days = (int) (time_since_install_ms / AlarmManager.INTERVAL_DAY);

			if (time_since_install_days < 0) {
				time_since_install_days = 0;
			}

			if (time_since_install_days >= probs_for_show_homeAD.length) {
				time_since_install_days = probs_for_show_homeAD.length - 1;
			}

			double prob = probs_for_show_homeAD[time_since_install_days];

			System.out.println("getHomeAdsProbability: " + prob);

			return prob;

		} else {

			System.out.println("getHomeAdsProbability: " + 1);

			return 1;
		}
	}*/


	public IAdLoadFlow createFlow_Banner_Mixed(ViewGroup frame, String adID, int gravity) {

		return createFlow_Banner_Rating(frame, adID, gravity, 1);
	}


	private IAdLoadFlow createFlow_Banner_Rating(ViewGroup frame, String adID, int gravity, double prob_homeAdFirst) {
		
		List<IAdsContainer> adsContainers = new ArrayList<IAdsContainer>();

		adsContainers.addAll(providersContainers_Banners.getAdsContainers());

		Collections.sort(adsContainers, new Comparator_Impressions(adsData_banner));

		//Print statistics
		for (int i = 0; i < adsContainers.size(); i++) {
			IAdsContainer cur_container = adsContainers.get(i);
			AdData ads_stats = adsData_banner.getAdData(cur_container.getProviderID());
			System.out.println("AdData: ProviderID=" + cur_container.getProviderID() + ", Impressions=" + ads_stats.getImpressions() + ", CTR=" + ads_stats.getCTR());
		}


		/*Collections.sort(adsContainers, new Comparator_Ratings(adsData_banner));


		int homeAdIndex = -1;
		IAdsContainer homeAds_Container = null;
		
		for (int i = 0; i < adsContainers.size(); i++) {
			IAdsContainer cur_container = adsContainers.get(i);
			if (cur_container.getProviderID() == IAdsProviders.ID_HOME_ADS) {
				homeAdIndex = i;
				homeAds_Container = cur_container;
				break;
			}
		}
		
		if (homeAdIndex >= 0) {
			
			adsContainers.remove(homeAdIndex);

			//AdData homeAdsData = adsData_banner.getAdData(IAdsProviders.ID_HOME_ADS);
			//if (homeAdsData.getClicksCount() == 0) {
			//	adsContainers.add(0, homeAds_Container);
			//	System.out.println("HomeAd (Banner) moved on the first place, because of less clicks");
			//} else {
				if (Math.random() <= 0.5d) {
					adsContainers.add(0, homeAds_Container);
					System.out.println("HomeAd (Banner) moved on the first place, because of probability");
				} else {
					adsContainers.add(homeAds_Container);
					System.out.println("HomeAd (Banner) moved on the last place");
				}
			//}
		}*/

		
		System.out.println("ADS ORDER (Banner)" + adsContainers);
		
		return createFlow_Banner(frame, adID, gravity, new AdsContainerSequence_Cycle(adsContainers));
	}

	
	private IAdLoadFlow createFlow_Banner(ViewGroup frame, String adID, int gravity, IAdsContainerSequence containers_sequance) {
		IAdLoadFlow flow = new AdLoadFlow_Banner(adID, frame, gravity, containers_sequance, adsData_banner, uiHandler, executor);
		return flow;
	}

	
	/*public IAdLoadFlow createFlow_Interstitial_Random(String adID) {
		
		List<IAdsContainer> adsContainers = new ArrayList<IAdsContainer>();
		adsContainers.addAll(providersContainers_Interstitials.getAdsContainers());
		Collections.shuffle(adsContainers);
		
		return createFlow_Interstitial(adID, new AdsContainerSequence_Cycle(adsContainers));
	}
	*/


	public IAdLoadFlow createFlow_Interstitial_Mixed(String adID) {

		return createFlow_Interstitial_Rating(adID, 1);
	}


	private IAdLoadFlow createFlow_Interstitial_Rating(String adID, double prob_homeAdFirst) {
		
		List<IAdsContainer> adsContainers = new ArrayList<IAdsContainer>();

		adsContainers.addAll(providersContainers_Interstitials.getAdsContainers());

		Collections.sort(adsContainers, new Comparator_Impressions(adsData_interstitial));

		//Print statistics
		for (int i = 0; i < adsContainers.size(); i++) {
			IAdsContainer cur_container = adsContainers.get(i);
			AdData ads_stats = adsData_interstitial.getAdData(cur_container.getProviderID());
			System.out.println("AdData: ProviderID=" + cur_container.getProviderID() + ", Impressions=" + ads_stats.getImpressions() + ", CTR=" + ads_stats.getCTR());
		}


		/*Collections.sort(adsContainers, new Comparator_Ratings(adsData_interstitial));
		
		int homeAdIndex = -1;
		IAdsContainer homeAds_Container = null;
		
		for (int i = 0; i < adsContainers.size(); i++) {
			IAdsContainer cur_container = adsContainers.get(i);
			if (cur_container.getProviderID() == IAdsProviders.ID_HOME_ADS) {
				homeAdIndex = i;
				homeAds_Container = cur_container;
				break;
			}
		}
		
		if (homeAdIndex >= 0) {
			
			adsContainers.remove(homeAdIndex);

			//AdData homeAdsData = adsData_banner.getAdData(IAdsProviders.ID_HOME_ADS);
			//if (homeAdsData.getClicksCount() == 0) {
			//	adsContainers.add(0, homeAds_Container);
			//	System.out.println("HomeAd (Interstitial) moved on the first place, because of less clicks");
			//} else {
				if (Math.random() <= 0.5d) {
					adsContainers.add(0, homeAds_Container);
					System.out.println("HomeAd (Interstitial) moved on the first place, because of probability");
				} else {
					adsContainers.add(homeAds_Container);
					System.out.println("HomeAd (Interstitial) moved on the last place");
				}
			//}
		}
		*/
		
		System.out.println("ADS ORDER (Interstitial)" + adsContainers);
		
		return createFlow_Interstitial(adID, new AdsContainerSequence_Cycle(adsContainers));
	}


	public IAdLoadFlow createFlow_RewardedVideo_Mixed(String adID) {

		return createFlow_RewardedVideo_Rating(adID);
	}


	private IAdLoadFlow createFlow_RewardedVideo_Rating(String adID) {

		List<IAdsContainer> adsContainers = new ArrayList<IAdsContainer>();

		adsContainers.addAll(providersContainers_RewardedVideos.getAdsContainers());

		//TODO: Currently we use the same statistics for the RewardedVideo, as for the Interstitial.
		Collections.sort(adsContainers, new Comparator_Impressions(adsData_interstitial));

		//Print statistics
		for (int i = 0; i < adsContainers.size(); i++) {
			IAdsContainer cur_container = adsContainers.get(i);
			AdData ads_stats = adsData_interstitial.getAdData(cur_container.getProviderID());
			System.out.println("AdData: ProviderID=" + cur_container.getProviderID() + ", Impressions=" + ads_stats.getImpressions() + ", CTR=" + ads_stats.getCTR());
		}

		System.out.println("ADS ORDER (Interstitial)" + adsContainers);

		return createFlow_RewardedVideo(adID, new AdsContainerSequence_Cycle(adsContainers));
	}

	/*private IAdLoadFlow createFlow_Interstitial(int adsProviderID,  String adID) {
		return createFlow_Interstitial(adID, new AdsContainerSequence_PermanentSingleton(providersContainers.get(adsProviderID)));
	}*/


	private IAdLoadFlow createFlow_RewardedVideo(String adID, IAdsContainerSequence containers_sequance) {

		AdLoadFlow_RewardedVideo flow = new AdLoadFlow_RewardedVideo(adID, containers_sequance, adsData_interstitial, uiHandler, executor);

		List<IAdsContainer> containers = containers_sequance.getAdsContainers();

		for (IAdsContainer container: containers) {

			try {

				container.initRewardedVideo(flow);

			} catch (Throwable t) {

				//Print the error and continue with what we have as successfully initialized containers
				t.printStackTrace();
			}
		}

		return flow;
	}


	private IAdLoadFlow createFlow_Interstitial(String adID, IAdsContainerSequence containers_sequance) {
		
		AdLoadFlow_Interstitial flow = new AdLoadFlow_Interstitial(adID, containers_sequance, adsData_interstitial, uiHandler, executor);
		
		List<IAdsContainer> containers = containers_sequance.getAdsContainers();

		for (IAdsContainer container: containers) {

			try {

				container.initInterstitial(flow);

			} catch (Throwable t) {

				//Print the error and continue with what we have as successfully initialized containers
				t.printStackTrace();
			}
		}
		
		return flow;
	}


	private static Object createObjectByClassName_Constructor1(String className, Context context) throws java.lang.NoSuchMethodException {

		Object result = null;

		try {

			Class clazz = AdsManager.class.getClassLoader().loadClass(className);

			Constructor constructor = clazz.getConstructor(Context.class);

			result = constructor.newInstance(context);

		} catch (ClassNotFoundException e) {

			throw new RuntimeException(e);

		} catch (InstantiationException e) {

			throw new RuntimeException(e);

		} catch (IllegalAccessException e) {

			throw new RuntimeException(e);

		} catch (SecurityException e) {

			throw new RuntimeException(e);

		} catch (IllegalArgumentException e) {

			throw new RuntimeException(e);

		} catch (InvocationTargetException e) {

			throw new RuntimeException(e);
		}

		return result;
	}


	private static Object createObjectByClassName_Constructor2(String className, Context context, IAdsConfiguration arg) {

		Object result = null;

		try {

			Class clazz = AdsManager.class.getClassLoader().loadClass(className);

			Constructor constructor = clazz.getConstructor(Context.class, IAdsConfiguration.class);

			result = constructor.newInstance(context, arg);

		} catch (ClassNotFoundException e) {

			throw new RuntimeException(e);

		} catch (InstantiationException e) {

			throw new RuntimeException(e);

		} catch (IllegalAccessException e) {

			throw new RuntimeException(e);

		} catch (SecurityException e) {

			throw new RuntimeException(e);

		} catch (IllegalArgumentException e) {

			throw new RuntimeException(e);

		} catch (NoSuchMethodException e) {

			throw new RuntimeException(e);

		} catch (InvocationTargetException e) {

			throw new RuntimeException(e);
		}

		return result;
	}
}
