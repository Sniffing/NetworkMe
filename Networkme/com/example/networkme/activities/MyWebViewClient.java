package example.networkme.activities;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by JOSE on 15/11/13.
 */
public class MyWebViewClient extends WebViewClient {
    private final MyListener listener;

    public interface MyListener {
        public void onComplete(String token);
    }

    MyWebViewClient(MyListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (url.startsWith("hyperlocalscheme://oauth/callback/instagram/")) {

            String parts[] = url.split("=");
            String token = parts[1];

            listener.onComplete(token);

            return true;
        }
        return false;
    }
}
