package com.ft4sua.sutdapp1d.EventPackage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ft4sua.sutdapp1d.DatabasePackage.Event;
import com.ft4sua.sutdapp1d.DatabasePackage.EventsHelper;
import com.ft4sua.sutdapp1d.EventManagerFragment;
import com.ft4sua.sutdapp1d.R;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by swonlek on 5/12/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CardViewHolder>{

    List<Event> events;
    Context context;

    public RVAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context=context;
    }
    public static class CardViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView event_name, event_date, event_type, event_start, event_end, event_venue;
        ImageButton edit, delete;

        CardViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            event_name = (TextView)itemView.findViewById(R.id.card_event_name);
            event_date = (TextView)itemView.findViewById(R.id.card_event_date);
            event_type = (TextView)itemView.findViewById(R.id.card_event_type);
            event_start = (TextView)itemView.findViewById(R.id.card_event_starttime);
            event_end = (TextView)itemView.findViewById(R.id.card_event_endtime);
            event_venue = (TextView)itemView.findViewById(R.id.card_event_venue);

            edit = (ImageButton)itemView.findViewById(R.id.card_edit);
            delete = (ImageButton)itemView.findViewById(R.id.card_delete);
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_event_manager, viewGroup, false);

        CardViewHolder CardViewHolder = new CardViewHolder(v);
        return CardViewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        final Event event = events.get(i);
        cardViewHolder.event_name.setText(event.getName());
        cardViewHolder.event_date.setText(event.getDate());
        cardViewHolder.event_type.setText(event.getTag());
        cardViewHolder.event_start.setText(event.getStart());
        cardViewHolder.event_end.setText(event.getEnd());
        cardViewHolder.event_venue.setText(event.getVenue());

        cardViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent=new Intent(context,EditEventActivity.class);
                editIntent.putExtra(context.getString(R.string.id_extra),event.getId());
                ((Activity) context).startActivityForResult(editIntent,1);
            }
        });
        cardViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventsHelper.getInstance(context).deleteEvent(event,context);
                Intent refresh = new Intent(context, EventManagerFragment.class);
                context.startActivity(refresh);
                ((Activity)context).setResult(RESULT_OK,null);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}