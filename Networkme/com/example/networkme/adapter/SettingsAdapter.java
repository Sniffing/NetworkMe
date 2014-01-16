package example.networkme.adapter;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.networkme.R;
import com.facebook.widget.LoginButton;

import example.networkme.activities.InstagramLoginActivity;
import example.networkme.activities.MainActivity;
import example.networkme.views.SquareImageView;

public class SettingsAdapter extends ArrayAdapter<Integer>{	
	private static final String TWITTER_STRING = "Twitter";
	private static final String FACEBOOK_STRING = "Facebook";
	private static final String INSTAGRAM_STRING = "Instagram";
	private static final String AVAILABLE = "Available";
	private static final String NOT_AVAILABLE = "Not logged in";
	private static final String SIGN_IN = "Sign in";
	private static final String SIGN_OUT = "Sign out";
	
	private FragmentActivity activity;
	private List<Integer> apis;
	private Context context;
		
	public SettingsAdapter(FragmentActivity a, List<Integer> apis) {
		super(a.getApplicationContext(), R.layout.list_row_twitter, apis);
		this.activity = a;
		this.context = a.getApplicationContext();
		this.apis = apis;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		int api = apis.get(position);
		
		View v = null;
		
		switch(api) {
		case MainActivity.TWITTER_API_ID: 
			v = inflater.inflate(R.layout.login_list_item, null, false);

			Button authButton_twitter = (Button)v.findViewById(R.id.log_in_out_button);
			TextView name_twitter = (TextView) v.findViewById(R.id.log_in_platform);
			TextView status_twitter = (TextView) v.findViewById(R.id.platform_status);
			SquareImageView pic_twitter = (SquareImageView) v.findViewById(R.id.login_media_icon);
			SquareImageView pic_status_twitter = (SquareImageView) v.findViewById(R.id.status_indicator);
			
			name_twitter.setText(TWITTER_STRING);
			pic_twitter.setImageResource(R.drawable.bird_blue_48);
			if (MainActivity.TWITTER_LOGGED_IN) {
				status_twitter.setText(AVAILABLE);
				pic_status_twitter.setImageResource(R.drawable.tick);
				authButton_twitter.setText(SIGN_OUT);
				authButton_twitter.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					}
				});
			} else {
				status_twitter.setText(NOT_AVAILABLE);
				pic_status_twitter.setImageResource(R.drawable.cross);
				authButton_twitter.setText(SIGN_OUT);
			}
			break;
		case MainActivity.FACEBOOK_API_ID: 
			v = inflater.inflate(R.layout.login_list_fb_item, null, false);

			LoginButton authButton_fb = (LoginButton)v.findViewById(R.id.log_in_out_button_fb);
			TextView name_fb = (TextView) v.findViewById(R.id.log_in_platform_fb);
			TextView status_fb = (TextView) v.findViewById(R.id.platform_status_fb);
			SquareImageView pic_fb = (SquareImageView) v.findViewById(R.id.login_media_icon_fb);
			SquareImageView pic_status_fb = (SquareImageView) v.findViewById(R.id.status_indicator_fb);

			name_fb.setText(FACEBOOK_STRING);
			pic_fb.setImageResource(R.drawable.fb_blue_50);
			authButton_fb.setReadPermissions(Arrays.asList("user_likes", "user_status", 
					"user_events", "friends_events"));
			
			if (MainActivity.FACEBOOK_LOGGED_IN) {
				pic_status_fb.setImageResource(R.drawable.tick);
				status_fb.setText(AVAILABLE);
				authButton_fb.setText(SIGN_OUT);
			} else {
				status_fb.setText(NOT_AVAILABLE);
				pic_status_fb.setImageResource(R.drawable.cross);
				authButton_fb.setText(SIGN_IN);
			}
			break;
		case MainActivity.INSTAGRAM_API_ID: 
			v = inflater.inflate(R.layout.login_list_item, null, false);

			Button authButton_instagram = (Button)v.findViewById(R.id.log_in_out_button);
			TextView name_instagram = (TextView) v.findViewById(R.id.log_in_platform);
			TextView status_instagram = (TextView) v.findViewById(R.id.platform_status);
			SquareImageView pic_instagram = (SquareImageView) v.findViewById(R.id.login_media_icon);
			SquareImageView pic_status_instagram = (SquareImageView) v.findViewById(R.id.status_indicator);
			
			name_instagram.setText(INSTAGRAM_STRING);
			pic_instagram.setImageResource(R.drawable.instagram_icon_50);
			if (MainActivity.INSTAGRAM_LOGGED_IN) {
				pic_status_instagram.setImageResource(R.drawable.tick);
				status_instagram.setText(AVAILABLE);
				authButton_instagram.setText(SIGN_OUT);
			} else {
				status_instagram.setText(NOT_AVAILABLE);
				pic_status_instagram.setImageResource(R.drawable.cross);
				authButton_instagram.setText(SIGN_IN);
				authButton_instagram.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent i = new Intent(activity,InstagramLoginActivity.class);
						activity.startActivity(i);
					}
				});
			}
			break;
		}
	
		return v;
	}

/* Attempts a hacky fix. doesnt quite work may return to it if desperate
	public static final void  signInWithInstagram(InstagramLoginActivity a) {
		LayoutInflater inflater = (LayoutInflater)a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		
		View v = inflater.inflate(R.layout.login_list_item, null, false);
		SquareImageView pic_status_instagram = (SquareImageView) v.findViewById(R.id.status_indicator);
		TextView status_instagram = (TextView) v.findViewById(R.id.platform_status);
		Button authButton_instagram = (Button)v.findViewById(R.id.log_in_out_button);
		
		pic_status_instagram.setImageResource(R.drawable.tick);
		authButton_instagram.setText(SIGN_OUT);
		status_instagram.setText(SIGN_OUT);			
	}
	
*/
}



