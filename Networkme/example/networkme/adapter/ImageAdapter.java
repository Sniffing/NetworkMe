package example.networkme.adapter;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ben
 */
public class ImageAdapter extends BaseAdapter{

    private Context mContext;
    private List<URL> urlList;

    public ImageAdapter(Context c, List<URL> urlList) {
        mContext = c;
        this.urlList = urlList;
    }

    public int getCount() {
        return urlList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

//        if (position == 2) {
//            TextView textView;
//            textView = new TextView(mContext);
//            textView.setText("Lady Gaga's honored to be producing the event, 2 years in the making. On 11.11 ART MUSIC FASHION + TECHNOLOGY explode into flight #ARTPOP");
//            textView.setLayoutParams(new GridView.LayoutParams(300, 153));
//            textView.setBackgroundColor(Color.WHITE);
//            textView.setTextColor(Color.BLACK);
//            textView.setPadding(5, 15, 5, 15);
//            return textView;
//        }

        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(153, 153));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageDrawable(null);

        URL url = urlList.get(position);

        new DownloadImageTask(imageView).execute(url);

        return imageView;
    }

    private class DownloadImageTask extends AsyncTask<URL, Void, Bitmap> {
        ImageView view;

        public DownloadImageTask(ImageView view) {
            this.view = view;
        }

        protected Bitmap doInBackground(URL... urls) {
            URL url = urls[0];
            Bitmap bitmap = null;

            try {
                InputStream in = url.openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (IOException ex) {
                Logger.getLogger(ImageAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            view.setImageBitmap(bitmap);
        }
    }

}
