package com.netoperation.default_db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "TableOptional")
public class TableOptional {
    @PrimaryKey()
    private int id = 1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private List<OptionsBean> options;

    public List<OptionsBean> getOptions() {
        return options;
    }

    public void setOptions(List<OptionsBean> options) {
        this.options = options;
    }

    public static class OptionsBean {
        public OptionsBean(String title, int id) {
            this.title = title;
            this.id = id;
        }

        /**
         * title : Read Later
         * id : 1
         */

        private String title;
        private int id;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
