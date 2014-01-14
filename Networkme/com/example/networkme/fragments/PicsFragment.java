package example.networkme.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.example.networkme.R;

import example.networkme.Handler.SplitList;
import example.networkme.activities.MainActivity;
import example.networkme.adapter.ImageAdapter;

/**
 * Created by Marcel on 11/9/13.
 */
public class PicsFragment extends Fragment {
    Button instaLoginButton;
    private final String TAG = "PICS_FRAGMENT";
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pics, container, false);
        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
		populateGrid(gridview);

        return rootView;

    }

    private void populateGrid(GridView gridview){
    	MainActivity main = (MainActivity)getActivity();
        
        SplitList mediaList = main.getMediaList();
        
        gridview.setAdapter(new ImageAdapter(getActivity().getApplicationContext(), mediaList.getPicsList()));
        gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Bundle args = new Bundle();
				args.putInt("position",position);
				showInformation(args);
			}				
		});
      
    }
    
    private void showInformation(Bundle otherData) {
    	DialogFragment fullView = new ExpandImageFragmentDialog();
    	fullView.setArguments(otherData);
    	fullView.show(((MainActivity)getActivity()).getSupportFragmentManager(), "expand_pic_dialog");
    }

}