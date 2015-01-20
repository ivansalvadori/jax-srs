package br.com.ivansalvadori.jax.srs.doc.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import br.com.ivansalvadori.jax.srs.annotation.Context;
import br.com.ivansalvadori.jax.srs.annotation.Link;
import br.com.ivansalvadori.jax.srs.annotation.LoadResourceOperation;
import br.com.ivansalvadori.jax.srs.annotation.SemanticClass;
import br.com.ivansalvadori.jax.srs.annotation.SupportedProperty;
import br.com.ivansalvadori.jax.srs.annotation.Vocabulary;

@Path("/representationX")
@SemanticClass(id = "vocab:ClassX")
@Context({ @Vocabulary(url = "www.vocab.com/", value = "ClassX") })
public class RepresentationX {

    @SupportedProperty
    private String propertyA;

    @SupportedProperty
    private SemanticEnum semanticEnum;

    @Link
    private String url;

    @Link(id = "url2")
    private String url2;

    @GET
    @LoadResourceOperation(returnedClass = RepresentationX.class)
    public Response loadClassX() {
        return null;
    }

}
