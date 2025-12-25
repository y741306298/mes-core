package com.brt.dto;

import java.io.Serializable;


public class AuthResponse implements Serializable {
    private static final long serialVersionUID = -1925565283639892505L;

    /**
     * jwt token
     */
    private String token;

    /**
     * 用于客户端混淆md5加密
     */
    private String randomKey;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRandomKey() {
        return randomKey;
    }

    public void setRandomKey(String randomKey) {
        this.randomKey = randomKey;
    }
    
    @Override
    public String toString() {
        return "AuthResponse{token=" + token + 
                ", randomKey=" + randomKey +
                "}";
    }
}
