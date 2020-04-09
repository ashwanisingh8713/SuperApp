package com.netoperation.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface DaoBreifing {

    @Insert
    void insertBreifing(TableBreifing tableBreifing);

    @Query("DELETE FROM TableBreifing")
    void deleteAll();

    @Query("SELECT * FROM TableBreifing")
    TableBreifing getBreifingTable();


}
