Hillfort Stories: DunSceal
=============================================

Incorporating features of::

Architecture Components:

* [Room](https://developer.android.com/topic/libraries/architecture/room.html)
* [ViewModels](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html)
* [LiveData](https://developer.android.com/reference/android/arch/lifecycle/LiveData.html)

Navigation component:

 * Navigating via actions
 * Transitions
 * Popping destinations from the back stack
 * Deep links (`www.example.com/user/{user name}` opens the profile screen)

Screenshots
-----------

Splash
![Splash Screen](docs/images/splash.png?raw=true "DunSceal Splash")

Login
![Login Screen](docs/images/login.png?raw=true "DunSceal Login")

Authentication
![Auth Screen](docs/images/auth.png?raw=true "DunSceal Authentication")

Home
![Home Screen](docs/images/home.png?raw=true "DunSceal Home")

Navigation
![Navigation Screen](docs/images/navmenu.png?raw=true "DunSceal Navigation")

Action menu
![Action menu](docs/images/action_bar1.png?raw=true "DunSceal Action Bar")

Date Picker
![Date Dialog](docs/images/datepicker.png?raw=true "DunSceal Date Picker")

List View
![ViewModel](docs/images/list.png?raw=true "DunSceal List")

Map
![Map Screen](docs/images/map.png?raw=true "DunSceal Map")


Introduction
-------------

### Features

This sample contains two screens: a list of duns and a detail view, that shows dun reviews.

#### Presentation layer

The presentation layer consists of the following components:
* A main activity that handles navigation.
* A fragment to display the list of duns.
* A fragment to display a dun review.

The app uses a Model-View-ViewModel (MVVM) architecture for the presentation layer. Each of the fragments corresponds to a MVVM View. The View and ViewModel communicate  using LiveData and the following design principles:

* ViewModel objects don't have references to activities, fragments, or Android views. That would cause leaks on configuration changes, such as a screen rotation, because the system retains a ViewModel across the entire lifecycle of the corresponding view.



![ViewModel Diagram](docs/images/VM_diagram.png?raw=true "ViewModel Diagram")


* ViewModel objects expose data using `LiveData` objects. `LiveData` allows you to observe changes to data across multiple components of your app without creating explicit and rigid dependency paths between them.

* Views, including the fragments used in this sample, subscribe to corresponding `LiveData` objects. Because `LiveData` is lifecycle-aware, it doesn’t push changes to the underlying data if the observer is not in an active state, and this helps to avoid many common bugs. This is an example of a subscription:

```java
        // Update the list of duns when the underlying data changes.
        viewModel.getDuns().observe(this, new Observer<List<DunEntity>>() {
            @Override
            public void onChanged(@Nullable List<DunEntity> myDuns) {
                if (myDuns != null) {
                    mBinding.setIsLoading(false);
                    mDunAdapter.setDunList(myDuns);
                } else {
                    mBinding.setIsLoading(true);
                }
            }
        });
```

#### Data layer

The database is created using Room and it has two entities: a `DunEntity` and a `InvestigationEntity` that generate corresponding SQLite tables at runtime.

Room populates the database asynchronously when it's created, via the `RoomDatabase#Callback`. To simulate low-performance, an artificial delay is added. To let 
 other components know when the data has finished populating, the `AppDatabase` exposes a 
 `LiveData` object..

To access the data and execute queries, you use a [Data Access Object](https://developer.android.com/topic/libraries/architecture/room.html#daos) (DAO). For example, a dun is loaded with the following query:

```java
    @Query("select * from duns where id = :dunId")
    LiveData<DunEntity> loadDun(int dunId);
```

Queries that return a `LiveData` object can be observed, so when  a change in one of the affected tables is detected, `LiveData` delivers a notification of that change to the registered observers.

The `DataRepository` exposes the data to the UI layer. To ensure that the UI uses the list of duns only after the database has been pre-populated, a [`MediatorLiveData`](https://developer.android.com/reference/android/arch/lifecycle/MediatorLiveData.html) object is used. This
observes the changes of the list of duns and only forwards it when the database is ready to be used.
 

License
--------

Copyright 2020 Noel MacLoughlin, The Android Open Source Project, Inc.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.



