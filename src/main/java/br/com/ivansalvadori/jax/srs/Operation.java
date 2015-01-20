package br.com.ivansalvadori.jax.srs;

import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Operation {

    @Expose
    private final String method;

    @Expose
    private final String url;

    @SerializedName("@id")
    @Expose
    private String id;

    @Expose
    private Map<String, Object> entity;

    @Expose
    private Map<String, String> headers;

    public Operation(String method, String url) {
        this.method = method;
        this.url = url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEntityMap(Map<String, Object> entityMap) {
        this.entity = entityMap;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

}
