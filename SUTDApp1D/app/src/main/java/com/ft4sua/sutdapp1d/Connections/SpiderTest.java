package Android1D.SUTDApp1D.app.src.main.java.com.ft4sua.sutdapp1d.Connections;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Yu Jin on 10/20/2017.
 */

public class SpiderTest
{
    /**
     * This is our test. It creates a spider (which creates spider legs) and crawls the web.
     *
     * @param args
     *            - not used
     */
    public static void main(String[] args)
    {
        Spider2 b = new Spider2();
        b.crawl("http://root.sutd.edu.sg/2017/09/25/tldr-25-sep-17-issue/");
        ArrayList<Event> a = b.search();

        b.crawl("http://root.sutd.edu.sg/2017/10/02/tldr-2-oct-17-issue/");
        a = b.search();
    }
}
