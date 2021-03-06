package com.testsoc.register;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.testsoc.R;
import com.testsoc.ui.signup.SignUpActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by felix on 1/9/18
 */

@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {


    @Rule
    public ActivityTestRule<SignUpActivity> rule = new ActivityTestRule<>(SignUpActivity.class, true, true);

    @Before
    public void fillingBaseData() {

        onView(withId(R.id.input_name)).perform(ViewActions.typeText("Artem"));
        onView(withId(R.id.input_surname)).perform(ViewActions.typeText("Bash"), closeSoftKeyboard());
        onView(withId(R.id.input_user_name)).perform(ViewActions.typeText("ArtemQQ"), closeSoftKeyboard());
        onView(withId(R.id.input_email)).perform(ViewActions.typeText("artem@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.input_password)).perform(ViewActions.typeText("pass1"), closeSoftKeyboard());
        onView(withId(R.id.input_password_repeat)).perform(ViewActions.typeText("pass1"), closeSoftKeyboard());

    }


    @Test
    public void fillingFormTest_Correctly() {

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.signup_scroll_view)).perform(swipeUp());

        onView(withId(R.id.input_name)).check(matches(withText("Artem")));
        onView(withId(R.id.input_surname)).check(matches(withText("Bash")));
        onView(withId(R.id.input_user_name)).check(matches(withText("ArtemQQ")));
        onView(withId(R.id.input_email)).check(matches(withText("artem@gmail.com")));
        onView(withId(R.id.input_password)).check(matches(withText("pass1234")));
        onView(withId(R.id.input_password_repeat)).check(matches(withText("pass1234")));

        onView(withId(R.id.btn_signup)).perform(click());


    }

    @Test
    public void fillingFormTest_UnCorrectly() {

        onView(withId(R.id.input_email)).perform(clearText());
        onView(withId(R.id.input_password)).perform(clearText());

        onView(withId(R.id.input_email)).perform(ViewActions.typeText(""), closeSoftKeyboard());
        onView(withId(R.id.input_password)).perform(ViewActions.typeText("pass"), closeSoftKeyboard());
        onView(withId(R.id.input_password_repeat)).perform(ViewActions.typeText("pass_not_identical"),
                closeSoftKeyboard());

        onView(withId(R.id.btn_signup)).perform(click());

        onView(withId(R.id.input_email)).check(matches(hasErrorText("enter a valid email address")));
        onView(withId(R.id.input_password_repeat)).check(matches(hasErrorText("passwords doesn't match")));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void fillingFormUnCorrectly_For_Server() {

        onView(withId(R.id.signup_scroll_view)).perform(swipeUp());
        onView(withId(R.id.btn_signup)).perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}
