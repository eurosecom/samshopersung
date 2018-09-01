package com.eusecom.samshopersung.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by eurosecom
 */

@Entity
public class Transport {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "trans")
    private int trans;

    @ColumnInfo(name = "nat")
    private String nat;

    @ColumnInfo(name = "prm1")
    private int prm1;

    public int getTrans() {
        return trans;
    }

    public void setTrans(int trans) {
        this.trans = trans;
    }

    public String getNat() {
        return nat;
    }

    public void setNat(String nat) {
        this.nat = nat;
    }

    public int getPrm1() {
        return prm1;
    }

    public void setPrm1(int prm1) {
        this.prm1 = prm1;
    }
}
