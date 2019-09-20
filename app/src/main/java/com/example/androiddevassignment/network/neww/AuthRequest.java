package com.example.androiddevassignment.network.neww;

import com.google.gson.annotations.SerializedName;

public class AuthRequest {

    @SerializedName("Login")
    private String login;
    @SerializedName("Password")
    private String password;
    @SerializedName("Thumbprint")
    private String thumbprint;
    @SerializedName("RefreshToken")
    private String refreshToken;
    @SerializedName("TenantId")
    private String tenantId;

    public AuthRequest(String tenantId, String login, String password){
        this.tenantId = tenantId;
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getThumbprint() {
        return thumbprint;
    }

    public void setThumbprint(String thumbprint) {
        this.thumbprint = thumbprint;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
