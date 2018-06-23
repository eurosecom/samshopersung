package com.eusecom.samfantozzi.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankItemList {

	@SerializedName("bankitems")
    private List<BankItem> bankitem;
    @SerializedName("balance")
    private String balance;

    public List<BankItem> getBankitem() {
        return bankitem;
    }

    public String getBalance() {
        return balance;
    }
}