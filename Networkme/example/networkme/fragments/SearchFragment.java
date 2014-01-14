package example.networkme.fragments;

import com.example.networkme.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import example.networkme.activities.*;

/**
 * Created by Marcel on 11/18/13.
 */
public class SearchFragment extends Fragment {

    String keyword = "";
    String location = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View searchFragmentView = inflater.inflate(R.layout.fragment_search, container, false);
        Button searchButton = (Button)searchFragmentView.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText keyW = (EditText)searchFragmentView.findViewById(R.id.SearchKeyword);
                keyword = keyW.getText().toString();
                EditText loc = (EditText)searchFragmentView.findViewById(R.id.Location);
                location = loc.getText().toString();
                ((MainActivity)getActivity()).searchFragmentButtonClick(keyword,location);
            }
        });

        return searchFragmentView;
    }
}