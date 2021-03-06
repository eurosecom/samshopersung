package com.eusecom.samshopersung.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by eurosecom
 */

@Entity
public class Payment {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pay")
    private int pay;

    @ColumnInfo(name = "nap")
    private String nap;

    @ColumnInfo(name = "prm1")
    private int prm1;

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public String getNap() {
        return nap;
    }

    public void setNap(String nap) {
        this.nap = nap;
    }

    public int getPrm1() {
        return prm1;
    }

    public void setPrm1(int prm1) {
        this.prm1 = prm1;
    }
}
