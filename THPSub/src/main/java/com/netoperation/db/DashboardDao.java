package com.netoperation.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.netoperation.model.RecoBean;

import java.util.List;


@Dao
public interface DashboardDao {

//    @Insert(onConflict = ABORT)
    @Insert
    void insertDashboard(DashboardTable HomeTable);

    @Query("SELECT * FROM DashboardTable WHERE recoFrom = :recoFrom")
    List<DashboardTable> getAllDashboardBean(String recoFrom);

    @Query("SELECT * FROM DashboardTable WHERE aid = :aid")
    DashboardTable getSingleDashboardBean(String aid);

    @Query("DELETE FROM DashboardTable")
    void deleteAll();

    @Query("DELETE FROM DashboardTable WHERE recoFrom = :recoFrom")
    void deleteAll(String recoFrom);

    @Query("UPDATE DashboardTable SET bean = :bean WHERE aid = :aid")
    int updateRecobean(String aid, RecoBean bean);


}
