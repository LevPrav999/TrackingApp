package ru.predprof.trackingapp.room;

import android.content.Context;

import androidx.room.Room;

/**
 * @brief Класс для работы с room базой данных
 */
public class RoomHandler {
    private static RoomHandler mInstance;
    private final Context mCtx;
    private final AppDatabase appDatabase;

    private RoomHandler(Context mCtx) {
        this.mCtx = mCtx;
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "trips")
                .build();
    }

    public static RoomHandler getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new RoomHandler(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
