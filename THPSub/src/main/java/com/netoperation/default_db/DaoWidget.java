package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;


@Dao
public interface DaoWidget {

    @Insert
    void insertWidget(TableWidget tableWidget);

    @Query("SELECT * FROM TableWidget")
    List<TableWidget> getWidgets();

}
