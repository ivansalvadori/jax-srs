package br.com.ivansalvadori.jax.srs.doc;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class DocumentationTemplate {

    @SerializedName("@context")
    private final String context = "http://www.w3.org/ns/hydra/context.jsonld";

    @SerializedName("@type")
    private final String type = "ApiDocumentation";

    @SerializedName("supportedClasses")
    private List<SupportedClassDoc> supportedClasses;

    public DocumentationTemplate(List<SupportedClassDoc> supportedClasses) {
        super();
        this.supportedClasses = supportedClasses;
    }

    public List<SupportedClassDoc> getSupportedClasses() {
        return this.supportedClasses;
    }

    public void setSupportedClasses(List<SupportedClassDoc> supportedClasses) {
        this.supportedClasses = supportedClasses;
    }

}
