package example.networkme.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.example.networkme.R;

import example.networkme.Geocoder.GeoCoder;
import example.networkme.Geocoder.Tuple;
import example.networkme.Handler.FacebookHandler;
import example.networkme.Handler.FacebookObject;
import example.networkme.Handler.InstagramHandler;
import example.networkme.Handler.InstagramObject;
import example.networkme.Handler.SocialMediaObject;
import example.networkme.Handler.SocialMediaPictureObject;
import example.networkme.Handler.SplitList;
import example.networkme.Handler.TwitterHandler;
import example.networkme.Handler.TwitterObject;
import example.networkme.adapter.TabsPagerAdapter;
import example.networkme.fragments.SearchInProgressFragmentDialog;
import example.networkme.fragments.SearchSettingsBarFragment;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private final String TAG = "Main Activity";
	
	public static final int SEARCH_TYPE_NONE = 0;
	public static final int SEARCH_TYPE_KEYWORD = 1;
	public static final int SEARCH_TYPE_LOCATION = 2;
	public static final int SEARCH_TYPE_BOTH = 3;
	
    public static final double INVALID_LAT_LONG_VALUE = 200;
    public static final String INVALID_LAT_LONG_STRING = "200";
    public static final String NOT_AVAILABLE_STRING = "N/A";
    
    public static boolean TWITTER_LOGGED_IN;
    public static boolean INSTAGRAM_LOGGED_IN;
    public static boolean FACEBOOK_LOGGED_IN;
    
	public static final int TWITTER_API_ID = 0;
	public static final int FACEBOOK_API_ID = 1;
	public static final int INSTAGRAM_API_ID = 2;
    
    //used to check when to sort.
    public static boolean LISTS_FINISHED_FLAG;

    public static final int LIST_PIC = 0;
    public static final int LIST_TEXT = 1;
    public static final int LIST_MASH = 2;
    
	FragmentManager fm = getFragmentManager();
	public TwitterHandler twitterHandler;
	public FacebookHandler facebookHandler;
	public InstagramHandler instagramHandler;
	
	private double latitude;
	private double longitude;
	private double localLat = INVALID_LAT_LONG_VALUE;
	private double localLon = INVALID_LAT_LONG_VALUE;
	
	public SplitList socialMediaList;

	private ViewPager viewPager;
	private TabsPagerAdapter tpAdapter;
	private ActionBar actionBar;
	private List<String> tabTitle = new ArrayList<String>();
	private LocationManager locationManager;
	private LocationListener locListener;
	private SearchInProgressFragmentDialog progressView;

	// radius in miles
	public final int radius = 25;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Holds all the social media
		socialMediaList = new SplitList();
		
		setContentView(R.layout.activity_main);

		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		tpAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(tpAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode((ActionBar.NAVIGATION_MODE_TABS));

		facebookHandler = FacebookHandler.getSingleton(this);
		twitterHandler = TwitterHandler.getSingleton(this);
		instagramHandler = InstagramHandler.getSingleton(this);
		
		FACEBOOK_LOGGED_IN = true;
		INSTAGRAM_LOGGED_IN = false;
		TWITTER_LOGGED_IN = true;		
		 
		
		tabTitle.add("Mash up");
		tabTitle.add("Map");
		tabTitle.add("Text");
		tabTitle.add("Pics");
		
		for (String tab_name : tabTitle) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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
		
		// Sets the location manager to get updates on GPS location
		// maybe change to .requestSingleUpdate(), need to read up
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locListener = new LocalLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locListener);
		
		SearchSettingsBarFragment isf = new SearchSettingsBarFragment();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.searchFrame, isf);
		ft.commitAllowingStateLoss();

	}

	private class LocalLocationListener implements LocationListener {
		/*http://stackoverflow.com/questions/17547426/how-to-show-latitude-and-longitude-instantly-in-android */
		@Override
		public void onLocationChanged(Location location) {
			// Called when a new location is found by the network location provider.
			setCurrentLocation(location);
			locationManager.removeUpdates(this);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.d(TAG,"PROVIDER DISABLED");
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.d(TAG,"PROVIDER ENABLED");
		}
	};
	
		
	private void setCurrentLocation(Location loc) {
		Toast.makeText(this, "FOUND A LOCATION", Toast.LENGTH_SHORT).show();
		localLat = loc.getLatitude();
		localLon = loc.getLongitude();
	}	

	/* Still have to handle the problem of clearing this list. When a search is conducted
	   how much do we want to save? how much do we want to keep? Should we destroy the list 
	   every time? Probably not as the amount of data you will receive will be low. HOWEVER
	   maybe e should as the new search could be totally unrelated and we do not want to keep 
	   that as the user clearly doesn't want to see this results any more(?). Due to the 
	   unreliability of the api returning the same results every time for the same arguments
	   we may want to think of some way to cache results from before?
	 */
	public void executeSearch(String keywords, String location) {
		LISTS_FINISHED_FLAG = false;
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locListener);
		int s = SEARCH_TYPE_NONE;
		
		Log.d(TAG,"Entered keyword and location are: " + keywords + "," + location);
		
		progressView = new SearchInProgressFragmentDialog();
		
		
		
		/*User shouldn't be able to press back, exit conditions are:
		 * Everything is complete (call allComplete())
		 * The search failed
		 */	
		//progressView.setCancelable(false);
		
		progressView.show(getSupportFragmentManager(), "search_progress_dialog");
		
		if (!keywords.equals("") && 
			!location.equals("")) { 		//Both arguments entered by user
			s = SEARCH_TYPE_BOTH;
			geoCoderCall(location,keywords);
		}
		else if (!location.equals("")) {	//Only location entered by user
			s = SEARCH_TYPE_LOCATION;
			geoCoderCall(location,keywords);
		}
		else { 								//Only keyword may or may not be entered by user, local search run
			s = SEARCH_TYPE_KEYWORD;
			if (Double.compare(localLat,INVALID_LAT_LONG_VALUE) == 0) {
				Toast.makeText(this, "Local Geocoordinates are not found, try again later", Toast.LENGTH_SHORT).show();
				searchFail();
			}
			else
				apiCall(localLat, localLon, keywords);
		}

	}
	
	/*Main call, will call to the APIs though if not local, this will be
	  called from the method below, geoCoderCall.
	 */	
	public void apiCall(double lat, double lon, String keywords) {
		Handler timeout_handler = new Handler();
		twitterHandler.doSearchTask(lat, lon, keywords);
		facebookHandler.executeQuery(lat, lon, keywords);
		instagramHandler.searchTagsAndLoc(lat, lon, keywords);
		
		//delays for two minutes before timeout
		/*
		timeout_handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if (twitterHandler.isRunning()) {twitterHandler.cancelTask();}
				if (instagramHandler.isRunning()) {instagramHandler.cancelTasks();} 
				Log.d(TAG,"CANCELLING TASKS, TIMEOUT");
				android.app.DialogFragment dialogFrag = new TimeoutFragmentDialog();
				dialogFrag.show(getFragmentManager(), "search_dialog");
				progressView.timeOut();
			}
		}, 120000);
*/
		//loadDummyData();
		//Toast.makeText(this, "chosen coordinates: " + lat + "," + lon,Toast.LENGTH_SHORT).show();
	}
	
	public void finishInstagram(boolean success) {
		((SearchInProgressFragmentDialog) progressView).instagramCompleted(success);
		if(LISTS_FINISHED_FLAG) {socialMediaList.sortData();}
	}
	
	public void finishTwitter(boolean success) {
		((SearchInProgressFragmentDialog) progressView).twitterCompleted(success);
		if(LISTS_FINISHED_FLAG) {socialMediaList.sortData();}
	}
	
	public void finishFacebook(boolean success) {
		((SearchInProgressFragmentDialog) progressView).facebookCompleted(success);
		if(LISTS_FINISHED_FLAG) {socialMediaList.sortData();}
	}
	
	public void geoCoderCall(String loc, String keywords) {
		Tuple<String, String> locArgPair = new Tuple<String, String>(loc,keywords);
		GeoCoder geocoder = GeoCoder.getInstance(this);
		Log.d(TAG,"In Geocoder call, looking for:" + loc);
		geocoder.runQuery(locArgPair);
	}
	
	public void searchFail() {
		progressView.dismiss();
	}
	
	public void sortMediaLists() {
		socialMediaList.sortData();
	}
	
	public void addToList(List<FacebookObject> fbData) {
		for (FacebookObject f : fbData) {
			socialMediaList.addToTextList(f);
			//Log.d(TAG,"Adding fb object:"+ f.getName());
		}
	}

	public void addDummyInstagram() {
		List<InstagramObject> instagram_list = instagramHandler.getDummyList();
		for (InstagramObject obj : instagram_list) {
			socialMediaList.addToPicsList(obj);
			//Log.d(TAG,"Adding insta object:"+ obj.getUsername());
		}
	}

	public void addDummyTweetPics() {
		List<TwitterObject> pics_list = twitterHandler.makeDummyPics();
		for (TwitterObject obj : pics_list) {
			if (obj.containsPicture()) {
				socialMediaList.addToPicsList(obj);
				//Log.d(TAG,"Adding twitter object:"+ obj.getUsername());
			} else {
				socialMediaList.addToTextList(obj);
			}
		}
	}
	
	public void changeTweetList(List<TwitterObject> tweetsArray) {
		for (TwitterObject t : tweetsArray) {
			if (t.containsPicture())
				socialMediaList.addToPicsList(t);
			else
				socialMediaList.addToTextList(t);
			//Log.d(TAG,"Adding twitter object:"+ t.getUsername());
		}
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
	}

	@Override
	public void onTabSelected(Tab t, FragmentTransaction arg1) {
		viewPager.setCurrentItem(tabTitle.indexOf(t.getText()));
	}
	
	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
	}

	public SplitList getMediaList() {
		return socialMediaList;
	}
	
	public SocialMediaPictureObject getImageAtIndex(int position) {
		return socialMediaList.getFromPicsAtPosition(position);
	}
	
	public SocialMediaObject getTextMediaAtIndex(int position) {
		return socialMediaList.getFromTextAtPosition(position);
	}
	
	public SocialMediaObject getSocialMediaAtIndex(int position) {
		return socialMediaList.getJointList().get(position);
	}
	
	public TwitterHandler getTwitterHandler() {
		return twitterHandler;
	}

	public FacebookHandler getFacebookHandler() {
		return facebookHandler;
	}
	
	public InstagramHandler getInstagramHandler() {
		return instagramHandler;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		return;		
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}
	
	public void loadDummyData(){
		addDummyInstagram();
		finishInstagram(true);
		addDummyTweetPics();
		twitterHandler.dummySearch();
		addToList(facebookHandler.dummySearch());
		finishFacebook(true);
		finishTwitter(true);
	}
	
}
