package example.networkme.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.networkme.R;

import example.networkme.activities.MainActivity;
import example.networkme.views.SquareImageView;

public class SettingsAdapter extends ArrayAdapter<Integer>{	
	private static final String TWITTER_STRING = "Twitter";
	private static final String FACEBOOK_STRING = "Facebook";
	private static final String INSTAGRAM_STRING = "Instagram";
	private static final String AVAILABLE = "Available";
	private static final String NOT_AVAILABLE = "Not logged in";
	private static final String LOG_OUT = "Log out";
	private static final String LOG_IN = "Sign in";
	
	private Context context;
	private List<Integer> apis;
	TextView name;
	TextView status;
	SquareImageView pic;
	
	public SettingsAdapter(Context context, List<Integer> apis) {
		super(context, R.layout.list_row_twitter, apis);
		this.context = context;
		this.apis = apis;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.login_list_item, null, false);
		
		Button authButton = (Button)v.findViewById(R.id.log_in_out_button);
		name = (TextView) v.findViewById(R.id.log_in_platform);
		status = (TextView) v.findViewById(R.id.platform_status);
		pic = (SquareImageView) v.findViewById(R.id.login_media_icon);
				
		
		int api = apis.get(position);
		String text;
		
		switch(api) {
		case MainActivity.TWITTER_API_ID: 
			name.setText(TWITTER_STRING);
			if(MainActivity.TWITTER_LOGGED_IN) {
				text = AVAILABLE; 
				authButton.setText(LOG_OUT);
			} else {
				text = NOT_AVAILABLE;
				authButton.setText(LOG_IN);
			}
			
			status.setText(text);	
			pic.setImageResource(R.drawable.bird_blue_48);
			break;
		case MainActivity.FACEBOOK_API_ID: name.setText(FACEBOOK_STRING);
			if (MainActivity.FACEBOOK_LOGGED_IN) {
				text = AVAILABLE; 
				authButton.setText(LOG_OUT);
			} else {
				text = NOT_AVAILABLE;
				authButton.setText(LOG_IN);
			}
			status.setText(text);
			pic.setImageResource(R.drawable.fb_blue_50);
			break;
		case MainActivity.INSTAGRAM_API_ID: name.setText(INSTAGRAM_STRING);
		 	if (MainActivity.INSTAGRAM_LOGGED_IN) {
		 		text = AVAILABLE; 
				authButton.setText(LOG_OUT);
			} else {
				text = NOT_AVAILABLE;
				authButton.setText(LOG_IN);
			}
			status.setText(text);
			pic.setImageResource(R.drawable.instagram_icon_50);
			break;
		}
	
		return v;
	}
}



