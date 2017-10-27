package SpiderSingle;

/**
 * Created by Yu Jin on 10/20/2017.
 */

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class Spider {
    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links = new LinkedList<String>();
    private Document htmlDocument;
    public boolean crawled = false;


    /**
     * This performs all the work. It makes an HTTP request, checks the response, and then gathers
     * up all the links on the page. Perform a searchForWord after the successful crawl
     *
     * @param url
     *            - The URL to visit
     * @return whether or not the crawl was successful
     */
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
    public ArrayList<String[]> search()
    {
        // Defensive coding. This method should only be used after a successful crawl.
        if (crawled == false){
            System.out.println("Crawl(link) first");
            return new ArrayList<String[]>();
        }

        if(this.htmlDocument == null)
        {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return new ArrayList<String[]>();
        }
        System.out.println("Searching for the word " + "...");

        String bodyText = this.htmlDocument.body().text();
        String[] daysinweek = {"MON ", "TUE ", "WED ", "THU ", "FRI ", "please refer to the latest Communications Guide"};
        Set<Character> venuemarkers = new HashSet<Character>();
        ArrayList<String[]> week_events = new ArrayList<String[]>();
        venuemarkers.add(')');venuemarkers.add('1');venuemarkers.add('2');venuemarkers.add('3');venuemarkers.add('4');
        venuemarkers.add('5');venuemarkers.add('6');venuemarkers.add('7');venuemarkers.add('8');venuemarkers.add('9');venuemarkers.add('0');

        for (int i = 0; i < 5; i++) {
            int x = bodyText.indexOf(daysinweek[i]);
            int y = bodyText.indexOf(daysinweek[i + 1]);
            if (x != -1) {
                String rawevent = bodyText.substring(x, y);
                while (rawevent.contains("Read more")) {
                    String[] event = new String[4];
                    event[0] = daysinweek[i].substring(0, 3);
                    int timeindex = rawevent.indexOf("pm");
                    if (timeindex == -1) {
                        timeindex = rawevent.indexOf("am");
                    }
                    int spaceindex = rawevent.indexOf(" ", timeindex - 5);
                    event[1] = rawevent.substring(4, spaceindex);
                    event[2] = rawevent.substring(spaceindex + 1, timeindex + 2);
                    int state = 0;
                    int venueindex = 0;
                    int dist = 0;
                    String venue = "";

                    for (char j : rawevent.substring(timeindex + 4).toCharArray()) {
                        venueindex += 1;
                        if (venueindex > rawevent.indexOf("Read more")){
                            break;
                        }
                        if (state == 0) {
                            dist = 0;
                            if (venuemarkers.contains(j)) {
                                venue = rawevent.substring(timeindex + 4, timeindex + 4 + venueindex);
                            } else if ((!venue.equals("")) && (!venuemarkers.contains(j))) {
                                state = 1;
                            }
                        } else if (state == 1) {
                            if (dist == 0) {
                                event[3] = venue;
                            }
                            dist += 1;
                            if (venuemarkers.contains(j)) {
                                venue = rawevent.substring(timeindex + 4, timeindex + 4 + venueindex);
                                dist = 0;
                            } else if (dist == 20) {
                                break;
                            }
                        }
                    }

                    if (rawevent.indexOf("Centre") != -1) {
                        if ((event[3] == null) && (rawevent.indexOf("Centre") < rawevent.indexOf("Read more"))) {
                            event[3] = rawevent.substring(timeindex + 4, rawevent.indexOf("Centre") + 6);
                        }
                    }

                    if (event[3] == null){
                        event[3] = "Outside of school";
                    }

                    rawevent = rawevent.substring(rawevent.indexOf("Read more") + 6);
                    week_events.add(event);
                }
            }
        }

        return week_events;
    }


    public List<String> getLinks()
    {
        return this.links;
    }

}