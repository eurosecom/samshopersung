package com.eusecom.samshopersung;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import java.util.List;
import rx.observers.TestSubscriber;
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

    @Test
    public void textFromMvvm_isCorrect() throws Exception {

        assertEquals("Mocked String from DataModel", myActivity.textFromMvvm());
    }


    @Test
    public void rxTextFromMvvm_isCorrect2() throws Exception {

        TestSubscriber<List<String>> testSubscriber = new TestSubscriber<>();
        myActivity.getRxString().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertUnsubscribed();
        testSubscriber.assertTerminalEvent();
        String threadname = testSubscriber.getLastSeenThread().getName();
        System.out.println("threadname " + threadname);

        List<List<String>> listresult = testSubscriber.getOnNextEvents();

        String observedstring = listresult.get(0).get(0).toString();
        System.out.println("observedstring " + observedstring);
        Assert.assertEquals(observedstring, "Mocked Rx String from DataModel");

    }



}
