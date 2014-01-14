package example.networkme.Handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.MediaEntity;
import twitter4j.Query;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import example.networkme.Geocoder.GeoCoder;
import example.networkme.activities.MainActivity;

/**
 * Created by tt1611 on 06/11/13.
 */

public class TwitterHandler {

	private MainActivity activity;
	private static TwitterHandler singleton;
	GeoCoder myGeocoder;

	public static final String CONSUMER_KEY = "xLTqkBqY5goMgKgSQZM9xg";
	public static final String CONSUMER_SECRET = "PCn78UFzLlJxNFLnhE33GQ66G3DQGzZB9MG8qebWkTU";
	public static final String ACCESS_TOKEN = "1957980702-2ApQS5HTZQc349FgEpCv4wIomExl5Ly8KuZGmVz";
	public static final String ACCESS_SECRET = "usjC0sEscGM4xhDlcinUKNlEDAHz6NfctJ08N7D4Yiw";

	private final String TAG = "TWITTER_HANDLER";
	private final String TWIT_DATE_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";
	
	private SearchTask currentSearchTask = null;
	
	List<Status> statusList = new ArrayList<Status>();

	private TwitterHandler(Activity activity) {
		this.activity = (MainActivity) activity;
	}

	public static TwitterHandler getSingleton(Activity a) {
		if (singleton == null) {
			singleton = new TwitterHandler(a);
		}
		return singleton;
	}

	/*
	 * This is sufficient for now, in the future, since with android guidelines,
	 * we want to avoid creating many objects, the string concatenation and then
	 * manipulation would be better, i.e. joing everything in a specific order
	 * and make your own parser method which splits the tokens up.
	 */
	public void doSearchTask(double lat, double lon, String queryString) {
		
		//If not logged in just fail and return
		if (!MainActivity.TWITTER_LOGGED_IN) {
			activity.finishTwitter(false);
			return;
		}
		
		currentSearchTask = new SearchTask();
		String[] searchTaskArgs;
		if (Double.compare(lat, MainActivity.INVALID_LAT_LONG_VALUE) == 0)
			searchTaskArgs = new String[] {
					MainActivity.INVALID_LAT_LONG_STRING,
					MainActivity.INVALID_LAT_LONG_STRING, queryString };
		else
			searchTaskArgs = new String[] { Double.toString(lat),
					Double.toString(lon), queryString };

		currentSearchTask.execute(searchTaskArgs);
	}

