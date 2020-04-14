package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;


@Dao
public abstract class DaoWidget {

    @Insert
    public abstract void insertWidget(TableWidget tableWidget);

    @Query("SELECT * FROM TableWidget")
    public abstract Observable<List<TableWidget>> getWidgets();

    @Query("SELECT * FROM TableWidget")
    public abstract Single<List<TableWidget>> getWidgetsSingle();

    @Query("SELECT * FROM TableWidget WHERE secId = :secId")
    public abstract TableWidget getWidget(String secId);

    @Transaction
    public void deleteAndInsertWidget(String secId, TableWidget tableWidget) {
        deleteWidget(secId);
        insertWidget(tableWidget);
    }

    @Query("DELETE FROM TableWidget WHERE secId = :secId")
    public abstract int deleteWidget(String secId);

    @Query("DELETE FROM TableWidget")
    public abstract void deleteAll();

}
