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

import ie.noel.dunsceal.persistence.db.entity.InvestigationEntity;

@Dao
public interface InvestigationDao {
    @Query("SELECT * FROM investigations where dunId = :dunId")
    LiveData<List<InvestigationEntity>> loadInvestigations(int dunId);

    @Query("SELECT * FROM investigations where dunId = :dunId")
    List<InvestigationEntity> loadInvestigationsSync(int dunId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InvestigationEntity> investigations);
}
