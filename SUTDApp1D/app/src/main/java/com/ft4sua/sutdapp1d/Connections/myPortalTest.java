package com.ft4sua.sutdapp1d.Connections;

import android.provider.ContactsContract;

import com.ft4sua.sutdapp1d.DatabasePackage.Event;

import java.util.Arrays;


/**
 * Created by Clemence on 10/20/2017.
 */

public class myPortalTest {
    public static void main(String[] args) {
        myPortal profile = new myPortal();
        String username = "1002075";
        String password = "Sde3354k";
        try{
            Event[] s = profile.timeTable(username,password);
            System.out.println(s[0].getName());
            System.out.println(s[0].getDate());
            System.out.println(s[0].getStart());
            System.out.println(s[0].getEnd());
            System.out.println(profile.getEmptyDays().get(0).getDate());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
