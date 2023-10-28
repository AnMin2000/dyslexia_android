package com.example.myapplication.dto;

import com.google.gson.annotations.SerializedName;

public class Summarize {
    @SerializedName("data2")
    private String data2;

    // Getter 및 Setter 메서드

    public String getData2() {
        return data2;
    }

    public void setData2(String data) {
        this.data2 = data;
    }
}
