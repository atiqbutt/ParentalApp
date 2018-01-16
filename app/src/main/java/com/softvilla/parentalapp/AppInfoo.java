package com.softvilla.parentalapp;

/**
 * Created by Malik on 01/08/2017.
 */

public class AppInfoo {

    public String name;
    public String pkgName;
    public int imageId;
    public int isLock;
    public int time;
    public  static  String childId;
    public  static  String pakgName;

    String userName,img,id,pkgNmae;
    int text;

    AppInfoo(String title, int imageId) {
        this.name = title;
        //this.description = description;
        this.imageId = imageId;
    }

    AppInfoo() {

    }
}
