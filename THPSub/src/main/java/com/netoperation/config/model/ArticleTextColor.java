package com.netoperation.config.model;

public class ArticleTextColor {
    /**
     * dark : {"title":"#191919","time":"#818181","author":"#818181","section":"#818181","detail":"#818181"}
     * light : {"title":"#191919","time":"#818181","author":"#818181","section":"#818181","detail":"#818181"}
     */

    private ColorBean dark;
    private ColorBean light;

    public ColorBean getDark() {
        return dark;
    }

    public void setDark(ColorBean dark) {
        this.dark = dark;
    }

    public ColorBean getLight() {
        return light;
    }

    public void setLight(ColorBean light) {
        this.light = light;
    }

    public static class ColorBean {
        /**
         * title : #191919
         * time : #818181
         * author : #818181
         * section : #818181
         * detail : #818181
         */

        private String title;
        private String time;
        private String author;
        private String section;
        private String detail;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }

}
