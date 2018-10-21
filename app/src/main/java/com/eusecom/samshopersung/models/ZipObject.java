package com.eusecom.samshopersung.models;

import com.eusecom.samshopersung.Invoice;

public class ZipObject {

    public int number;
    public String alphabet;
    public Invoice invx;
    public EkassaRequestBackup reqx;

    public ZipObject() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }

    public Invoice getInvx() {
        return invx;
    }

    public void setInvx(Invoice invx) {
        this.invx = invx;
    }

    public EkassaRequestBackup getReqx() {
        return reqx;
    }

    public void setReqx(EkassaRequestBackup reqx) {
        this.reqx = reqx;
    }
}

