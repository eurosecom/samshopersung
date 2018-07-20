package com.eusecom.samshopersung;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;
import dagger.android.AndroidInjection;

public class FlombulatorActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    @Inject
    FlombulatorI flombulator;

    @Inject
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flumborator);

        String flumx = flumbolate();
        Log.d("Flombulated text", flumx );

        String serverx = "From act " + mSharedPreferences.getString("servername", "");
        Toast.makeText(this, serverx, Toast.LENGTH_SHORT).show();

    }

    public String flumbolate() {
        return flombulator.flombulateMe();
    }
}
