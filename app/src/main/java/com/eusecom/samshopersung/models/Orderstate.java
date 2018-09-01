package com.eusecom.samshopersung.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by eurosecom
 */

@Entity
public class Orderstate {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ost")
    private int ost;

    @ColumnInfo(name = "nas")
    private String nas;

    @ColumnInfo(name = "prm1")
    private int prm1;

    public int getOst() {
        return ost;
    }

    public void setOst(int ost) {
        this.ost = ost;
    }

    public String getNas() {
        return nas;
    }

    public void setNas(String nas) {
        this.nas = nas;
    }

    public int getPrm1() {
        return prm1;
    }

    public void setPrm1(int prm1) {
        this.prm1 = prm1;
    }
}
