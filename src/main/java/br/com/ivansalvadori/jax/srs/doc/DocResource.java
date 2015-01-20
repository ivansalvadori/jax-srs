package br.com.ivansalvadori.jax.srs.doc;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.ivansalvadori.jax.srs.SemanticRestApplication;

@Path("")
public class DocResource {

    private final DocumentationTemplate documentationTemplate = new DocumentationTemplate(SemanticRestApplication.supportedClasses);

    @GET
    @Produces("application/ld+json;qs=0.1")
    public Response getWebApiDoc() {
        return Response.ok(this.documentationTemplate).build();
    }

}
