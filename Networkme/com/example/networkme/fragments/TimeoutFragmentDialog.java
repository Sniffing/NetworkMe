package example.networkme.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.networkme.R;

public class TimeoutFragmentDialog extends DialogFragment {
	private final String CONFIRM_CHOICE = "Ok!";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(R.layout.time_out_dialog, null);

		dialogBuilder.setView(view);
        
        dialogBuilder.setPositiveButton(CONFIRM_CHOICE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        
        return dialogBuilder.create();
	}
}