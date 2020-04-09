package com.netoperation.default_db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName ="TableConfiguration")
public class TableConfiguration {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
