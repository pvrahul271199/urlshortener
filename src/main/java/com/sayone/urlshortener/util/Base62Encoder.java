package com.sayone.urlshortener.util;

import org.springframework.stereotype.Component;

@Component
public class Base62Encoder {

    private static final String chars =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String encode(long id){
        StringBuilder sb = new StringBuilder();
        while(id>0){
            sb.append(chars.charAt((int) (id % 62)));
            id/=62;
        }
        return sb.reverse().toString();
    }

}
