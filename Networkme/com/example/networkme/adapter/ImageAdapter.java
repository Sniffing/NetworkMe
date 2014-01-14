package example.networkme.adapter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.networkme.R;
import com.loopj.android.image.SmartImageView;

import example.networkme.Handler.InstagramObject;
import example.networkme.Handler.SocialMediaPictureObject;
import example.networkme.Handler.TwitterObject;

/**
 * 
 * @author ben
 */
public class ImageAdapter extends BaseAdapter {

	private final String TAG = "Image_Adapter";
	private Context context;
	private List<SocialMediaPictureObject> picsList;

	public ImageAdapter(Context c, List<SocialMediaPictureObject> picsList) {
		context = c;
		this.picsList = picsList;
	}

	@Override
	public int getCount() {
		return picsList.size();
	}

	@Override
	public SocialMediaPictureObject getItem(int i) {
		return picsList.get(i);
	}
	
	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.grid_pic_layout, parent, false);
		}

		SocialMediaPictureObject obj = picsList.get(position);
		URL url = null;
		
		try {
			url = obj.getUrl();
		} catch (MalformedURLException e) {
			Log.d(TAG,"Shit urls");
			e.printStackTrace();
		}
		int type = obj.getNumeratedType();

		SmartImageView pic = (SmartImageView) view.findViewById(R.id.grid_picture);
		ProgressBar progress = (ProgressBar)view.findViewById(R.id.grid_pic_progress);
		
		progress.setVisibility(View.VISIBLE);
		pic.setVisibility(View.GONE);
		
		TextView text = (TextView) view.findViewById(R.id.grid_text);
		ImageView icon = (ImageView) view.findViewById(R.id.grid_icon);
		
		
		switch (type) {
		//Case 3 is instagram
		case 3:
			InstagramObject instagram_object = (InstagramObject)obj;
			int InstagramColour = Color.parseColor("#BD517fA4");
			text.setBackgroundColor(InstagramColour);
			icon.setImageResource(R.drawable.instagram_icon_50);
			icon.setBackgroundColor(InstagramColour);
			// Make sure that the display is FIXED SIZE add ellipsis and cut off
			text.setText(instagram_object.getFlattenedTags());
			//text.setSelected(true);
			break;
		//Case 2 is twitterpic
		case 2:
			TwitterObject twitter_object = (TwitterObject)obj;
			int twitterColour = Color.parseColor("#BD2A2A2A");
			text.setBackgroundColor(twitterColour);
			icon.setBackgroundColor(twitterColour);
			icon.setImageResource(R.drawable.bird_blue_48);
			text.setText(twitter_object.getUsername() + ": " +twitter_object.getStatus());
			break;
		}

		pic.setImageUrl(url.toString());
		
		//Does this even work wtf
		progress.setVisibility(View.GONE);
		pic.setVisibility(View.VISIBLE);
		
		return view;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

}
