/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ie.noel.dunsceal.persistence.ui;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.CountingTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import ie.noel.dunsceal.persistence.AppExecutors;
import ie.noel.dunsceal.persistence.EspressoTestUtil;
import ie.noel.dunsceal.R;
import ie.noel.dunsceal.persistence.db.MockDatabase;
import ie.noel.dunsceal.persistence.ui.SearchActivity;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

public class SearchActivityTest {

    @Rule
    public ActivityTestRule<SearchActivity> mActivityRule = new ActivityTestRule<>(
            SearchActivity.class);

    @Rule
    public CountingTaskExecutorRule mCountingTaskExecutorRule = new CountingTaskExecutorRule();

    public SearchActivityTest() {
        // delete the database
        ApplicationProvider.getApplicationContext().deleteDatabase(MockDatabase.DATABASE_NAME);
    }

    @Before
    public void disableRecyclerViewAnimations() {
        // Disable RecyclerView animations
        EspressoTestUtil.disableAnimations(mActivityRule);
    }

    @Before
    public void waitForDbCreation() throws Throwable {
        final CountDownLatch latch = new CountDownLatch(1);
        final LiveData<Boolean> databaseCreated = MockDatabase.getInstance(ApplicationProvider.getApplicationContext(), new AppExecutors()).getDatabaseCreated();
        mActivityRule.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                databaseCreated.observeForever(new Observer<Boolean>() {

                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        if (Boolean.TRUE.equals(aBoolean)) {
                            databaseCreated.removeObserver(this);
                            latch.countDown();
                        }
                    }
                });
            }
        });
        MatcherAssert.assertThat("database should've initialized",
                latch.await(1, TimeUnit.MINUTES), CoreMatchers.is(true));
    }

    @Test
    public void clickOnFirstItem_opensInvestigations() throws Throwable {
        drain();
        // When clicking on the first dun
        onView(ViewMatchers.withContentDescription(R.string.cd_duns_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        drain();
        // Then the second screen with the investigations should appear.
        onView(withContentDescription(R.string.cd_investigations_list))
                .check(matches(isDisplayed()));
        drain();
        // Then the second screen with the investigations should appear.
        onView(withContentDescription(R.string.cd_dun_name))
                .check(matches(not(withText(""))));

    }

    private void drain() throws TimeoutException, InterruptedException {
        mCountingTaskExecutorRule.drainTasks(1, TimeUnit.MINUTES);
    }
}