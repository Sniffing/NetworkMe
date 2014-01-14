package example.networkme.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.networkme.R;
import com.loopj.android.image.SmartImageView;

import example.networkme.Handler.FacebookObject;
import example.networkme.Handler.InstagramObject;
import example.networkme.Handler.SocialMediaObject;
import example.networkme.Handler.TwitterObject;
import example.networkme.views.SquareImageView;

public class SocialMediaAdapter extends ArrayAdapter<SocialMediaObject> {
	private final Context context;
	private final List<SocialMediaObject> media;

	ProgressBar progress;
	
	public SocialMediaAdapter(Context context, List<SocialMediaObject>media) {
		super(context, R.layout.list_row_twitter, media);
		this.context = context;
		this.media = media;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;
		SocialMediaObject mediaObject = media.get(position);
		
		/*
		 * The code below will check each Social Media Object in the
		 * collection and its type. Based on the type it is, it will recast
		 * to the correct type and perform appropriate field get methods.
		 * This also changes the display of the UI based on where the data 
		 * is coming from.
		 */
		
		//Maybe change the below into a switch case?
		if(mediaObject.getType().equals("twitter")) {
			TwitterObject obj = (TwitterObject)mediaObject;
			if (obj.containsPicture()){
				rowView = inflater.inflate(R.layout.list_row_twitpic, parent,false);
				TextView twitter_username = (TextView) rowView.findViewById(R.id.twitpic_username);
				SquareImageView twitter_pic = (SquareImageView) rowView.findViewById(R.id.twitpic_img);
				TextView twitter_loc = (TextView) rowView.findViewById(R.id.twitpic_location);
				progress = (ProgressBar)rowView.findViewById(R.id.pic_progress_twitpic);
				
				progress.setVisibility(View.VISIBLE);
				twitter_pic.setVisibility(View.GONE);
				
				twitter_username.setText(obj.getUsername());
				twitter_loc.setText(obj.getStatus());
				twitter_pic.setImageUrl(obj.getUrlString());

				//DOES THIS EVEN WORK might have to move into async task
				progress.setVisibility(View.GONE);
				twitter_pic.setVisibility(View.VISIBLE);
				
				return rowView;
			} else {
				rowView = inflater.inflate(R.layout.list_row_twitter, parent, false);
				TextView tweet_text = (TextView) rowView.findViewById(R.id.secondLine);
				TextView tweet_username = (TextView) rowView.findViewById(R.id.titleLine);
				TextView tweet_time = (TextView)rowView.findViewById(R.id.time_created);
				TextView tweet_date = (TextView)rowView.findViewById(R.id.date_created);
				
				tweet_date.setText(obj.getDate());
				tweet_time.setText(obj.getTime());
				tweet_username.setText(obj.getUsername());
				tweet_text.setText(obj.getStatus());
				return rowView;
			}
		} else if (mediaObject.getType().equals("facebook")) {
			FacebookObject obj = (FacebookObject)mediaObject;
			rowView = inflater.inflate(R.layout.list_row_facebook, parent, false);
			TextView event_name = (TextView) rowView.findViewById(R.id.fb_event_name);
			TextView event_loc = (TextView) rowView.findViewById(R.id.fb_event_location);
			TextView event_time = (TextView) rowView.findViewById(R.id.fb_time);
			TextView event_info = (TextView) rowView.findViewById(R.id.fb_event_info);
			
			
			event_name.setText(obj.getName());
			event_loc.setText(obj.getLocation());
			event_time.setText(obj.getStartTime() +" " + obj.getDate() + "  to  " +
								obj.getEndDate() + " " + obj.getEndDate());
			event_info.setText(obj.getDescription());
			
			return rowView;
		} else if (mediaObject.getType().equals("instagram")){ 			
			InstagramObject obj = (InstagramObject)mediaObject;
			rowView = inflater.inflate(R.layout.list_row_instagram, parent, false);
			TextView instagram_tags = (TextView) rowView.findViewById(R.id.instagram_text);
			SquareImageView instagram_pic = (SquareImageView) rowView.findViewById(R.id.instagram_img);
			TextView instagram_loc = (TextView) rowView.findViewById(R.id.instagram_location);
			progress = (ProgressBar) rowView.findViewById(R.id.pic_progress_insta);

			progress.setVisibility(View.VISIBLE);
			instagram_pic.setVisibility(View.GONE);
			
			String hashtags = "";
			for (String tag: obj.getTagsList())
			{
				hashtags += ("#"+tag +" "); 
			}
			
			instagram_tags.setText(hashtags);
			instagram_loc.setText(obj.getLocation());
			instagram_pic.setImageUrl(obj.getUrlString());
			
			progress.setVisibility(View.GONE);
			instagram_pic.setVisibility(View.VISIBLE);
			
			return  rowView;
		} else {
			Log.d("IN SOCIAL MEDIA ADAPTER", "COULD NOT IDENTIFY TYPE OF DATA");
			return null;
		}
	}
	
}
	