	// PRE: Must take at least 3 Strings, a latitude, longitude and then the
	// search term
	private class SearchTask extends
			AsyncTask<String, Void, List<twitter4j.Status>> {

		@Override
		protected List<twitter4j.Status> doInBackground(String... strings) {
			// Authenticate with account, build request object
			ConfigurationBuilder cb = authWithAccount();
			twitter4j.Twitter twitter = new TwitterFactory(cb.build())
					.getInstance();

			// Lat and Long range is (-90,-180) to (90,180)
			Double latitude = Double.parseDouble(strings[0]);
			Double longitude = Double.parseDouble(strings[1]);
			String fullQuery = concatStringArray(Arrays.copyOfRange(strings, 2,
					strings.length));
			Query query = new Query(fullQuery);
			// Sets max return amount
			//query.setCount(25);
			

			// Run search
			try {
				// Need to stick to these magic numbers
				if ((latitude != MainActivity.INVALID_LAT_LONG_VALUE)
						|| (longitude != MainActivity.INVALID_LAT_LONG_VALUE)) {
					query.setGeoCode(new GeoLocation(latitude, longitude), 25,
							"mi");
				}

				return twitter.search(query).getTweets();

			} catch (TwitterException te) {
				te.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<twitter4j.Status> searchResult) {
			super.onPostExecute(searchResult);
			statusList = searchResult;
			boolean success = true;
			double lat, lon;
			SplitList list = activity.getMediaList();
			int count = 0;

			if (searchResult == null || searchResult.isEmpty()) {
				success = false;
			} else {

				for (twitter4j.Status s : searchResult) {
					TwitterObject obj;
					lat = (s.getGeoLocation() != null) ? s.getGeoLocation()
							.getLatitude()
							: MainActivity.INVALID_LAT_LONG_VALUE;
					lon = (s.getGeoLocation() != null) ? s.getGeoLocation()
							.getLongitude()
							: MainActivity.INVALID_LAT_LONG_VALUE;

					if (s.getMediaEntities() != null) {
						MediaEntity[] entities = s.getMediaEntities();
						for (MediaEntity entity : entities) {
							if (entity.getType().equals("photo")) {
								String url = entity.getMediaURL();
								obj = new TwitterObject(url, s.getText(), s
										.getUser().getScreenName(), convertTime(s
										.getCreatedAt().toString()), s.getUser()
										.getOriginalProfileImageURL(), s
										.getUser().getName(), lat, lon);
								list.addToPicsList(obj);
								++count;
							}
						}
					} else {
						obj = new TwitterObject(s.getText(), s.getUser()
								.getScreenName(), convertTime(s.getCreatedAt().toString()), s
								.getUser().getOriginalProfileImageURL(), s
								.getUser().getName(), lat, lon);
						list.addToTextList(obj);
						++count;
					}
				}
			}
			
			if (count == 0);
				fallBacks();
				activity.finishTwitter(success);

			Log.d(TAG,"THE AMOUNT OF RESULTS IS:" + count);
		}
	}
	
	public boolean isRunning() {
		if (currentSearchTask == null)
			return false;
		else 
			return (currentSearchTask.getStatus() == AsyncTask.Status.RUNNING);	
	}
	
	public void cancelTask() {
		currentSearchTask.cancel(true);

	}

	public ConfigurationBuilder authWithAccount() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthAccessToken(ACCESS_TOKEN);
		cb.setOAuthAccessTokenSecret(ACCESS_SECRET);
		cb.setOAuthConsumerKey(CONSUMER_KEY);
		cb.setOAuthConsumerSecret(CONSUMER_SECRET);
		cb.setDebugEnabled(true);
		return cb;
	}

	private String convertTime(String twit_time) {
		SimpleDateFormat format = new SimpleDateFormat(TWIT_DATE_FORMAT);
		Date d = null;
		try {
			d = format.parse(twit_time);
		} catch (ParseException e) {
			Log.d(TAG, "Couldn't parse twitter date");
			e.printStackTrace();
		}
		return SplitList.STANDARD_TIME_FORMATTER.format(d);
	}
	
	public String concatStringArray(String[] stringArray) {
		int size = stringArray.length;
		String concattedString = new String();

		if (size == 1)
			return stringArray[0];

		for (int i = 0; i < size; ++i) {
			concattedString += stringArray[i] + " ";
		}

		// Log.d(TAG,"Concat string is:" + concattedString);
		return concattedString;
	}

	/* Use the following when the API overpolls */

	/*********************************************************************/
	/***************************** DUMMY CODE ****************************/
	/*********************************************************************/
	
