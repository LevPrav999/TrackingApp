package ru.predprof.trackingapp.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ru.predprof.trackingapp.models.Trip;

@Dao
public interface TripDao {
    @Query("SELECT * FROM trip")
    List<Trip> getAll();

    @Query("SELECT * FROM trip WHERE number IN (:userIds)")
    List<Trip> loadAllByIds(int[] userIds);


    @Insert
    void insertAll(Trip... users);

    @Delete
    void delete(Trip user);
}

