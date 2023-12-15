package com.starleken.springchannel.constants.exceptionConstants;

public abstract class UserExceptionConstants {

    public static String getNotFoundText(Long id){
        return "User is not found by id: "+ id;
    }

    static public String getNotFoundTextByLogin(String login){
        return "User by login: " + login + " is not found";
    }

    static public String getLoginCredentialsText(){
        return "Login is taken";
    }

    static public String getEmailCredentialsText(){
        return "Email is taken";
    }
}
