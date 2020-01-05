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

import static ie.noel.dunsceal.persistence.db.TestData.DUNS;
import static ie.noel.dunsceal.persistence.db.TestData.DUN_ENTITY;

import static junit.framework.Assert.assertTrue;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;
import ie.noel.dunsceal.persistence.LiveDataTestUtil;

import ie.noel.dunsceal.models.entity.DunEntity;
import ie.noel.dunsceal.persistence.db.mock.MockDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Test the implementation of {@link DunDao}
 */
@RunWith(AndroidJUnit4.class)
public class DunDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MockDatabase mDatabase;

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

        mDunDao = mDatabase.dunDao();
    }

    @After
    public void closeDb() throws Exception {
        mDatabase.close();
    }

    @Test
    public void getDunsWhenNoDunInserted() throws InterruptedException {
        List<DunEntity> duns = LiveDataTestUtil.getValue(mDunDao.loadAllDuns());

        assertTrue(duns.isEmpty());
    }

    @Test
    public void getDunsAfterInserted() throws InterruptedException {
        mDunDao.insertAll(DUNS);

        List<DunEntity> duns = LiveDataTestUtil.getValue(mDunDao.loadAllDuns());

        assertThat(duns.size(), is(DUNS.size()));
    }

    @Test
    public void getDunById() throws InterruptedException {
        mDunDao.insertAll(DUNS);

        DunEntity dun = LiveDataTestUtil.getValue(mDunDao.loadDun
                (DUN_ENTITY.getId()));

        assertThat(dun.getId(), is(DUN_ENTITY.getId()));
        assertThat(dun.getName(), is(DUN_ENTITY.getName()));
        assertThat(dun.getDescription(), is(DUN_ENTITY.getDescription()));
        assertThat(dun.getPrice(), is(DUN_ENTITY.getPrice()));
    }

}
