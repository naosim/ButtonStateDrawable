package com.naosim.buttonstatedrawable;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import com.naosim.buttonstatedrawable.FilteredDrawableFacory.RGBA;
import com.naosim.buttonstatedrawable.FilteredDrawableFacory.RGBAFilter;


public class ButtonStateDrawableFactory {
	private Resources mResources;
	private FilteredDrawableFacory mFilteredDrawableFacory;

	public ButtonStateDrawableFactory(Resources resources) {
		mResources = resources;
		mFilteredDrawableFacory = new FilteredDrawableFacory(mResources);
	}
	
	public Drawable createFormResId(int resId) {
		Drawable normal = mResources.getDrawable(resId);
		Drawable disable = mFilteredDrawableFacory.createDrawable(resId, transparentUpdate);
		Drawable tap = mFilteredDrawableFacory.createDrawable(resId, grayUpdate);

		return createDrawable(normal, tap, disable);
	}

	public Drawable createFromColor(int color) {
		Drawable normal = new ColorDrawable(color);
		RGBA rgba = new RGBA(color);
		Drawable tap = new ColorDrawable(grayUpdate.update(rgba)
				.hexColor());
		Drawable tomei = new ColorDrawable(transparentUpdate.update(rgba)
				.hexColor());

		return createDrawable(normal, tap, tomei);
	}
	
	private static Drawable createDrawable(Drawable normal, Drawable tap, Drawable disable) {
		StateListDrawable result = new StateListDrawable();
		result.addState(new int[] { android.R.attr.state_pressed }, tap);
		result.addState(new int[] { android.R.attr.state_focused }, tap);
		result.addState(new int[] { android.R.attr.state_enabled }, normal);
		result.addState(new int[] { -android.R.attr.state_enabled },
				disable);

		return result;
	}

	private RGBAFilter grayUpdate = new RGBAFilter() {

		@Override
		public RGBA update(RGBA color) {
			return color.r(color.r() / 2).g(color.g() / 2).b(color.b() / 2);
		}
	};

	private RGBAFilter transparentUpdate = new RGBAFilter() {

		@Override
		public RGBA update(RGBA color) {
			return color.a(color.a() / 2);
		}
	};
}