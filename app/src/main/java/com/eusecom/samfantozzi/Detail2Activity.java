package com.eusecom.samfantozzi;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import javax.inject.Inject;
import dagger.android.AndroidInjection;


public class Detail2Activity extends AppCompatActivity {

    @Inject
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainfantozzi);

        String serverx = prefs.getString("servername", "");
        Toast.makeText(Detail2Activity.this, serverx, Toast.LENGTH_SHORT).show();


    }


}
