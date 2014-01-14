package example.networkme.fragments;  
    
    
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.networkme.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import example.networkme.Handler.SocialMediaObject;
import example.networkme.activities.MainActivity;
    
/**  
 * Created by Marcel on 11/9/13.  
 */
public class AppMapFragment extends Fragment implements OnMarkerClickListener{  
        
	private SupportMapFragment fragment;
	public static String TAG="MAP";
	private GoogleMap googleMap;  
    Map<Integer,SocialMediaObject> poiMap = new HashMap<Integer,SocialMediaObject>();    
	//Map<String, LatLng> poiMap = new HashMap<String, LatLng>();
    //String [] poiName = new String[4];
    final LatLng LONDON = new LatLng(51.50722, -0.12750);  
    final Context context = getActivity();  
    LatLng myPos;
    int objID = 1;
        
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
    	Log.d(TAG, " on Create View");
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);  
        return rootView;  
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
    }
/*    
    @Override
    public void onStart() {
    	Log.d(TAG, " on Start");
        super.onStart();  
        setUpMap();  
    }  
*/    
    @Override
    public void onResume() {
    	Log.d(TAG, " on Resume");
        super.onResume();  
        setUpMap();
    }  
 
    @Override
    public void onDestroyView() { 
    	Log.d(TAG, " on Destroy View");
        super.onDestroyView(); 
  
        Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.map); 
        if (f!= null){ 
            getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
        }
        googleMap = null;
        objID = 1;
        poiMap.clear();
  
    } 
  
    @Override
    public void onDetach() {
    	Log.d(TAG, " on Detach");
        super.onDetach(); 
  
        Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.map); 
        if (f!= null){ 
            getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit(); 
        }
        googleMap = null;
        objID = 1;
        poiMap.clear();

        		
    } 
    
    private void setUpMap(){
    	Log.d(TAG, " on set up map");
    	if (googleMap == null) {
            googleMap = fragment.getMap();           
            initializePOI();  
            addMarkers();
            //Log.d(TAG, " on setting location");        
            googleMap.setOnMarkerClickListener(this);
            //googleMap.setMyLocationEnabled(true);        
            //Log.d(TAG, "location enabled");        
            //googleMap.getUiSettings().setMyLocationButtonEnabled(true);        
            LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);  
            Criteria criteria = new Criteria();          
            String provider = locationManager.getBestProvider(criteria,true);          
            Location location = locationManager.getLastKnownLocation(provider);          
            if (location!=null) {  
                double latitude = location.getLatitude();  
                double longitude = location.getLongitude();  
                myPos = new LatLng (latitude, longitude);  
               // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 10));         
            } else {  
            // Move the camera instantly to London with a zoom of 15.  
            	googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LONDON, 10));          
            }      
            // Zoom in, animating the camera.  
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null); 
            Log.d(TAG, "set map finished");
        }
    }
    
    private void addMarkers() {  
        // TODO Auto-generated method stub
    	/*
        for (int i =0; i<poiName.length; ++i){  
            googleMap.addMarker(new MarkerOptions()  
                    .position(poiMap.get(poiName[i])).title(poiName[i]));  
        }
        */
    	for (Map.Entry<Integer,SocialMediaObject> entry : poiMap.entrySet()) {
    		int entryID = entry.getKey();
    		SocialMediaObject entryObj = entry.getValue();
    		
    		int random = (int)Math.floor(Math.random());
    		int marker = 0;
    		switch (random % 3) {
    		case 0 : marker = R.drawable.twitter_map_marker; break;
    		case 1 : marker = R.drawable.facebook_map_marker; break;
    		case 2 : marker = R.drawable.instagram_map_marker; break;
    		}
    		
    		googleMap.addMarker(new MarkerOptions()  
            .position(new LatLng(entryObj.getLatitude(), entryObj.getLongitude()))	
            				.title(Integer.toString(entryID))
            				.icon(BitmapDescriptorFactory.fromResource(marker)));
    		Log.d(TAG, Double.toString(entryObj.getLatitude()));
    		Log.d(TAG, Double.toString(entryObj.getLongitude()));
    	}
    	googleMap.addMarker(new MarkerOptions().position(new LatLng(51.4983, -0.1769)).title("IMP").icon(BitmapDescriptorFactory.fromResource(R.drawable.instagram_map_marker)));
    	googleMap.addMarker(new MarkerOptions().position(new LatLng(50.4983, -0.1719)).title("HOPE").icon(BitmapDescriptorFactory.fromResource(R.drawable.facebook_map_marker)));
    	googleMap.addMarker(new MarkerOptions().position(new LatLng(33.4983, 122.1969)).title("IMP2").icon(BitmapDescriptorFactory.fromResource(R.drawable.twitter_map_marker)));
    }  
    
    private void initializePOI() {  
    	
        //TODO Auto-generated method stub
    	/*
        poiName[0] = "London Eye";  
        poiName[1] = "Buckingham Palace";  
        poiName[2] = "Windsor Castle";  
        poiName[3] = "Imperial Wharf";  
              
        poiMap.put(poiName[0], new LatLng(51.5033, -0.1197));  
        poiMap.put(poiName[1], new LatLng(51.501, -0.142));  
        poiMap.put(poiName[2], new LatLng(51.4837, -0.6042));  
        poiMap.put(poiName[3], new LatLng(51.475027,-0.182905));
        */
    	List<SocialMediaObject> mediaList = ((MainActivity) getActivity()).getMediaList().getJointList();
    	for (SocialMediaObject temp : mediaList) {
    		double Lat = temp.getLatitude();
    		double Long = temp.getLongitude();
    		if (Lat == MainActivity.INVALID_LAT_LONG_VALUE || Long == MainActivity.INVALID_LAT_LONG_VALUE ) {
    		} else {
    			poiMap.put(objID, temp);
    			objID++;
    		}
    	}
    	
    	
    }

	@Override
	public boolean onMarkerClick(Marker arg0) {
		//SocialMediaObject temp = poiMap.get(Integer.parseInt(arg0.getTitle()));
		
		return false;
	}  
    
   
}