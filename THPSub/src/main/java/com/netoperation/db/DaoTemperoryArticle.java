package com.netoperation.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.netoperation.default_db.TableTemperoryArticle;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;


@Dao
public interface DaoTemperoryArticle {

    @Insert
    void insertTemperoryArticle(TableTemperoryArticle temperoryArticle);

    @Query("SELECT * FROM TableTemperoryArticle")
    List<TableTemperoryArticle> getAllTempBean();

    @Query("SELECT * FROM TableTemperoryArticle")
    Observable<List<TableTemperoryArticle>> getAllTempBeanObservable();

    @Query("SELECT * FROM TableTemperoryArticle WHERE aid = :aid")
    TableTemperoryArticle getSingleTemperoryBean(String aid);

    @Query("DELETE FROM TableTemperoryArticle")
    void deleteAll();

    @Query("DELETE FROM TableTemperoryArticle WHERE aid = :aid")
    void delete(String aid);

}
