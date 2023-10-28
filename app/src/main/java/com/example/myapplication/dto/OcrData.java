package com.example.myapplication.dto;

import com.google.gson.annotations.SerializedName;


public class OcrData {

    private String pictureID, id;

    @SerializedName("data")
    private String data;

    // Getter 및 Setter 메서드

    public String getData() {
        return data;
    }
    public String getPictureID() {return pictureID;}
    public String getId() {return id;}

    public void setData(String data) {
        this.data = data;
    }
    public void setPictureID(String pictureID) { this.pictureID = pictureID; }
    public void setId(String id){this.id = id;}
}
