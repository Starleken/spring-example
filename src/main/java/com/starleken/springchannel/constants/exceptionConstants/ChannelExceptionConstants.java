package com.starleken.springchannel.constants.exceptionConstants;

public abstract class ChannelExceptionConstants {

    public static String getNotFoundText(Long id){
        return "Channel by id: "+ id +" is not found";
    }

    static public String getNameCredentialsText(){
        return "Name is taken";
    }
}
