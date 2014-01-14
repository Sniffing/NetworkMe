package example.networkme.fragments;

import com.example.networkme.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import example.networkme.activities.MainActivity;

/**
 * Created by Marcel on 11/18/13.
 */
public class InitSearchFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_init_search, container, false);
        Button b = (Button) v.findViewById(R.id.InitSearchButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).initSearchFragmentButtonClick();
            }
        });
        return v;
    }

}