package com.starleken.springchannel.constants.exceptionConstants;

public abstract class PostExceptionConstants {

    static public String getNotFoundTextById(Long id){
        return "Post by id: " + id + " is not found";
    }
}
