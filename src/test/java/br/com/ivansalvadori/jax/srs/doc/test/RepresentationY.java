package br.com.ivansalvadori.jax.srs.doc.test;

import java.util.Date;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import br.com.ivansalvadori.jax.srs.annotation.Context;
import br.com.ivansalvadori.jax.srs.annotation.CreateResourceOperation;
import br.com.ivansalvadori.jax.srs.annotation.DeleteResourceOperation;
import br.com.ivansalvadori.jax.srs.annotation.IriTemplateMapping;
import br.com.ivansalvadori.jax.srs.annotation.LoadResourceOperation;
import br.com.ivansalvadori.jax.srs.annotation.ReplaceResourceOperation;
import br.com.ivansalvadori.jax.srs.annotation.SemanticClass;
import br.com.ivansalvadori.jax.srs.annotation.SemanticHeader;
import br.com.ivansalvadori.jax.srs.annotation.SemanticHeaders;
import br.com.ivansalvadori.jax.srs.annotation.SupportedOperation;
import br.com.ivansalvadori.jax.srs.annotation.SupportedProperty;
import br.com.ivansalvadori.jax.srs.annotation.Vocabulary;

@Path("/representationY")
@SemanticClass(id = "vocab:ClassY")
@Context({ @Vocabulary(url = "www.vocab.com/", value = "ClassY") })
@SemanticHeaders(@SemanticHeader(id = "vocab:token", name = "token"))
public class RepresentationY {

    @SupportedProperty
    private String propertyS;

    @SupportedProperty
    private RepresentationX repx;

    @PathParam("idY")
    @IriTemplateMapping(id = "vocab:ID")
    private String idY;

    @QueryParam("x")
    @IriTemplateMapping
    private String x;

    @QueryParam("y")
    @IriTemplateMapping
    private String y;

    @SemanticHeader
    @HeaderParam("someToken")
    private String token;

    @GET
    @Path("/load/{idY}/")
    @SupportedOperation(id = "ok:op1", returnedClass = RepresentationZ.class)
    public Response loadClassY(@PathParam("idY") @IriTemplateMapping(id = "vocab:ID") String idY) {
        System.out.println(this.idY + ": " + this.x);
        return null;
    }

    @GET
    @Path("/load/{idY}/loades")
    @LoadResourceOperation(id = "ok:op1", returnedClass = RepresentationZ.class)
    public Response loadClassYx(@IriTemplateMapping(required = false) @PathParam("idy") Date x) {
        return null;
    }

    @POST
    @Path("/load/{idY}/loades")
    @CreateResourceOperation(returnedClass = RepresentationZ.class, expectedClass = RepresentationZ.class)
    public Response post(@IriTemplateMapping @QueryParam("qp1") int qp1,
            @QueryParam("qp2") @IriTemplateMapping(id = "vocab:qp2", required = true) String qp2, @QueryParam("qp3") @IriTemplateMapping boolean qp3) {
        return null;
    }

    @POST
    @Path("/load/{idY}/loades/s")
    @CreateResourceOperation(returnedClass = RepresentationZ.class, expectedClass = RepresentationZ.class)
    public Response posts(@IriTemplateMapping @QueryParam("qp1") int qp1) {
        return null;
    }

    @DELETE
    @Path("/load/{idY}/loades")
    @SemanticHeaders(@SemanticHeader(id = "vocab:credential", name = "cred"))
    @DeleteResourceOperation(returnedClass = RepresentationZ.class, expectedClass = RepresentationZ.class)
    public Response delete(@HeaderParam("passwd") @SemanticHeader(id = "vocab:passwd", required = true) String passwd) {
        return null;
    }

    @PUT
    @Path("/load/{idY}/loades")
    @ReplaceResourceOperation(returnedClass = RepresentationZ.class, expectedClass = RepresentationZ.class)
    public Response put() {
        return null;
    }

}
