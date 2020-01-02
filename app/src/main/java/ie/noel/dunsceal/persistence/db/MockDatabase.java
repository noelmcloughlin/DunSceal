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

package ie.noel.dunsceal.persistence.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import ie.noel.dunsceal.models.entity.DunEntity;
import ie.noel.dunsceal.models.entity.EntranceEntity;
import ie.noel.dunsceal.persistence.AppExecutors;
import ie.noel.dunsceal.models.entity.DunFtsEntity;
import ie.noel.dunsceal.models.entity.InvestigationEntity;
import ie.noel.dunsceal.persistence.db.dao.DunDao;
import ie.noel.dunsceal.persistence.db.dao.EntranceDao;
import ie.noel.dunsceal.utils.DateConverter;
import ie.noel.dunsceal.persistence.db.dao.InvestigationDao;

import java.util.List;

@Database(entities = {DunEntity.class, DunFtsEntity.class, InvestigationEntity.class, EntranceEntity.class}, version = 2)
@TypeConverters(DateConverter.class)
public abstract class MockDatabase extends RoomDatabase {

    private static MockDatabase sInstance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "basic-sample-db";

    public abstract DunDao dunDao();

    public abstract InvestigationDao InvestigationDao();

    abstract EntranceDao EntranceDao();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static MockDatabase getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (MockDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext(), executors);
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static MockDatabase buildDatabase(final Context appContext,
                                              final AppExecutors executors) {
        return Room.databaseBuilder(appContext, MockDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(() -> {
                            // Add a delay to simulate a long-running operation
                            addDelay();
                            // Generate the data for pre-population
                            MockDatabase database = MockDatabase.getInstance(appContext, executors);
                            List<DunEntity> duns = MockDataGenerator.generateDuns();
                            List<InvestigationEntity> investigations =
                                    MockDataGenerator.generateInvestigationsForDuns(duns);
                            List<EntranceEntity> entrances =
                                MockDataGenerator.generateEntrancesForDuns(duns);

                            insertData(database, duns, investigations, entrances);
                            // notify that the database was created and it's ready to be used
                            database.setDatabaseCreated();
                        });
                    }
                })
            .addMigrations(MIGRATION_1_2)
            .build();
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    private static void insertData(final MockDatabase database, 
                                   final List<DunEntity> duns,
                                   final List<InvestigationEntity> investigations,
                                   final List<EntranceEntity> entrances) {
        database.runInTransaction(() -> {
            database.dunDao().insertAll(duns);
            database.InvestigationDao().insertAll(investigations);
            database.EntranceDao().insertAll(entrances);

        });
    }

    private static void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // IDE warning about 'TEXT,' can be ignored in Java
            database.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `dunsFts` USING FTS4("
                + "`name` TEXT, `description` TEXT, content=`duns`)");
            database.execSQL("INSERT INTO dunsFts (`rowid`, `name`, `description`) "
                + "SELECT `id`, `name`, `description` FROM duns");

        }
    };
}
