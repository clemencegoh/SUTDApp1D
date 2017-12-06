package com.ft4sua.sutdapp1d.EventPackage;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.ft4sua.sutdapp1d.EventManagerFragment;
import com.ft4sua.sutdapp1d.MainActivity;
import com.ft4sua.sutdapp1d.R;

import java.util.List;

/**
 * Created by swonlek on 5/12/2017.
 */

public class RVAdapter extends ExpandableRecyclerAdapter<RVAdapter.CardParentViewHolder, RVAdapter.CardChildViewHolder>{

    LayoutInflater layoutInflater;

    public RVAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public CardParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View v = layoutInflater.inflate(R.layout.card_collapsed, viewGroup, false);
        return new CardParentViewHolder(v);
    }

    @Override
    public CardChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View v = layoutInflater.inflate(R.layout.card_expanded, viewGroup, false);
        return new CardChildViewHolder(v);
    }

    @Override
    public void onBindParentViewHolder(CardParentViewHolder cardParentViewHolder, int i, Object o) {
        CardParent cardParent = (CardParent) o;
        cardParentViewHolder.event_name.setText(cardParent.getEvent_name());
        cardParentViewHolder.event_details.setText(cardParent.getEvent_details());
    }

    @Override
    public void onBindChildViewHolder(CardChildViewHolder cardChildViewHolder, int i, Object o) {
        CardChild cardChild = (CardChild)o;
        cardChildViewHolder.event_type.setText(cardChild.getEvent_type());
        cardChildViewHolder.event_start.setText(cardChild.getEvent_start());
        cardChildViewHolder.event_end.setText(cardChild.getEvent_end());
        cardChildViewHolder.event_venue.setText(cardChild.getEvent_venue());

    }

//    List<Event> events;
//    public RVAdapter(List<Event> events) {
//        this.events = events;
//    }
//
//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//    }
//
//    @Override
//    public CardParentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_event_manager, parent, false);
//        CardParentViewHolder eventViewHolder = new CardParentViewHolder(v);
//        return eventViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(final CardParentViewHolder holder, int position) {
//
//        // Expandable card
//        //https://www.youtube.com/watch?v=z9qScBaKfnM
//
//
//        // updating each cardview
//        holder.event_name.setText(events.get(position).event_name);
//        holder.event_details.setText(events.get(position).event_details);
//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.delete.setColorFilter(R.color.sutd_lightgreen);
//            }
//        });
//
//        holder.edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.edit.setColorFilter(Color.rgb(255,255,255));
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return events.size();
//    }

    public static class CardParentViewHolder extends ParentViewHolder  {
//        CardView cv;
        TextView event_name, event_details;
        ImageButton edit, delete, expand;

        CardParentViewHolder(View itemView) {
            super(itemView);
//            cv = (CardView)itemView.findViewById(R.id.card_view);
            event_name = (TextView)itemView.findViewById(R.id.card_event_name);
            event_details = (TextView)itemView.findViewById(R.id.card_details);
            edit = (ImageButton)itemView.findViewById(R.id.card_edit);
            delete = (ImageButton)itemView.findViewById(R.id.card_delete);
            expand = (ImageButton)itemView.findViewById(R.id.expand_arrow);
        }
    }

    public static class CardChildViewHolder extends ChildViewHolder {
//        CardView cv;
        TextView event_type, event_start, event_end, event_venue;
        ImageButton edit, delete;

        CardChildViewHolder(View itemView) {
            super(itemView);
//            cv = (CardView)itemView.findViewById(R.id.card_view);
            event_type = (TextView)itemView.findViewById(R.id.excard_event_type);
            event_start = (TextView)itemView.findViewById(R.id.excard_event_starttime);
            event_end = (TextView)itemView.findViewById(R.id.excard_event_endtime);
            edit = (ImageButton)itemView.findViewById(R.id.excard_edit);
            delete = (ImageButton)itemView.findViewById(R.id.excard_delete);
        }


    }

}