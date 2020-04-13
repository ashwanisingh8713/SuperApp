package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

import io.reactivex.Observable;


@Dao
public interface DaoPersonaliseDefault {


    @Insert
    void insertBookmark(TablePersonaliseDefault tableBookmark);

    @Query("SELECT * FROM TablePersonaliseDefault")
    List<TablePersonaliseDefault> getAllPersonalise();

    @Query("SELECT * FROM TablePersonaliseDefault WHERE category = :category")
    List<TablePersonaliseDefault> getPersonaliseFromCategory(String category);

    @Query("SELECT * FROM TablePersonaliseDefault WHERE category = :category AND isSelected = :isSelected")
    Observable<List<TablePersonaliseDefault>> getOnlySelectedPersonaliseFromCategory(String category, boolean isSelected);

    @Query("UPDATE TablePersonaliseDefault SET isSelected = :isSelected WHERE secId = :secId")
    int updatePersonalise(int secId, boolean isSelected);

    @Query("DELETE FROM TableWidget")
    void deleteAll();



}
