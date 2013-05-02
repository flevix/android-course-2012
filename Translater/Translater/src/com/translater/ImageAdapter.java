package com.translater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
	
public class ImageAdapter extends BaseAdapter {
		
	private Context myContext;
	private final Integer[] myImage = { R.drawable.fish, R.drawable.fish,
			R.drawable.fish, R.drawable.fish, R.drawable.fish, R.drawable.fish,
			R.drawable.fish, R.drawable.fish, R.drawable.fish, R.drawable.fish };
	
	public ImageAdapter(Context context) {
		myContext = context;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return myImage.length;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return myImage[arg0];
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return myImage[arg0];
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ImageView view = new ImageView(myContext);
		view.setImageResource(myImage[arg0]);
		view.setPadding(20, 20, 20, 20);
		view.setLayoutParams(new Gallery.LayoutParams(300, 300));
		view.setScaleType(ImageView.ScaleType.FIT_XY);
		return view;
	}
	
}
