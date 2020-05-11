package com.netoperation.model;

/**
 * Created by arvind on 23/5/16.
 */
public class UpdateModel {

    private String app_store_url;
    private String version_code;
    private boolean force_upgrade;
    private String message;
    private String remind_me;

    public String getApp_store_url() {
        return app_store_url;
    }

    public void setApp_store_url(String app_store_url) {
        this.app_store_url = app_store_url;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public boolean getForce_upgrade() {
        return force_upgrade;
    }

    public void setForce_upgrade(boolean force_upgrade) {
        this.force_upgrade = force_upgrade;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRemind_me() {
        return remind_me;
    }

    public void setRemind_me(String remind_me) {
        this.remind_me = remind_me;
    }
}
