package com.abhishek.amplyweather;

import android.view.View;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class FirstTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    private String zip = "64112";
    private String city = "Kansas City";
    private String state = "Missouri";

    @Before
    public void setUp() throws Exception {
        //Before Test case execution
    }

    @Test
    public void test1ChatId() {
        onView(isRoot()).perform(waitFor(6000));
        onView(withId(R.id.tv_city)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_state)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_highTempVal)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_lowTempVal)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_currTemp)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_currWeather)).check(matches(isDisplayed()));
        onView(withId(R.id.img_icon)).check(matches(isDisplayed()));

        onView(withId(R.id.tv_day1)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_day1Temp)).check(matches(isDisplayed()));
        onView(withId(R.id.iv_day1Icon)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_day1Weather)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_day1High)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_day1Low)).check(matches(isDisplayed()));

        onView(withId(R.id.tv_day2)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_day2Temp)).check(matches(isDisplayed()));
        onView(withId(R.id.iv_day2Icon)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_day2Weather)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_day2High)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_day2Low)).check(matches(isDisplayed()));

        onView(withId(R.id.tv_day3)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_day3Temp)).check(matches(isDisplayed()));
        onView(withId(R.id.iv_day3Icon)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_day3Weather)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_day3High)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_day3Low)).check(matches(isDisplayed()));

        onView(withId(R.id.button)).perform(click());

        onView(withId(R.id.et_zip)).perform(typeText(zip), closeSoftKeyboard());

        onView(withId(R.id.bt_updateLoc)).perform(click());
        onView(isRoot()).perform(waitFor(3000));
        onView(withId(R.id.tv_city)).check(matches(isDisplayed()));
    }

    public static ViewAction waitFor(long delay) {
        return new ViewAction() {
            @Override public Matcher<View> getConstraints() {
                return ViewMatchers.isRoot();
            }

            @Override public String getDescription() {
                return "wait for " + delay + "milliseconds";
            }

            @Override public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(delay);
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        //After Test case Execution
    }
}

