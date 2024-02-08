package ru.predprof.trackingapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ru.predprof.trackingapp.models.Trip;

@Database(entities = {Trip.class}, version = 1, exportSchema = true)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TripDao tripDao();
}
