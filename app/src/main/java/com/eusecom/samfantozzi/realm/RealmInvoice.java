package com.eusecom.samfantozzi.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

//data class Invoice(var drh : String, var uce : String, var dok : String, var ico: String, var nai: String
//        , var fak: String, var ksy: String, var ssy: String
//        , var ume: String, var dat: String, var daz: String, var das: String, var poz: String
//        , var hod: String, var zk0: String, var zk1: String, var dn1: String, var zk2: String, var dn2: String
//        , var saved: Boolean, var datm: Long, var uzid: String)

public class RealmInvoice extends RealmObject {

    //1=customers invoice, 2=supliers invoice, 31=cash document receipt, 32=cash document expense, 4=bank document, 5=internal document
    private String drh;
    private String uce;
    @PrimaryKey
    private String dok;
    private String ico;
    private String nai;
    private String fak;
    private String ksy;
    private String ssy;
    private String ume;
    private String dat;
    private String daz;
    private String das;
    private String poz;
    private String hod;
    private String zk0;
    private String zk1;
    private String dn1;
    private String zk2;
    private String dn2;
    private String saved;
    private String datm;
    private String uzid;
    private String kto;
    private String poh;

    public String getKto() {
        return kto;
    }

    public void setKto(String kto) {
        this.kto = kto;
    }

    public String getPoh() {
        return poh;
    }

    public void setPoh(String poh) {
        this.poh = poh;
    }

    public String getDrh() {
        return drh;
    }

    public void setDrh(String drh) {
        this.drh = drh;
    }

    public String getUce() {
        return uce;
    }

    public void setUce(String uce) {
        this.uce = uce;
    }

    public String getDok() {
        return dok;
    }

    public void setDok(String dok) {
        this.dok = dok;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getNai() {
        return nai;
    }

    public void setNai(String nai) {
        this.nai = nai;
    }

    public String getFak() {
        return fak;
    }

    public void setFak(String fak) {
        this.fak = fak;
    }

    public String getKsy() {
        return ksy;
    }

    public void setKsy(String ksy) {
        this.ksy = ksy;
    }

    public String getSsy() {
        return ssy;
    }

    public void setSsy(String ssy) {
        this.ssy = ssy;
    }

    public String getUme() {
        return ume;
    }

    public void setUme(String ume) {
        this.ume = ume;
    }

    public String getDat() {
        return dat;
    }

    public void setDat(String dat) {
        this.dat = dat;
    }

    public String getDaz() {
        return daz;
    }

    public void setDaz(String daz) {
        this.daz = daz;
    }

    public String getDas() {
        return das;
    }

    public void setDas(String das) {
        this.das = das;
    }

    public String getPoz() {
        return poz;
    }

    public void setPoz(String poz) {
        this.poz = poz;
    }

    public String getHod() {
        return hod;
    }

    public void setHod(String hod) {
        this.hod = hod;
    }

    public String getZk0() {
        return zk0;
    }

    public void setZk0(String zk0) {
        this.zk0 = zk0;
    }

    public String getZk1() {
        return zk1;
    }

    public void setZk1(String zk1) {
        this.zk1 = zk1;
    }

    public String getDn1() {
        return dn1;
    }

    public void setDn1(String dn1) {
        this.dn1 = dn1;
    }

    public String getZk2() {
        return zk2;
    }

    public void setZk2(String zk2) {
        this.zk2 = zk2;
    }

    public String getDn2() {
        return dn2;
    }

    public void setDn2(String dn2) {
        this.dn2 = dn2;
    }

    public String getSaved() {
        return saved;
    }

    public void setSaved(String saved) {
        this.saved = saved;
    }

    public String getDatm() {
        return datm;
    }

    public void setDatm(String datm) {
        this.datm = datm;
    }

    public String getUzid() {
        return uzid;
    }

    public void setUzid(String uzid) {
        this.uzid = uzid;
    }


}

