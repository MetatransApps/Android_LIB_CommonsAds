package org.metatrans.commons.ads.impl.providers.home_ads;


import org.metatrans.commons.R;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
//import org.metatrans.commons.cfg.publishedapp.IPublishedApplication;
import org.metatrans.commons.cfg.publishedapp.IHomeAdInfo;
import org.metatrans.commons.ui.ButtonAreaClick_Image;
import org.metatrans.commons.ui.IButtonArea;
import org.metatrans.commons.ui.TextArea;
import org.metatrans.commons.ui.utils.BitmapUtils;
import org.metatrans.commons.ui.utils.DrawingUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class BannerView extends View implements OnTouchListener {
	
	
	//private boolean initialized;
	
	private RectF rectf_main;
	private RectF rectf_main_inner;
	private RectF rectangle_icon;
	private RectF rectangle_text1;
	private RectF rectangle_text2;
	//private RectF rectangle_type1;
	//private RectF rectangle_type2;
	
	private IButtonArea buttonarea_icon;
	private IButtonArea buttonarea_text1;
	private IButtonArea buttonarea_text2;
	private IButtonArea buttonarea_text3;
	private IButtonArea buttonarea_type1;
	private IButtonArea buttonarea_type2;
	
	protected Paint paint_background;
	
	private IConfigurationColours coloursCfg;
	private IHomeAdInfo adInfo;
	private AdsContainer_HomeAds_BaseImpl homeAdsContainer;

	private int colour_area;
	
	private Runnable click_action;
	
	private int MAX_COUNTER_SWITCH = 100;
	private int counter_switch = MAX_COUNTER_SWITCH;
	private IButtonArea current_text2 = null;
	
	
	public BannerView(Context context, IConfigurationColours _coloursCfg, IHomeAdInfo _adInfo, AdsContainer_HomeAds_BaseImpl _homeAdsContainer) {
		
		super(context);
		
		
		coloursCfg 						= _coloursCfg;

		adInfo 							= _adInfo;
		homeAdsContainer 				= _homeAdsContainer;
		rectf_main 						= new RectF();
		rectf_main_inner 				= new RectF();
		rectangle_icon 					= new RectF();
		rectangle_text1 				= new RectF();
		rectangle_text2			 		= new RectF();
		//rectangle_type1 				= new RectF();
		//rectangle_type2			 		= new RectF();
		
		paint_background 				= new Paint();
		
		colour_area = coloursCfg.getColour_Background();
		
		setOnTouchListener(this);
	}
	
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		//if (!initialized) {
			
			int MARGIN = 7;
			
			if (getMeasuredWidth() > getMeasuredHeight()) {
				
				rectf_main.left = 0;//1 * getMeasuredWidth() / 6;
				rectf_main.right = rectf_main.left + (4 * getMeasuredWidth()) / 8;
				
				rectf_main.top = (/*9*/0 * getMeasuredHeight()) / 11;
				rectf_main.bottom = rectf_main.top + (2 * getMeasuredHeight()) / 11;
			} else {
				
				rectf_main.left = 0;//1 * getMeasuredWidth() / 19;
				rectf_main.right = rectf_main.left + getMeasuredWidth();

				rectf_main.top = 0;
				rectf_main.bottom = rectf_main.top + (2 * getMeasuredHeight()) / 19;
			}


			rectf_main_inner.left = rectf_main.left + 0;
			rectf_main_inner.right = rectf_main.right - 0;
			rectf_main_inner.top = rectf_main.top + 0;
			rectf_main_inner.bottom = rectf_main.bottom - 0;
			
			
			rectangle_icon.top = rectf_main_inner.top + MARGIN;
			rectangle_icon.bottom = rectf_main_inner.bottom - MARGIN;
			rectangle_icon.left = rectf_main_inner.left + MARGIN;
			rectangle_icon.right = rectangle_icon.left + (rectangle_icon.bottom - rectangle_icon.top) - MARGIN;
			
			/*rectangle_type1.top = rectf_main_inner.top + MARGIN;
			rectangle_type1.bottom = rectf_main_inner.top + (rectf_main_inner.bottom - rectf_main_inner.top) / 2 - MARGIN / 2;
			rectangle_type1.right = rectf_main_inner.right - MARGIN;
			rectangle_type1.left = rectangle_type1.right - (rectangle_icon.bottom - rectangle_icon.top);// + MARGIN;

			rectangle_type2.top = rectf_main_inner.top + (rectf_main_inner.bottom - rectf_main_inner.top) / 2 + MARGIN / 2;
			rectangle_type2.bottom = rectf_main_inner.bottom - MARGIN;
			rectangle_type2.right = rectf_main_inner.right - MARGIN;
			rectangle_type2.left = rectangle_type2.right - (rectangle_icon.bottom - rectangle_icon.top);// + MARGIN;*/

			
			rectangle_text1.left = rectangle_icon.right + MARGIN;
			rectangle_text1.right = rectf_main_inner.right - MARGIN;
			rectangle_text1.top = rectf_main_inner.top + MARGIN;
			rectangle_text1.bottom = rectf_main_inner.top + (rectf_main_inner.bottom - rectf_main_inner.top) / 2 - MARGIN / 2;
			
			rectangle_text2.left = rectangle_icon.right + MARGIN;
			rectangle_text2.right = rectf_main_inner.right - MARGIN;
			rectangle_text2.top = rectf_main_inner.top + (rectf_main_inner.bottom - rectf_main_inner.top) / 2 + MARGIN / 2;
			rectangle_text2.bottom = rectf_main_inner.bottom - MARGIN;
			
			
			buttonarea_icon =  new ButtonAreaClick_Image(rectangle_icon,
					BitmapUtils.fromResource(getContext(), adInfo.getIconResID()),
					coloursCfg.getColour_Delimiter(),
					//coloursCfg.getColour_Square_Black(),
					coloursCfg.getColour_Square_White(),
					false
					);

			String appName = getResources().getString(adInfo.getName());
			if (appName.length() > 50) {
				appName = appName.substring(0, Math.min(appName.length(), 50));
				appName += " ...";
			}
			buttonarea_text1 =   new TextArea(rectangle_text1, true, appName,
					//coloursCfg.getColour_Delimiter(), coloursCfg.getColour_Square_White(), coloursCfg.getColour_Square_ValidSelection());
					coloursCfg.getColour_Delimiter(),
					coloursCfg.getColour_Square_White());

			String promoText1 = getResources().getString(adInfo.getDescription_Line1());
			if (promoText1.length() > 100) {
				promoText1 = promoText1.substring(0, Math.min(promoText1.length(), 100));
				promoText1 += " ...";
			}
			buttonarea_text2 =  new TextArea(rectangle_text2, true, promoText1,
					//coloursCfg.getColour_Delimiter(), coloursCfg.getColour_Square_White(), coloursCfg.getColour_Square_ValidSelection());
					coloursCfg.getColour_Delimiter(),
					coloursCfg.getColour_Square_White());

			String promoText2 = getResources().getString(adInfo.getDescription_Line2());
			if (promoText2.length() > 100) {
				promoText2 = promoText2.substring(0, Math.min(promoText2.length(), 100));
				promoText2 += " ...";
			}
			buttonarea_text3 =  new TextArea(rectangle_text2, true, promoText2,
					//coloursCfg.getColour_Delimiter(), coloursCfg.getColour_Square_White(), coloursCfg.getColour_Square_ValidSelection());
					coloursCfg.getColour_Delimiter(),
					coloursCfg.getColour_Square_White());
			
			current_text2 = buttonarea_text2;
			
			
			/*if (adInfo.isPaid()) {
				
				buttonarea_type1 =   new TextArea(rectangle_type1, true, getResources().getString(R.string.label_advertising_paid_1),
						coloursCfg.getColour_Delimiter(),
						coloursCfg.getColour_Square_MarkingSelection());

				buttonarea_type2 =  new TextArea(rectangle_type2, true, getResources().getString(R.string.label_advertising_paid_2),
						coloursCfg.getColour_Delimiter(),
						coloursCfg.getColour_Square_MarkingSelection());
				
			} else {
				
				buttonarea_type1 =   new TextArea(rectangle_type1, true, getResources().getString(R.string.label_advertising_free_1),
						coloursCfg.getColour_Delimiter(),
						coloursCfg.getColour_Square_ValidSelection());

				if (adInfo.hasAds()) {
					buttonarea_type2 = new TextArea(rectangle_type2, true, getResources().getString(R.string.label_advertising_free_2),
							coloursCfg.getColour_Delimiter(),
							coloursCfg.getColour_Square_ValidSelection());
				} else {
					buttonarea_type2 =  new TextArea(rectangle_type2, true, getResources().getString(R.string.label_advertising_paid_2),
							coloursCfg.getColour_Delimiter(),
							coloursCfg.getColour_Square_MarkingSelection());
				}
			}*/
			
			
			//initialized = true;
			
			setMeasuredDimension( (int) (rectf_main.right - rectf_main.left), (int) (rectf_main.bottom - rectf_main.top));
			
			System.out.println("BannerView.init: dims=" + (rectf_main.right - rectf_main.left) + ", " + (rectf_main.bottom - rectf_main.top));
			//(new Exception()).printStackTrace();
		//} else {
		//	System.out.println("BannerView.init: already initialized");
			//(new Exception()).printStackTrace();
		//}
	}
	
	
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		
		counter_switch--;
		if (counter_switch <= 0) {
			counter_switch = MAX_COUNTER_SWITCH;
			if (current_text2 == buttonarea_text2) {
				current_text2 = buttonarea_text3;
			} else {
				current_text2 = buttonarea_text2;
			}
		}
		
		super.onDraw(canvas);
		
		paint_background.setColor(coloursCfg.getColour_Delimiter());
		DrawingUtils.drawRoundRectangle(canvas, paint_background, rectf_main);
		
		paint_background.setColor(colour_area);
		DrawingUtils.drawRoundRectangle(canvas, paint_background, rectf_main_inner);
		
		buttonarea_icon.draw(canvas);
		buttonarea_text1.draw(canvas);
		current_text2.draw(canvas);
		//buttonarea_type1.draw(canvas);
		//buttonarea_type2.draw(canvas);
		
		invalidate();
		//System.out.println("View_Achievements_And_Leaderboards_Base> onDraw in rect=" + rectf_main);
	}
	
	
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		if (!rectf_main.contains(x, y)) {
			return false;
		}
		
		synchronized (this) {
			
			int action = event.getAction();

			if (action == MotionEvent.ACTION_DOWN) {

				processEvent_DOWN(event);

			} else if (action == MotionEvent.ACTION_MOVE) {

				processEvent_MOVE(event);
				
			} else if (action == MotionEvent.ACTION_UP
					|| action == MotionEvent.ACTION_CANCEL) {

				processEvent_UP(event);

			}
		}
		
		invalidate();
		
		return true;
	}
	
	
	private void processEvent_DOWN(MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		if (rectf_main.contains(x, y)) {
			colour_area = coloursCfg.getColour_Square_ValidSelection();
			buttonarea_text1.select();
			buttonarea_text2.select();
		} else {
			colour_area = coloursCfg.getColour_Background();
			buttonarea_text1.deselect();
			buttonarea_text2.deselect();
		}
	}
	
	
	private void processEvent_MOVE(MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		if (rectf_main.contains(x, y)) {
			colour_area = coloursCfg.getColour_Square_ValidSelection();
			buttonarea_text1.select();
			buttonarea_text2.select();
		} else {
			colour_area = coloursCfg.getColour_Background();
			buttonarea_text1.deselect();
			buttonarea_text2.deselect();
		}
	}
	
	
	private void processEvent_UP(MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		colour_area = coloursCfg.getColour_Background();
		buttonarea_text1.deselect();
		buttonarea_text2.deselect();
		
		if (click_action != null) {
			click_action.run();
		}
		
		//buttonarea_text1.deselect();
		//buttonarea_text2.deselect();
		
		/*if (isOverButton_OpenLeaderboard(x, y)) {
			
			int modeID = Application_Base.getInstance().getUserSettings().modeID;
			provider.getLeaderboardsProvider().openLeaderboard_LocalOnly(modeID);
			provider.getLeaderboardsProvider().openLeaderboard(modeID);
			
        	IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();
    		eventsManager.register(getContext(),
    				eventsManager.create(IEvent_Base.MENU_OPERATION, IEvent_Base.MENU_OPERATION_ENG_PROV_LEADERBOARDS,
    				"MENU_OPERATION", "ENG_PROV_LEADERBOARDS"));
			
		} else if (isOverButton_OpenAchievements(x, y)) {
			
			provider.getAchievementsProvider().openAchievements();
			
        	IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();
    		eventsManager.register(getContext(),
    				eventsManager.create(IEvent_Base.MENU_OPERATION, IEvent_Base.MENU_OPERATION_ENG_PROV_ACHIEVEMENTS,
    				"MENU_OPERATION", "ENG_PROV_ACHIEVEMENTS"));
		}*/
	}


	public void setClickAction(Runnable click_action) {
		this.click_action = click_action;
	}


	public boolean openTarget() {
		return homeAdsContainer.openTarget(adInfo);
	}
}
