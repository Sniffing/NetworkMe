package example.networkme.activities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.networkme.R;

import example.networkme.fragments.LoginFragment;
import example.networkme.fragments.SettingsFragment;
import example.networkme.views.SquareImageView;

public class AppSettingsActivity extends FragmentActivity{
	Button fbLogin;
	Button instagramLogin;
	Button twitterLogin;
	private final String TAG = "APP_SETTINGS_ACTIVITY";
	private SettingsFragment settingFragment;
	
	public AppSettingsActivity(){
	}

	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        try {
	            PackageInfo info;
	            info = getPackageManager().getPackageInfo(
	                    "com.facebook.android", PackageManager.GET_SIGNATURES);
	            for (android.content.pm.Signature signature : info.signatures) {
	                MessageDigest md = MessageDigest.getInstance("SHA");
	                md.update(signature.toByteArray());
	                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	        } catch (PackageManager.NameNotFoundException e) {
	        } catch (NoSuchAlgorithmException e) {
	        }

	        if (savedInstanceState == null) {
	            // Add the fragment on initial activity setup
	            settingFragment = new SettingsFragment();
	            getSupportFragmentManager()
	                    .beginTransaction()
	                    .add(android.R.id.content, settingFragment).commit();
	        } else {
	            // Or set the fragment from restored state info
	            settingFragment = (SettingsFragment) getSupportFragmentManager()
	                    .findFragmentById(android.R.id.content);
	        }






	    }

	
}
