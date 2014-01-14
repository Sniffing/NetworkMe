package example.networkme.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;


import example.networkme.fragments.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by JOSE on 06/11/13.
 */
public class SettingsActivity extends FragmentActivity {

    private LoginFragment lgFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            PackageInfo info;
            info = getPackageManager().getPackageInfo(
                    "com.facebook.android", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }

        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            lgFragment = new LoginFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, lgFragment).commit();
        } else {
            // Or set the fragment from restored state info
            lgFragment = (LoginFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }

        implementFinish();





    }

    private void implementFinish() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                finish();

            }
        }, 5000);
    }
}
