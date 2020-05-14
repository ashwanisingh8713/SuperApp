package com.netoperation.config.model;

public class AppThemeBean {
    /**
     * textColors : {"dark":{"XL":"#000000","L":"#000000","M":"#000000","S":"#818181","XS":"#818181","detailpageText":"#000000","WigetText":"#ffffff","breadcrumSelect":"ffffff","breadcrumUnSelect":"818181"},"light":{"XL":"#ffffff","L":"#ffffff","M":"#ffffff","S":"#818181","XS":"#818181","detailpageText":"#ffffff","WigetText":"#ffffff","breadcrumSelect":"ffffff","breadcrumUnSelect":"818181"}}
     * tilesBg : {"dark":"#191919","light":"#ffffff"}
     * screenBg : {"dark":"#191919","light":"#ffffff"}
     * topBarBg : {"dark":"#313131","light":"#ffffff"}
     * bottomBarBg : {"dark":"#313131","light":"#ffffff"}
     * widgetBg : {"background":{"dark":"#191919","light":"#ffffff"}}
     * breadcrumBg : {"dark":"#313131","light":"#ffffff"}
     */

    private TextColorsBean textColors;
    private ColorOptionBean tilesBg;
    private ColorOptionBean screenBg;
    private ColorOptionBean topBarBg;
    private Breadcrumb bottomBar;
    private WidgetBgBean widgetBg;
    private Breadcrumb breadcrumb;

    public TextColorsBean getTextColors() {
        return textColors;
    }

    public void setTextColors(TextColorsBean textColors) {
        this.textColors = textColors;
    }

    public ColorOptionBean getTilesBg() {
        return tilesBg;
    }

    public void setTilesBg(ColorOptionBean tilesBg) {
        this.tilesBg = tilesBg;
    }

    public ColorOptionBean getScreenBg() {
        return screenBg;
    }

    public void setScreenBg(ColorOptionBean screenBg) {
        this.screenBg = screenBg;
    }

    public ColorOptionBean getTopBarBg() {
        return topBarBg;
    }

    public void setTopBarBg(ColorOptionBean topBarBg) {
        this.topBarBg = topBarBg;
    }

    public Breadcrumb getBottomBar() {
        return bottomBar;
    }

    public void setBottomBar(Breadcrumb bottomBarBg) {
        this.bottomBar = bottomBarBg;
    }

    public WidgetBgBean getWidgetBg() {
        return widgetBg;
    }

    public void setWidgetBg(WidgetBgBean widgetBg) {
        this.widgetBg = widgetBg;
    }

    public Breadcrumb getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(Breadcrumb breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public static class TextColorsBean {
        /**
         * dark : {"XL":"#000000","L":"#000000","M":"#000000","S":"#818181","XS":"#818181","detailpageText":"#000000","WigetText":"#ffffff","breadcrumSelect":"ffffff","breadcrumUnSelect":"818181"}
         * light : {"XL":"#ffffff","L":"#ffffff","M":"#ffffff","S":"#818181","XS":"#818181","detailpageText":"#ffffff","WigetText":"#ffffff","breadcrumSelect":"ffffff","breadcrumUnSelect":"818181"}
         */

        private TextColor dark;
        private TextColor light;

        public TextColor getDark() {
            return dark;
        }

        public void setDark(TextColor dark) {
            this.dark = dark;
        }

        public TextColor getLight() {
            return light;
        }

        public void setLight(TextColor light) {
            this.light = light;
        }


    }


    public static class WidgetBgBean {
        /**
         * background : {"dark":"#191919","light":"#ffffff"}
         */

        private ColorOptionBean background;

        public ColorOptionBean getBackground() {
            return background;
        }

        public void setBackground(ColorOptionBean background) {
            this.background = background;
        }


    }


}
