package edu.gvsu.cis.mqtt_sweeper;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import com.fooock.shodan.ShodanRestApi;

public class InternetExposureChecker {

    private String API_KEY;
    private ShodanRestApi api;

    public InternetExposureChecker() {
        Context context = Activity.getApplicationContext().getString(R.string.shodan_api_key);
        api = new ShodanRestApi(API_KEY);
    }

    // Credit to https://stackoverflow.com/a/14541376/11407115
    private static String getIp() throws Exception {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
