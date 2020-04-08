package com.netoperation.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface BreifingDao {

    @Insert
    void insertBreifing(BreifingTable breifingTable);

    @Query("DELETE FROM BreifingTable")
    void deleteAll();

    @Query("SELECT * FROM BreifingTable")
    BreifingTable getBreifingTable();


}
