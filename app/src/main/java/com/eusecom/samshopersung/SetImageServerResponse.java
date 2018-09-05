package com.eusecom.samshopersung;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shaon on 12/3/2016.
 */

public class SetImageServerResponse {
    // variable name should be same as in the json response from php    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;

    String getMessage() {
        return message;
    }

    boolean getSuccess() {
        return success;
    }
}