package com.eusecom.samshopersung;

import android.content.SharedPreferences;
import javax.inject.Inject;

public class ImageUrl {

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    public ImageUrl() {

    }

    public String getUrl(String cis) {

        String url = Constants.IMAGE_URL + cis;
        String wimage = mSharedPreferences.getString("wimage", "0");
        if (wimage.equals("0")) {
            url = Constants.IMAGE_URL_SERVER + cis + ".jpg";;
        }

        return url;
    }


}