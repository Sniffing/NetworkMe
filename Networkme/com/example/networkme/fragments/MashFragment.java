package example.networkme.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.networkme.R;

import example.networkme.Handler.FacebookHandler;
import example.networkme.Handler.SplitList;
import example.networkme.Handler.TwitterHandler;
import example.networkme.activities.MainActivity;
import example.networkme.adapter.SocialMediaAdapter;

/**
 * Created by Marcel on 11/9/13.
 */
public class MashFragment extends Fragment {
	
	private final String TAG = "MASH_FRAGMENT";
	ListView listview;

	public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
		MainActivity activity = (MainActivity)getActivity();
		
		View rootView = inflater.inflate(R.layout.fragment_list, container, false);
		
		listview = (ListView)rootView.findViewById(R.id.display_list);
	    
	    SplitList mediaList = activity.getMediaList();
	    ListAdapter adapter = new SocialMediaAdapter(this.getActivity(),mediaList.getJointList());
	    listview.setAdapter(adapter);
	    
        listview.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View view, int pos,
        			long arg3) {
        			Bundle args = new Bundle();
        			args.putInt("position", pos);
        			args.putInt("type", MainActivity.LIST_MASH);
        			showListDialog(args);
        	}
		});
        listview.setAdapter(adapter);
	    
	    return rootView;
	}
	
	private void showListDialog(Bundle args) {
		DialogFragment infoView = new ExpandTextFragmentDialog();
		infoView.setArguments(args);
		infoView.show(((MainActivity)getActivity()).getSupportFragmentManager(), "expand_mash_dialog");
	}
}