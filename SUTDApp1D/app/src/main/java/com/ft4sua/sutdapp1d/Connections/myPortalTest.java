package com.ft4sua.sutdapp1d.Connections;

import android.provider.ContactsContract;

import com.ft4sua.sutdapp1d.DatabasePackage.Event;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Clemence on 10/20/2017.
 */

public class myPortalTest {
    public static void main(String[] args) {
        myPortal profile = new myPortal();
        String username = "";
        String password = "";
        try{
            ArrayList<Event> s = profile.timeTable("1002208","1hcoatBs");
            System.out.println(s.get(0).getName());
            System.out.println(s.get(0).getDate());
        }catch(Exception ex){
            System.out.println("Login failed");
        }
    }
}
