package com.eusecom.samshopersung.models;

import com.eusecom.samshopersung.Invoice;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class InvoiceList {

	@SerializedName("invoices")
    private List<Invoice> invoice;
    @SerializedName("balance")
    private String balance;

    public List<Invoice> getInvoice() {
        return invoice;
    }

    public String getBalance() {
        return balance;
    }
}