package com.challengesstore.register;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.challengesstore.R;
import com.challengesstore.ui.login.LoginActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by felix on 1/14/18
 */

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

    @Before
    public void init() {
        onView(withId(R.id.login_input_email)).perform(ViewActions.typeText("Bash"),closeSoftKeyboard());
        onView(withId(R.id.login_input_password)).perform(ViewActions.typeText("artem12345"),closeSoftKeyboard());
    }

    @Test
    public void onInputValid_Test() {
        onView(withId(R.id.btn_signin)).perform(click());
    }
}
