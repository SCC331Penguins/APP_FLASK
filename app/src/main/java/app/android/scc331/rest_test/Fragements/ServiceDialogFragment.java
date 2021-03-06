package app.android.scc331.rest_test.Fragements;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import app.android.scc331.rest_test.R;

import static app.android.scc331.rest_test.LoginActivity.defaultTheme;

/**
 * Created by Nikola on 15/02/2018.
 */

public class ServiceDialogFragment extends DialogFragment
{
    public interface NoticeDialogListener {
        public void onDialogClick(int mode, String result);
        public void actuatorActionAdded(String action);
        public void actuatorActionRemoved(String action);

    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), defaultTheme ? R.style.AppTheme :R.style.AppTheme1);
        Bundle bundle = this.getArguments();
        String[] values = null;
        boolean[] checked = null;
        String title = "Select a service";
        if (bundle != null)
        {
            values = bundle.getStringArray("values");
            checked = bundle.getBooleanArray("checked");
            title = bundle.getString("title");
        }

        final String finalTitle = title;
        final String[] finalValues = values;
        builder.setTitle(title);
        if(!title.equals("Choose an action"))
        {
            builder.setItems(values, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    if (finalTitle.equals("Select a service"))
                        mListener.onDialogClick(0, finalValues[which]);
                    else if (finalTitle.equals("Choose a sensor to use for service"))
                        mListener.onDialogClick(1, finalValues[which]);
                    else
                        mListener.onDialogClick(2, finalValues[which]);
                }
            });
        }
        else
        {
            String[] finalValues1 = values;
            builder.setMultiChoiceItems(values, checked, new DialogInterface.OnMultiChoiceClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int which, boolean isChecked)
                {
                    if(isChecked)
                        mListener.actuatorActionAdded(finalValues1[which]);
                    else
                        mListener.actuatorActionRemoved(finalValues1[which]);


                }
            });
        }
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) getParentFragment();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
