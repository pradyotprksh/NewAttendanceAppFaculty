package com.application.pradyotprakash.newattendanceappfaculty;

public class EventList extends EventId{

    private String title, description, uploadedBy, imageLink, uploadedOn;

    public EventList() {
    }

    public EventList(String title, String description, String uploadedBy, String imageLink, String uploadedOn) {
        this.title = title;
        this.description = description;
        this.uploadedBy = uploadedBy;
        this.imageLink = imageLink;
        this.uploadedOn = uploadedOn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }
}
