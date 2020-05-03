package com.netoperation.config.model;

public class OtherIconsDownloadUrls {


    /**
     * dark : {"Logo":"url","Listing":{"bookmark":"url","unbookmark":"url","like":"url","dislike":"url","favorite":"url","unfavorite":"url"},"topbar":{"left_slider_three_line":"url","logo":"url","search":"url","overflow_verticle_dot":"url","back":"url","ttsPlay":"url","ttsPause":"url","comment":"url","bookmark":"url","unbookmark":"url","textSize":"url","share":"url","crown":"url","like":"url","dislike":"url","favorite":"url","unfavorite":"url"}}
     * light : {"Logo":"url","Listing":{"bookmark":"url","unbookmark":"url","like":"url","dislike":"url","favorite":"url","unfavorite":"url"},"topbar":{"left_slider_three_line":"url","logo":"url","search":"url","overflow_verticle_dot":"url","back":"url","ttsPlay":"url","ttsPause":"url","comment":"url","bookmark":"url","unbookmark":"url","textSize":"url","share":"url","crown":"url","like":"url","dislike":"url","favorite":"url","unfavorite":"url"}}
     */

    private OtherIconUrls dark;
    private OtherIconUrls light;

    public OtherIconUrls getDark() {
        return dark;
    }

    public void setDark(OtherIconUrls dark) {
        this.dark = dark;
    }

    public OtherIconUrls getLight() {
        return light;
    }

    public void setLight(OtherIconUrls light) {
        this.light = light;
    }

}
