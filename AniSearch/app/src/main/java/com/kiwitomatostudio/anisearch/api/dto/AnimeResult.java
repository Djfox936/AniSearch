package com.kiwitomatostudio.anisearch.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class AnimeResult{
    public Anilist anilist;
    public String filename;
    public double episode;
    public double from;
    @SerializedName("to")
    public double myto;
    public double similarity;
    public String video;
    public String image;
}