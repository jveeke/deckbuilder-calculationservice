package com.deckbuilder.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class JsonCard {
    //The oracle_id which will be used as the card's uuid.
    @SerializedName("oracle_id")
    private String oracleId;

    //Relevant information about the card
    @SerializedName("name")
    private String name;
    @SerializedName("type_line")
    private String typeLine;
    @SerializedName("colors")
    private List<String> colors;
    @SerializedName("color_identity")
    private List<String> colorIdentity;
    @SerializedName("mana_cost")
    private String manaCost;
    @SerializedName("convertedManaCost")
    private String convertedManaCost;
    @SerializedName("keywords")
    private List<String> keywords;
    @SerializedName("power")
    private String power;
    @SerializedName("toughness")
    private String toughness;
    @SerializedName("oracle_text")
    private String oracleText;

    @SerializedName("rarity")
    private String rarity;
    @SerializedName("lang")
    private String language;
    @SerializedName("artist")
    private String artist;
    @SerializedName("set")
    private String setCode;
    @SerializedName("set_name")
    private String setName;

    //The card's layout (needed to properly process the image)
    @SerializedName("layout")
    private String layout;
}