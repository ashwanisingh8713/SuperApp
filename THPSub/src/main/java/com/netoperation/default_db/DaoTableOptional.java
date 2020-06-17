package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import io.reactivex.Single;

@Dao
public interface DaoTableOptional {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTableOptional(TableOptional tableOptional);

    @Query("SELECT * from TableOptional")
    Single<TableOptional> getItemTableOptional();

    @Query("DELETE from TableOptional")
    void deleteTableOptional();

}
