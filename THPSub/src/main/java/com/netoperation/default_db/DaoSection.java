package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoSection {

    @Insert
    void insertSection(TableSection tableSection);

    @Query("SELECT * FROM TableSection")
    List<TableSection> getSections();

    @Query("SELECT * FROM TableSection WHERE show_on_burger = :show_on_burger")
    List<TableSection> getSectionsOfBurger(boolean show_on_burger);

    @Query("SELECT * FROM TableSection WHERE show_on_explore = :show_on_explore")
    List<TableSection> getSectionsOfExplore(boolean show_on_explore);

    @Query("SELECT * FROM TableSection WHERE secId = :secId")
    List<TableSection> getSubSections(String secId);

    @Query("SELECT * FROM TableSection WHERE secId = :secId AND show_on_burger = :show_on_burger")
    List<TableSection> getSubSectionsOfBurger(String secId, boolean show_on_burger);

    @Query("SELECT * FROM TableSection WHERE secId = :secId AND show_on_explore = :show_on_explore")
    List<TableSection> getSubSectionsOfExplore(String secId, boolean show_on_explore);

    @Query("DELETE FROM TableSection")
    void deleteAll();

}
