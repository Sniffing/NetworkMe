package example.networkme.Handler;

import java.text.ParseException;
import java.util.Date;

import android.util.Log;

/* Base class for all of the social media data we will get
 * Others will implement their own field call methods.
 * This class enforces the need for latitude and longitude
 * as well as setting the "Media Type" which will be 
 * used to disambiguate the types of data. This is so all
 * the data can be compiled into one list and still
 * be able to be recognised without using "Instance of".
 * 
 * Disambiguation is used to vary the display of the interface.
 */

public abstract class SocialMediaObject  implements Comparable<SocialMediaObject>{
	
	/* Be aware of memory overhead 
	 * Be knowledgeable about the cost and overhead of the language and 
	 * libraries you are using, and keep this information in mind when you 
	 * design your app, from start to finish. Often, things on the surface 
	 * that look innocuous may in fact have a large amount of overhead. 
	 * Examples include:
	 * 
	 * Enums often require more than twice as much memory as static constants. 
	 * You should strictly avoid using enums on Android.
	 * 
	 * Every class in Java (including anonymous inner classes) uses about 500 
	 * bytes of code.
	 * 
	 * Every class instance has 12-16 bytes of RAM overhead.
	 * 
	 * Putting a single entry into a HashMap requires the allocation of an 
	 * additional entry object that takes 32 bytes use sparseArrays.
	 */
	
	public static final int TYPE_FB = 1;
	public static final int TYPE_TWITTER = 2;
	public static final int TYPE_INSTAGRAM = 3;
	private static final String TAG = "SocialMediaObject";
	
	private int objectType;
	private String date;
	private String time;
	private double latitude;
	private double longitude;
	private Date myDate;

	
	public SocialMediaObject(int type, String time, double lat, double lon) {
		this.objectType = type;
		this.latitude = lat;
		this.longitude = lon;
		setTimeAndDate(time);
		myDate = give_me_the_D(time);
	}

	public String getType() {
		
		switch(this.objectType) {
		case 1:
			return "facebook";
		case 2:
			return "twitter";
		case 3:
			return "instagram";
		default:
			return "unknown";
		}
	}
	
	//Not able to think of a better solution, too much
	//coupling unfortunately :(, well I could pass dates but
	//at the moment that is long to do, aint no one got time for dat
	private void setTimeAndDate(String t){
		String[] timeParts = t.split("#");
		//Log.d(TAG,"THE STRING IS: "+t);
		this.time = timeParts[0];
		this.date = timeParts[1];
	}
	
	protected Date give_me_the_D(String t) {
		Date D = null;
		
		try {
			D = SplitList.STANDARD_TIME_FORMATTER.parse(t);
		} catch (ParseException e) {
			Log.d(TAG,"Could not parse the date");
			e.printStackTrace();
		}
		return D;
	}
	
	public String getTime() {
		return time;
	}
	
	public String getDate() {
		return date;
	}
	
	public int getNumeratedType() {
		return this.objectType;
	}
	
	public double getLongitude() {
		return this.longitude;
	}
	
	public double getLatitude() {
		return this.latitude;
	}
	
	public void setLongitude(double lon) {
		this.longitude = lon;
	}
	
	public void setLatitude(double lat) {
		this.latitude = lat;
	}
	
	public Date getDateFormat(){
		return myDate;
	}
	
	//For ordering results
	public boolean happensBefore(SocialMediaObject s) {
		return s.getDateFormat().after(this.myDate);
	}
	
	
	@Override
	public int compareTo(SocialMediaObject another) {
		if(this.happensBefore(another)) {return 1;}
		else if(this.myDate.equals(another.getDateFormat())) {return 0;}
		else return -1;
	}
}
