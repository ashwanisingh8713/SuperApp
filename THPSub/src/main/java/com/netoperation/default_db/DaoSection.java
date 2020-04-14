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

    @Query("SELECT * FROM TableSection WHERE customScreen = 2 ORDER BY customScreenPri ASC")
    List<TableSection> getRegionalSection();

    @Query("SELECT * FROM TableSection WHERE customScreen = 1 ORDER BY customScreenPri ASC")
    List<TableSection> getHomeNewsFeedSection();

    @Query("SELECT * FROM TableSection WHERE isUserPreferred = 1")
    Observable<List<TableSection>> getUserPreferredSectionObservable();

    @Query("SELECT * FROM TableSection WHERE isUserPreferred = 1")
    List<TableSection> getUserPreferredSection();

    @Query("SELECT * FROM TableSection WHERE show_on_burger = :show_on_burger")
    Observable<List<TableSection>> getSectionsOfBurger(boolean show_on_burger);

    @Query("SELECT * FROM TableSection WHERE show_on_explore = :show_on_explore")
    List<TableSection> getSectionsOfExplore(boolean show_on_explore);

    @Query("SELECT * FROM TableSection WHERE secId = :secId")
    TableSection getSubSections(String secId);

    @Query("SELECT * FROM TableSection WHERE secId = :secId AND show_on_explore = :show_on_explore")
    List<TableSection> getSubSectionsOfExplore(String secId, boolean show_on_explore);

    @Query("DELETE FROM TableSection")
    void deleteAll();

}
