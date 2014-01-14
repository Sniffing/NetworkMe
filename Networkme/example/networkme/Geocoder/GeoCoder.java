package example.networkme.Geocoder;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import example.networkme.Handler.TwitterAPIHandler;
import example.networkme.activities.MainActivity;

import java.io.IOException;
import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.Query;


/**
 * Created by tt1611 on 14/11/13.
 */
public class GeoCoder {

    private Context twitterContext;
    private MainActivity activity;
    TwitterAPIHandler apiHandler;

    // Constructor, we should base class our API handlers or overload constructor
    // e.g GeoCode(MainActivity fragment, InstagramAPIHandler hander)
    // GeoCode(MainActivity fragment, FacebookAPIHandler handler)
    // Better to use base class see other functions in this class
    public GeoCoder(MainActivity activity, TwitterAPIHandler handler){
        this.activity = activity;
        twitterContext = activity.getApplicationContext();
        apiHandler = handler;
    }

    //Called by other classes
    public void runQuery(String[] args){
        GeoCodeTask task = new GeoCodeTask();
        task.execute(args);
    }

    //TODO: STILL YET TO IMPLEMENT
    public void runLocalQuery(String[] args){
        //CurrentGeoLocator task = new CurrentGeoLocator();
        //task.execute(args);

    }


    // Will take a given location name and find a latitude and longitude close
    // to it and then conduct a search task which should be implemented in all APIs
    // Maybe make each API handler extend from a base class? refactoring possibility for next sprint
    private class GeoCodeTask extends AsyncTask<String,Void, GeoLocation> {

        Query query;
        String queryString = "";

        @Override
        protected GeoLocation doInBackground(String... strings) {

            Log.d("DOING AN ASYNC TASK", "GEOCODE TASK");

            double longitude = 0;
            double latitude = 0;

            int numArgs = strings.length;
            Log.d("TESTING NUM ARGS", "NUM ARGS IS: " +numArgs);

            // Set the searched keyword if it has been given
            if (numArgs == 2) {
                queryString = strings[1];
                query = new Query(queryString);
            }else {
                query = new Query("");
            }

            Geocoder locationFinder = new Geocoder(twitterContext);

            try{
                String locationName = strings[0];
                Log.d("TESTING LOCATION STRING","LOCATION NAME IS: " + locationName);
                List<Address> possibleAddresses = locationFinder.getFromLocationName(locationName,10);

                if (possibleAddresses != null && !possibleAddresses.isEmpty()) {
                    int random = (int)(Math.floor(Math.random()) % 10);

                    Address chosenAddress = possibleAddresses.get(random);

                    if (chosenAddress != null) {
                        longitude = chosenAddress.getLongitude();
                        latitude = chosenAddress.getLatitude();
                        Log.e("THE CHOSEN ADDRESS FOR THE ARG WAS: ",chosenAddress.getCountryName() + ", " + chosenAddress.getThoroughfare());
                    }
                }
                else
                {
                    Log.d("SEARCHING A LOCATION TOO GENERAL","ENTER A LOCATION NOT A COUNTRY");
                    longitude = 0; latitude = 0;
                }

            } catch (IOException e)
            {
                longitude = 0; latitude = 0;
            }
            Log.d("THE CHOSEN ADDRESS IS OF", "LATITUDE: "+ latitude + " LONGITUDE: " + longitude);
            activity.setLatitude(latitude);
            activity.setLongitude(longitude);

            GeoLocation location = new GeoLocation(latitude,longitude);
            return location;
        }

        @Override
        protected void onPostExecute(GeoLocation geoLocation) {
            super.onPostExecute(geoLocation);
            apiHandler.doSearchTask(geoLocation, queryString);
        }
    }


    /*
    private class CurrentGeoLocator extends AsyncTask<String, Void,GeoLocation> {
        GeoLocation geoLoc;
        String queryString = "";
        LocationManager locManager;
        LocationListener locationListener;

        @Override
        protected GeoLocation doInBackground(String... strings) {
            double lon = 0;
            double lat = 0;

            queryString = strings[0];

            locManager = (LocationManager) twitterContext.getSystemService(twitterContext.LOCATION_SERVICE);

            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    geoLoc = new GeoLocation(location.getLatitude(),location.getLongitude());
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {}

                public void onProviderEnabled(String provider) {}

                public void onProviderDisabled(String provider) {}
            };

            locManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,locationListener,Looper.myLooper());
            return geoLoc;
        }

        @Override
        protected void onPostExecute(GeoLocation geoLocation) {
            super.onPostExecute(geoLocation);
            apiHandler.doSearchTask(geoLocation, queryString);
        }
    }

*/


}
