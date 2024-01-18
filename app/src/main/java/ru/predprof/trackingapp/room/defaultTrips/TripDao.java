package ru.predprof.trackingapp.room.defaultTrips;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ru.predprof.trackingapp.models.DefaultTrip;
import ru.predprof.trackingapp.models.Trip;

@Dao
public interface TripDao {
    @Query("SELECT * FROM defaulttrip")
    List<DefaultTrip> getAll();

    @Query("SELECT * FROM defaulttrip WHERE name = (:name)")
    DefaultTrip getTripByName(String name);

    @Query("SELECT * FROM defaulttrip WHERE id = (:id)")
    DefaultTrip getTripById(int id);

    @Query("SELECT * FROM defaulttrip ORDER BY id DESC LIMIT 1")
    DefaultTrip getlastTrip();

    @Query("SELECT * FROM defaulttrip WHERE id IN (:userIds)")
    List<DefaultTrip> loadAllByIds(int[] userIds);


    @Insert
    void insertAll(DefaultTrip... users);

    @Delete
    void delete(DefaultTrip user);

    @Query("DELETE FROM defaulttrip")
    void delete_all();
}

