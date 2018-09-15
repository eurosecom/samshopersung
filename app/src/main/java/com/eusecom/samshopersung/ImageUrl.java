package com.eusecom.samshopersung;

import android.content.SharedPreferences;
import javax.inject.Inject;

public class ImageUrl {

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    public ImageUrl() {

    }

    public String getUrlJpg(String cis) {

        String url = Constants.IMAGE_URL + cis;
        String wimage = mSharedPreferences.getString("wimage", "0");
        String wfir = mSharedPreferences.getString("fir", "0");
        if (wimage.equals("0")) {
            url = Constants.IMAGE_URL_SERVER + wfir + Constants.IMAGE_URL_SERVER_END  + cis + ".jpg";;
        }

        return url;
    }

    public String getUrlPng(String cis) {

        String url = Constants.IMAGE_URL + cis;
        String wimage = mSharedPreferences.getString("wimage", "0");
        String wfir = mSharedPreferences.getString("fir", "0");
        if (wimage.equals("0")) {
            url = Constants.IMAGE_URL_SERVER + wfir + Constants.IMAGE_URL_SERVER_END  + cis + ".png";;
        }

        return url;
    }


}