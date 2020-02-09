package com.movieproject.bean;

import java.util.Date;
import java.util.UUID;

public class ConfirmationToken {


    private long tokenid;

    private String confirmationToken;

    private Date createdDate;

    private UserBody userbody;

    private String email;

    public ConfirmationToken(){};

    public ConfirmationToken(UserBody userbody) {
        this.userbody = userbody;
        createdDate = new Date();
        confirmationToken = UUID.randomUUID().toString();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public UserBody getUser() {
        return userbody;
    }

    public String getEmail() {
        return email;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setTokenid(long tokenid) {
        this.tokenid = tokenid;
    }

    public void setUserbody(UserBody userbody) {
        this.userbody = userbody;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // getters and setters
}


