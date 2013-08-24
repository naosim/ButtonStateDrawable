package com.naosim.buttonstatedrawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

public class FilteredDrawableFacory {
	private Resources mResources;
	private FilteredDrawableFacory.RGBA rgba = new RGBA();
	public FilteredDrawableFacory(Resources resources) {
		mResources = resources;
	}
	
	public Drawable createDrawable(int resId, RGBAFilter filter) {
		Bitmap bmp = BitmapFactory.decodeResource(mResources,
				resId);
		byte[] chunk = bmp.getNinePatchChunk();
		if (!bmp.isMutable()) {
			bmp = bmp.copy(Bitmap.Config.ARGB_8888, true);
		}

		int width = bmp.getWidth();
		int height = bmp.getHeight();

		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for(int i = 0; i < pixels.length; i++) {
			int color = pixels[i];
			rgba = filter.update(rgba.hexColor(color));
			pixels[i] = rgba.hexColor();
		}
		bmp.setPixels(pixels, 0, width, 0, 0, width, height);

		return chunk == null ? new BitmapDrawable(mResources,
				bmp) : new NinePatchDrawable(mResources, bmp,
				chunk, new Rect(), null);
	}

	
	public interface RGBAFilter {
		FilteredDrawableFacory.RGBA update(FilteredDrawableFacory.RGBA color);
	}

	public static class RGBA {
		private int r;
		private int g;
		private int b;
		private int a;

		public RGBA() {
			hexColor(0);
		}
		
		public RGBA(int hexColor) {
			hexColor(hexColor);
		}
		
		public FilteredDrawableFacory.RGBA hexColor(int hexColor) {
			a = (hexColor >>> 24) & 0xff;
			r = (hexColor >>> 16) & 0xff;
			g = (hexColor >>> 8) & 0xff;
			b = hexColor & 0xff;
			return this;
		} 

		public int hexColor() {
			return (a << 24) ^ (r << 16) ^ (g << 8) ^ b;
		}

		public int r() {
			return r;
		}

		public FilteredDrawableFacory.RGBA r(int r) {
			this.r = clipValue(r);
			return this;
		}

		public int g() {
			return g;
		}

		public FilteredDrawableFacory.RGBA g(int g) {
			this.g = clipValue(g);
			return this;
		}

		public int b() {
			return b;
		}

		public FilteredDrawableFacory.RGBA b(int b) {
			this.b = clipValue(b);
			return this;
		}

		public int a() {
			return a;
		}

		public FilteredDrawableFacory.RGBA a(int a) {
			this.a = clipValue(a);
			return this;
		}

		private static int clipValue(int v) {
			return Math.min(Math.max(0, v), 255);
		}

	}
}