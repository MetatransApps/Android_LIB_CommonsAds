package com.apps.mobile.android.commons.ads.impl.flow;


import java.util.concurrent.ExecutorService;

import android.os.Handler;

import com.apps.mobile.android.commons.DeviceUtils;
import com.apps.mobile.android.commons.ads.impl.AdsManager;
import com.apps.mobile.android.commons.ads.impl.IAdsContainer;
import com.apps.mobile.android.commons.ads.impl.sequence.IAdsContainerSequence;
import com.apps.mobile.android.commons.ads.impl.stat.model.AdData;
import com.apps.mobile.android.commons.ads.impl.stat.model.AdDataUtils;
import com.apps.mobile.android.commons.ads.impl.stat.model.AdsData;


public abstract class AdLoadFlow_Base implements IAdLoadFlow {
	
	
	private static final int RETRY_WAIT_TIME = 100;
	
	
	private String adID;
	private IAdsContainerSequence containers_sequance;
	private IAdsContainer current_container;
	
	private boolean isActive;
	private boolean isLoading;
	private boolean isDetached; //corresponding AD exists into the internal store
	
	private Handler uiHandler;
	private ExecutorService executor;
	
	private AdsData adsData;
	private long time_current_container_start;
	
	private int counter = 0;
	
	private Retry current_retry_job;
	
	
	public AdLoadFlow_Base(String _adID, IAdsContainerSequence _containers_sequance, AdsData _adsData, Handler _uiHandler, ExecutorService _executor) {
		
		adID = _adID;
		containers_sequance = _containers_sequance;
		adsData = _adsData;
		uiHandler = _uiHandler;
		executor = _executor;

		current_container = _containers_sequance.getAdsContainers().get(0);

		isDetached = true;
	}
	
	
	protected void retry() {
		isDetached = false;
		time_current_container_start = System.currentTimeMillis();
	}
	
	
	public void cleanCurrent() {
		isDetached = true;
	}
	
	
	@Override
	public String getAdID() {
		return adID;
	}
	
	
	protected IAdsContainer getCurrentContainer() {
		return current_container;
	}

	
	protected void nextContainer() {
		
		current_container = containers_sequance.next();
		
		System.out.println("AdLoadFlow_Base: nextContainer=" + current_container);
	}
	

	public boolean isActive() {
		return isActive;
	}
	
	
	protected boolean isLoading() {
		return isLoading;
	}


