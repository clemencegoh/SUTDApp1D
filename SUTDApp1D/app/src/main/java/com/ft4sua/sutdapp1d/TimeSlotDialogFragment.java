package com.ft4sua.sutdapp1d;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.GpsStatus;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link TimeSlotDialogFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link TimeSlotDialogFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class TimeSlotDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match

    TimeSlotDialogListener mListener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TimeSlotDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Time Slot Expanded View")
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


    public interface TimeSlotDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_slot_dialog, container, false);
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
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (TimeSlotDialogListener) context;

        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            Log.d("lll","ooo");
            e.printStackTrace();
            throw new ClassCastException(context.toString() + " must implement TimeSlotDialogListener");
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