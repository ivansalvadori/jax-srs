package br.com.ivansalvadori.jax.srs.doc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class SupportedClassDoc {

    @SerializedName("@context")
    private Map<String, String> context = new HashMap<>();

    @SerializedName("@id")
    private List<String> id;

    @SerializedName("@type")
    private SemanticClassType type;

    private List<SupportedPropertyDoc> supportedProperties;

    private List<SupportedOperationDoc> supportedOperations;

    private List<String> supportedConstants;

    private Set<IriTemplateMappingDoc> globalIriTemplateMapping;

    private Set<HeaderDoc> globalHeaders;

    public void setId(String... id) {
        if (this.id == null) {
            this.id = new ArrayList<>();
        }

        for (String string : id) {
            this.id.add(string);
        }

    }

    public Map<String, String> getContext() {
        return this.context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    public List<String> getId() {
        return this.id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }

    public List<SupportedPropertyDoc> getSupportedProperties() {
        return this.supportedProperties;
    }

    public void setSupportedProperties(List<SupportedPropertyDoc> supportedProperties) {
        this.supportedProperties = supportedProperties;
    }

    public List<SupportedOperationDoc> getSupportedOperations() {
        return this.supportedOperations;
    }

    public void setSupportedOperations(List<SupportedOperationDoc> supportedOperations) {
        this.supportedOperations = supportedOperations;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public SemanticClassType getType() {
        return this.type;
    }

    public void setType(SemanticClassType type) {
        this.type = type;
    }

    public List<String> getSupportedConstants() {
        return this.supportedConstants;
    }

    public void setSupportedConstants(List<String> supportedConstants) {
        this.supportedConstants = supportedConstants;
    }

    public Set<IriTemplateMappingDoc> getGlobalIriTemplateMapping() {
        return this.globalIriTemplateMapping;
    }

    public void setGlobalIriTemplateMapping(Set<IriTemplateMappingDoc> globalIriTemplateMapping) {
        this.globalIriTemplateMapping = globalIriTemplateMapping;
    }

    public Set<HeaderDoc> getGlobalHeaders() {
        return globalHeaders;
    }

    public void setGlobalHeaders(Set<HeaderDoc> globalHeaders) {
        this.globalHeaders = globalHeaders;
    }

}
