package example.networkme.fragments;

import com.example.networkme.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import example.networkme.Geocoder.Tuple;
import example.networkme.activities.*;
import example.networkme.Handler.*;

/**
 * Created by Marcel on 11/9/13.
 */
public class TextFragment extends Fragment {

    TextView t;
    private FacebookHandler facebookHandler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_text, container, false);

        MainActivity activity = (MainActivity)getActivity();
        activity.setTextFragmentBool(true);

        //TODO: the query should be normal search!!! long and lat need to be implemented

        t = (TextView)rootView.findViewById(R.id.textView);
        activity.setTextView(t);
        t.setMovementMethod(new ScrollingMovementMethod());
        Log.v("textview", t.toString());




        String query = "london";
        String location = "london";
        facebookHandler = new FacebookHandler(activity);
        TwitterAPIHandler twitterHandler = new TwitterAPIHandler(activity);
        ((MainActivity)getActivity()).setTwitterHandler(twitterHandler);
        ((MainActivity)getActivity()).setFacebookHandler(facebookHandler);
        facebookHandler.executeQuery(query);
        Tuple<String,String> locArgPair = new Tuple<String, String>(location, query);
        twitterHandler.searchWithQuery(MainActivity.SearchType.BOTH,locArgPair);



        return rootView;
    }




}