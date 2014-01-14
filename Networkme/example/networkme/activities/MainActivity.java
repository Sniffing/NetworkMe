package example.networkme.activities;

import com.example.networkme.R;

import example.networkme.Geocoder.Tuple;
import example.networkme.Handler.FacebookData;
import example.networkme.Handler.FacebookHandler;
import example.networkme.Handler.InstagramHandler;
import example.networkme.Handler.TwitterAPIHandler;
import example.networkme.adapter.TabsPagerAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import example.networkme.fragments.InitSearchFragment;
import example.networkme.fragments.SearchFragment;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener// InitSearchFragment.InitSearchListener {
{

    public enum SearchType {
        KEY_WORD,LOCATION,BOTH,NONE
    };

    private boolean textFragmentBool;
    public static example.networkme.Handler.InstagramHandler instagramHandler = new example.networkme.Handler.InstagramHandler();
    FragmentManager fm = getFragmentManager();
    public TwitterAPIHandler twitterHandler;
    public FacebookHandler facebookHandler;
    private TextView textView;
    private double latitude;
    private double longitude;

    String[] textList = new String[5];
    ArrayAdapter<String> textAdapter;

    private ViewPager viewPager;
    private TabsPagerAdapter tpAdapter;
    private ActionBar actionBar;
    private String[] tabTitle = {"Main", "Map", "MashItUp", "Text", "Pics"};

    private String fbDataString;
    private String twitterDataString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        tpAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(tpAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode((ActionBar.NAVIGATION_MODE_TABS));

        latitude = 51.4983;
        longitude = 0.1769;



        for (String tab_name : tabTitle) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int pos) {
                actionBar.setSelectedNavigationItem(pos);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        fbDataString = "";
        twitterDataString = "";

        InitSearchFragment isf = new InitSearchFragment();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.searchFrame, isf);
        ft.commit();

    }


    @Override
    public void onTabReselected(Tab tab, android.app.FragmentTransaction ft){

    }


    @Override
    public void onTabSelected(Tab tab, android.app.FragmentTransaction ft){
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft){

    }

    public InstagramHandler getInstagram() {
        return instagramHandler;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
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
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void changeTextView(FacebookData fbData){

        String fbDataString = getFbDataString();
        String twitterDataString =  getTwitterDataString();

        int i = 0;
        while(fbData.hasNext() ){

            fbDataString += fbData.toString();


            fbData = fbData.getNext();


            i++;
        }

        setFbDataString(fbDataString);



        textView.setText(fbDataString+twitterDataString);
    }

    public void initSearchFragmentButtonClick(){
        SearchFragment sf = new SearchFragment();

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.searchFrame, sf);
        ft.commit();

    }
    public TwitterAPIHandler getTwitterHandler() {
        return twitterHandler;
    }

    public void setTwitterHandler(TwitterAPIHandler t) {
        this.twitterHandler = t;
    }

    public FacebookHandler getFacebookHandler() {
        return facebookHandler;
    }

    public void setFacebookHandler(FacebookHandler f) {
        this.facebookHandler = f;
    }


    public void searchFragmentButtonClick(String keyword, String location) {

        if (textFragmentBool){
            fbDataString = "";
            twitterDataString = "";
            facebookHandler.setFirst(null);

            SearchType s = SearchType.NONE;
            Log.v("SEARCHFRAGMENT", "KeyWorrd" + keyword);
            Log.v("SEARCHFRAGMENT", "Location"+location);
            String fbQuery = keyword;
            if(keyword != null){
                s = SearchType.KEY_WORD;
            }
            if(location != null){
                s = SearchType.LOCATION;
                fbQuery = location;
            }
            if (keyword != null && location != null){
                s = SearchType.BOTH;
                fbQuery = keyword;
            }

            //get the enum

            Tuple<String,String> locArgPair = new Tuple<String, String>(location, keyword);

            TwitterAPIHandler twitterAPIHandler = getTwitterHandler();
            FacebookHandler facebookHandler = getFacebookHandler();

            facebookHandler.executeQuery(fbQuery);
            twitterAPIHandler.searchWithQuery(s,locArgPair);
            Object lat = getLatitude();
            Object lng = getLongitude();

            instagramHandler.getUrlListForLocation(lat, lng);

            InitSearchFragment isf = new InitSearchFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.searchFrame, isf);
            ft.commit();

        }else{
            AlertDialog.Builder altDialog= new AlertDialog.Builder(this);
            altDialog.setMessage("TextFragment not initialised"); // here add your message
            altDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    Toast.makeText(getApplicationContext(), "Go to text fragment ", Toast.LENGTH_LONG).show();
                }
            });
            altDialog.show();

        }


    }
    public void setTextView(TextView textView) {
        this.textView = textView;
    }


    public String getTwitterDataString() {
        return twitterDataString;
    }

    public void setTwitterDataString(String twitterDataString) {
        this.twitterDataString = twitterDataString;
    }

    public String getFbDataString() {
        return fbDataString;
    }

    public void setFbDataString(String fbDataString) {
        this.fbDataString = fbDataString;
    }


    public void setTextFragmentBool(boolean textFragmentBool) {
        this.textFragmentBool = textFragmentBool;
    }





    public void changeTweetList(String[] tweetsArray) {

        String fbDataString = getFbDataString();
        String twitterDataString =  getTwitterDataString();

        if (textList.length <= tweetsArray.length) {
            for(int i = 0; i < textList.length; ++i) {
                textList[i] = tweetsArray[i];
                twitterDataString += tweetsArray[i]+"\n";
            }
        }
        else {
            int i = 0;
            for(; i < tweetsArray.length; ++i) {
                textList[i] = tweetsArray[i];

                twitterDataString += tweetsArray[i]+"\n";
            }
            for(; i < textList.length; ++ i) {
                textList[i] = "No other tweets...";
                twitterDataString += "No other tweets... \n";
            }
        }
        setTwitterDataString(twitterDataString);
        textView.setText(fbDataString+twitterDataString);

    }

    public void showTweetsLoading() {
        String s = "";
        for (int i = 0; i< textList.length; ++i) {
            textList[i] = "Loading Tweet...";
            s = "Loading Tweet... \n";
        }
    }


    public String[] getTextList() {
        return textList;
    }

}
