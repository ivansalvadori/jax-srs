package br.com.ivansalvadori.jax.srs;

import java.util.ArrayList;
import java.util.List;

import org.glassfish.jersey.server.ResourceConfig;

import br.com.ivansalvadori.jax.srs.doc.DocResource;
import br.com.ivansalvadori.jax.srs.doc.SemanticDocGenerator;
import br.com.ivansalvadori.jax.srs.doc.SupportedClassDoc;
import br.com.ivansalvadori.jax.srs.doc.WebDocResource;
import br.com.ivansalvadori.jax.srs.provider.HtmlObjectMessageBodyProvider;
import br.com.ivansalvadori.jax.srs.provider.JsonLdObjectMessageBodyWriter;
import br.com.ivansalvadori.jax.srs.provider.JsonObjectMessageBodyReader;

public class SemanticRestApplication extends ResourceConfig {

    public static final List<SupportedClassDoc> supportedClasses = new ArrayList<>();

    public SemanticRestApplication() {
        super();
        this.register(WebDocResource.class);
        this.register(DocResource.class);
        this.register(HtmlObjectMessageBodyProvider.class);
        this.register(JsonObjectMessageBodyReader.class);
        this.register(JsonLdObjectMessageBodyWriter.class);
    }

    public void scanSupportedClasses(String classesPackagePath) {
        SemanticDocGenerator semanticDocGenerator = new SemanticDocGenerator();
        supportedClasses.addAll(semanticDocGenerator.loadSupportedClasses(classesPackagePath));
    }

}
