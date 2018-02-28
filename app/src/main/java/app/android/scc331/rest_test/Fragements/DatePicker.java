package app.android.scc331.rest_test.Fragements;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.android.scc331.rest_test.MainActivity;
import app.android.scc331.rest_test.R;

public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String ARG_PARAM1 = "param1";

    private DateInteractionListener mListener;

    private int type;


    public DatePicker() {
        // Required empty public constructor
    }

    public static DialogFragment newInstance(int param1) {
        DialogFragment fragment = new DatePicker();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        if (getArguments() != null) {
            type = getArguments().getInt(ARG_PARAM1);
        }
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public String getValidMonthDay(int md) {
        if (md >= 10) return String.valueOf(md);
        return "0" + String.valueOf(md);
    }

    public long getUnixTime(String dateSt){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(dateSt);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long unixTime = date.getTime()/1000;
        return unixTime;
    }


    public interface DateInteractionListener {
        void onDateChange(long utc, int type);
        void onDateChange(String text, int type);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DateInteractionListener) {
            mListener = (DateInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LiveDataInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        String date = view.getYear() + "-" + getValidMonthDay(view.getMonth()+1)+ "-" + getValidMonthDay(view.getDayOfMonth());
        if(type == 0){
            long utc = getUnixTime(date);
            System.out.println("Start date: " + utc);
            mListener.onDateChange(utc,0);
            mListener.onDateChange(date,0);

        }else{
            long utc = getUnixTime(date);
            System.out.println("End date: " + utc);
            mListener.onDateChange(utc,1);
            mListener.onDateChange(date,1);
        }
    }

}
