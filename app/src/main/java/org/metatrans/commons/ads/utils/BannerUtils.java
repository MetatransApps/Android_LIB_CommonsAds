package org.metatrans.commons.ads.utils;


import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;


public class BannerUtils {


	public static final int AD_BANNER_VIEW_ID = 12345;//R.id.ads_view;
	public static final int AD_BANNER_LAYOUT_ID = 1234567;//R.id.ads_view;

	public static final LinearLayout createView(Context activity, View view, int gravity) {

		//gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

		//Root linear layout
		LinearLayout root = new LinearLayout(activity);

		root.setId(AD_BANNER_LAYOUT_ID);
		root.setGravity(gravity);
		root.setOrientation(LinearLayout.HORIZONTAL);
		//root.setPadding(3, 3, 3, 3);

		LinearLayout.LayoutParams root_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		//root_params.weight = 1;
		root_params.gravity = gravity;

		root.setLayoutParams(root_params);

		//Admob view layout
		LinearLayout.LayoutParams view_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(view_params);

		root.addView(view);

		return root;
	}
}
