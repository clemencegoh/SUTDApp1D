package com.ft4sua.sutdapp1d.Connections;

/**
 * Created by Yu Jin on 10/20/2017.
 */

import com.ft4sua.sutdapp1d.DatabasePackage.Event;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Calendar;

public class Spider2 {
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links = new LinkedList<String>();
    private Document htmlDocument;
    public boolean crawled = false;


    public boolean crawl(String url)
    {
        try
        {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;
            if(connection.response().statusCode() == 200) // 200 is the HTTP OK status code
            // indicating that everything is great.
            {
                System.out.println("\n**Visiting** Received web page at " + url);
            }
            if(!connection.response().contentType().contains("text/html"))
            {
                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }
            Elements linksOnPage = htmlDocument.select("a[href]");
            System.out.println("Found (" + linksOnPage.size() + ") links");
            for(Element link : linksOnPage)
            {
                this.links.add(link.absUrl("href"));
            }
            crawled = true;
            return true;
        }
        catch(IOException ioe)
        {
            // We were not successful in our HTTP request
            return false;
        }
    }


    /**
     * Performs a search on the body of on the HTML document that is retrieved. This method should
     * only be called after a successful crawl.
     *
     *            - The word or string to look for
     * @return whether or not the word was found
     */
    public ArrayList<Event> search()
    {
        // Defensive coding. This method should only be used after a successful crawl.
        if (crawled == false){
            System.out.println("Crawl(link) first");
            return new ArrayList<Event>();
            //return new ArrayList<String[]>();
        }

        if(this.htmlDocument == null)
        {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return new ArrayList<Event>();
            //return new ArrayList<String[]>();
        }
        System.out.println("Searching for the word " + "...");

        String bodyText = this.htmlDocument.body().text();
        String body = this.htmlDocument.body().toString();
        String[] daysinweek = {"MON ", "TUE ", "WED ", "THU ", "FRI ", "please refer to the latest Communications Guide"};
        Set<Character> numbers = new HashSet<Character>();
        numbers.add('0');numbers.add('1');numbers.add('2');numbers.add('3');numbers.add('4');numbers.add('5');numbers.add('6');
        numbers.add('7');numbers.add('8');numbers.add('9');
        ArrayList<Event> week_events = new ArrayList<Event>();
        //ArrayList<String[]> week_events = new ArrayList<>();
        String[] monthlist = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        HashMap<String,Integer> monthconvert = new HashMap<>();
        monthconvert.put("Jan",0); monthconvert.put("Feb",1); monthconvert.put("Mar",2); monthconvert.put("Apr", 3);
        monthconvert.put("May",4); monthconvert.put("Jun",5); monthconvert.put("Jul",6); monthconvert.put("Aug", 7);
        monthconvert.put("Sep",8); monthconvert.put("Oct",9); monthconvert.put("Nov",10); monthconvert.put("Dec",11);
        int searchpoint;
        String searchlocation;

        for (int i = 0; i < 5; i++) {
            int x = bodyText.indexOf(daysinweek[i]);
            int y = bodyText.indexOf(daysinweek[i + 1]);
            if (x != -1) {
                String rawevent = bodyText.substring(x, y);
                while (rawevent.contains("Read more")) {
                    String[] event = new String[6];
                    String date;
                    int year = Calendar.getInstance().get(Calendar.YEAR);
                    String yearstring = Integer.toString(year);
                    int dateindex= bodyText.indexOf(", " + yearstring);
                    int nextyearindex = bodyText.indexOf(", "+ Integer.toString(year + 1));
                    int prevyearindex = bodyText.indexOf(", "+ Integer.toString(year - 1));
                    int correctyear;
                    if (dateindex != -1) {
                        correctyear = year;
                        if (numbers.contains(bodyText.charAt(dateindex - 2))) {
                            date = bodyText.substring(dateindex - 2, dateindex);
                        } else {
                            date = bodyText.substring(dateindex - 1, dateindex);
                        }
                    } else if (nextyearindex != -1){
                        correctyear = year+1;
                        if (numbers.contains(bodyText.charAt(nextyearindex-2))){
                            date = bodyText.substring(nextyearindex-2,nextyearindex);
                        } else {
                            date = bodyText.substring(nextyearindex-1,nextyearindex);
                        }
                    } else {
                        correctyear = year-1;
                        if (numbers.contains(bodyText.charAt(prevyearindex-2))){
                            date = bodyText.substring(prevyearindex-2,prevyearindex);
                        } else {
                            date = bodyText.substring(prevyearindex-1,prevyearindex);
                        }
                    }

                    dateindex = bodyText.indexOf(" ", 12);
                    event[0] = daysinweek[i].substring(0, 3) + "," + date + bodyText.substring(dateindex,dateindex+4);
                    String month = bodyText.substring(dateindex+1,dateindex+4);
                    if (month.equals("Jan") || month.equals("Mar") || month.equals("May") || month.equals("Jul")
                            || month.equals("Aug") || month.equals("Oct") || month.equals("Dec")){
                        int newdate = Integer.parseInt(date) + i;
                        if ((newdate/31) > 0){
                            String newmonth = monthlist[(monthconvert.get(month) + 1)%12];
                            event[0] = daysinweek[i].substring(0, 3) + ", " + Integer.toString(newdate%31) + " " + newmonth + " " + correctyear;
                        } else {
                            event[0] = daysinweek[i].substring(0, 3) + ", " + newdate + " " + month + " " + correctyear;
                        }
                    } else if (month.equals("Apr") || month.equals("Jun") ||month.equals("Sep")
                            || month.equals("Nov") ){
                        int newdate = Integer.parseInt(date) + i;
                        if ((newdate/30) > 0){
                            String newmonth = monthlist[monthconvert.get(month) + 1];
                            event[0] = daysinweek[i].substring(0, 3) + ", " +Integer.toString(newdate%30) + " " + newmonth + " " + correctyear;
                        }else {
                            event[0] = daysinweek[i].substring(0, 3) + ", " + newdate + " " + month + " " + correctyear;
                        }
                    } else if (month.equals("Feb")){
                        int newdate = Integer.parseInt(date) + i;
                        int leap1 = year %400;
                        int leap2 = leap1 % 100;
                        int leap3 = leap2 %4;
                        int leapyear;
                        if (leap1 == 0) {
                            leapyear = 1;
                        } else if (leap3 == 0 && leap2 != 0){
                            leapyear = 1;
                        } else {
                            leapyear = 0;
                        }
                        if ((newdate/(28+leapyear)) > 0){
                            String newmonth = monthlist[monthconvert.get(month) + 1];
                            event[0] = daysinweek[i].substring(0, 3) + ", " +Integer.toString(newdate%(28+leapyear)) + " " + newmonth + " " + correctyear;
                        }else {
                            event[0] = daysinweek[i].substring(0, 3) + ", " + newdate + " " + month + " " + correctyear;
                        }
                    }
                    int timeindex = rawevent.indexOf("pm,");
                    if ((timeindex == -1)||(timeindex>rawevent.indexOf("Read more"))){
                        timeindex = rawevent.indexOf("am,");
                        int spaceindex = rawevent.indexOf(" ", timeindex - 5);
                        event[1] = rawevent.substring(4, spaceindex);
                        String rawtime = rawevent.substring(spaceindex + 1, timeindex);
                        if (rawtime.length() == 1){
                            event[2] = "0" + rawtime + "00";
                        } else if (rawtime.length() == 2){
                            event[2] = rawtime + "00";
                        } else if (rawtime.length() == 3){
                            event[2] = "0" + rawtime;
                        } else {
                            event[2] = rawtime;
                        }
                        int val = Integer.parseInt(event[2].substring(0,2)) + 2;
                        if (val < 10){
                            event[3] = "0" + Integer.toString(val) + event[2].substring(2,4);
                        } else {
                            event[3] = Integer.toString(val) + event[2].substring(2, 4);
                        }

                    } else {
                        int spaceindex = rawevent.indexOf(" ", timeindex - 5);
                        event[1] = rawevent.substring(4, spaceindex);
                        int rawtime = Integer.parseInt(rawevent.substring(spaceindex + 1, timeindex));
                        if (rawtime < 13){
                            event[2] = Integer.toString(rawtime*100 + 1200);
                            if (rawtime < 10){
                                event[3] = Integer.toString((rawtime+2)*100 + 1200);
                            } else {
                                event[3] = Integer.toString((rawtime+2-12)*100 + 1200);
                            }
                        } else {
                            event[2] = Integer.toString(rawtime + 1200);
                            if (rawtime < 1000){
                                event[3] = Integer.toString(rawtime+200 + 1200);
                            } else {
                                event[3] = Integer.toString((rawtime-1000) + 1200);
                            }
                        }
                    }
                    StringBuilder venue = new StringBuilder();
                    int breakpoint = body.indexOf("Read more");
                    while (breakpoint < 600){
                        body = body.substring(body.indexOf("Read more")+6);
                        breakpoint = body.indexOf("Read more");
                    }
                    searchpoint = body.indexOf("pm,",body.indexOf(event[1]));
                    if ((searchpoint == -1) || (searchpoint > body.indexOf("Read more"))) {
                        searchpoint = body.indexOf("am,",body.indexOf(event[1]));
                    }

                    searchlocation = body.substring(searchpoint+4, searchpoint + 70);
                    for (int j =0; j < 70; j++){
                        if (rawevent.charAt(timeindex+4+j) == searchlocation.charAt(0+j)){
                            venue.append(rawevent.charAt(timeindex+4+j));
                        } else {
                           // venue.delete(venue.length()-1,venue.length());
                            break;
                        }
                    }
                    event[4] = venue.toString();
                    rawevent = rawevent.substring(rawevent.indexOf("Read more") + 6);
                    body = body.substring(body.indexOf("Read more")+6);
                    event[5] = "School event";
                    Event event1 = new Event(event[1],event[0],event[3],event[4],event[5],"1");
                    week_events.add(event1);
                }
            }
        }

        return week_events;
    }

}