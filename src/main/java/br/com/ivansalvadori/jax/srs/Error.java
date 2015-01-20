package br.com.ivansalvadori.jax.srs;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Error {

    @SerializedName("@type")
    private final String type = "Error";

    @SerializedName("@id")
    private String id;

    private String title;
    private String description;

    private List<Error> errors;

}
