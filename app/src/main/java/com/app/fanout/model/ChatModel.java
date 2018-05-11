package com.app.fanout.model;

import android.text.TextUtils;

import com.app.fanout.ChatActivity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by basim on 4/9/18.
 */

public class ChatModel {
    public String id;

    @SerializedName("from")
    public String userName;

    @SerializedName("text")
    public String message;

    public String date;

    public boolean isSender() {
        return !TextUtils.isEmpty(ChatActivity.sUsername) && userName.equalsIgnoreCase(ChatActivity.sUsername);
    }
}