	public ExecutorService getExecutor() {
		return executor;
	}
	
	
	public Handler getUiHandler() {
		return uiHandler;
	}
	
	
	@Override
	public synchronized void resume() {
		
		long mem_mb = DeviceUtils.getAvailableMemory_InMB();
		System.out.println("AdLoadFlow_Base: AD FLOW: resume - " + getAdID() + " available memory is " + mem_mb + " MB");
		if (mem_mb <= 3) { //3 MB for buffer should be enough for 1 ad)
			System.out.println("AD FLOW: resume - SKIPPED, because available memory is " + mem_mb + " MB");
			return;
		}
		
		if(isLoading()) {
			//Do nothing
			System.out.println("AdLoadFlow_Base: AdLoadFlow is already in loading mode. adID=" + getAdID() + ", obj=" + this);
		} else {
			
			if (!isDetached) {
				System.out.println("AdLoadFlow_Base : AdLoadFlow is attached but resume is called. adID=" + getAdID() + ", obj=" + this);
				if (!isActive()) {
					//throw new IllegalStateException("AdLoadFlow is not active but is attached and resume is called. adID=" + getAdID() + ", obj=" + this);
					//TODO: CHECK this
					isActive = true;
				}
			} else {
			
				isActive = true;
				
				startLoading();
				
				asyncRetry();
			}
		}
		
		System.out.println("AdLoadFlow_Base: AD FLOW: resume - OK");
	}
	
	
	@Override
	public synchronized void pause() {
		
		System.out.println("AdLoadFlow_Base: AD FLOW: " + "pause " + getAdID() + ", isDetached=" + isDetached);
		
		counter = 0;
		
		containers_sequance.reset();
		
		if (current_retry_job != null) {
			current_retry_job.stoped = true;
		}
		
		isActive = false;
		
		if (isLoading()) {
			stopLoading();
		}
		
		if (!isDetached /*&& !hasOverlayScreen()*/) { 
			cleanCurrent();
		}
		
		AdsManager.getSingleton().storeAdsData();
	}
	
	
	private void asyncRetry() {
		
		if (current_retry_job != null) {
			//TODO: throw new IllegalStateException();
			//No retry
			
			System.out.println("AdLoadFlow_Base: asyncRetry: EXIT because current_retry_job is NOT null");
			
			return;
		}
		
		getExecutor().submit(new Runnable() {
			
			@Override
			public void run() {
				
				current_retry_job = new Retry();
				
				getUiHandler().post(current_retry_job);	
			}
		});
	}
	
	
	public synchronized void loadOK() {
		
		stopLoading();
		
		AdData adData = adsData.getAdData(getCurrentContainer().getProviderID());
		AdDataUtils.addSuccess(adData, System.currentTimeMillis() - time_current_container_start);
	}
	
	
	public synchronized void loadFailed() {
		
		cleanCurrent();
		
		AdData adData = adsData.getAdData(getCurrentContainer().getProviderID());
		AdDataUtils.addFail(adData);
		
		if (current_retry_job != null) {
			
			//throw new IllegalStateException();
			
			current_retry_job.stoped = true;
			current_retry_job = null;
			
			if (!isActive()) {
				System.out.println("AdLoadFlow_Base: loadFailed for container " + getCurrentContainer() + " - exit because the flow is not active");
				return;
			}
		}
		
		System.out.println("AdLoadFlow_Base: loadFailed for container " + getCurrentContainer() + " - schedule next container.");
		
		getExecutor().submit(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					long wait_time = RETRY_WAIT_TIME * Math.max(1, counter);
					System.out.println("AdLoadFlow_Base: scheduling wait time is " + wait_time + ", now waiting ...");
					Thread.sleep(wait_time);
					System.out.println("AdLoadFlow_Base: scheduling wait finished current_retry_job = " + current_retry_job);
					
				} catch (InterruptedException e) {}
				
				current_retry_job = new Retry();
				
				getUiHandler().post(current_retry_job);	
			}
		});
	}
	
	
	public synchronized void clicked() {
		//System.out.println("OPA1");
		AdData adData = adsData.getAdData(getCurrentContainer().getProviderID());
		//System.out.println("OPA2");
		AdDataUtils.addClick(adData);
		//System.out.println("CLICKED AD: " + adData.getClicksCount());

	}
	
	
	protected synchronized void nextRetry() {
		
		System.out.println("AdLoadFlow_Base: RETRY " + (counter + 1) + ", adID=" + getAdID() + ", obj=" + this);
		counter++;
		
		nextContainer();
		
		retry();
	}
	
	
	protected synchronized void startLoading() {
		if (!isActive()) {
			throw new IllegalStateException("AdLoadFlow_Base: AdLoadFlow is not active. adID=" +adID + ", obj=" + this);
		}
		if (isLoading()) {
			throw new IllegalStateException("AdLoadFlow_Base: AdLoadFlow is already in loading mode. adID=" +adID + ", obj=" + this);
		}
		
		isLoading = true;
	}
	
	
	protected synchronized void stopLoading() {
		if (!isLoading()) {
			//throw new IllegalStateException("AdLoadFlow is not in loading mode. adID=" +adID + ", obj=" + this);
		}
		
		isLoading = false;
	}
	
	
	private class Retry implements Runnable {
		
		
		boolean stoped = false;
		
		
		@Override
		public void run() {
			
			System.out.println("AdLoadFlow_Base: Retry Job: Running ... ");
			
			synchronized (AdLoadFlow_Base.this) {
				
				if (current_retry_job != null && !stoped && isActive()) {
					
					try {
						
						nextRetry();
						
					} catch(Throwable t) {
						//Print the error and continue with the next Ads container
						t.printStackTrace();

						loadFailed();
					}
				} else {
					System.out.println("AdLoadFlow_Base: Retry Job: NOT EXECUTED, because: current_retry_job=" + current_retry_job + ", stoped=" + stoped + ", isActive()=" + isActive());
				}
				
				current_retry_job = null;	
			}
		}
	}
}
