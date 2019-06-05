package com.example.geocalculatorapp;

import org.parceler.Parcel;

@Parcel
public class LocationLookup {
    String origLat;
    String origLng;
    String destLat;
    String destLng;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    String timeStamp;
    String _key;

    public String getOrigLat() {
        return origLat;
    }

    public void setOrigLat(String origLat) {
        this.origLat = origLat;
    }

    public String getOrigLng() {
        return origLng;
    }

    public void setOrigLng(String origLng) {
        this.origLng = origLng;
    }

    public String getDestLat() {
        return destLat;
    }

    public void setDestLat(String destLat) {
        this.destLat = destLat;
    }

    public String getDestLng() {
        return destLng;
    }

    public void setDestLng(String destLng) {
        this.destLng = destLng;
    }

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }
}
