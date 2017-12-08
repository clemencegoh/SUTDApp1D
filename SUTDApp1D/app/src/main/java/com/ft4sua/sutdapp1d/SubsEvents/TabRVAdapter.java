package com.ft4sua.sutdapp1d.SubsEvents;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.ft4sua.sutdapp1d.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by swonlek on 7/12/2017.
 */

public class TabRVAdapter extends RecyclerView.Adapter<TabRVAdapter.TabViewHolder>{
    List<Tabbed> tabbeds;
    public TabRVAdapter(List<Tabbed> tabbeds) {
        this.tabbeds = tabbeds;
    }


    public static class TabViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        CheckBox checkBox;
        TextView event_details, event_name;

        public TabViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view1);
            event_name = (TextView) itemView.findViewById(R.id.tab_event_name1);
            checkBox = (CheckBox)itemView.findViewById(R.id.tab_event_check);
            event_details = (TextView)itemView.findViewById(R.id.tab_event_details1);
        }
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public TabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_events, parent, false);
        TabViewHolder tabViewHolder = new TabViewHolder(v);
        return tabViewHolder;
    }

    @Override
    public void onBindViewHolder(TabViewHolder holder, int position) {
        Tabbed tabbed = tabbeds.get(position);
//        holder.event_name.setChecked(true);
        holder.event_name.setText(tabbed.event_name);
        holder.event_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.event_details.setText(tabbed.event_details);
    }

    @Override
    public int getItemCount() {
        return tabbeds.size();
    }




}
