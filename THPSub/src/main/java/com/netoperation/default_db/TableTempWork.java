package com.netoperation.default_db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TableTempWork")
public class TableTempWork {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String workId;
    private String jsonString;

    public TableTempWork(String workId, String jsonString) {
        this.workId = workId;
        this.jsonString = jsonString;
    }

    public String getWorkId() {
        return workId;
    }

    public String getJsonString() {
        return jsonString;
    }
}
