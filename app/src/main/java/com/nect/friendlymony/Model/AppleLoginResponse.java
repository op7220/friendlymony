package com.nect.friendlymony.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppleLoginResponse {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("kid")
    @Expose
    private String kid;
    @SerializedName("alg")
    @Expose
    private String alg;
    @SerializedName("iss")
    @Expose
    private String iss;
    @SerializedName("aud")
    @Expose
    private String aud;
    @SerializedName("sub")
    @Expose
    private String sub;
    @SerializedName("exp")
    @Expose
    private String exp;
    @SerializedName("iat")
    @Expose
    private String iat;
    @SerializedName("access_token")
    @Expose
    private String access_token;
    @SerializedName("expires_in")
    @Expose
    private String expires_in;
    @SerializedName("refresh_token")
    @Expose
    private String refresh_token;
    @SerializedName("id_token")
    @Expose
    private String id_token;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getIat() {
        return iat;
    }

    public void setIat(String iat) {
        this.iat = iat;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }
}
