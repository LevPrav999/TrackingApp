package ru.predprof.trackingapp.room.defaultTrips;

import android.content.Context;

import androidx.room.Room;

/**
 * @brief Класс для работы с room базой данных
 */
public class DefaultRoomHandler {
    private static DefaultRoomHandler mInstance;
    private final Context mCtx;
    private final AppDatabase appDatabase;

    private DefaultRoomHandler(Context mCtx) {
        this.mCtx = mCtx;
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "defaulttrips")
                .build();
    }

    public static DefaultRoomHandler getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DefaultRoomHandler(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
