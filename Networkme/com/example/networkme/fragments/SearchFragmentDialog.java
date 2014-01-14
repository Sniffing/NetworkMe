package example.networkme.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.networkme.R;

import example.networkme.activities.MainActivity;

/**
 * Created by Marcel on 11/18/13.
 */
public class SearchFragmentDialog extends DialogFragment {
	private final String SEARCH_CHOICE = "Search!";
	private final String CANCEL_CHOICE = "Cancel";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(R.layout.search_button_dialog, null);
		final EditText keywords = (EditText)view.findViewById(R.id.keyword_field);
		final EditText location = (EditText)view.findViewById(R.id.location_field);

        dialogBuilder.setView(view);
        
        dialogBuilder.setPositiveButton(CANCEL_CHOICE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        
        dialogBuilder.setNegativeButton(SEARCH_CHOICE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enteredKeywords = keywords.getText().toString();
                String enteredLocation = location.getText().toString();
               /*
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                           InputMethodManager.HIDE_NOT_ALWAYS);
                */
                ((MainActivity)getActivity()).executeSearch(enteredKeywords, enteredLocation);
                Toast.makeText(getActivity(), "SEARCH ENTERED: " + enteredKeywords + ", " + enteredLocation, Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        return dialogBuilder.create();
	}
}