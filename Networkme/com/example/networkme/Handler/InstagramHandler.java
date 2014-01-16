package example.networkme.Handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.sola.instagram.InstagramSession;
import com.sola.instagram.auth.AccessToken;
import com.sola.instagram.auth.InstagramAuthentication;
import com.sola.instagram.exception.InstagramException;
import com.sola.instagram.model.Media;
import com.sola.instagram.util.PaginatedCollection;

import example.networkme.activities.MainActivity;

/**
 * 
 * @author ben
 */
public class InstagramHandler {
	
	private searchLocTask currentLocTask = null;
	private searchTagsTask currentTagsTask = null;

	private final String TAG = "INSTAGRAM_HANDLER";
	private static InstagramHandler singleton;
	
	private final String CLIENT_SECRET = "8fb3843010dc4066a4169e7f0c90c3a4";
	private final String CLIENT_ID = "11e87e3a24894b5e84711a015816e9e9";

	private String authUrl;
	private InstagramSession session;
	private MainActivity activity;
	private String tokenString;
	
	private boolean HASHTAGS_FLAG = false;
	private boolean LOCATION_FLAG = false;
	private boolean RESULTS_FLAG = false;

	private InstagramHandler(Activity activity) {
		this.activity = (MainActivity) activity;
		try {
			InstagramAuthentication auth = new InstagramAuthentication();

			String authUrl = auth
					.setRedirectUri(
							"hyperlocalscheme://oauth/callback/instagram/")
					.setClientSecret(CLIENT_SECRET)
					.setClientId(CLIENT_ID)
					.getAuthorizationUri();
			String authUrl2 = authUrl.replace("code", "token");
			this.authUrl = authUrl2;
		} catch (InstagramException ex) {
			Logger.getLogger(InstagramHandler.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	public static InstagramHandler getSingleton(Activity activity) {
		if (singleton == null) {
			singleton = new InstagramHandler(activity);
		}
		return singleton;
	}

	public String getAuthorizationUrl() {
		return authUrl;
	}

	public void setToken(String tokenString) {
		AccessToken token = new AccessToken(tokenString);
		session = new InstagramSession(token);
	}

	public void searchTagsAndLoc(double lat, double lon, String tag) {
		
		//Finishes if not logged in
		if(!MainActivity.INSTAGRAM_LOGGED_IN) {
			activity.finishInstagram(false);
			return;
		}
		
		HASHTAGS_FLAG = false;
		LOCATION_FLAG = false;
		RESULTS_FLAG = false;
		
		if (Double.compare(lat, MainActivity.INVALID_LAT_LONG_VALUE) == 0) {
			Log.d(TAG, "No valid location");
		} else {
			LOCATION_FLAG = true;
			currentLocTask = new searchLocTask();
			currentLocTask.execute(new Double[] { lat, lon });
		}

		if (!tag.equals("")) {
			HASHTAGS_FLAG = true;
			currentTagsTask = new searchTagsTask();
			currentTagsTask.execute(tag);
		} else {
			Log.d(TAG, "No valid tag");
		}
	}

	// Updated: serarchTagsTask now supports multiple tags
	// The return type has to be changed to List<Media>
	// due to the limitations of PaginatedCollection<Media>

	private class searchTagsTask extends AsyncTask<String, Void, List<Media>> {
		@Override
		protected List<Media> doInBackground(String... params) {
			List<Media> combinedList = new ArrayList<Media>();
			// Check back if list is actually needed, need clarification on
			// "limitations of paginated collection"
			try {
				for (String tag : params) {
					PaginatedCollection<Media> medialist = searchHashTag(tag);
					Log.d(TAG,
							"The size of the media list is:" + medialist.size());

					// Due to being paginated, the iterator would have called over
					// and over the pages of results, so we limit it to just the
					// size of the first page, which is 20 by default. Default set by
					// Instagram themselves.
					for (int i = 0; i < medialist.size(); ++i) {
						combinedList.add(medialist.get(i));
					}
				}
			} catch (Exception e) {
				Log.d(TAG, "Please check in searchTagsTask");
				e.printStackTrace();
			}
			return combinedList;
		}

		@Override
		protected void onPostExecute(List<Media> result) {
			super.onPostExecute(result);
			String location;
			boolean success = true;
			double lon, lat;

			if (result == null || result.isEmpty()) {
				success = false;
			} else {
				RESULTS_FLAG = true;
				for (Media media : result) {
					Media.Image image = media.getStandardResolutionImage();
					String urlString = image.getUri();

					if (media.getLocation() == null) {
						location = MainActivity.NOT_AVAILABLE_STRING;
						lat = MainActivity.INVALID_LAT_LONG_VALUE;
						lon = lat;
					} else {
						location = media.getLocation().getName();
						lon = media.getLocation().getLongitude();
						lat = media.getLocation().getLatitude();
					}
					
					String time = convertTime(media.getCreatedTimestamp());

					InstagramObject obj = new InstagramObject(urlString, media
							.getUser().getUserName(), location,
							time, media.getTags(), lat,
							lon);

					activity.getMediaList().addToPicsList(obj);
				}
			}
			
			if (!LOCATION_FLAG)
				activity.finishInstagram(success);
			else
				activity.finishInstagram(RESULTS_FLAG);
		}
	}

	//Instagram returns a Unix timestamp, this converts to a standard format for
	//storing, display and comparison
	private String convertTime(String timestamp) {
		Date time = new Date((long)(Integer.parseInt(timestamp))*1000);
		return SplitList.convertDateToString(time);
	}
	
	private PaginatedCollection<Media> searchHashTag(String tag)
			throws Exception {
		return session.getRecentMediaForTag(tag);
	}

	private class searchLocTask extends AsyncTask<Double, Void, List<Media>> {
		@Override
		protected List<Media> doInBackground(Double... params) {
			Double lat = params[0];
			Double lon = params[1];
			List<Media> medialist = null;

			try {
				medialist = searchLocation(lat, lon);
			} catch (Exception e) {
				Log.d(TAG, "Please check in searchLocTask");
				e.printStackTrace();
			}
			return medialist;
		}

		@Override
		protected void onPostExecute(List<Media> result) {
			super.onPostExecute(result);
			boolean success = true;
			String location;
			double lat, lon;

			if (result == null || result.isEmpty()) {
				success= false;
			} else {
				RESULTS_FLAG = true;
				for (Media media : result) {
					Media.Image image = media.getStandardResolutionImage();
					String urlString = image.getUri();

					if (media.getLocation() == null) {
						location = MainActivity.NOT_AVAILABLE_STRING;
						lat = MainActivity.INVALID_LAT_LONG_VALUE;
						lon = lat;
					} else {
						location = media.getLocation().getName();
						lon = media.getLocation().getLongitude();
						lat = media.getLocation().getLatitude();
					}

					String time = convertTime(media.getCreatedTimestamp());
					
					InstagramObject obj = new InstagramObject(urlString, media
							.getUser().getUserName(), location,
							time, media.getTags(), lat,
							lon);

					activity.getMediaList().addToPicsList(obj);
				}
			}
			if(!HASHTAGS_FLAG)
				activity.finishInstagram(success);
			else
				activity.finishInstagram(RESULTS_FLAG);
		}
	}

	private List<Media> searchLocation(double latitude, double longitude)
			throws Exception {

		return session.searchMedia(latitude, longitude, null, null, null);
	}

	public void setTokenString(String tokenString) {
		this.tokenString = tokenString;
	}

	public boolean isLoggedin() {
		return (tokenString != null);
	}
	
	public boolean isRunning() {
		if (currentLocTask == null && currentTagsTask == null)
			return false;
		else if (currentLocTask == null && currentTagsTask != null)
			return (currentTagsTask.getStatus() == AsyncTask.Status.RUNNING);
		else if (currentLocTask != null && currentTagsTask == null)
			return (currentLocTask.getStatus() == AsyncTask.Status.RUNNING);
		else
			return ((currentLocTask.getStatus() == AsyncTask.Status.RUNNING) 
					|| (currentTagsTask.getStatus() == AsyncTask.Status.RUNNING));	
	}
	
	public void cancelTasks() {
		if (currentLocTask == null && currentTagsTask != null)
			currentTagsTask.cancel(true);
		else if (currentLocTask != null && currentTagsTask == null)
			currentLocTask.cancel(true);
		else {
			currentLocTask.cancel(true);
			currentTagsTask.cancel(true);
		}
				
	}
	
	/*********************************************************************/
	/***************************** DUMMY CODE ****************************/
	/*********************************************************************/

	public List<InstagramObject> getDummyList() {
		List<InstagramObject> pics = new ArrayList<InstagramObject>();

		List<String> tagsList1 = new ArrayList<String>();
		List<String> tagsList2 = new ArrayList<String>();
		List<String> tagsList3 = new ArrayList<String>();
		List<String> tagsList4 = new ArrayList<String>();
		tagsList1.add("#LeeYeonHee");
		tagsList1.add("#Korea");
		tagsList1.add("#Actress");

		tagsList2.add("#Krystal");
		tagsList2.add("#Fx");
		tagsList2.add("#JungSister");

		tagsList3.add("#ShinHye");
		tagsList3.add("#Heirs");

		tagsList4.add("#SOJIN");
		tagsList4.add("#Engineer");

		pics.add(new InstagramObject(
				"http://snsddreamz.files.wordpress.com/2012/03/tumblr_ksuquod74d1qzcemao1_500.jpg",
				"Terence", "KOREA", convertTime("1360446400"), tagsList1, 0, 0));
		pics.add(new InstagramObject(
				"http://www4.images.coolspotters.com/photos/887542/f-x-gallery.png",
				"HaHa", "KOREA", convertTime("720446400"), tagsList2, 0, 0));
		pics.add(new InstagramObject(
				"http://i630.photobucket.com/albums/uu26/dramabeans/drama/2012/RM/RM_120/RM120-00694.jpg",
				"Yoona", "KOREA", convertTime("720446400"), tagsList3, 0, 0));
		pics.add(new InstagramObject(
				"http://i1097.photobucket.com/albums/g349/Korean_Dreams_Girls/Park%20SoJin%20-%20Girls%20Day/bth_SoJin285_zps27560f1d.jpg",
				"Sojin", "KOREA", convertTime("720446400"), tagsList4, 0, 0));
		
		return pics;
	}
}
