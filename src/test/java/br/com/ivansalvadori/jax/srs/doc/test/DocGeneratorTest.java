package br.com.ivansalvadori.jax.srs.doc.test;

import java.util.List;

import org.junit.Test;

import br.com.ivansalvadori.jax.srs.doc.SemanticDocGenerator;
import br.com.ivansalvadori.jax.srs.doc.SupportedClassDoc;

public class DocGeneratorTest {

    @Test
    public void loadSemanticClassesTest() {
        SemanticDocGenerator semanticDocGenerator = new SemanticDocGenerator();
        List<SupportedClassDoc> supportedClasses = semanticDocGenerator
                .loadSupportedClasses("br.com.ivansalvadori.jax.srs.doc.test");
        for (SupportedClassDoc supportedClassDoc : supportedClasses) {
			
        	System.out.println(supportedClassDoc);
		}
    }
}
