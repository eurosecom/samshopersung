package com.eusecom.samfantozzi.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmDomain extends RealmObject {

    @PrimaryKey
    private String domain;
    private String prm1;
    private String prm2;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPrm1() {
        return prm1;
    }

    public void setPrm1(String prm1) {
        this.prm1 = prm1;
    }

    public String getPrm2() {
        return prm2;
    }

    public void setPrm2(String prm2) {
        this.prm2 = prm2;
    }
}

