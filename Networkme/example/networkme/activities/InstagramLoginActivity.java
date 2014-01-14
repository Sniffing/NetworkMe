package example.networkme.activities;

import com.example.networkme.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import example.networkme.activities.*;


/**
 * Created by JOSE on 15/11/13.
 */

public class InstagramLoginActivity extends Activity implements MyWebViewClient.MyListener {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        String authUrl2 = MainActivity.instagramHandler.getAuthorizationUrl();
        WebView myWebView = (WebView)findViewById(R.id.webview);
        myWebView.setWebViewClient(new MyWebViewClient(this));
        myWebView.loadUrl(authUrl2);

    }

    public void onComplete(String tokenString) {

        MainActivity.instagramHandler.setTokenString(tokenString);
        MainActivity.instagramHandler.setToken(tokenString);
        finish();
    }


}
