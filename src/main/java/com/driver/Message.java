package com.driver;

import java.util.Date;

public class Message {
    private int id;
    private String content;
    private Date timestamp;



    public Message(int id, String content) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
