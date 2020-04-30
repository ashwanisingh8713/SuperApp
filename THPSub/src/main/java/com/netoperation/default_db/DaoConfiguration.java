package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


@Dao
public interface DaoConfiguration {

    @Insert
    void insertConfiguration(TableConfiguration tableConfiguration);

    @Query("SELECT * FROM TableConfiguration")
    TableConfiguration getConfiguration();

    @Query("DELETE FROM TableConfiguration")
    void deleteAll();

}
