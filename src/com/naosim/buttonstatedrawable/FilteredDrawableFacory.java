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
		private EightBit r = new EightBit();
		private EightBit g = new EightBit();
		private EightBit b = new EightBit();
		private EightBit a = new EightBit();

		public RGBA() {
			hexColor(0);
		}
		
		public RGBA(int hexColor) {
			hexColor(hexColor);
		}
		
		public FilteredDrawableFacory.RGBA hexColor(int hexColor) {
			a.value((hexColor >>> 24) & 0xff);
			r.value((hexColor >>> 16) & 0xff);
			g.value((hexColor >>> 8) & 0xff);
			b.value(hexColor & 0xff);
			return this;
		} 

		public int hexColor() {
			return (a.value() << 24) ^ (r.value() << 16) ^ (g.value() << 8) ^ b.value();
		}

		public EightBit r() {
			return r;
		}

		public EightBit g() {
			return g;
		}

		public EightBit b() {
			return b;
		}

		public EightBit a() {
			return a;
		}
		
		public static class EightBit {
			private int v = 0;
			public void value(int v) {
				this.v = Math.min(Math.max(0, v), 255);
			}
			
			public EightBit devide(int d) {
				value(value() / d);
				return this;
			}
			
			public int value() {
				return v;
			}
		}

	}
}