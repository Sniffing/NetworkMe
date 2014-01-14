package example.networkme.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.networkme.R;

import example.networkme.activities.MainActivity;

public class SearchInProgressFragmentDialog extends DialogFragment{
	private boolean twitterComplete;
	private boolean facebookComplete;
	private boolean instagramComplete;
	
	private ImageView fb_result;
	private ImageView instagram_result;
	private ImageView twitter_result;
	
	private ProgressBar fb_progress;
	private ProgressBar instagram_progress;
	private ProgressBar twitter_progress;
	
	private TextView fb_comp_text;
	private TextView instagram_comp_text;
	private TextView twitter_comp_text;
	
	private final String TAG = "SEARCH_PROG_DIALOG";
	private final String NO_RESULTS_FOUND_STRING = "No results found";
	private final String NO_RESULTS_COLOUR = "#EE3B3B";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		twitterComplete = false;
		facebookComplete = false;
		instagramComplete = false;
		
		View view = getActivity().getLayoutInflater().inflate(R.layout.search_progress_dialog, null, false);
		fb_result = (ImageView) view.findViewById(R.id.facebook_result);
		fb_progress = (ProgressBar) view.findViewById(R.id.facebook_prog_bar);
		fb_comp_text = (TextView) view.findViewById(R.id.fb_complete_text);
		instagram_result = (ImageView) view.findViewById(R.id.instagram_result);
		instagram_progress = (ProgressBar) view.findViewById(R.id.instagram_prog_bar);
		instagram_comp_text = (TextView) view.findViewById(R.id.instagram_complete_text);
		twitter_result = (ImageView) view.findViewById(R.id.twitter_result);
		twitter_progress = (ProgressBar) view.findViewById(R.id.twitter_prog_bar);
		twitter_comp_text = (TextView) view.findViewById(R.id.twitter_complete_text);
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		dialogBuilder.setView(view);
			
		return dialogBuilder.create();
	}
	
	public void instagramCompleted(boolean success) {
		this.instagramComplete = true;
		instagram_result.setImageResource(R.drawable.instagram_icon_50);
		instagram_progress.setVisibility(View.GONE);
		
		if(!success) {
			instagram_comp_text.setText(NO_RESULTS_FOUND_STRING);
			instagram_comp_text.setTextColor(Color.parseColor(NO_RESULTS_COLOUR));
		}
		instagram_comp_text.setVisibility(View.VISIBLE);
		completionStatus();
	}
	
	public void facebookCompleted(boolean success) {
		this.facebookComplete = true;
		fb_result.setImageResource(R.drawable.fb_blue_50);
		fb_progress.setVisibility(View.GONE);
		
		if(!success) {
			fb_comp_text.setText(NO_RESULTS_FOUND_STRING);
			fb_comp_text.setTextColor(Color.parseColor(NO_RESULTS_COLOUR));
		}
		fb_comp_text.setVisibility(View.VISIBLE);
		completionStatus();
	}
	
	public void twitterCompleted(boolean success) {
		this.twitterComplete = true;
		twitter_result.setImageResource(R.drawable.bird_blue_48);
		twitter_progress.setVisibility(View.GONE);
		
		if (!success){
			twitter_comp_text.setText(NO_RESULTS_FOUND_STRING);
			twitter_comp_text.setTextColor(Color.parseColor(NO_RESULTS_COLOUR));
		}
		twitter_comp_text.setVisibility(View.VISIBLE);
		completionStatus();
	}
	
	
	private void completionStatus() {
		if (allComplete()){
			MainActivity.LISTS_FINISHED_FLAG=true;
			dismiss();
		}
	}
	
	public void timeOut() {
		resetStatuses();
		dismiss();
	}
	
	public void resetStatuses() {
		this.twitterComplete = false;
		this.instagramComplete = false;
		this.facebookComplete = false;
		
		twitter_result.setImageResource(R.drawable.bird_blue_48_greyed);
		instagram_result.setImageResource(R.drawable.instagram_icon_50_greyed);
		fb_result.setImageResource(R.drawable.fb_blue_50_greyed);
	}
	
	public boolean allComplete() {
		return (twitterComplete && instagramComplete && facebookComplete);
	}
	
}
