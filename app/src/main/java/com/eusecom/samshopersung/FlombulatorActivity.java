package com.eusecom.samshopersung;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import javax.inject.Inject;
import dagger.android.AndroidInjection;

public class FlombulatorActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    @Inject
    Flombulator flombulator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flumborator);

        String flumx = flumbolate();
        Log.d("Flombulated text", flumx );

    }

    public String flumbolate() {
        return flombulator.flombulateMe();
    }
}
