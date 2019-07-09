package com.example.piggie.SQLite;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RecordDao {

    @Query("SELECT * FROM record")
    List<Record> getAll();

    @Insert
    long insert(Record record);

    @Query("SELECT * FROM record WHERE name = :name")
    List<Record> findByName(String name);

    @Query("DELETE FROM record WHERE name = :name AND date = :date")
    void deleteByNameAndDate(String name, String date);

    @Query("DELETE FROM record WHERE id = :id")
    void deleteById(int id);

    @Query("DELETE FROM record")
    void deleteAll();

}
