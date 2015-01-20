package br.com.ivansalvadori.jax.srs.doc;

import com.google.gson.annotations.SerializedName;

public class HeaderDoc {

    private final String header;

    @SerializedName("@id")
    private final String id;

    private boolean required;

    public HeaderDoc(String header, String id, boolean required) {
        super();
        this.header = header;
        this.id = id;
        this.required = required;
    }

    public String getHeader() {
        return this.header;
    }

    public String getId() {
        return this.id;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

}
