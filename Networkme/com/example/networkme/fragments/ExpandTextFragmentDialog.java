package example.networkme.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.networkme.R;
import com.loopj.android.image.SmartImageView;

import example.networkme.Handler.FacebookObject;
import example.networkme.Handler.InstagramObject;
import example.networkme.Handler.SocialMediaObject;
import example.networkme.Handler.TwitterObject;
import example.networkme.activities.MainActivity;
import example.networkme.views.SquareImageView;

public class ExpandTextFragmentDialog extends DialogFragment {
	private SocialMediaObject object;
	private final String TAG = "EXPAND_TEXT_FRAGMENT_DIALOG";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);

		final int position = getArguments().getInt("position");
		final int list_type = getArguments().getInt("type");
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
				getActivity());
		
		switch(list_type) {
		case MainActivity.LIST_MASH: this.object = ((MainActivity)getActivity())
				.getSocialMediaAtIndex(position);
				break;
		case MainActivity.LIST_TEXT: this.object = ((MainActivity) getActivity())
				.getTextMediaAtIndex(position);
				break;		
		}
		
		View view = null;
		String type = object.getType();

		Log.d(TAG, "Is object null" + object.getType());

		if (type.equals("twitter")) {
			if (!((TwitterObject) object).containsPicture()) {
				TwitterObject obj = (TwitterObject) object;
				view = getActivity().getLayoutInflater().inflate(
						R.layout.item_click_twitter_dialog, null, false);
				final TextView twitter_username = (TextView) view
						.findViewById(R.id.textClick_twitter_username);
				final TextView twitter_status = (TextView) view
						.findViewById(R.id.textClick_twitter_status);
				final TextView twitter_time = (TextView) view
						.findViewById(R.id.textClick_twitter_time);
				final TextView twitter_actualname = (TextView) view
						.findViewById(R.id.textClick_twitter_realname);
				final SquareImageView twitter_profilePic = (SquareImageView) view
						.findViewById(R.id.textClick_twitter_PP);
				final TextView twitter_date = (TextView) view
						.findViewById(R.id.textClick_twitter_date);

				twitter_status.setText('\"' + obj.getStatus() + '"');
				twitter_time.setText(obj.getTime());
				twitter_date.setText(obj.getDate());
				twitter_username.setText("@"+obj.getUsername());
				twitter_actualname.setText(obj.getActualName());

				twitter_profilePic.setImageUrl(obj.getPPUrlString());
				twitter_username.setSelected(true);
				twitter_actualname.setSelected(true);
				dialogBuilder.setView(view);
			} else {
				view = getActivity().getLayoutInflater().inflate(
						R.layout.pic_click_twitter_dialog, null, false);
				final TextView username = (TextView) view
						.findViewById(R.id.onclick_twitter_username);
				final TextView time = (TextView) view
						.findViewById(R.id.onclick_twitter_time);
				final TextView tweet = (TextView) view
						.findViewById(R.id.onclick_twitter_tweet);
				final SquareImageView image = (SquareImageView) view
						.findViewById(R.id.onclick_twitter_img);
				dialogBuilder.setTitle("Twitter Image");
				
				
				TwitterObject obj = (TwitterObject) object;
				username.setText(obj.getUsername());
				time.setText(obj.getTime() + "  " + obj.getDate());
				tweet.setText(obj.getStatus());

				image.setImageUrl(obj.getUrlString());
				dialogBuilder.setView(view);
			}
		}

		if (type.equals("facebook")) {
			// Consider not making a new object every time, just go straight for
			// the cast if using up too much memory.
			FacebookObject obj = (FacebookObject) object;

			view = getActivity().getLayoutInflater().inflate(
					R.layout.item_click_facebook_dialog, null, false);
			final TextView fb_location = (TextView) view
					.findViewById(R.id.onclick_fbevent_location);
			final TextView fb_name = (TextView) view
					.findViewById(R.id.onclick_fbevent_name);
			final TextView fb_start = (TextView) view
					.findViewById(R.id.onclick_fbevent_starttime);
			final TextView fb_finish = (TextView) view
					.findViewById(R.id.onclick_fbevent_finishtime);
			final TextView fb_desc = (TextView) view
					.findViewById(R.id.onclick_fbevent_description);
			final TextView fb_hosts = (TextView) view
					.findViewById(R.id.onclick_fbevent_hosts);
			final SmartImageView fb_coverpic = (SmartImageView) view
					.findViewById(R.id.onclick_fbevent_cover);
			final TextView fb_attending = (TextView) view
					.findViewById(R.id.onclick_fbevent_atten);

			fb_desc.setText(obj.getDescription());
			fb_finish.setText(obj.getEndTime());
			fb_start.setText(obj.getStartTime());
			fb_hosts.setText(obj.getHost());
			fb_name.setText(obj.getName());
			fb_location.setText(obj.getLocation());
			fb_attending.setText(Integer.toString(obj.getAttending()));
			
			fb_name.setSelected(true);
			if (obj.getPicCoverString().equals(MainActivity.NOT_AVAILABLE_STRING)) {
				fb_coverpic.setImageResource(R.drawable.no_cover);
			} else {
				fb_coverpic.setImageUrl(obj.getPicCoverString());
			}
			dialogBuilder.setView(view);
		}

		if (type.equals("instagram")) {
			view = getActivity().getLayoutInflater().inflate(
					R.layout.pic_click_instagram_dialog, null, false);
			final TextView username = (TextView) view
					.findViewById(R.id.onclick_instagram_username);
			final TextView time = (TextView) view
					.findViewById(R.id.onclick_instagram_time);
			final TextView location = (TextView) view
					.findViewById(R.id.onclick_instagram_location);
			final TextView tags = (TextView) view
					.findViewById(R.id.onclick_instagram_hashtags);
			final SmartImageView image = (SmartImageView) view
					.findViewById(R.id.onclick_instagram_img);

			InstagramObject obj = (InstagramObject) object;
			username.setText(obj.getUsername());
			time.setText(obj.getTimeStamp());
			location.setText(obj.getLocation());
			tags.setText(obj.getFlattenedTags());

			image.setImageUrl(obj.getUrlString());	
			dialogBuilder.setView(view);
		}

		return dialogBuilder.create();
	}

}
