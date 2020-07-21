package com.pact.consumer;

import com.mashape.unirest.http.exceptions.UnirestException;

import java.time.LocalDateTime;

public class Consumer {
    public static void main(String[] args) throws UnirestException {
        System.out.println(new Client("http://localhost:8080")
                .fetchUserData(args.length> 0 ? Integer.parseInt(args[0]) : 1));
    }
}
