package com.naosim.buttonstatedrawable;

import android.app.Activity;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ButtonStateDrawableFactory factory = new ButtonStateDrawableFactory(getApplicationContext().getResources());
		Button b = (Button) findViewById(R.id.button1);
		b.setBackgroundDrawable(factory.createFormResId(R.drawable.ic_launcher));

		b = (Button) findViewById(R.id.button2);
		b.setBackgroundDrawable(factory.createFormResId(R.drawable.ic_launcher));

		b = (Button) findViewById(R.id.button3);
		b.setBackgroundDrawable(factory.createFormResId(R.drawable.ic_launcher));

		// change base color
		ImageView iv = (ImageView) findViewById(R.id.imageView1);
		iv.setImageDrawable(factory.createFormResId(R.drawable.white_icon));
		iv.setColorFilter(0xffff0000, Mode.MULTIPLY);
	}
}
