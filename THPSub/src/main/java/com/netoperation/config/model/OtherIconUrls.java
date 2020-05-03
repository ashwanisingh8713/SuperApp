package com.netoperation.config.model;

public class OtherIconUrls {
    /**
     * Logo : url
     * Listing : {"bookmark":"url","unbookmark":"url","like":"url","dislike":"url","favorite":"url","unfavorite":"url"}
     * topbar : {"left_slider_three_line":"url","logo":"url","search":"url","overflow_verticle_dot":"url","back":"url","ttsPlay":"url","ttsPause":"url","comment":"url","bookmark":"url","unbookmark":"url","textSize":"url","share":"url","crown":"url","like":"url","dislike":"url","favorite":"url","unfavorite":"url"}
     */

    private String Logo;
    private ListingIconUrl Listing;
    private TopbarIconUrl topbar;

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String Logo) {
        this.Logo = Logo;
    }

    public ListingIconUrl getListing() {
        return Listing;
    }

    public void setListing(ListingIconUrl Listing) {
        this.Listing = Listing;
    }

    public TopbarIconUrl getTopbar() {
        return topbar;
    }

    public void setTopbar(TopbarIconUrl topbar) {
        this.topbar = topbar;
    }




}
