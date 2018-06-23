package com.eusecom.samfantozzi;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

/**
 * Demonstrates usage of basic LifecycleObserver.
 */
public class DomainsLifecycleObserver implements LifecycleObserver {
    private static final String LOG_TAG = DomainsLifecycleObserver.class.getSimpleName();

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        Log.d(LOG_TAG, "DomainsViewModel resumed observing lifecycle.");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        Log.d(LOG_TAG, "DomainsViewModel paused observing lifecycle.");
    }
}
