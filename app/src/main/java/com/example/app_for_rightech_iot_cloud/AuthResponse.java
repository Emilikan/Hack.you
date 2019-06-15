package com.example.app_for_rightech_iot_cloud;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("sucess")
    @Expose
    private Boolean sucess;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("issuedAt")
    @Expose
    private Integer issuedAt;
    @SerializedName("expiresAt")
    @Expose
    private Integer expiresAt;

    public Boolean getSucess() {
        return sucess;
    }

    public void setSucess(Boolean sucess) {
        this.sucess = sucess;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Integer issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Integer getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Integer expiresAt) {
        this.expiresAt = expiresAt;
    }

}

