package com.example.linkshare.Models;

public class WebModel {
    String title;
    String description;
    String mainIamgeURL;

    public String getTilte() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getMainIamgeURL() {
        return mainIamgeURL;
    }

    public void setTilte(String title) {
        this.title = title;
    }

    public void setMainIamgeURL(String mainIamgeURL) {
        this.mainIamgeURL = mainIamgeURL;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}