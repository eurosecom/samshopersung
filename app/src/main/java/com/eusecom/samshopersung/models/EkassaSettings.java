package com.eusecom.samshopersung.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by eurosecom
 */

@Entity(indices = {@Index(value = {"id"}, unique = true)})
public class EkassaSettings {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "compname")
    private String compname;

    @ColumnInfo(name = "headquarters")
    private String headquarters;

    @ColumnInfo(name = "shopadress")
    private String shopadress;

    @ColumnInfo(name = "compidc")
    private int compidc;

    @ColumnInfo(name = "compdic")
    private String compdic;

    @ColumnInfo(name = "compicd")
    private String compicd;

    @ColumnInfo(name = "dkp")
    private String dkp;

    @ColumnInfo(name = "orsr")
    private String orsr;

    @ColumnInfo(name = "denna")
    private int denna;

    @ColumnInfo(name = "mesacna")
    private int mesacna;

    @ColumnInfo(name = "pata1")
    private String pata1;

    @ColumnInfo(name = "pata2")
    private String pata2;


    public EkassaSettings() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompname() {
        return compname;
    }

    public void setCompname(String compname) {
        this.compname = compname;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public String getShopadress() {
        return shopadress;
    }

    public void setShopadress(String shopadress) {
        this.shopadress = shopadress;
    }

    public int getCompidc() {
        return compidc;
    }

    public void setCompidc(int compidc) {
        this.compidc = compidc;
    }

    public String getCompdic() {
        return compdic;
    }

    public void setCompdic(String compdic) {
        this.compdic = compdic;
    }

    public String getCompicd() {
        return compicd;
    }

    public void setCompicd(String compicd) {
        this.compicd = compicd;
    }

    public String getDkp() {
        return dkp;
    }

    public void setDkp(String dkp) {
        this.dkp = dkp;
    }

    public String getOrsr() {
        return orsr;
    }

    public void setOrsr(String orsr) {
        this.orsr = orsr;
    }

    public int getDenna() {
        return denna;
    }

    public void setDenna(int denna) {
        this.denna = denna;
    }

    public int getMesacna() {
        return mesacna;
    }

    public void setMesacna(int mesacna) {
        this.mesacna = mesacna;
    }

    public String getPata1() {
        return pata1;
    }

    public void setPata1(String pata1) {
        this.pata1 = pata1;
    }

    public String getPata2() {
        return pata2;
    }

    public void setPata2(String pata2) {
        this.pata2 = pata2;
    }
}
