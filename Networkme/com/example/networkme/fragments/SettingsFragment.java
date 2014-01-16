package example.networkme.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.networkme.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import example.networkme.activities.InstagramLoginActivity;
import example.networkme.activities.LoginActivity;
import example.networkme.activities.MainActivity;
import example.networkme.adapter.SettingsAdapter;
import example.networkme.views.SquareImageView;

/**
 * Created by JOSE on 06/11/13.
 */
public class SettingsFragment extends Fragment implements OnClickListener {
    private LoginButton authButton;
	Button instagramLogin;
	Button twitterLogin;
	ListView listview;
	List<Integer> APIlist;
	private static SquareImageView fbstatus;
	private static SquareImageView twitterstatus;
	private static SquareImageView instagramstatus;

    @Override
    public View onCreateView(LayoutInflater inflater,
                            ViewGroup container,
                            Bundle savedInstanceState) {
    	APIlist = new ArrayList<Integer>();
    	APIlist.add(MainActivity.FACEBOOK_API_ID);
    	APIlist.add(MainActivity.TWITTER_API_ID);
    	APIlist.add(MainActivity.INSTAGRAM_API_ID);
    	
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        listview = (ListView)view.findViewById(R.id.display_list);
        view.setBackgroundColor(Color.parseColor("#404040"));
        listview.setBackgroundColor(Color.parseColor("#404040"));
        
        ListAdapter adapter = new SettingsAdapter(this.getActivity(),APIlist);
        listview.setAdapter(adapter);
        /*
        Log.v("Status:", "oncreateview");
        authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setFragment(this);
        authButton.setReadPermissions(Arrays.asList("user_likes", "user_status", "user_events",
                "friends_events"));
        */
        return view;
    }
	
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            if(this.getActivity() instanceof LoginActivity){
                Intent i = new Intent(getActivity(), MainActivity.class);
                LoginActivity activity = (LoginActivity)(getActivity());
                startActivity(i);
                activity.pleaseDestroy();
                
            }
            MainActivity.FACEBOOK_LOGGED_IN = true;
            //fbstatus.setImageResource(R.drawable.tick);
            Log.d("Status:" , "Logged IN...");
        } else if (state.isClosed()) {
            //queryButton.setVisibility(View.INVISIBLE);
            Log.d("Status:" , "Logged out...");
            MainActivity.FACEBOOK_LOGGED_IN = false;
            //fbstatus.setImageResource(R.drawable.cross);
        } 
    }
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private UiLifecycleHelper uiHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity a = getActivity();
        Log.v("activity", a.getPackageName());
        uiHelper = new UiLifecycleHelper(a, callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_instagram_login:
			Intent i = new Intent(getActivity(),InstagramLoginActivity.class);
			startActivity(i);
			break;
		case R.id.button_twitter_login:
			
			break;
		default:
			break;
		}
		
	}

}
