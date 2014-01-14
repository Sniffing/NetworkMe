package example.networkme.Handler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import example.networkme.Geocoder.GeoCoder;
import example.networkme.Geocoder.Tuple;
import example.networkme.activities.MainActivity;
import example.networkme.fragments.TextFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by tt1611 on 06/11/13.
 */

public class TwitterAPIHandler {

    private Context twitterContext;
    private MainActivity activity;
    GeoCoder myGeocoder;

    public static final String CONSUMER_KEY = "xLTqkBqY5goMgKgSQZM9xg";
    public static final String CONSUMER_SECRET = "PCn78UFzLlJxNFLnhE33GQ66G3DQGzZB9MG8qebWkTU";
    public static final String ACCESS_TOKEN = "1957980702-2ApQS5HTZQc349FgEpCv4wIomExl5Ly8KuZGmVz";
    public static final String ACCESS_SECRET = "usjC0sEscGM4xhDlcinUKNlEDAHz6NfctJ08N7D4Yiw";

    public final String TAG = "TWITTER_API_HANDLER";


    List<Status> statusList = new ArrayList<Status>();
    //List<String> picturesList = new ArrayList<String>();
    //ImageView imagesView = null;

    public TwitterAPIHandler(MainActivity activity) {
        this.activity = activity;
        this.twitterContext = activity.getApplicationContext();
        //must make a base APIHandler Class
        myGeocoder = new GeoCoder(activity, this);
    }


    public void searchWithQuery(MainActivity.SearchType type, Tuple<String, String> locArgPair) {
        String query;
        switch (type) {
            case NONE:
                query = locArgPair.getSecond();
                String[] noneArgs = {query};
                activity.showTweetsLoading();
                myGeocoder.runLocalQuery(noneArgs);
                break;
            case BOTH:
                String location = locArgPair.getFirst();
                query = locArgPair.getSecond();
                String[] args = {location, query};
                activity.showTweetsLoading();
                myGeocoder.runQuery(args);
                break;
            case KEY_WORD:
                activity.showTweetsLoading();
                query = locArgPair.getSecond();
                SearchTask streamtask = new SearchTask();
                String[] test = {"100", "200", query};
                streamtask.execute(test);
                break;
            case LOCATION:
                activity.showTweetsLoading();
                query = locArgPair.getFirst();
                String[] singleArg = {query};
                myGeocoder.runQuery(singleArg);
        }

    }

    // Called by the Geolocation class after it converts to a lat/long
    public void doSearchTask(GeoLocation loc, String queryString) {
        SearchTask searchTask = new SearchTask();
        String lat = Double.toString(loc.getLatitude());
        String lon = Double.toString(loc.getLongitude());
        String[] searchTaskArgs = {lat, lon, queryString};
        searchTask.execute(searchTaskArgs);
    }

    private Tuple<String, String> splitArgs(String query) {

        int firstSpace = query.indexOf(" ");

        //Should not logically happen, cover the case though
        if (firstSpace == -1) {
            Log.d(TAG, "Check you're combining search terms correctly, should not enter here");
            return null;
        } else {
            String location = query.substring(0, firstSpace);
            String restOfQuery = query.substring(firstSpace + 1, query.length());
            return new Tuple<String, String>(location, restOfQuery);
        }
    }

    // PRE: Must take at least 3 Strings, a latitude, longitude and then the search term
    private class SearchTask extends AsyncTask<String, Void, List<twitter4j.Status>> {

