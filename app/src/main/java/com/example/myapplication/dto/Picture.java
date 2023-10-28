package com.example.myapplication.dto;

import java.util.Date;

public class Picture {
    private String pictureID, route, date, albumID;

    public String getPictureID(){
        return pictureID;
    }
    public String getRoute() {return  route;}
    public String  getDate() {return  date;}
    public String getAlbumID() {return  albumID;}

    public void setPictureID(String pictureID){
        this.pictureID = pictureID;
    }
    public void setRoute(String route){
        this.route = route;
    }
    public void setDate(String date){
        this.date = date;
    }
    public void setAlbumID(String albumID){
        this.albumID = albumID;
    }

}
