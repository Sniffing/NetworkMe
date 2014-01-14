package example.networkme.Handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

import example.networkme.activities.MainActivity;

/**
 * Created by JOSE on 13/11/13.
 */
public class FacebookHandler {
	
	private boolean searchInProgress;
	
    private final MainActivity activity;
    private static FacebookHandler singleton;
    private final String TAG = "FACEBOOK_HANDLER";
    private final String FB_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private final String FB_LOCAL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    
    private FacebookHandler(Activity activity){
        this.activity = (MainActivity) activity;
    }

    public static FacebookHandler getSingleton(Activity a) {
    	if (singleton == null) {
    		singleton = new FacebookHandler(a);
    	}
    	return singleton;
    }
    
    private String getToday() {
    	Date today = new Date();
    	SimpleDateFormat fb_formatter = new SimpleDateFormat(FB_DATE_FORMAT);
    	return fb_formatter.format(today); 
    }
    
    private String getMonthInFuture() {
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.MONTH,1);
    	SimpleDateFormat fb_formatter = new SimpleDateFormat(FB_DATE_FORMAT);
    	return fb_formatter.format(cal.getTime());
    }
    
    public void executeQuery(double lat, double lon, String query){
    	
    	// fails automaticaly if not logged in
    	if(!MainActivity.FACEBOOK_LOGGED_IN){
    		activity.finishFacebook(false);
    		return;
    	}
    	
    	String fqlQuery;
    	Log.d(TAG, "QUERY HERE IS: "+ query);
    	
    	if (query.equals("")) {
    		fqlQuery = searchLocation(lat, lon);
    	}
    	else if (Double.compare(lat, MainActivity.INVALID_LAT_LONG_VALUE) == 0) {
    		fqlQuery = searchKeyWords(query);
    		Log.d(TAG, "INVALID LAT LONG HERE FROM FB HANDLER");
    	} else {
    		fqlQuery = searchBoth(query, lat, lon);
    	}
    	
		Log.d(TAG, fqlQuery);
		
        Bundle params = new Bundle();
        params.putString("q", fqlQuery);
        Session session = Session.getActiveSession();
        Request request = new Request(session,
                "/fql",
                params,
                HttpMethod.GET,
                new Request.Callback(){
                    public void onCompleted(Response response) {
                    	Log.d(TAG,"Response was completed in this time");
                        parse(response);       
                        return;
                    }
                });
        
        searchInProgress = true;
        
        Request.executeBatchAsync(request);
        Log.d(TAG,"Reponse sent out now");
	}
    
    private String searchBoth(String q, double lat, double lon) {
    	String now_string = getToday();
    	String month_future_string = getMonthInFuture();
        float offsetLat = getOffsetLat();
        float offsetLon = getOffsetLon(lon);
        
    	String fql = "SELECT name, description, location,venue, start_time, host, end_time, " +
				"pic_cover, attending_count " +
				"FROM event" +
				//" WHERE (eid IN " +
				//"(SELECT eid FROM event_member WHERE uid IN" + 
				//"(SELECT uid2 FROM friend WHERE uid1 = me()) OR uid = me())) " +
				"WHERE" +
				"AND " +
				" contains('"+q+"') " +
				//"AND venue.longitude < ('"+lon+"' + '"+offsetLon+"') " +
				//"AND venue.latitude <  ('"+lat+"' + '"+offsetLat+"')  " +
				//"AND venue.longitude >  ('"+lon+"' - '"+offsetLon+"') " +
				//"AND venue.latitude >  ('"+lat+"' - '"+offsetLat+"') " +
				"AND start_time > '" + now_string + "' " +
				"AND start_time < '" + month_future_string + "' " +
				"ORDER BY start_time DESC LIMIT 25";
    	return fql;
    }
    
    
    private String searchKeyWords(String query) {
    	String now_string = getToday();	
    	String month_future_string = getMonthInFuture();
		String fql = "SELECT name, description, location,venue, start_time, host, end_time, " +
				"pic_cover, attending_count " +
				"FROM event" +
				" WHERE ((eid IN " +
				"(SELECT eid FROM event_member WHERE uid IN" + 
				"(SELECT uid2 FROM friend WHERE uid1 = me()) OR uid = me())) " +
				"AND (contains('"+query+"')) " +
				"AND start_time > '" + now_string + "' " +
				"AND start_time < '" + month_future_string + "' " +
				"ORDER BY start_time DESC LIMIT 25";
		return fql;
    }
    
    private String searchLocation(double lat, double lon) {
    	String now_string = getToday();
    	String month_future_string = getMonthInFuture();
        float offsetLat = getOffsetLat();
        float offsetLon = getOffsetLon(lon);
        
    	String fql = "SELECT name, description, location,venue, start_time, host, end_time, " +
				"pic_cover, attending_count " +
				"FROM event" +
				" WHERE (eid IN " +
				"(SELECT eid FROM event_member WHERE uid IN" + 
				"(SELECT uid2 FROM friend WHERE uid1 = me()) OR uid = me())) " +
				//"AND venue.longitude < ('"+lon+"' + '"+offsetLon+"') " +
				//"AND venue.latitude <  ('"+lat+"' + '"+offsetLat+"')  " +
				//"AND venue.longitude >  ('"+lon+"' - '"+offsetLon+"') " +
				//"AND venue.latitude >  ('"+lat+"' - '"+offsetLat+"') " +
				"AND start_time > '" + now_string + "' " +
				"AND start_time < '" + month_future_string + "' " +
				"ORDER BY start_time DESC LIMIT 25";
    	return fql;
    }

	private void parse(Response response) {
		boolean success = false;
        GraphObject graphObject = response.getGraphObject();
        
        if (graphObject != null) {
            JSONObject jsonObject = graphObject.getInnerJSONObject();
            try {
                JSONArray array = jsonObject.getJSONArray("data");
                //Log.d(TAG,"RESPONSE STRING:" + array.toString());
                
                for (int i = 1; i < array.length(); i++) {
                    String name, description, start_time, location; 
                    String end_time, host, picture_url;
					int attending;
                    double longitude = 0;
                    double latitude =0;
                    
                    JSONObject object = (JSONObject) array.get(i);
                    
                    if (object.get("venue") instanceof JSONArray) {
                    	//indicates empty
                    	//Log.d(TAG,"WAS INSTANCE OF JSON ARRAY");
                    } else {
                    	JSONObject venue = (JSONObject) object.get("venue");
                    	latitude = (venue.isNull("latitude")) ? MainActivity.INVALID_LAT_LONG_VALUE: Double.parseDouble(venue.getString("latitude"));
                        longitude = (venue.isNull("longitude")) ? MainActivity.INVALID_LAT_LONG_VALUE: Double.parseDouble(venue.getString("longitude"));
                    }
                    
                    if (!object.isNull("pic_cover")) {
                    	JSONObject cover_pic =  (JSONObject) object.get("pic_cover");
                    	picture_url = cover_pic.getString("source");
                    } else {
                    	picture_url = MainActivity.NOT_AVAILABLE_STRING;
                    }
                    
                    name = object.getString("name");
                    description = object.getString("description");
                    location = object.isNull("location") ? MainActivity.NOT_AVAILABLE_STRING : object.getString("location");
                    start_time = object.getString("start_time");
                    host = object.getString("host");
                    end_time = object.getString("end_time");
                    attending = object.getInt("attending_count");
                    
                    FacebookObject obj = new FacebookObject(name, location, description, 
                    		convertTime(start_time),convertTime(end_time),host,
                    		picture_url,attending, latitude, longitude);
                   
                    success = true; //at least one result
                    
                    Log.d(TAG,"Adding a new object:" + obj.getName());
                    activity.getMediaList().addToTextList(obj);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
        	Log.d(TAG,"Null graph object");
        }
        searchInProgress = false;
        activity.finishFacebook(success);
    }
    
	private float getOffsetLat() {
		float ans;
		int deg = 69;
		ans = activity.radius / deg;
		return ans;
	}
	
	private float getOffsetLon(double longitude) {
		float ans;
		float deg = (float) (112*Math.cos(longitude)*0.62);
		ans = activity.radius / deg;
		return ans;
	}
	
	private String convertTime(String fb_time){
		SimpleDateFormat format = new SimpleDateFormat(FB_DATE_FORMAT);
		SimpleDateFormat alt_format = new SimpleDateFormat(FB_LOCAL_FORMAT);
		Date fb_date = null;
		
		try {
			fb_date = format.parse(fb_time);
		} catch (ParseException e) {
			Log.d(TAG,"Trying the other parse");
			try {
				fb_date = alt_format.parse(fb_time);
			} catch (ParseException e1) {
				fb_date = new Date();  // If nothing, return today this is another dirty fix
			}
			//e.printStackTrace();
		}
		
		return SplitList.STANDARD_TIME_FORMATTER.format(fb_date);
	}

	public boolean isRunning() {
		return searchInProgress;
	}
	

	
	/*********************************************************************/
	/***************************** DUMMY CODE ****************************/
	/*********************************************************************/
	
    public void executeDummyQuery() {
    	activity.addToList(dummySearch());
    }
    
    public List<FacebookObject> dummySearch() {
    	List<FacebookObject> dummyList = new ArrayList<FacebookObject>();
		dummyList.add(new FacebookObject("IMPERIAL", "Beit Hall",
				"Clubbing at Metric.. sigh", convertTime("2013-07-26T19:00:00+0000"), convertTime("2013-07-26T23:30:00+0000"), null, MainActivity.NOT_AVAILABLE_STRING, 1248,
				0, 0));
		dummyList.add(new FacebookObject("APPOINTMENT!", "Ocean Labs Offices",
				"OceanLabs appointment	", convertTime("2013-06-20T14:00:00+0200"), convertTime("2013-06-21T00:00:00+0200"), null, MainActivity.NOT_AVAILABLE_STRING, 34, 0, 0));
		dummyList.add(new FacebookObject("FINAL PRESENTATION", "HUXLEY",
				"DEADLINE 7th JANUARY", convertTime("2014-01-01T03:00:00+0100"),convertTime("2014-01-01T13:00:00+0100"), null, MainActivity.NOT_AVAILABLE_STRING, 503, 0, 0));
		dummyList.add(new FacebookObject("FACADES UNITED", "Fabric",
				"This is a fake event, so we don't overpoll the API!", convertTime("2013-08-30T22:00:00-0400"),
				convertTime("2013-08-30T22:05:00-0400"), null, MainActivity.NOT_AVAILABLE_STRING, 16, 0, 0));
		dummyList
				.add(new FacebookObject(
						"A fifth event", "University of hell",
						"This description will be longer, also please change how the arrays work, " +
						"possibly change them into lists  because size unknown. In addition let " +
						"me just place in here any random thoughts at the point of writing this. " +
						"First of all, How are we going too achieve an effective cache of the bitmaps " +
						"of all the pictures? I guess we can store a list again for O(1) access, or " +
						"maybe use a hashmap? But that would require finding a key which I suppose could" +
						" be a number from 1..50. There are also many Guidelines that a developer has to " +
						"follow when using the brands of social media sites. Facebook's requirements are so" +
						" long I decided against reading it. Thi project does have scope to be developed but " +
						"there will probably be no real time component at the date of completion. I'm also" +
						" thinking about a datastructure which will avoid duplication of data. I cannot " +
						"express it in java code possibly due too my limited knowledge and googling abilities. " +
						"It needs to be a list which is in fact 3 lists. The main list contains all objects that " +
						"are some derivation of a base class. Each element then needs to ALSO belong to another " +
						"list based on its actual class. The lists should be implemented as array lists as to" +
						" take the benefit of the O(1) access time. This means that if I wanted ONLY those of list " +
						"one,, then i can access in O(1), similarly with those in list two. However, this was " +
						"with the main goal of having the WHOLE list as O(1) as well. I can think of some solution " +
						"to this using the C language but in Java I've had to stick with duplicating list objects. " +
						"While this should not be a problem at the moment due to a limited 'n' of total objects " +
						"being present in the memory at one time. I'd like to implement this data structure to " +
						"improve efficiency of memory AND of retrieval. I need to know how Java implements its " +
						"array list such that O(1) access is preserved. Probably through a dynamic array but even " +
						"then I cannot add objects into multiple arrays without duplication? Wait a minute, maybe I " +
						"can. Since java uses references when it wants to use objects! There is hope after all! " +
						"You can think of this data structure as two intertwined lists such that their elements " +
						"line up neatly together such that it looks like there is a third list(which there should be).",
						convertTime("2013-12-13T11:00:00+0000"),convertTime("2013-12-13T18:00:00+0000"),null,
						"http://www.mtviggy.com/wp-content/uploads/2012/03/apink-feature-2-copy.png",
						50252, 0, 0));
		
		return dummyList;
    }

}
