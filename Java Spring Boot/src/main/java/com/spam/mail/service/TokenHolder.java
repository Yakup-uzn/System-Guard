package com.spam.mail.service;

import org.springframework.stereotype.Component;

@Component
public class TokenHolder {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
