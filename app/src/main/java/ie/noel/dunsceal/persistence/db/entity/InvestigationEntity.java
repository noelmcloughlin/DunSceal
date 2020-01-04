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

package ie.noel.dunsceal.persistence.db.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

import ie.noel.dunsceal.persistence.model.Investigation;

@Entity(tableName = "investigations",
        foreignKeys = {
                @ForeignKey(entity = DunEntity.class,
                        parentColumns = "id",
                        childColumns = "dunId",
                        onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "dunId")
        })
public class InvestigationEntity implements Investigation {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int dunId;
    private String text;
    private Date postedAt;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getDunId() {
        return dunId;
    }

    public void setDunId(int dunId) {
        this.dunId = dunId;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Date getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Date postedAt) {
        this.postedAt = postedAt;
    }

    public InvestigationEntity() {
    }

    @Ignore
    public InvestigationEntity(int id, int dunId, String text, Date postedAt) {
        this.id = id;
        this.dunId = dunId;
        this.text = text;
        this.postedAt = postedAt;
    }
}
