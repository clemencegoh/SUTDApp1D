package AppLogic.myPortal;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Created by Clemence on 10/20/2017.
 */

public class myPortal {
    private String url =
            "https://myportal.sutd.edu.sg/psp/EPPRD/?&cmd=login&languageCd=ENG";
    private String userId;
    private String password;

    public myPortal() {
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private Document timeTableHtmlBody(){
        try{
            Connection.Response init = Jsoup.
                    connect(this.url).
                    validateTLSCertificates(false).followRedirects(true).
                    method(Connection.Method.GET).execute();
            Connection.Response login = Jsoup.
                    connect(this.url).
                    validateTLSCertificates(false).followRedirects(true).
                    data("userid",this.userId).
                    data("pwd",this.password).
                    cookies(init.cookies()).
                    method(Connection.Method.POST).
                    execute();

            Document doc = Jsoup.
                    connect("https://sams.sutd.edu.sg/psc/CSPRD/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SSR_SSENRL_SCHD_W.GBL").
                    validateTLSCertificates(false).
                    followRedirects(true).
                    cookies(login.cookies()).
                    get();


            System.out.println("Connection successful!");
            return doc;

        }catch(Exception ex) {
            System.out.println("Exception found");
            System.out.println(ex);
        }
        // JSOUP Automatically closes connection after each request, no need for
        // a finally block.
        return null;
    }

    // constructTable reconstructs the timetable based on the raw data given
    private String[] constructTable(Elements rawData) {
        // TODO: pointerA might be redundant, deprecate eventually
        int ptA = 0;
        int ptB = 1;

        String[] table = new String[98]; //Max size
        try {
            System.out.printf("data size: %s\n",rawData.size());
            for (Element data : rawData) {
                System.out.printf("data longtext: %s\n",data.text());
                while (table[ptB] != null) {
                    ptB++;
                    System.out.printf("Node skipped, not nil ptB: %s\n",ptB);
                }
                if (data.text().length() == 1) {
                    table[ptB] = " - ";

                }
                else {
                    if (data.text().length() > 13) {

                        //TODO: logic for event handling here
                        // Idea here is that this would be the first time it touches
                        // the event, then skips it later, due to the not null catcher

                        Pattern p = Pattern.compile("(1[012]|[1-9]):[0-5][0-9]?(am|pm|AM|PM)");
                        Matcher m = p.matcher(data.text());
                        int count = 0;
                        int[] time = new int[2];

                        // Note that only 2 times should be stated

                        while (m.find()){

                            // Time is PM, change to 24 hour
                            if (m.group().contains("PM")){
                                String[] temp1 = m.group().split(":");
                                int tempTime = Integer.parseInt(temp1[0]);
                                if (tempTime!=12){
                                    time[count] = Integer.parseInt(temp1[0]) + 12;
                                }else{
                                    time[count] = Integer.parseInt(temp1[0]);
                                }
                                if (count==1){
                                    if (temp1[1].contains("30")) time[count] += 1;
                                }
                            }
                            // Time is AM
                            else{
                                String[] temp1 = m.group().split(":");
                                time[count] = Integer.parseInt(temp1[0]);
                                if (count==1){
                                    if (temp1[1].contains("30")) time[count] += 1;
                                }
                            }
                            count++;
                            if (count > 1) count=0;
                        }

                        int timeDiff = time[1]-time[0];
                        System.out.printf("Timediff: %s\n",timeDiff);

                        for (int i=0;i<timeDiff;i++){
                            table[ptB+8*i] = data.text();
                        }


                    }

                    // Put data in the table
                    table[ptB] = data.text();

                }
                ptB++;
                if ((ptB - ptA) > 7) {
                    ptA += 8;
                    System.out.printf("PointerA: %s\n",ptA);
                    table[ptA - 1] += "\n";
                }

                System.out.printf("PTB: %s\n",ptB);
            }
            return table;
        }catch (Exception ex){
            System.out.printf("error occured during construction:\n%s\n",ex);
        }
        return null;
        }

    public String[] getTimeTable(){
        // list of size 77 + 7 for the labels
        String[] timetable = new String[84];

        // Get html body from a connection
        Document doc = this.timeTableHtmlBody();

        // First round of parsing to look for elements needed
        System.out.println("Start of Document Parsing...\n");
        try{
            Elements overall = doc.
                    select(".SSSWEEKLYDAYBACKGROUND," +
                            ".SSSTEXTWEEKLYTIME," +
                            ".PSLEVEL3GRID," +
                            ".SSSTEXTWEEKLY");

            // Second round of parsing to construct the timetable from data
            timetable = this.constructTable(overall);
            System.out.println(Arrays.toString(timetable));

        }catch(Exception ex){
            System.out.printf("Error at parsing table stage:\n%s\n",ex);
        }finally {
            System.out.println("Parsing of table done.");
        }
        return timetable;
    }
}
