package example.networkme.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import com.example.networkme.R;
import com.loopj.android.image.SmartImageView;

import example.networkme.Handler.InstagramObject;
import example.networkme.Handler.SocialMediaObject;
import example.networkme.Handler.TwitterObject;
import example.networkme.activities.MainActivity;

public class ExpandImageFragmentDialog extends DialogFragment{
	SocialMediaObject object;
	
	private final String TAG = "EXPAND_IMAGE_FRAG";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final int position = getArguments().getInt("position");
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		this.object = ((MainActivity)getActivity()).getImageAtIndex(position);
		View view = null;
		String type = object.getType();
		
		//Log.d(TAG,"Is object null" + object.getType());
		
		if (type.equals("twitter")) {
			view = getActivity().getLayoutInflater().inflate(R.layout.pic_click_twitter_dialog, null,false);
			final TextView username = (TextView)view.findViewById(R.id.onclick_twitter_username);
			final TextView time = (TextView)view.findViewById(R.id.onclick_twitter_time);
			final TextView tweet = (TextView)view.findViewById(R.id.onclick_twitter_tweet);
			final SmartImageView image = (SmartImageView)view.findViewById(R.id.onclick_twitter_img);
			dialogBuilder.setTitle("Twitter Image");
			TwitterObject obj = (TwitterObject)object;
			username.setText(obj.getUsername());
			time.setText(obj.getTime());
			tweet.setText(obj.getStatus());
			
			image.setImageUrl(obj.getUrlString());
			//image.setImageBitmap(new WebImage(url.toString()).getBitmap(getActivity()));
			dialogBuilder.setView(view);
		} 
	
		if (type.equals("instagram")) {
			view = getActivity().getLayoutInflater().inflate(R.layout.pic_click_instagram_dialog, null,false);
			final TextView username = (TextView)view.findViewById(R.id.onclick_instagram_username);
			final TextView time = (TextView)view.findViewById(R.id.onclick_instagram_time);
			final TextView location = (TextView)view.findViewById(R.id.onclick_instagram_location);
			final TextView tags = (TextView)view.findViewById(R.id.onclick_instagram_hashtags);
			final SmartImageView image = (SmartImageView)view.findViewById(R.id.onclick_instagram_img);
			dialogBuilder.setTitle("Instagram Image");
			InstagramObject obj = (InstagramObject)object;
			username.setText(obj.getUsername());
			time.setText(obj.getTimeStamp());
			String loc = (obj.getLocation() == null)? "Location " +MainActivity.NOT_AVAILABLE_STRING: obj.getLocation();
			location.setText(loc);
			tags.setText(obj.getFlattenedTags());

			
			image.setImageUrl(obj.getUrlString());
			//image.setImageBitmap(new WebImage(url.toString()).getBitmap(getActivity()));
			dialogBuilder.setView(view);
		}
		
		return dialogBuilder.create();
		
	}

}
