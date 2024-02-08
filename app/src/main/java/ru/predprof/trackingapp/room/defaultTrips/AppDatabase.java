package ru.predprof.trackingapp.room.defaultTrips;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ru.predprof.trackingapp.models.DefaultTrip;

@Database(entities = {DefaultTrip.class}, version = 1, exportSchema = true)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TripDao tripDao();
}
