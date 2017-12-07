package com.ft4sua.sutdapp1d.DatabasePackage;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lim Ken Jyi on 6/12/2017.
 */

public class EventListParser {

    List<Event> eventList;
    HashMap<Integer,List<Event>> dayEventMap;

    public EventListParser(List<Event> eventList){
        this.eventList = eventList;
    }

    public void dayOrganiser(){
        String mCurrentDate = "";
        int mCurrentDayCount = -1;
        for(Event ii: eventList){
            if(ii.getDate().equals(mCurrentDate)){
                dayEventMap.get(mCurrentDayCount).add(ii);
            }
            else{
                mCurrentDate = ii.getDate();
                mCurrentDayCount+=1;
            }
        }
    }

    public List<Event> getDayList(int day){
        return dayEventMap.get(day);
    }
}
