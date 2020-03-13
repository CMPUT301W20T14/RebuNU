//package com.example.rebunu;
//
//import android.app.Activity;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import androidx.test.platform.app.InstrumentationRegistry;
//
//import com.robotium.solo.Solo;
//import androidx.test.rule.ActivityTestRule;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//
//import static junit.framework.TestCase.assertEquals;
//import static junit.framework.TestCase.assertFalse;
//import static junit.framework.TestCase.assertTrue;
//
//public class LoginActivityTest {
//
//    private Solo solo;
//
//    @Rule
//    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);
//
//    @Before
//    public void setUp() throws Exception{
//        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
//    }
//
//    @Test
//    public void start() throws Exception{
//        Activity activity = rule.getActivity();
//    }
//
//    @Test
//    public void checkList(){
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//        solo.clickOnButton("ADD CITY");
//        solo.enterText((EditText)solo.getView(R.id.editText_name), "Edmonton");
//        solo.clickOnButton("CONFIRM");
//        solo.clearEditText((EditText)solo.getView(R.id.editText_name));
//        assertTrue(solo.waitForText("Edmonton", 1, 2000));
//
//        solo.clickInList(0);
//        assertTrue(solo.waitForActivity(showActivity.class, 2000));
//        assertTrue(solo.searchText("Edmonton"));
//
//        solo.clickOnButton("BACK");
//        assertTrue(solo.waitForActivity(MainActivity.class, 2000));
//
//        solo.clickOnButton("ClEAR ALL");
//        assertFalse(solo.searchText("Edmonton"));
//    }
//
//
//    @Test
//    public void checkListItem(){
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//        solo.clickOnButton("ADD CITY");
//        solo.enterText((EditText)solo.getView(R.id.editText_name), "Edmonton");
//        solo.clickOnButton("CONFIRM");
//
//        assertTrue(solo.waitForText("Edmonton", 1, 2000));
//        MainActivity activity = (MainActivity) solo.getCurrentActivity();
//        final ListView cityList = activity.cityList;
//        String city = (String) cityList.getItemAtPosition(0);
//
//        assertEquals("Edmonton", city);
//
//        solo.clickInList(0);
//        assertTrue(solo.waitForActivity(showActivity.class, 2000));
//        showActivity activityShow = (showActivity) solo.getCurrentActivity();
//        TextView textView = activityShow.textView;
//        String text = textView.getText().toString();
//
//        assertEquals("Edmonton", text);
//
//
//
//    }
//
//    @After
//    public void tearDown() throws Exception{
//        solo.finishOpenedActivities();
//    }
//
//
//}
