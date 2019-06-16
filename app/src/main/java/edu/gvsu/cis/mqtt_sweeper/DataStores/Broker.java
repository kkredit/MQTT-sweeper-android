package edu.gvsu.cis.mqtt_sweeper.DataStores;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.joda.time.DateTime;
import org.parceler.Parcel;

@Parcel
public class Broker {
    public String bid;
    public String servername;
    public String url;
    public String username;
    public String password;
    public String _key;


    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getServername() {
        return servername;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
