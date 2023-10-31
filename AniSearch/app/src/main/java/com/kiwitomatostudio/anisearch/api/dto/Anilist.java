package com.kiwitomatostudio.anisearch.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Anilist{
    public int id;
    public int idMal;
    public Title title;
    public ArrayList<String> synonyms;
    public boolean isAdult;
    private class Title{
        @SerializedName("native")
        public String mynative;
        public String romaji;
        public Object english;
    }
    public String getNative(){
        return title.mynative;
    }
    public String getRomaji(){
        return title.romaji;
    }
}

