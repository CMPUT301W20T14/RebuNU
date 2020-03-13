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
public class SignUpActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class,
            true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * See if sign up button working
     */
    @Test
    public void checkSignUp() {
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnButton("SIGN UP");
        assertTrue(solo.waitForText("CONFIRM PASSWORD"));
        solo.enterText(0, "name");
        solo.enterText(1, "ttt@ualberta.ca");
        solo.enterText(2, "587-447-2294");
        solo.enterText(3, "111");
        solo.enterText(4, "111");
        solo.clickOnRadioButton(1);
        solo.clickOnButton("FINISH");
        assertTrue(solo.waitForText("CONFIRM PASSWORD", 1, 2000));
    }
}
