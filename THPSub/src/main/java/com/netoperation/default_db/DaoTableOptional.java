package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoTableOptional {

    @Insert
    void insertTableOptional(TableOptional tableOptional);

    @Query("SELECT * from TableOptional")
    List<TableOptional> getListOptions();

    @Query("DELETE from TableOptional")
    int deleteTableOptional();

}
