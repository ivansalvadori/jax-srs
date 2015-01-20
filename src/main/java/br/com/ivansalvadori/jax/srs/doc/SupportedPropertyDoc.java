package br.com.ivansalvadori.jax.srs.doc;

import com.google.gson.annotations.SerializedName;

public class SupportedPropertyDoc {

    @SerializedName("@id")
    private String[] id;

    @SerializedName("property")
    private String property;

    @SerializedName("@type")
    private PropertyType propertyType;

    private boolean writeonly = false;
    private boolean readonly = false;
    private boolean required = false;

    public SupportedPropertyDoc(String[] id, String property, PropertyType propertyType) {
        super();
        this.id = id;
        this.property = property;
        this.propertyType = propertyType;
    }

    public SupportedPropertyDoc() {
    }

    public String[] getId() {
        return this.id;
    }

    public void setId(String[] id) {
        this.id = id;
    }

    public String getProperty() {
        return this.property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public PropertyType getPropertyType() {
        return this.propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setWriteonly(boolean writeonly) {
        this.writeonly = writeonly;
    }

    public boolean isWriteonly() {
        return this.writeonly;
    }

    public boolean isReadonly() {
        return this.readonly;
    }

    public boolean isRequired() {
        return this.required;
    }

}
