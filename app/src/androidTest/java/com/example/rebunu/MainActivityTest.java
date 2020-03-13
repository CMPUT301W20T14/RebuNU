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

/**
 * @author Zijian Xi
 * UI Test for MainActivity
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class,
            true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * See if login button working
     */
    @Test
    public void checkLogin() {
        // assert that the current activity is MainActivity
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnButton("LOG IN");
        assertTrue(solo.waitForText("PASSWORD", 1, 2000));
    }

    /**
     * See if sign up button working
     */
    @Test
    public void checkSignUp() {
        solo.assertCurrentActivity("Wrong activity",MainActivity.class);
        solo.clickOnButton("SIGN UP");
        assertTrue(solo.waitForText("CONFIRM PASSWORD"));
        assertTrue(solo.waitForText("", 1, 2000));
    }
}
