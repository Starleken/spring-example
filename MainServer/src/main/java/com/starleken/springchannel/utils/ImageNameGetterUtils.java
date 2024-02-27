package com.starleken.springchannel.utils;

public abstract class ImageNameGetterUtils {

    public static String getFromUrl(String imageUrl){
        String[] splited = imageUrl.split("/");

        return splited[splited.length - 1];
    }
}
