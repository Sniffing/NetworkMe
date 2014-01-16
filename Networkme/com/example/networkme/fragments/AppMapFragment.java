package example.networkme.fragments;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import example.networkme.Handler.FacebookObject;
import example.networkme.Handler.InstagramObject;
import example.networkme.Handler.SocialMediaObject;
import example.networkme.Handler.TwitterObject;
import example.networkme.activities.MainActivity;

/**
 * Created by Marcel on 11/9/13.
 */
public class AppMapFragment extends Fragment implements OnMarkerClickListener {

	private SupportMapFragment fragment;
	private final int TITLE_LENGTH = 35;
	public static String TAG = "MAP";
	private GoogleMap googleMap;

	/*
	 * Debate over using sparse array, apparently it is more space efficient,
	 * but time complexity of retrieval is equivalent to binary search O(logn)
	 * while hashmap remains O(1). This seems okay but the line:
	 * "For containers holding up to hundreds of items, the performance difference is not significant, less than 50%."
	 * in the android documentation scares me , we have at max 75 items after
	 * one search. This may be extended in the future, so stick with hashmap
	 */
	Hashtable<Marker, Integer> MarkerIndexMap = new Hashtable<Marker,Integer>();
	
	final LatLng LONDON = new LatLng(51.50722, -0.12750);
	final Context context = getActivity();
	LatLng myPos;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, " on Create View");
		View rootView = inflater.inflate(R.layout.fragment_map, container,
				false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FragmentManager fm = getChildFragmentManager();
		fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
		if (fragment == null) {
			fragment = SupportMapFragment.newInstance();
			fm.beginTransaction().replace(R.id.map, fragment)
					.commitAllowingStateLoss();
		}
	}

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

		Fragment f = getActivity().getSupportFragmentManager()
				.findFragmentById(R.id.map);
		if (f != null) {
			getActivity().getSupportFragmentManager().beginTransaction()
					.remove(f).commitAllowingStateLoss();
		}
		googleMap = null;
		MarkerIndexMap.clear();

	}

	@Override
	public void onDetach() {
		Log.d(TAG, " on Detach");
		super.onDetach();

		Fragment f = getActivity().getSupportFragmentManager()
				.findFragmentById(R.id.map);
		if (f != null) {
			getActivity().getSupportFragmentManager().beginTransaction()
					.remove(f).commitAllowingStateLoss();
		}
		googleMap = null;
	    MarkerIndexMap.clear();

	}

	private void setUpMap() {
		Log.d(TAG, " on set up map");
		if (googleMap == null) {
			googleMap = fragment.getMap();

			addMarkers();
			// Log.d(TAG, " on setting location");
			googleMap.setOnMarkerClickListener(this);
			// googleMap.setMyLocationEnabled(true);
			// Log.d(TAG, "location enabled");
			// googleMap.getUiSettings().setMyLocationButtonEnabled(true);

			LocationManager locationManager = (LocationManager) getActivity()
					.getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			String provider = locationManager.getBestProvider(criteria, true);
			Location location = locationManager.getLastKnownLocation(provider);

			if (location != null) {
				double latitude = location.getLatitude();
				double longitude = location.getLongitude();
				myPos = new LatLng(latitude, longitude);
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos,
						10));
			} else {
				// Move the camera instantly to London with a zoom of 15.
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LONDON,
						10));
			}
			// Zoom in, animating the camera.
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
			// Log.d(TAG, "set map finished");
		}
	}

	private void addMarkers() {
		List<SocialMediaObject> medialist = ((MainActivity)getActivity()).getMediaList().getJointList();
		
		/* Since there is need for list index lookup, uses a counted for loop
		 * as it is 3X faster since we use array list (developer.android.com/training/articles/perf-tips.html)
		 */
		for (int i=0; i<medialist.size(); ++i) {
			SocialMediaObject obj = medialist.get(i);
			BitmapDescriptor marker_Type = BitmapDescriptorFactory
					.defaultMarker();

			switch (obj.getNumeratedType()) {
			case SocialMediaObject.TYPE_FB:
				marker_Type = BitmapDescriptorFactory
						.fromResource(R.drawable.facebook_map_marker);
				break;
			case SocialMediaObject.TYPE_INSTAGRAM:
				marker_Type = BitmapDescriptorFactory
						.fromResource(R.drawable.instagram_map_marker);
				break;
			case SocialMediaObject.TYPE_TWITTER:
				marker_Type = BitmapDescriptorFactory
						.fromResource(R.drawable.twitter_map_marker);
				break;
			}

			// Invalid means do not plot
			if (Double.compare(obj.getLatitude(),
					MainActivity.INVALID_LAT_LONG_VALUE) != 0) {
				double lat = obj.getLatitude();
				double lon = obj.getLongitude();
				String textSummary = getSummary(obj);
				MarkerOptions markerOpts = new MarkerOptions().position(new LatLng(lat,lon))
				.title(textSummary).icon(marker_Type);
				Marker marker = googleMap.addMarker(markerOpts);
				//adds to map
				MarkerIndexMap.put(marker,i);
			}

		}
	}

	private String getSummary(SocialMediaObject obj) {
		// Uses a String builder as per android guidelines with objects.
		// However, not sure if this is correct as I return a substring (String object)
		StringBuilder text = new StringBuilder();

		switch (obj.getNumeratedType()) {
		case SocialMediaObject.TYPE_FB:
			text.append(((FacebookObject) obj).getName());
			return text.substring(0, Math.min(text.length(), TITLE_LENGTH))
					+ "...";
		case SocialMediaObject.TYPE_INSTAGRAM:
			text.append(((InstagramObject)obj).getFlattenedTags());
			return text.substring(0, Math.min(text.length(), TITLE_LENGTH))
					+ "...";
		case SocialMediaObject.TYPE_TWITTER:
			text.append(((TwitterObject) obj).getStatus());
			return text.substring(0, Math.min(text.length(), TITLE_LENGTH))
					+ "...";
		default:
			return "Look here!";
		}
	}

	//Does not make a new listnere per point as that would cause too much object creation
	//overhead, instead, adds listener to the map as a whole
	@Override
	public boolean onMarkerClick(Marker marker) {
		  int indexPosition = MarkerIndexMap.get(marker); 
		  Bundle args = new Bundle(); 
		  args.putInt("position", indexPosition);
		  args.putInt("type", MainActivity.LIST_MASH);
		  
		  DialogFragment infoView = new ExpandTextFragmentDialog();
		  infoView.setArguments(args);
		  infoView.show(((MainActivity)getActivity(
		  )).getSupportFragmentManager(), "expand_mash_dialog"); 
		
		
		return false;
	}
	
	
	
	/****************** DEBUGGING ******************/
	private void printHashTable() {
		if (MarkerIndexMap == null || MarkerIndexMap.isEmpty())
			Log.d(TAG, "THE HASHTABLE IS NULL OR EMPTY");
		else {
			Set<Marker> keyset = MarkerIndexMap.keySet();
			for(Marker key:keyset) {
				Log.d(TAG,"Key: " + key.getTitle() + ", value: " + MarkerIndexMap.get(key));
			}
		}
	}

}