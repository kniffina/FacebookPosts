package com.kniffina.android.facebookposts;

/**
 * Created by adamj on 6/10/2017.
 */

//simple class to store date and message of facebook data. We can build it out
public class FacebookData {

    private String date;
    private String message;
    private String id;

    public FacebookData(String d, String m, String i) {
        this.date = d;
        this.message = m;
        this.id = i;
    }

    public String getDate() { return this.date;}
    public String getMessage(){ return this.message; }
    public String getId() { return this.id; }
}