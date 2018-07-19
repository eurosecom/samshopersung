package com.eusecom.samshopersung;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import static junit.framework.Assert.assertEquals;

@Config(constants = BuildConfig.class, sdk = 21, application = TestApplication.class)
@RunWith(RobolectricTestRunner.class)
public class TestFlombulatorActivity {

    private FlombulatorActivity myActivity;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void flombulatedtext_isCorrect() throws Exception {
        myActivity = Robolectric.buildActivity(FlombulatorActivity.class).create().visible().get();

        assertEquals("flombulated test", myActivity.flumbolate());
    }

}
