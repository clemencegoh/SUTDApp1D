package com.ft4sua.sutdapp1d.EventPackage;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.ft4sua.sutdapp1d.R;

import java.util.List;

/**
 * Created by swonlek on 5/12/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CardViewHolder>{


    List<Event> events;
    public RVAdapter(List<Event> events) {
        this.events = events;
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
        Event event = events.get(i);
        cardViewHolder.event_name.setText(event.event_name);
        cardViewHolder.event_date.setText(event.event_date);
        cardViewHolder.event_type.setText(event.event_type);
        cardViewHolder.event_start.setText(event.event_start);
        cardViewHolder.event_end.setText(event.event_end);
        cardViewHolder.event_venue.setText(event.event_venue);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }


}