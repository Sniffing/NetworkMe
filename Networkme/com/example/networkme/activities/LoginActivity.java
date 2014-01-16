package example.networkme.activities;

/* I added crashlytics after talking to Ocean Labs
 * about it, we should add MixPanel too if we have the time
 */
//import com.crashlytics.android.Crashlytics;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.networkme.R;

import example.networkme.fragments.LoginFragment;

/**
 * Created by JOSE on 06/11/13.
 */
public class LoginActivity extends FragmentActivity {

    private LoginFragment lgFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Crashlytics.start(this);
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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.settings:
                Intent i = new Intent(this, AppSettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    
    public void pleaseDestroy() {
    	this.finish();
    }
}
