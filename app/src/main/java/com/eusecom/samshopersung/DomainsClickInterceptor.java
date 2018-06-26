package com.eusecom.samshopersung;

import android.util.Log;

public class DomainsClickInterceptor {
    private static final String LOG_TAG = DomainsClickInterceptor.class.getSimpleName();

    public void intercept(int clickCount) {
        Log.d(LOG_TAG, "processed click " + clickCount);
    }
}
