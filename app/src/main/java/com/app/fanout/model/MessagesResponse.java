package com.app.fanout.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by basim on 4/9/18.
 */

public class MessagesResponse {
    @SerializedName("last-event-id")
    public String lastEventId;

    public List<ChatModel> messages;
}
