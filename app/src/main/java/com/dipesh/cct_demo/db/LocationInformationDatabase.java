package com.dipesh.cct_demo.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dipesh.cct_demo.db.dao.LocationInfoDao;
import com.dipesh.cct_demo.db.entity.LocationInfo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {LocationInfo.class}, version = 1)
public abstract class LocationInformationDatabase extends RoomDatabase {

    public abstract LocationInfoDao locationInfoDao();


    private static volatile LocationInformationDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static LocationInformationDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LocationInformationDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocationInformationDatabase.class, "location_information_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
            });
        }
    };
}
