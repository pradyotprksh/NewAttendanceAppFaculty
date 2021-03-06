package com.application.pradyotprakash.newattendanceappfaculty;

public class NoteList extends NotesId{

    private String branch, description, name, noteLink, title, uploadedBy, uploadedOn;

    public NoteList() {
    }

    public NoteList(String branch, String description, String name, String noteLink, String title, String uploadedBy, String uploadedOn) {
        this.branch = branch;
        this.description = description;
        this.name = name;
        this.noteLink = noteLink;
        this.title = title;
        this.uploadedBy = uploadedBy;
        this.uploadedOn = uploadedOn;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoteLink() {
        return noteLink;
    }

    public void setNoteLink(String noteLink) {
        this.noteLink = noteLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

}
