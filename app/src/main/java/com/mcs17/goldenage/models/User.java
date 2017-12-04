package com.mcs17.goldenage.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by twindo-mac2 on 27/11/17.
 */
@IgnoreExtraProperties
public class User {
    public String username;
    public String email;

    public User() {

    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
