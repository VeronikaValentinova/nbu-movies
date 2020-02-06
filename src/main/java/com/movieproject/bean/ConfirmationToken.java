package com.movieproject.bean;

import java.util.Date;
import java.util.UUID;

public class ConfirmationToken {


    private long tokenid;

    private String confirmationToken;

    private Date createdDate;

    private User user;

    public ConfirmationToken(User user) {
        this.user = user;
        createdDate = new Date();
        confirmationToken = UUID.randomUUID().toString();
    }

    // getters and setters
}
