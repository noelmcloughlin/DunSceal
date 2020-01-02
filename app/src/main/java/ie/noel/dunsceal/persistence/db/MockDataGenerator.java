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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import ie.noel.dunsceal.models.entity.DunEntity;
import ie.noel.dunsceal.models.entity.EntranceEntity;
import ie.noel.dunsceal.models.entity.InvestigationEntity;

/**
 * Generates data to pre-populate the database
 */
public class MockDataGenerator {

    private static final String[] FIRST = new String[]{
            "Special edition", "New", "Cheap", "Quality", "Used"};
    private static final String[] SECOND = new String[]{
            "Three-headed Monkey", "Rubber Chicken", "Pint of Grog", "Monocle"};
    private static final String[] DESCRIPTION = new String[]{
            "is finally here", "is recommended by Stan S. Stanman",
            "is the best sold dun on Mêlée Island", "is \uD83D\uDCAF", "is ❤️", "is fine"};
    private static final String[] INVESTIGATIONS = new String[]{
            "InvestigationModel 1", "InvestigationModel 2", "InvestigationModel 3", "InvestigationModel 4", "InvestigationModel 5", "InvestigationModel 6"};
    private static final String[] ENTRANCES = new String[]{
        "EntranceModel 1", "EntranceModel 2", "EntranceModel 3", "EntranceModel 4", "EntranceModel 5", "EntranceModel 6"};

    public static List<DunEntity> generateDuns() {
        List<DunEntity> duns = new ArrayList<>(FIRST.length * SECOND.length);
        Random rnd = new Random();
        for (int i = 0; i < FIRST.length; i++) {
            for (int j = 0; j < SECOND.length; j++) {
                DunEntity dun = new DunEntity();
                dun.setName(FIRST[i] + " " + SECOND[j]);
                dun.setDescription(dun.getName() + " " + DESCRIPTION[j]);
                dun.setPrice(rnd.nextInt(240));
                dun.setId(FIRST.length * i + j + 1);
                duns.add(dun);
            }
        }
        return duns;
    }

    public static List<InvestigationEntity> generateInvestigationsForDuns(
            final List<DunEntity> duns) {
        List<InvestigationEntity> investigations = new ArrayList<>();
        Random rnd = new Random();

        for (DunEntity dunModel : duns) {
            int investigationsNumber = rnd.nextInt(5) + 1;
            for (int i = 0; i < investigationsNumber; i++) {
                InvestigationEntity investigation = new InvestigationEntity();
                investigation.setDunId(dunModel.getId());
                investigation.setText(ENTRANCES[i] + " for " + dunModel.getName());
                investigation.setPostedAt(new Date(System.currentTimeMillis()
                        - TimeUnit.DAYS.toMillis(investigationsNumber - i) + TimeUnit.HOURS.toMillis(i)));
                investigations.add(investigation);
            }
        }

        return investigations;
    }

    public static List<EntranceEntity> generateEntrancesForDuns(
        final List<DunEntity> duns) {
        List<EntranceEntity> entrances = new ArrayList<>();
        Random rnd = new Random();

        for (DunEntity dunModel : duns) {
            int entrancesNumber = rnd.nextInt(5) + 1;
            int i;
            for (i = 0; i < entrancesNumber; i++) {
                EntranceEntity entrance = new EntranceEntity();
                entrance.setDunId(dunModel.getId());
                entrance.setText(INVESTIGATIONS[i] + " for " + dunModel.getName());
                entrance.setPostedAt(new Date(System.currentTimeMillis()
                    - TimeUnit.DAYS.toMillis(entrancesNumber - i) + TimeUnit.HOURS.toMillis(i)));
                entrances.add(entrance);
            }
        }

        return entrances;
    }
}
