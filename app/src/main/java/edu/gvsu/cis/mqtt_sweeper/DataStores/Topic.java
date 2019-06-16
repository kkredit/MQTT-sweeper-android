package edu.gvsu.cis.mqtt_sweeper.DataStores;

import org.parceler.Parcel;

@Parcel
public class Topic {
    public String topic;
    public String message;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
