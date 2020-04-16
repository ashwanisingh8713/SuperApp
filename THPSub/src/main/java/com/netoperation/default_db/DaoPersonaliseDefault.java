package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface DaoPersonaliseDefault {


    @Insert
    void insertDefaultPersonalise(TablePersonaliseDefault tableBookmark);

    @Query("SELECT * FROM TablePersonaliseDefault")
    List<TablePersonaliseDefault> getAllPersonalise();

    @Query("SELECT * FROM TablePersonaliseDefault WHERE category = :category")
    List<TablePersonaliseDefault> getCategoryPersonalise(String category);

    @Query("DELETE FROM TablePersonaliseDefault WHERE category = :category")
    void delete(String category);

    @Query("DELETE FROM TablePersonaliseDefault")
    void deleteAll();



}
