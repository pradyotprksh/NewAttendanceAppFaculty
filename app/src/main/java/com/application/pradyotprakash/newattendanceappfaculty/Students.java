package com.application.pradyotprakash.newattendanceappfaculty;

/**
 * Created by pradyot on 14/03/18.
 */

public class Students extends StudentId {

    String usn;
    String image;
    String branch;
    String className;
    String semester;

    public Students(String usn, String image, String branch, String className, String semester) {
        this.usn = usn;
        this.image = image;
        this.branch = branch;
        this.className = className;
        this.semester = semester;
    }

    public Students() {

    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
