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

package ie.noel.dunsceal.persistence.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ie.noel.dunsceal.persistence.db.entity.DunEntity;

@Dao
public interface DunDao {
    @Query("SELECT * FROM duns")
    LiveData<List<DunEntity>> loadAllDuns();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DunEntity> duns);

    @Query("select * from duns where id = :dunId")
    LiveData<DunEntity> loadDun(int dunId);

    @Query("select * from duns where id = :dunId")
    DunEntity loadDunSync(int dunId);

    @Query("SELECT duns.* FROM duns JOIN dunsFts ON (duns.id = dunsFts.rowid) "
        + "WHERE dunsFts MATCH :query")
    LiveData<List<DunEntity>> searchAllDuns(String query);
}
