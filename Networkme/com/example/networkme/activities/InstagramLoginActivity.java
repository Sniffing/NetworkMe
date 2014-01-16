package example.networkme.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.example.networkme.R;

import example.networkme.Handler.InstagramHandler;
import example.networkme.adapter.SettingsAdapter;
import example.networkme.fragments.LoginFragment;
import example.networkme.fragments.SettingsFragment;


/**
 * Created by JOSE on 15/11/13.
 */

public class InstagramLoginActivity extends Activity implements MyWebViewClient.MyListener {
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        String authUrl = InstagramHandler.getSingleton(this).getAuthorizationUrl();
        WebView myWebView = (WebView)findViewById(R.id.webview);
        myWebView.setWebViewClient(new MyWebViewClient(this));
        myWebView.loadUrl(authUrl);

    }

    public void onComplete(String tokenString) {
        InstagramHandler.getSingleton(this).setTokenString(tokenString);
        InstagramHandler.getSingleton(this).setToken(tokenString);
        Log.d("INSTAGRAM", "TOKEN STRING:" + tokenString);
        MainActivity.INSTAGRAM_LOGGED_IN = true;
        finish();
    }


}