	public List<TwitterObject> makeDummyPics() {
		List<TwitterObject> picsList = new ArrayList<TwitterObject>();
		picsList.add(new TwitterObject(
				"http://www.soompi.com/wp-content/uploads/a/9/a6/474670/474670.jpg",
				"Got a new pic for of the lovely",
				"Terence",
				convertTime("Thu Oct 01 19:36:17 +0500 2013"),
				"http://upload.enewsworld.net/News/Contents/20130704/34941611.jpg",
				"Mike", 0, 0));
		picsList.add(new TwitterObject(
				"http://2.bp.blogspot.com/-gAO3gehN4HA/TyLJ-34N6kI/AAAAAAAAALY/oGfI9nJE3VM/s1600/1111.png",
				"Filming for another RM ep! #Betrayers",
				"HaHa",
				convertTime("Tue Nov 21 13:00:00 +0000 2013"),
				"http://www.soompi.com/wp-content/uploads/2013/09/GoAra_voguegirl.jpg",
				"Frank", 0, 0));
		picsList.add(new TwitterObject(
				"http://p.twimg.com/A7sTLbvCQAAXd3w.jpg:large",
				"Miss Mung looking mong as always", "Song Jihyo", convertTime("Wed Jan 14 10:05:00 -0700 2013"),
				"http://asianwiki.com/images/6/6f/Jung_Eun-Ji-p1.jpg",
				"Elliott", 0, 0));
		picsList.add(new TwitterObject(
				"http://hanhyojooworld.files.wordpress.com/2012/12/121216-c2b7c2b1c2b4c397c2b8c3a7-e124-c2bcc2b1c3a5c3a3-c2bfc3b5c3a0c3a7-c3a0c3bcc3a0c3af-hdtv-720p-x264-aac-mp4-random-mp4_204133.jpg?w=560&h=537",
				"Arumdapda",
				"Han Hyo Joo",
				convertTime("Sun Nov 22 14:39:19 +0200 2013"),
				"http://kpoplovely.files.wordpress.com/2009/10/0-833803001251134266.jpg",
				"Keith", 0, 0));
		return picsList;
	}

	public void dummySearch() {
		List<TwitterObject> dummytweets = new ArrayList<TwitterObject>();
		dummytweets
				.add(new TwitterObject(
						"And their words to the root and the rock would echo down, down and the magic would hear and answer, faint as a falling butterfly.",
						"Terence",
						convertTime("Sat Dec 29 07:44:07 +0200 2013"),
						"http://dzpy14fd0up32.cloudfront.net/wp-content/uploads/2013/10/Son-Na-eun.jpg",
						"Kevin", 54.5081, 0.0811));
		dummytweets
				.add(new TwitterObject(
						"Because the API will probably overpoll",
						"Jose",
						convertTime("Thu Dec 25 00:00:55 +0000 2013"),
						"http://images5.fanpop.com/image/photos/28400000/-yoo-jae-suk-28463812-500-676.jpg",
						"Yang", 52.5031, 0.0844));
		dummytweets
				.add(new TwitterObject(
						"@Jose So this is indeed a better option",
						"Terence",
						convertTime("Wed Nov 12 19:44:30 +0100 2013"),
						"http://www.soompi.com/wp-content/uploads/2013/09/GoAra_voguegirl.jpg",
						"Arnold", 51.3081, 0.0900));
		dummytweets
				.add(new TwitterObject(
						"Hurry the F*** up guys",
						"Fionn",
						convertTime("Mon Oct 05 12:31:55 +0000 2013"),
						"http://www.soompi.com/wp-content/uploads/2013/11/Lim-Kim-Goodbye-20-main-800x450.png",
						"Aziz", 50.5081, 0.0799));
		dummytweets
				.add(new TwitterObject(
						"@Fionn Well, it could be worse",
						"Charlie",
						convertTime("Thu Oct 06 19:36:17 +0000 2013"),
						"http://star.koreandrama.org/wp-content/uploads/2011/01/Lee-Ji-Eun-4.jpg",
						"Chang", 51.5081, 0.0878));

		activity.changeTweetList(dummytweets);
	}
	
	
	/****************************************************************************************/
	/************************ FOR PRESENTATION, JUST IN CASE ********************************/
	/****************************************************************************************/
	public void fallBacks() {
		TwitterObject a = new TwitterObject("What's new blog: Homo 'arrivus' http://www.nhm.ac.uk/natureplus/blogs/whats-new/2014/01/13/homo-arrivus … < " +
				"A Neanderthal and early Homo sapiens cause a stir ahead " +
				"of http://www.nhm.ac.uk/britainmillionyears …", "NHM_London", convertTime("Mon Jan 13 16:34:22 +0000 2014"),
				"https://pbs.twimg.com/profile_images/1124196071/natural-history-museum.jpg", "NaturalHistoryMuseum", 51.4960, 0.1764);
		
		activity.socialMediaList.addToTextList(a);
	}
}
