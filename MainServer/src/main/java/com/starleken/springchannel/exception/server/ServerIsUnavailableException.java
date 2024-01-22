package com.starleken.springchannel.exception.server;

public class ServerIsUnavailableException extends RuntimeException{

    public ServerIsUnavailableException(String message) {
        super(message);
    }
}
