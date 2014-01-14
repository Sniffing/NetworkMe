package example.networkme.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import example.networkme.activities.InstagramLoginActivity;
import example.networkme.activities.MainActivity;
import example.networkme.adapter.ImageAdapter;

import java.net.URL;
import java.util.List;

import com.example.networkme.R;

/**
 * Created by Marcel on 11/9/13.
 */
public class PicsFragment extends Fragment {
    Button instaLoginButton;
//    TextView t;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pics, container, false);
        instaLoginButton = (Button)rootView.findViewById(R.id.instagram_login);
        instaLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InstagramLoginActivity.class);
                startActivity(intent);
            }

        });

      // t = (TextView)rootView.findViewById(R.id.insta_text_view);

//        t.setText(MainActivity.instagramHandler.getTokenString()+" tokenstring");

        return rootView;

    }

    @Override
    public void onResume(){
        if (MainActivity.instagramHandler.isLoggedin()){
            instaLoginButton.setVisibility(View.INVISIBLE);

            GridView gridview = (GridView) getView().findViewById(R.id.gridview);
            gridview.setVisibility(View.VISIBLE);
            populateGrid(gridview);

        }
    //    t.setText(MainActivity.instagramHandler.getTokenString()+" tokenstring");


        super.onResume();
    }

    private void populateGrid(GridView gridview) {

        //TODO: the params are long and lat

        Object latitude = ((MainActivity)getActivity()).getLatitude();
        Object longitude =((MainActivity)getActivity()).getLongitude();
        List<URL> urlList = MainActivity.instagramHandler.getUrlListForLocation(latitude, longitude);

        gridview.setAdapter(new ImageAdapter(getActivity().getApplicationContext(), urlList));

    }

}