/*
 * Copyright (C) 2017 The Android Open Source Project
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

package ie.noel.dunsceal.persistence.db;

import static ie.noel.dunsceal.persistence.db.TestData.INVESTIGATIONS;
import static ie.noel.dunsceal.persistence.db.TestData.INVESTIGATION_ENTITY;
import static ie.noel.dunsceal.persistence.db.TestData.DUNS;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import android.database.sqlite.SQLiteConstraintException;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;
import ie.noel.dunsceal.persistence.LiveDataTestUtil;

import ie.noel.dunsceal.models.entity.InvestigationEntity;
import ie.noel.dunsceal.persistence.db.mock.MockDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Test the implementation of {@link InvestigationDao}
 */
@RunWith(AndroidJUnit4.class)
public class InvestigationDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MockDatabase mDatabase;

    private InvestigationDao mInvestigationDao;

    private DunDao mDunDao;

    @Before
    public void initDb() throws Exception {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                MockDatabase.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();

        mInvestigationDao = mDatabase.investigationDao();
        mDunDao = mDatabase.dunDao();
    }

    @After
    public void closeDb() throws Exception {
        mDatabase.close();
    }

    @Test
    public void getInvestigationsWhenNoInvestigationInserted() throws InterruptedException {
        List<InvestigationEntity> investigations = LiveDataTestUtil.getValue(mInvestigationDao.loadInvestigations
                (INVESTIGATION_ENTITY.getDunId()));

        assertTrue(investigations.isEmpty());
    }

    @Test
    public void cantInsertInvestigationWithoutDun() throws InterruptedException {
        try {
            mInvestigationDao.insertAll(INVESTIGATIONS);
            fail("SQLiteConstraintException expected");
        } catch (SQLiteConstraintException ignored) {

        }
    }

    @Test
    public void getInvestigationsAfterInserted() throws InterruptedException {
        mDunDao.insertAll(DUNS);
        mInvestigationDao.insertAll(INVESTIGATIONS);

        List<InvestigationEntity> investigations = LiveDataTestUtil.getValue(mInvestigationDao.loadInvestigations
                (INVESTIGATION_ENTITY.getDunId()));

        assertThat(investigations.size(), is(1));
    }

    @Test
    public void getInvestigationByDunId() throws InterruptedException {
        mDunDao.insertAll(DUNS);
        mInvestigationDao.insertAll(INVESTIGATIONS);

        List<InvestigationEntity> investigations = LiveDataTestUtil.getValue(mInvestigationDao.loadInvestigations(
                (INVESTIGATION_ENTITY.getDunId())));

        assertThat(investigations.size(), is(1));
    }

}
