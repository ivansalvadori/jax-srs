package br.com.ivansalvadori.jax.srs.doc;

import com.google.gson.annotations.SerializedName;

public class IriTemplateMappingDoc {

    @SerializedName("@type")
    private final String type = "IriTemplateMapping";

    @SerializedName("@id")
    private final String id;

    private final String variable;

    private final boolean required;

    private final String range;

    public IriTemplateMappingDoc(String id, String variable, boolean required, String range) {
        super();
        this.id = id;
        this.variable = variable;
        this.required = required;
        this.range = range;
    }

    public String getId() {
        return this.id;
    }

    public String getVariable() {
        return this.variable;
    }

    public String getType() {
        return this.type;
    }

    public boolean isRequired() {
        return this.required;
    }

    public String getRange() {
        return this.range;
    }

}
