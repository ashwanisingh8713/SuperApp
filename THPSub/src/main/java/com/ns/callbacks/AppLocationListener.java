package com.ns.callbacks;

/**
 * Created by ashwanisingh on 02/03/18.
 */

public interface AppLocationListener {

    void locationReceived(String maxAddress, String pin, String state, String city,
                          String subCity, String countryCode, String countryName);
    void locationFailed();
}
