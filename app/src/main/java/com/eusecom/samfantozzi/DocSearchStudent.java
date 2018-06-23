package com.eusecom.samfantozzi;

import java.io.Serializable;

public class DocSearchStudent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String emailId;

    public DocSearchStudent() {

    }
    public DocSearchStudent(String name, String emailId) {
        this.name = name;
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }


}