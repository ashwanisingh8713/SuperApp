package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Single;


@Dao
public abstract class DaoTempWork {

    @Insert
    public abstract void insertTempWork(TableTempWork tableRead);

    @Query("SELECT * FROM TableTempWork WHERE workId = :workId")
    public abstract TableTempWork getTableTempWork(String workId);

    @Query("DELETE FROM TableTempWork WHERE workId = :workId")
    public abstract int deleteTempWork(String workId);


}
