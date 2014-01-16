package example.networkme.Geocoder;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import example.networkme.activities.MainActivity;

/**
 * Created by tt1611 on 14/11/13.
 */
public class GeoCoder {
	private static GeoCoder singleton = null;

	private final String TAG = "GeoCoder";
	private Context context;
	private MainActivity activity;
	LocationManager locationManager;

	private double currLat;
	private double currLon;
	private String keywords;

	public static GeoCoder getInstance(MainActivity activity) {
		if (singleton == null){
			singleton = new GeoCoder(activity);
			return singleton;
		} else {
			return singleton;
		}
	}
	
	private GeoCoder(MainActivity activity) {
		this.activity = activity;
		currLat = MainActivity.INVALID_LAT_LONG_VALUE;
		currLon = MainActivity.INVALID_LAT_LONG_VALUE;
		context = activity.getApplicationContext();
	}

	// Called Main Activity
	public void runQuery( Tuple<String, String> args) {
		GeoCodeTask task = new GeoCodeTask();
		Log.d(TAG,"Running GeoCodeTask");
		task.execute(args);
	}

	private class GeoCodeTask extends
		AsyncTask<Tuple<String, String>, Void, Void> {

		/*
		 * Pre-condition is that we supply the query which the user entered as a
		 * tuple, the first element is the location, the second is the fully
		 * concatted keywords string.
		 */

		@Override
		protected Void doInBackground(Tuple<String, String>[] query) {
			Tuple<String, String> fullQuery = query[0];
			keywords = fullQuery.getSecond();
			
			
			Geocoder locationFinder = new Geocoder(context);
			try {
				String locationName = fullQuery.getFirst();
				List<Address> possibleAddresses = locationFinder
						.getFromLocationName(locationName, 10);

				if (possibleAddresses != null && !possibleAddresses.isEmpty()) {
					int random = (int) (Math.floor(Math.random()) % 10);
					Address chosenAddress = possibleAddresses.get(random);

					if (chosenAddress != null) {
						currLon = chosenAddress.getLongitude();
						currLat = chosenAddress.getLatitude();
					}
				} else {
					Log.d(TAG,"Location entered is too general (e.g. country typed in)");
					currLat = MainActivity.INVALID_LAT_LONG_VALUE;
					currLon = MainActivity.INVALID_LAT_LONG_VALUE;
				}

			} catch (IOException e) {
				//Defaults to imperial college if failure
				currLat = 51.5081;
				currLon = -0.1769;
				Log.d(TAG, "ERROR!");
				e.printStackTrace();
			}

			Log.d(TAG, "(lat,long) chosen are: (" + currLat + "," + currLon
					+ ")");
			Void _ = null;
			return _;
		}

		@Override
		protected void onPostExecute(Void _) {
			super.onPostExecute(_);
			if ((Double.compare(currLat,MainActivity.INVALID_LAT_LONG_VALUE)==0) && 
					(Double.compare(currLon,MainActivity.INVALID_LAT_LONG_VALUE) == 0))
				Toast.makeText(activity, "Geocoder could not find the location at the moment", Toast.LENGTH_SHORT);
			else
				activity.apiCall(currLat, currLon, keywords);
		}
	}
}
