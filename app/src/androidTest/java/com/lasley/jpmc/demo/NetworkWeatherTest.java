package com.lasley.jpmc.demo;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import com.lasley.jpmc.demo.activity.MainActivity;
import com.lasley.jpmc.demo.util.Constants;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.lasley.jpmc.demo.util.Constants.BUNDLE_DATA;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
public class NetworkWeatherTest extends InstrumentationTestCase {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);
    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        server = new MockWebServer();
        server.start();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        Constants.BASE_URL = server.url("/").toString();
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void test_happypath() throws Exception {
        String fileName = "weather_200_response.json";
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(getStringFromFile(getInstrumentation().getContext(), fileName)));

        Intent intent = new Intent();
        intent.putExtra(BUNDLE_DATA, fileName);
        mActivityRule.launchActivity(intent);

        onView(withText("Hilo")).check(matches(isDisplayed()));
        onView(withText(containsString("few clouds"))).check(matches(isDisplayed()));
    }

    @Test
    public void test_sadpath_invalidData() throws Exception {
        String fileName = "weather_200_invalidData_response.json";
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(getStringFromFile(getInstrumentation().getContext(), fileName)));

        Intent intent = new Intent();
        intent.putExtra(BUNDLE_DATA, fileName);
        mActivityRule.launchActivity(intent);

        onView(withText("city not found")).check(matches(isDisplayed()));
    }

    @Test
    public void test_sadpath() throws Exception {
        String fileName = "weather_404_response.json";
        server.enqueue(new MockResponse()
                .setResponseCode(404));

        Intent intent = new Intent();
        intent.putExtra(BUNDLE_DATA, fileName);
        mActivityRule.launchActivity(intent);

        onView(withText("Data was unable to be retrieved")).check(matches(isDisplayed()));
    }

    private String getStringFromFile(Context context, String filePath) throws Exception {
        final InputStream stream = context.getResources().getAssets().open(filePath);

        String ret = convertStreamToString(stream);
        //Make sure you close all streams.
        stream.close();
        return ret;
    }


    private String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }
}
