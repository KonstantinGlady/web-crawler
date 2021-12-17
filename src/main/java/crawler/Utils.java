package crawler;

import java.net.MalformedURLException;
import java.net.URL;

public class Utils {
    private Utils() {
    }

    public static String getHost(String url) {
        try {
            return new URL(url).getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
