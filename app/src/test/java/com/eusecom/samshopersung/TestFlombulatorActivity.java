package com.eusecom.samshopersung;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import static junit.framework.Assert.assertEquals;

@Config(constants = BuildConfig.class, sdk = 21, application = TestApplication.class)
@RunWith(RobolectricTestRunner.class)
public class TestFlombulatorActivity {

    private FlombulatorActivity myActivity;

    @Before
    public void setUp() throws Exception {

        myActivity = Robolectric.buildActivity(FlombulatorActivity.class).create().visible().get();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application);
        sharedPreferences.edit().putString("servername", "www.eshoptest.sk").commit();

    }

    @Test
    public void flombulatedtext_isCorrect() throws Exception {

        assertEquals("flombulated test", myActivity.flumbolate());
    }

    @Test
    public void textFromPref_isCorrect() throws Exception {

        assertEquals("From act www.eshoptest.sk", myActivity.textFromPref());
    }

}
