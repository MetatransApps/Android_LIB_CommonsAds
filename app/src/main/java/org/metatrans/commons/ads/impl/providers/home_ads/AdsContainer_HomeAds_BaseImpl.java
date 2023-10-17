package org.metatrans.commons.ads.impl.providers.home_ads;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import org.metatrans.commons.DeviceUtils;
import org.metatrans.commons.ads.api.IAdsConfiguration;
import org.metatrans.commons.ads.impl.flow.AdLoadFlow_Banner;
import org.metatrans.commons.ads.impl.flow.AdLoadFlow_Interstitial;
import org.metatrans.commons.ads.impl.flow.AdLoadFlow_RewardedVideo;
import org.metatrans.commons.ads.impl.providers.AdsContainer_Base;
import org.metatrans.commons.ads.utils.BannerUtils;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.appstore.IAppStore;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.cfg.publishedapp.IHomeAdInfo;
import org.metatrans.commons.events.api.IEvent_Base;
import org.metatrans.commons.events.api.IEventsManager;


public abstract class AdsContainer_HomeAds_BaseImpl extends AdsContainer_Base  {


    public AdsContainer_HomeAds_BaseImpl(Context _activity, IAdsConfiguration adsConf) {
        super(_activity, adsConf);
    }


    @Override
    public abstract int getProviderID();


    protected int[] getExcludedStores() {
        return new int[0];
    }


    @Override
    protected boolean canWorkOffline() {
        return true;
    }


    protected abstract IHomeAdInfo getNextHomeAdInfo();


    //Return true if opened successfully
    protected abstract void openTarget(IHomeAdInfo promoted);


    protected abstract Intent createInterstitialIntent(Activity currentActivity);


    @Override
    protected View createBanner(final AdLoadFlow_Banner flow) {

        System.out.println("AdsContainer_HomeAds: createBanner called");

        IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(Application_Base.getInstance().getUserSettings().uiColoursID);

        AdsContainer_HomeAds_BaseImpl currentContainer = getCurrentHomeAdsSubContainer();
        final IHomeAdInfo homeAdInfo = getNextHomeAdInfo();

        View bannerView = new BannerView(getActivity(), coloursCfg, homeAdInfo, currentContainer);

        bannerView.setId(BannerUtils.AD_BANNER_VIEW_ID);

        View wrapper = BannerUtils.createView(getActivity(), bannerView, flow.getGravity());

        return wrapper;
    }


    /*
     * Override in case of Composite Home Ads container
     */
    protected AdsContainer_HomeAds_BaseImpl getCurrentHomeAdsSubContainer() {
        return this;
    }


    @Override
    protected Object createBannerListener(AdLoadFlow_Banner flow) {
        throw new UnsupportedOperationException();
    }


    @Override
    protected void destroyBanner(Object bannerView) {
        if (bannerView instanceof View) {
            ((View)bannerView).destroyDrawingCache();
        }
    }


    @Override
    protected Object createInterstitial(AdLoadFlow_Interstitial flow) {

        System.out.println("AdsContainer_HomeAds: createInterstitial called");

        Object dummy = new Object();
        return dummy;
    }


    @Override
    protected Object createInterstitialListener(AdLoadFlow_Interstitial flow, Object interstitial) {
        throw new UnsupportedOperationException();
    }


    @Override
    protected void destroyInterstitial(Object ad) {
        //Do nothing
    }


    @Override
    protected Object createRewardedVideo(AdLoadFlow_RewardedVideo flow) {

        System.out.println("AdsContainer_HomeAds: createRewardedVideo called");

        Object dummy = new Object();
        return dummy;
    }


    @Override
    protected void destroyRewardedVideo(Object ad) {

    }


    @Override
    protected void request_sync_banner(final View adview, AdLoadFlow_Banner flow) {
        request_sync(adview, flow);
    }


    @Override
    protected void request_sync_banner(View adview) {
        throw new UnsupportedOperationException();
    }


    @Override
    protected void showInterstitial(Object dummy, AdLoadFlow_Interstitial flow) {
        request_sync(flow);
    }


    @Override
    protected void showInterstitial(Object ad) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void showRewardedVideo(Object ad) {

        //Do nothing
    }

    private void request_sync(final View bannerView, final AdLoadFlow_Banner flow) {

        System.out.println("AdsContainer_HomeAds: request_sync for banner called. bannerView=" + bannerView);

        //bannerView.setVisibility(View.VISIBLE);

        ((BannerView)bannerView).setClickAction(new Runnable() {

            @Override
            public void run() {
                flow.clicked();

                ((BannerView)bannerView).openTarget();

            }
        });


        flow.loadOK();

    }


    private void request_sync(AdLoadFlow_Interstitial flow) {

        System.out.println("AdsContainer_HomeAds: request_sync for interstitial called");

        int rand = (int) (Math.random() * 100d);

        Activity currentActivity = Application_Base.getInstance().getCurrentActivity();
        if (currentActivity == null) {
            System.out.println("AdsContainer_HomeAds: EXIT because current activity is null");
            flow.loadFailed();
            return;
        }

        final IHomeAdInfo promoted = getNextHomeAdInfo();

        if (rand <= 50 || promoted == null || !DeviceUtils.isConnected()) {

            System.out.println("AdsContainer_HomeAds: request_sync show APP_LIST");

            Intent intent = createInterstitialIntent(currentActivity);

            if (intent != null) {

                currentActivity.startActivity(intent);

                flow.loadOK();

                //flow.cleanCurrent();

                try {

                    IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();
                    Context context = (getActivity() != null) ? getActivity() : Application_Base.getInstance();
                    eventsManager.register(context,
                            IEvent_Base.EVENT_MARKETING_HOME_AD_INTERSTITIAL_OPENED.createByVarianceInCategory3(
                                    "APP_LIST".hashCode(),
                                    "APP_LIST"
                            )
                    );

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                flow.loadFailed();
            }
        } else {

            System.out.println("AdsContainer_HomeAds: request_sync promoted=" + promoted);

            if (promoted != null) {

                openTarget(promoted);

                flow.loadOK();

                try {

                    IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();

                    Context context = (getActivity() != null) ? getActivity() : Application_Base.getInstance();

                    eventsManager.register(context,
                            IEvent_Base.EVENT_MARKETING_HOME_AD_INTERSTITIAL_OPENED.createByVarianceInCategory3(
                                    promoted.getID().hashCode(),
                                    context.getString(promoted.getName())
                            )
                    );

                } catch(Exception e) {

                    e.printStackTrace();
                }

            } else {

                flow.loadFailed();
            }
        }
    }
}
