package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface DaoSection {

    @Insert
    void insertSection(TableSection tableSection);

    @Query("SELECT * FROM TableSection")
    List<TableSection> getSections();

    @Query("SELECT * FROM TableSection")
    Observable<List<TableSection>> getSectionsObs();

    @Query("SELECT * FROM TableSection WHERE customScreen = 2 ORDER BY customScreenPri ASC")
    List<TableSection> getRegionalSection();

    @Query("SELECT * FROM TableSection WHERE customScreen = 1")
    List<TableSection> getHomeNewsFeedSection();

    @Query("SELECT * FROM TableSection WHERE show_on_burger = 1")
    Observable<List<TableSection>> getSectionsOfBurger();

    @Query("SELECT * FROM TableSection WHERE show_on_burger = 1 OR show_on_explore = 1 ORDER BY overridePriority ASC")
    Observable<List<TableSection>> getSectionsOfTopBar();

    @Query("SELECT * FROM TableSection WHERE show_on_explore = :show_on_explore")
    List<TableSection> getSectionsOfExplore(boolean show_on_explore);

    @Query("SELECT * FROM TableSection WHERE secId = :secId")
    TableSection getSubSections(String secId);

    @Query("SELECT * FROM TableSection WHERE secId = :secId AND show_on_explore = :show_on_explore")
    List<TableSection> getSubSectionsOfExplore(String secId, boolean show_on_explore);

    @Query("DELETE FROM TableSection")
    void deleteAll();

}
