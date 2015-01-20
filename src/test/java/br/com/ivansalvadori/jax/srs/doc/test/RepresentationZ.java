package br.com.ivansalvadori.jax.srs.doc.test;

import javax.ws.rs.Path;

import br.com.ivansalvadori.jax.srs.annotation.Context;
import br.com.ivansalvadori.jax.srs.annotation.SemanticClass;
import br.com.ivansalvadori.jax.srs.annotation.SupportedProperty;
import br.com.ivansalvadori.jax.srs.annotation.Vocabulary;

@Path("/representationZ")
@Context({ @Vocabulary(url = "www.vocab.com/", value = "ClassZ") })
@SemanticClass
public class RepresentationZ {

    @SupportedProperty
    private String propertyZa;

}
