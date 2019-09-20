package com.example.androiddevassignment.network.neww;

import com.google.gson.annotations.SerializedName;

public class TokenResponse {
    @SerializedName("AccessToken")
    private String accessToken;
    @SerializedName("TokenType")
    private String tokenType;
    @SerializedName("ExpiresIn")
    private int expiresIn;
    @SerializedName("RefreshToken")
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
