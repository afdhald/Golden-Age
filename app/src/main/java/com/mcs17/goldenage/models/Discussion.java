package com.mcs17.goldenage.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by twindo-mac2 on 27/11/17.
 */
@IgnoreExtraProperties
public class Discussion {
    public String uid;
    public String topic;
    public String content;

    public Discussion() {

    }

    public Discussion(String uid, String topic, String content) {
        this.uid = uid;
        this.topic = topic;
        this.content = content;
    }
}
