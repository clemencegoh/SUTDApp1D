package AppLogic.myPortal;

import java.util.Arrays;

/**
 * Created by Clemence on 10/20/2017.
 */

public class myPortalTest {
    public static void main(String[] args) {
        myPortal clemenceProfile = new myPortal();
        secret pass = new secret();
        String[][] s = clemenceProfile.getTimeTableDetails("1002075",pass.getPassword());
    }
}
