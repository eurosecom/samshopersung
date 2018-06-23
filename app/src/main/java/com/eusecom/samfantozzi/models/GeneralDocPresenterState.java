package com.eusecom.samfantozzi.models;

import java.io.Serializable;

public class GeneralDocPresenterState implements Serializable {

    public String querystring;
    public String string2;
    public String string3;

    public GeneralDocPresenterState() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public GeneralDocPresenterState(String querystring, String string2, String string3) {
        this.querystring = querystring;
        this.string2 = string2;
        this.string3 = string3;
    }

    public String getQuerystring() {
        return querystring;
    }

    public void setQuerystring(String querystring) {
        this.querystring = querystring;
    }

    public String getString2() {
        return string2;
    }

    public void setString2(String string2) {
        this.string2 = string2;
    }

    public String getString3() {
        return string3;
    }

    public void setString3(String string3) {
        this.string3 = string3;
    }



}
