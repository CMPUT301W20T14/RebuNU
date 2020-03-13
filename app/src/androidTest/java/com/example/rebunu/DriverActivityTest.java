package com.example.rebunu;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DriverActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class,
            true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void checkDriver() {
        solo.assertCurrentActivity("Wrong city", MainActivity.class);
        solo.clickOnButton("LOG IN");
        assertTrue(solo.waitForText("PASSWORD", 1, 2000));

        solo.enterText(0, "1");
        solo.enterText(1, "1");
        solo.clickOnButton("LOG IN");
        solo.assertCurrentActivity("Wrong activity", DriverActivity.class);
    }
}