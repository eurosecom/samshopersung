package com.eusecom.samfantozzi.realm;

import io.realm.RealmObject;

public class RealmAccount extends RealmObject {

    private String accname;
    private String accnumber;
    private String accdoc;
    private String accdov;
    //acctype 1=receipt cash, 2=expense cash...
    private String acctype;
    private String datm;
    private String logprx;

    public String getAccname() {
        return accname;
    }

    public void setAccname(String accname) {
        this.accname = accname;
    }

    public String getAccnumber() {
        return accnumber;
    }

    public void setAccnumber(String accnumber) {
        this.accnumber = accnumber;
    }

    public String getAccdoc() {
        return accdoc;
    }

    public void setAccdoc(String accdoc) {
        this.accdoc = accdoc;
    }

    public String getAccdov() {
        return accdov;
    }

    public void setAccdov(String accdov) {
        this.accdov = accdov;
    }

    public String getAcctype() {
        return acctype;
    }

    public void setAcctype(String acctype) {
        this.acctype = acctype;
    }

    public String getLogprx() {
        return logprx;
    }

    public void setLogprx(String logprx) {
        this.logprx = logprx;
    }

    public String getDatm() {
        return datm;
    }

    public void setDatm(String datm) {
        this.datm = datm;
    }
}

