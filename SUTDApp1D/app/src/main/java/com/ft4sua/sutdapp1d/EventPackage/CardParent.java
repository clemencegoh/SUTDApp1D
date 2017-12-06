package com.ft4sua.sutdapp1d.EventPackage;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;
import java.util.UUID;

/**
 * Created by swonlek on 6/12/2017.
 */

public class CardParent implements ParentObject {
    private List<Object> expandedList;
    private String event_name, event_details;

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_details() {
        return event_details;
    }

    public void setEvent_details(String event_details) {
        this.event_details = event_details;
    }

    public CardParent(String event_name, String event_details) {
        this.event_name = event_name;
        this.event_details = event_details;
    }

    @Override
    public List<Object> getChildObjectList() {
        return expandedList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        expandedList = list;
    }
}
