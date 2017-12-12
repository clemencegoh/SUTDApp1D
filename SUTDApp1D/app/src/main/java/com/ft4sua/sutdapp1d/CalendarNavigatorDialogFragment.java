package com.ft4sua.sutdapp1d;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link CalendarNavigatorDialogFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link CalendarNavigatorDialogFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class CalendarNavigatorDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match

    CalendarNavigatorDialogListener mListener;
    CalendarView calendar;
    Date date = new Date();
    PagerAdapter mAdapter;
    ViewPager mPager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarNavigatorDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.calendar_view_navigator, null);

        builder.setView(v);

        calendar = (CalendarView) v.findViewById(R.id.calendarview);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                mAdapter = new SectionPagerAdapter(getActivity().getSupportFragmentManager());

                Calendar calendar = Calendar.getInstance();

                calendar.set(year, month, dayOfMonth);

                String dateString = calendar.getTime().toString();
                String dateStringParsed = dateString.substring(0,3) + ", " + dayOfMonth + " " + dateString.substring(4,7) + " " + year;

                Log.i("Kenjyi", dateString);

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("Date", dateStringParsed);
                getActivity().startActivity(intent);
            }
        });


        builder
                .setPositiveButton("GO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }


    public interface CalendarNavigatorDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar_navigator_dialog, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the CalendarNavigatorDialogListener so we can send events to the host
            mListener = (CalendarNavigatorDialogListener) context;


        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            Log.d("lll","ooo");
            e.printStackTrace();
            throw new ClassCastException(context.toString() + " must implement CalendarNavigatorDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}