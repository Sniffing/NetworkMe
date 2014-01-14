package example.networkme.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.networkme.R;

import example.networkme.activities.AppSettingsActivity;

/**
 * Created by Marcel on 11/18/13.
 */
public class SearchSettingsBarFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_init_search, container, false);
		Button search = (Button) v.findViewById(R.id.search_button);
		Button settings = (Button) v.findViewById(R.id.settings_button); 
		
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showSearchDialog();
			}
		});
		
		settings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), AppSettingsActivity.class);
				startActivity(i);
			}
		});

		return v;
	}

	private void showSearchDialog() {
		DialogFragment dialogFrag = new SearchFragmentDialog();
		dialogFrag.show(getFragmentManager(), "search_dialog");
	}
}