package com.example.app_for_rightech_iot_cloud;

public class NotificationsForRecycler {

    private String indicator;
    private String date;
    private String param;
    private int image;
    private int warningImage;

    public NotificationsForRecycler(String indicator, String date, int image,int warningImage,String param){

        this.indicator = indicator;
        this.param = param;
        this.date = date;
        this.image = image;
        this.warningImage = warningImage;
    }

    public String getIndicator() {
        return this.indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImage() {
        return this.image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getWarningImage() {
        return this.warningImage;
    }

    public void setWarningImage(int warningImage) {
        this.warningImage = warningImage;
    }

    public String getParam() {
        return this.param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
