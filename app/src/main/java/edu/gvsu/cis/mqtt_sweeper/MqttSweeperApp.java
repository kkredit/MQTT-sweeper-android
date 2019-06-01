package edu.gvsu.cis.mqtt_sweeper;

import android.app.Application;
import net.danlew.android.joda.JodaTimeAndroid;

public class MqttSweeperApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
