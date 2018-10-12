package com.eusecom.samshopersung.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by eurosecom
 */

@Entity(indices = {@Index(value = {"requestUuid"}, unique = true)})
public class EkassaRequestBackup {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "requestUuid")
    private String requestUuid;

    @ColumnInfo(name = "requestDate")
    private String requesDate;

    @ColumnInfo(name = "SendingCount")
    private int SendingCount;

    @ColumnInfo(name = "receiptNumber")
    private String receiptNumber;

    @ColumnInfo(name = "responseUuid")
    private String responseUuid;

    @ColumnInfo(name = "processDate")
    private String processDate;

    @ColumnInfo(name = "receiptDataId")
    private String receiptDataId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequestUuid() {
        return requestUuid;
    }

    public void setRequestUuid(String requestUuid) {
        this.requestUuid = requestUuid;
    }

    public String getRequesDate() {
        return requesDate;
    }

    public void setRequesDate(String requesDate) {
        this.requesDate = requesDate;
    }

    public int getSendingCount() {
        return SendingCount;
    }

    public void setSendingCount(int sendingCount) {
        SendingCount = sendingCount;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getResponseUuid() {
        return responseUuid;
    }

    public void setResponseUuid(String responseUuid) {
        this.responseUuid = responseUuid;
    }

    public String getProcessDate() {
        return processDate;
    }

    public void setProcessDate(String processDate) {
        this.processDate = processDate;
    }

    public String getReceiptDataId() {
        return receiptDataId;
    }

    public void setReceiptDataId(String receiptDataId) {
        this.receiptDataId = receiptDataId;
    }
}
