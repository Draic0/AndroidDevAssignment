package com.example.androiddevassignment.network.neww;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class SyncMessage {

    @SerializedName("Id")
    private String id;
    @SerializedName("RequestId")
    private String requestId;
    @SerializedName("MessageType")
    private String messageType;
    @SerializedName("SessionToken")
    private String sessionToken;
    @SerializedName("Message")
    private String message;
    @SerializedName("SettingsHash")
    private long settingsHash;

    public SyncMessage(String requestId, String messageType){
        settingsHash = 0;
        this.requestId = requestId;
        this.messageType = messageType;
        this.message = "{}";
    }

    public SyncMessage(String requestId, String messageType, AuthRequest authRequest){
        this.requestId = requestId;
        this.messageType = messageType;
        Gson gson = new Gson();
        this.message = gson.toJson(authRequest);
    }

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getSettingsHash() {
        return settingsHash;
    }

    public void setSettingsHash(long settingsHash) {
        this.settingsHash = settingsHash;
    }
}
