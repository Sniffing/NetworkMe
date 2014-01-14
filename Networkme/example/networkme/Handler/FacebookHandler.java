package example.networkme.Handler;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import example.networkme.activities.MainActivity;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JOSE on 13/11/13.
 */
public class FacebookHandler {

    String s;


    private FacebookData prev = null;
    private FacebookData first = null;
    private MainActivity activity;

    public FacebookHandler( MainActivity activity){
        this.activity = activity;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

    }

    public void executeQuery(String query){

        Log.v("TEXTFRAGMENT-Query", "Query  "+query);

        String fqlQuery = "SELECT name,description, location, start_time FROM event WHERE "+
                "                                           contains('"+query+"') LIMIT 25";
        Bundle params = new Bundle();
        params.putString("q", fqlQuery);
        Session session = Session.getActiveSession();
        Request request = new Request(session,
                "/fql",
                params,
                HttpMethod.GET,
                new Request.Callback(){
                    public void onCompleted(Response response) {
                        first = null;
                        parse(response);
                        activity.changeTextView(first);
                        return;
                    }
                });
        Request.executeBatchAsync(request);
//        Request.e

//        Log.v("TEXTFRAGMENT","before req and wait");
//        Request.executeAndWait(request);
//
//
//        Log.v("TEXTFRAGMENT","after req and wait");


    }
    private void parse(Response response) {
        GraphObject graphObject = response.getGraphObject();
        String str = "";
        if (graphObject != null) {
            JSONObject jsonObject = graphObject.getInnerJSONObject();
            try {
                JSONArray array = jsonObject.getJSONArray("data");

                for (int i = 1; i < array.length(); i++) {
                    String name, description, start_time, location;

                    JSONObject object = (JSONObject) array.get(i);
                    str += "Name: ";
                    str += object.get("name");
                    name = ""+object.get("name");


                    str += "\nDescription: ";
                    str += object.get("description");
                    description = ""+object.get("description");


                    str += "\nLocation: ";
                    str += object.get("location");
                    location = ""+object.get("location");


                    str += "\nStart Time";
                    str += object.get("start_time");
                    start_time = ""+object.get("start_time");
                    str += "-----------\n\n";



                    s+= i+"\n\n\n\n";
                    FacebookData fbData = new FacebookData(name,location,description,start_time, i);
                    if(prev == null){
                        prev = fbData;
                        first = fbData;
                    }else{

                        Log.v("fbexec",""+fbData.toString());
                        prev.setNext(fbData);
                        prev = prev.getNext();

                    }

                    str = "";

                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }

    }

    public FacebookData getFirst() {
        return first;
    }
    public void setFirst(FacebookData fbData) {
        prev = null;
        first = fbData;
    }

}
