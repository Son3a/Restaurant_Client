package com.nsb.restaurant.model;

public class SaleModel {
    private String id, content, reason, image, timeStart, timeClose;

    public SaleModel() {
    }

    public SaleModel(String id, String content, String reason, String image, String timeStart, String timeClose) {
        this.id = id;
        this.content = content;
        this.reason = reason;
        this.image = image;
        this.timeStart = timeStart;
        this.timeClose = timeClose;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeClose() {
        return timeClose;
    }

    public void setTimeClose(String timeClose) {
        this.timeClose = timeClose;
    }
}
