package edu.gvsu.cis.mqtt_sweeper.DataStores;

import org.parceler.Parcel;

@Parcel
public class Topic {
    public String topic;
    public String message;
    public String _key;

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }

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
