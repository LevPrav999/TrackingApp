package ru.predprof.trackingapp.room;

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

    @Query("SELECT * FROM trip WHERE name = (:name)")
    Trip getTripById(String name);

    @Query("SELECT * FROM trip ORDER BY number DESC LIMIT 1")
    Trip getlastTrip();

    @Query("SELECT * FROM trip WHERE number IN (:userIds)")
    List<Trip> loadAllByIds(int[] userIds);


    @Insert
    void insertAll(Trip... users);

    @Delete
    void delete(Trip user);

    @Query("DELETE FROM trip")
    void delete_all();
}

