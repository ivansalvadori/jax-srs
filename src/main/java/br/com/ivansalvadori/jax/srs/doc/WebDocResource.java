package br.com.ivansalvadori.jax.srs.doc;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/webdoc")
public class WebDocResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    @SuppressWarnings("resource")
    public Response getWebApiDoc() {
        InputStream in = this.getClass().getResourceAsStream("/webdoc.html");
        return Response.ok(in).build();
    }

    @GET
    @Path("/jquery")
    @SuppressWarnings("resource")
    public Response loadJquery() {
        InputStream in = this.getClass().getResourceAsStream("/jquery-2.1.1.min.js");
        return Response.ok(in).build();
    }

    @GET
    @Path("/webdoc")
    @SuppressWarnings("resource")
    public Response loadWebdocJS() {
        InputStream in = this.getClass().getResourceAsStream("/webdoc.js");
        return Response.ok(in).build();
    }

    @GET
    @Path("/bootstrap")
    @SuppressWarnings("resource")
    public Response loadBoostrap() {
        InputStream in = this.getClass().getResourceAsStream("/bootstrap.css");
        return Response.ok(in).build();
    }

}
