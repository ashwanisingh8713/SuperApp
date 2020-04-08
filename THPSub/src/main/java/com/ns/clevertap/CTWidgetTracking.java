package com.ns.clevertap;

import java.util.List;

public class CTWidgetTracking {

    final List<List<String>> widgetArticleIdsList;
    final List<String> widgetName;

    public CTWidgetTracking(List<List<String>> widgetArticleIdsList, List<String> widgetName) {
        this.widgetArticleIdsList = widgetArticleIdsList;
        this.widgetName = widgetName;
    }

    public List<List<String>> getWidgetArticleIdsList() {
        return widgetArticleIdsList;
    }

    public List<String> getWidgetName() {
        return widgetName;
    }
}
