package br.com.ivansalvadori.jax.srs.doc.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import br.com.ivansalvadori.jax.srs.annotation.LoadResourceOperation;
import br.com.ivansalvadori.jax.srs.annotation.SemanticClass;
import br.com.ivansalvadori.jax.srs.annotation.SupportedProperty;

@SemanticClass
@Path("/pseudoClass")
public class PseudoSemanticClass {

    @SupportedProperty
    private String pseudoProperty1;

    @SupportedProperty
    private String pseudoProperty2;

    @SupportedProperty
    private String pseudoProperty3;

    @GET
    @LoadResourceOperation(returnedClass = PseudoSemanticClass.class)
    public Response loadPseudoSemanticClass() {
        return null;
    }

}
