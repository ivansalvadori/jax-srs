package br.com.ivansalvadori.jax.srs.doc;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SupportedOperationDoc {

    public SupportedOperationDoc() {
        // TODO Auto-generated constructor stub
    }

    @SerializedName("@id")
    private String[] id = null;

    @SerializedName("method")
    private String method;

    @SerializedName("operation")
    private String operation;

    @SerializedName("url")
    private String uri;

    @SerializedName("expects")
    private String expects;

    @SerializedName("returns")
    private String returns;

    @SerializedName("@type")
    private SupportedOperationType type;

    private List<IriTemplateMappingDoc> mapping;

    private List<HeaderDoc> headers;

    public SupportedOperationType getType() {
        return this.type;
    }

    public void setType(SupportedOperationType type) {
        this.type = type;
    }

    public String[] getId() {
        return this.id;
    }

    public void setId(String[] id) {
        this.id = id;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getOperation() {
        return this.operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getExpects() {
        return this.expects;
    }

    public void setExpects(String expects) {
        this.expects = expects;
    }

    public String getReturns() {
        return this.returns;
    }

    public void setReturns(String returns) {
        this.returns = returns;
    }

    public List<IriTemplateMappingDoc> getMapping() {
        return this.mapping;
    }

    public void setMapping(List<IriTemplateMappingDoc> mapping) {
        this.mapping = mapping;
    }

    public List<HeaderDoc> getHeaders() {
        return this.headers;
    }

    public void setHeaders(List<HeaderDoc> headers) {
        this.headers = headers;
    }

}