        @Override
        protected List<twitter4j.Status> doInBackground(String... strings) {

            Log.d("DOING AN ASYNC TASK", "SEARCH TASK");

            // Authenticate with account, build request object
            ConfigurationBuilder cb = authWithAccount();
            twitter4j.Twitter twitter = new TwitterFactory(cb.build()).getInstance();

            // Lat and Long range is (-90,-180) to (90,180)
            // Build search Query and extract lat and long (default to 0,0 if not supplied)
            Double latitude = Double.parseDouble(strings[0]);
            Double longitude = Double.parseDouble(strings[1]);
            String fullQuery = concatStringArray(Arrays.copyOfRange(strings, 2, strings.length));
            Query query = new Query(fullQuery);

//            ListView textListView = (ListView) textFragment.findViewById(R.id.textView);
//            ArrayAdapter<String> textAdapter = (ArrayAdapter<String>) textListView.getAdapter();

            // Run search
            try {
                // Sets max return amount
                query.setCount(30);

                //Need to stick to these magic numbers
                if (latitude != 100 && longitude != 200)
                    query.setGeoCode(new GeoLocation(latitude, longitude), 10, "mi");

                return twitter.search(query).getTweets();

            } catch (TwitterException te) {
                te.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> searchResult) {
            super.onPostExecute(searchResult);
            statusList = searchResult;

            String[] tweetMessages = new String[activity.getTextList().length];

            for (int i = 0; i < Math.min(activity.getTextList().length, searchResult.size()); ++i) {
                tweetMessages[i] = searchResult.get(i).getUser().getScreenName() + ": " + searchResult.get(i).getText();

                if (searchResult.get(i).getGeoLocation() == null) {
                    Log.d("GEOLOC IS NULL:", "IS NULLLLLLLL");
                } else {
                    Log.d("CHECKING LONG AND LAT", " lat:" + Double.toString(searchResult.get(i).getGeoLocation().getLatitude()) + " long:" + Double.toString(searchResult.get(i).getGeoLocation().getLongitude()));
                }
            }
            activity.changeTweetList(tweetMessages);
            /*
            pictureAdd picture = new pictureAdd();
            Log.d("SEARCH POST EXECUTE","GETS HERE!!!!!!!!!!!!!!!!!!!");


            if (!picturesList.isEmpty())
                picture.execute(picturesList.get(0));

                    {
                        String[] array = new String[picturesList.size()];
                        int i = 0;
                        for (String string : picturesList)
                        {
                            array[i] = string;
                            ++i;
                            Log.d("CHECKING THE PICTURES LIST", "URL IS: " + string);
                            Log.d("CHECKING THE ARRAY", "ARRAY CONTENT IS" + array[i]);
                        }
                        picture.execute(array);
                    }
                    */
        }

    }





/*

    private class pictureAdd extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {

            imagesView = (ImageView)findViewById(R.id.imageFeed);

            try {
                URL url = new URL(strings[0]);
                HttpGet httpRequest = null;

                httpRequest = new HttpGet(url.toURI());

                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);

                HttpEntity entity = response.getEntity();
                BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
                InputStream input = b_entity.getContent();

                //Bitmap[] bitmap = new Bitmap[strings.length];

                Bitmap firstPic = BitmapFactory.decodeStream(input);

                for (int i = 0; i< bitmap.length; ++i) {
                    bitmap[i] = BitmapFactory.decodeStream(input);
                }

                Log.d("PICTURE ADD TAG", "TRYING TO DISPLAY THE PICTURE: " + firstPic);

                return firstPic;

            } catch (Exception ex) {
                Log.d("PICTURE ADD TAG","SOMETHING WENT WRONG");
                return null;
            }

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            Log.d("PICTURE POST EXECUTE TAG","GETS HEREEEEEEEEEEETOOOOO");
            //final Bitmap[] bitmapcopy = bitmap;

            imagesView.setImageBitmap(bitmap);


            imagesView.setOnClickListener(new View.OnClickListener() {
                int count = 0;

                @Override
                public void onClick(View v) {
                    count = (count >= bitmapcopy.length) ? count = 0: ++count;
                    imagesView.setImageBitmap(bitmapcopy[count]);
                }
            });

        }
    }

*/

/*

    private void changeTextList(List<Status> tweets)
    {
        for(int i =0; i < textList.length; ++i) {
            if (tweets.isEmpty())
                textList[i] = "your list is empty?";
            else{
                Status tweet = tweets.get(i);
                textList[i] = tweet.getUser().getScreenName() + ": " + tweet.getText();

                if (tweet.getMediaEntities() != null) {
                    MediaEntity[] entities = tweet.getMediaEntities();
                    for (MediaEntity entity : entities) {
                        if (entity.getType().equals("photo")) {
                            String url = entity.getMediaURL();
                            picturesList.add(url);
                        }
                    }
                }
            }
        }

        textAdapter.notifyDataSetChanged();
    }
*/

    private String statusToString(Status s) {
        String name = s.getUser().getName();
        String text = s.getText();

        return (new String(name + ": " + text));
    }

    public ConfigurationBuilder authWithAccount() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthAccessToken(ACCESS_TOKEN);
        cb.setOAuthAccessTokenSecret(ACCESS_SECRET);
        cb.setOAuthConsumerKey(CONSUMER_KEY);
        cb.setOAuthConsumerSecret(CONSUMER_SECRET);
        cb.setDebugEnabled(true);

        return cb;
    }

    public String concatStringArray(String[] stringArray) {
        int size = stringArray.length;
        String concattedString = new String();

        if (size == 1)
            return stringArray[0];

        for (int i = 0; i < size; ++i) {
            concattedString += stringArray[i] + " ";
        }

        Log.d("CONCATTING STRING BECOMES", concattedString);
        return concattedString;
    }
}

/*
    public class TwitterStreamTask extends AsyncTask<String,Void,List<Status>> {
        private List<twitter4j.Status> tweetList = new ArrayList<twitter4j.Status>();
        private int tweetCount = 0;

        @Override
        protected List<twitter4j.Status> doInBackground(String... strings) {
            ConfigurationBuilder cb = authWithAccount();

            StatusListener listener = new StatusListener() {

                public void onStatus(twitter4j.Status status) {
                    if ((status.getPlace() != null) && (status.getGeoLocation() !=null)) {

                        if (tweetCount < textList.length){
                            tweetList.add(status);
                            ++tweetCount;
                            Log.d("STREAM TAG", "STATUS RECEIVED");
                        }
                        else {
                            Log.d("STREAM TAG", "ENTERED STATE WITH ENOUGH TWEETS");
                        }
                    }
                }
                public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                    Log.d("STREAM TAG", "DELETION REQUEST");
                }
                public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                    Log.d("STREAM TAG","YOURE BEING LIMITED, ENHANCE CALM");
                }
                public void onException(Exception ex) {
                    ex.printStackTrace();
                }
                @Override
                public void onScrubGeo(long arg0, long arg1) { // do nothing
                }
                @Override
                public void onStallWarning(StallWarning arg0) { // do nothing
                }

            };

            TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

            FilterQuery fq = new FilterQuery();
            String keywords[] = strings;

            fq.track(keywords);
            twitterStream.addListener(listener);

            twitterStream.filter(fq);

            while (tweetCount < textList.length) {}

            twitterStream.cleanUp();

            for (int i = 0; i < tweetList.size(); ++i)
            {
                Log.d("TWEETLIST TAG", "OBJECT WAS" + tweetList.get(i).getText());
            }

            Log.d("ADDING TAG", "PASSES THE RETURN VALUE NOW");
            return tweetList;
        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> streamResult) {
            super.onPostExecute(streamResult);
            Log.d("POST EXECUTE TAG", "THE STREAM SHOULD HAVE FINISHED");
            Log.d("POST EXECUTE TAG", "THE LENGTH OF THE LIST NOW IS " + streamResult.size());
            changeTextList(streamResult);
        }
    }
}
*/