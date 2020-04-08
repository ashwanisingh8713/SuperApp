package com.ns.tts;

/**
 * Created by ashwanisingh on 28/11/18.
 */

public class LanguageItem {

    public String displayName;
    public String country;
    public String language;
    public boolean isSelected;
    public boolean isExist;


    @Override
    public boolean equals(Object obj) {
        super.equals(obj);

        if(obj instanceof LanguageItem) {
            LanguageItem item = (LanguageItem) obj;
            return item.country.equals(country) && item.language.equals(language);
        }

        return false;

    }

    @Override
    public int hashCode() {
        super.hashCode();
        return country.hashCode()+language.hashCode();
    }
}
