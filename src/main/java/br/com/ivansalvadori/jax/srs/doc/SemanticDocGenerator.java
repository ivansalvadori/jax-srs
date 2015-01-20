package br.com.ivansalvadori.jax.srs.doc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import br.com.ivansalvadori.jax.srs.annotation.Context;
import br.com.ivansalvadori.jax.srs.annotation.CreateResourceOperation;
import br.com.ivansalvadori.jax.srs.annotation.DeleteResourceOperation;
import br.com.ivansalvadori.jax.srs.annotation.IriTemplateMapping;
import br.com.ivansalvadori.jax.srs.annotation.Link;
import br.com.ivansalvadori.jax.srs.annotation.LoadResourceOperation;
import br.com.ivansalvadori.jax.srs.annotation.ReplaceResourceOperation;
import br.com.ivansalvadori.jax.srs.annotation.SemanticClass;
import br.com.ivansalvadori.jax.srs.annotation.SemanticCollection;
import br.com.ivansalvadori.jax.srs.annotation.SemanticHeader;
import br.com.ivansalvadori.jax.srs.annotation.SemanticHeaders;
import br.com.ivansalvadori.jax.srs.annotation.SupportedCollection;
import br.com.ivansalvadori.jax.srs.annotation.SupportedOperation;
import br.com.ivansalvadori.jax.srs.annotation.SupportedProperty;
import br.com.ivansalvadori.jax.srs.annotation.Vocabulary;

import com.google.gson.Gson;

public class SemanticDocGenerator {

    public List<SupportedClassDoc> loadSupportedClasses(String packagePath) {
        List<SupportedClassDoc> supportedClasses = new ArrayList<>();
        Reflections reflections = new Reflections(packagePath);
        Set<Class<?>> scanedClasses = reflections.getTypesAnnotatedWith(SemanticClass.class);
        for (Class<?> scaned : scanedClasses) {
            SupportedClassDoc supportedClass = new SupportedClassDoc();
            supportedClass.setType(SemanticClassType.Class);
            if (scaned.isEnum()) {
                supportedClass.setType(SemanticClassType.Enumeration);
                supportedClass.setSupportedConstants(this.processSupportedConstants(scaned));
            }
            String[] ids = this.extractClassIDs(scaned);
            supportedClass.setId(ids);
            Map<String, String> processedContext = this.processContext(scaned);
            supportedClass.setContext(processedContext);
            supportedClass.setSupportedProperties(this.processSupportedProperties(scaned));
            supportedClass.setSupportedOperations(this.processSupportedOperations(scaned));
            supportedClass.setGlobalIriTemplateMapping(this.processGlobalIriTemplateMapping(scaned));
            supportedClass.setGlobalHeaders(this.processGlobalHeaders(scaned));
            supportedClasses.add(supportedClass);
        }

        Set<Class<?>> scanedCollections = reflections.getTypesAnnotatedWith(SemanticCollection.class);
        for (Class<?> scaned : scanedCollections) {
            SupportedClassDoc supportedClass = new SupportedClassDoc();
            supportedClass.setType(SemanticClassType.Collection);
            String[] ids = this.extractCollectionIDs(scaned);
            supportedClass.setId(ids);
            Map<String, String> processedContext = this.processContext(scaned);
            supportedClass.setContext(processedContext);
            supportedClass.setSupportedProperties(this.processSupportedProperties(scaned));
            supportedClass.setSupportedOperations(this.processSupportedOperations(scaned));
            supportedClasses.add(supportedClass);
        }

        return supportedClasses;
    }

    private Set<IriTemplateMappingDoc> processGlobalIriTemplateMapping(Class<?> scaned) {
        Set<IriTemplateMappingDoc> iriTemplateMapping = new HashSet<>();
        Field[] declaredFields = scaned.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(IriTemplateMapping.class)) {
                String id = field.getAnnotation(IriTemplateMapping.class).id();
                if (StringUtils.isEmpty(id)) {
                    id = field.getType().getSimpleName();
                }

                // XXX: Fazer a conversão dos tipos java pasa os tipos XSD
                String range = field.getType().getSimpleName();

                boolean required = field.getAnnotation(IriTemplateMapping.class).required();

                String variable = null;
                if (field.isAnnotationPresent(PathParam.class)) {
                    variable = field.getAnnotation(PathParam.class).value();
                    required = true;
                } else if (field.isAnnotationPresent(QueryParam.class)) {
                    variable = field.getAnnotation(QueryParam.class).value();
                }

                iriTemplateMapping.add(new IriTemplateMappingDoc(id, variable, required, range));
            }
        }
        if (iriTemplateMapping.isEmpty()) {
            return null;
        }

        return iriTemplateMapping;
    }

    private Set<HeaderDoc> processGlobalHeaders(Class<?> scaned) {
        Set<HeaderDoc> headersDoc = new HashSet<>();

        if (scaned.isAnnotationPresent(SemanticHeaders.class)) {
            SemanticHeader[] classHeaders = scaned.getAnnotation(SemanticHeaders.class).value();
            for (SemanticHeader semanticHeader : classHeaders) {
                headersDoc.add(new HeaderDoc(semanticHeader.name(), semanticHeader.id(), semanticHeader.required()));
            }
        }

        Field[] declaredFields = scaned.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(SemanticHeader.class)) {
                String id = field.getAnnotation(SemanticHeader.class).id();
                if (StringUtils.isEmpty(id)) {
                    id = field.getType().getSimpleName();
                }

                String tokenName = null;
                if (field.isAnnotationPresent(HeaderParam.class)) {
                    tokenName = field.getAnnotation(HeaderParam.class).value();
                }

                boolean required = field.getAnnotation(SemanticHeader.class).required();
                headersDoc.add(new HeaderDoc(tokenName, id, required));
            }
        }
        if (headersDoc.isEmpty()) {
            return null;
        }

        return headersDoc;
    }

    private List<String> processSupportedConstants(Class<?> scaned) {
        List<String> constants = new ArrayList<>();
        List<?> enumConstants = Arrays.asList(scaned.getEnumConstants());
        for (Object object : enumConstants) {
            constants.add(object.toString());
        }
        if (constants.isEmpty()) {
            return null;
        }
        return constants;
    }

    private String[] extractClassIDs(Class<?> scaned) {
        String[] ids = scaned.getAnnotation(SemanticClass.class).id();
        if (ids[0].isEmpty()) {
            ids[0] = scaned.getSimpleName();
        }
        return ids;
    }

    private String[] extractCollectionIDs(Class<?> scaned) {
        String[] ids = scaned.getAnnotation(SemanticCollection.class).id();
        if (ids[0].isEmpty()) {
            ids[0] = scaned.getSimpleName();
        }
        return ids;
    }

    private Map<String, String> processContext(Class<?> scaned) {
        Map<String, String> context = new HashMap<>();
        if (scaned.isAnnotationPresent(Context.class)) {
            Vocabulary[] vocabularies = scaned.getAnnotation(Context.class).value();
            for (Vocabulary vocabulary : vocabularies) {
                context.put(vocabulary.value(), vocabulary.url());
            }

        } else {
            // TODO: tratar o contexto padrao
            // context.put("xs", "http://www.w3.org/2001/XMLSchema");
        }
        if (context.isEmpty()) {
            return null;
        }
        return context;
    }

    private List<SupportedPropertyDoc> processSupportedProperties(Class<?> scaned) {
        List<SupportedPropertyDoc> supportedProperties = new ArrayList<>();
        Field[] fields = scaned.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            SupportedPropertyDoc supportedPropertyDoc = null;
            String[] id = null;
            PropertyType propertyType = PropertyType.SupportedProperty;

            if (field.isAnnotationPresent(SupportedProperty.class) || field.isAnnotationPresent(Link.class)
                    || field.isAnnotationPresent(SupportedCollection.class)) {

                boolean writeOnly = false;
                boolean readOnly = false;
                boolean required = false;

                if (field.isAnnotationPresent(SupportedProperty.class)) {
                    id = field.getAnnotation(SupportedProperty.class).id();
                    writeOnly = field.getAnnotation(SupportedProperty.class).writeonly();
                    readOnly = field.getAnnotation(SupportedProperty.class).readonly();
                    required = field.getAnnotation(SupportedProperty.class).required();

                } else if (field.isAnnotationPresent(SupportedCollection.class)) {
                    id = field.getAnnotation(SupportedCollection.class).id();
                    writeOnly = field.getAnnotation(SupportedCollection.class).writeonly();
                    readOnly = field.getAnnotation(SupportedCollection.class).readonly();
                    required = field.getAnnotation(SupportedCollection.class).required();
                    propertyType = PropertyType.SupportedCollection;

                }

                if (id != null && id[0].isEmpty()) {
                    id[0] = field.getType().getSimpleName();
                }

                if (field.isAnnotationPresent(Link.class)) {
                    id = field.getAnnotation(Link.class).id();
                    propertyType = PropertyType.Link;
                    if (id != null && id[0].isEmpty()) {
                        id = new String[] { "Link" };
                    }
                }

                String name = field.getName();
                supportedPropertyDoc = new SupportedPropertyDoc(id, name, propertyType);

                supportedPropertyDoc.setReadonly(readOnly);
                supportedPropertyDoc.setRequired(required);
                supportedPropertyDoc.setWriteonly(writeOnly);

                supportedProperties.add(supportedPropertyDoc);
            }

        }
        if (supportedProperties.isEmpty()) {
            return null;
        }
        return supportedProperties;
    }

    private List<SupportedOperationDoc> processSupportedOperations(Class<?> scaned) {
        List<SupportedOperationDoc> supportedOperations = new ArrayList<>();
        Method[] declaredMethods = scaned.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(SupportedOperation.class) || method.isAnnotationPresent(LoadResourceOperation.class)
                    || method.isAnnotationPresent(CreateResourceOperation.class) || method.isAnnotationPresent(ReplaceResourceOperation.class)
                    || method.isAnnotationPresent(DeleteResourceOperation.class)) {
                SupportedOperationDoc supportedOperationDoc = new SupportedOperationDoc();
                String httpMethod = this.getHttpMethod(method);
                supportedOperationDoc.setMethod(httpMethod);

                if (method.isAnnotationPresent(LoadResourceOperation.class)) {
                    String[] operationId = method.getAnnotation(LoadResourceOperation.class).id();
                    if (StringUtils.isNoneEmpty(operationId)) {
                        supportedOperationDoc.setId(operationId);
                    }
                    supportedOperationDoc.setType(SupportedOperationType.LoadResourceOperation);
                    if (method.getAnnotation(LoadResourceOperation.class).returnedClass() != Object.class) {
                        Class<?> returnedClass = method.getAnnotation(LoadResourceOperation.class).returnedClass();
                        this.processOperationReturns(supportedOperationDoc, returnedClass);
                    }
                    if (StringUtils.isNoneEmpty(method.getAnnotation(LoadResourceOperation.class).returnedId())) {
                        String returnedId = method.getAnnotation(LoadResourceOperation.class).returnedId();
                        supportedOperationDoc.setReturns(returnedId);
                    }

                    if (method.getAnnotation(LoadResourceOperation.class).expectedClass() != Object.class) {
                        Class<?> expectedClass = method.getAnnotation(LoadResourceOperation.class).expectedClass();
                        this.processOperationExpects(supportedOperationDoc, expectedClass);
                    }
                    if (StringUtils.isNotEmpty(method.getAnnotation(LoadResourceOperation.class).expectedId())) {
                        String expectedId = method.getAnnotation(LoadResourceOperation.class).expectedId();
                        supportedOperationDoc.setExpects(expectedId);
                    }
                }
                if (method.isAnnotationPresent(SupportedOperation.class)) {
                    supportedOperationDoc.setType(SupportedOperationType.SupportedOperation);

                    String[] operationId = method.getAnnotation(SupportedOperation.class).id();
                    if (StringUtils.isNoneEmpty(operationId)) {
                        supportedOperationDoc.setId(operationId);
                    }

                    if (method.getAnnotation(SupportedOperation.class).returnedClass() != Object.class) {
                        Class<?> returnedClass = method.getAnnotation(SupportedOperation.class).returnedClass();
                        this.processOperationReturns(supportedOperationDoc, returnedClass);
                    }

                    if (StringUtils.isNoneEmpty(method.getAnnotation(SupportedOperation.class).returnedId())) {
                        String returnedId = method.getAnnotation(SupportedOperation.class).returnedId();
                        supportedOperationDoc.setReturns(returnedId);
                    }

                    if (method.getAnnotation(SupportedOperation.class).expectedClass() != Object.class) {
                        Class<?> expectedClass = method.getAnnotation(SupportedOperation.class).expectedClass();
                        this.processOperationExpects(supportedOperationDoc, expectedClass);
                    }
                    if (StringUtils.isNotEmpty(method.getAnnotation(SupportedOperation.class).expectedId())) {
                        String expectedId = method.getAnnotation(SupportedOperation.class).expectedId();
                        supportedOperationDoc.setExpects(expectedId);
                    }
                }
                if (method.isAnnotationPresent(CreateResourceOperation.class)) {

                    String[] operationId = method.getAnnotation(CreateResourceOperation.class).id();
                    if (StringUtils.isNoneEmpty(operationId)) {
                        supportedOperationDoc.setId(operationId);
                    }

                    supportedOperationDoc.setType(SupportedOperationType.CreateResourceOperation);
                    if (method.getAnnotation(CreateResourceOperation.class).returnedClass() != Object.class) {
                        Class<?> returnedClass = method.getAnnotation(CreateResourceOperation.class).returnedClass();
                        this.processOperationReturns(supportedOperationDoc, returnedClass);
                    }
                    if (StringUtils.isNoneEmpty(method.getAnnotation(CreateResourceOperation.class).returnedId())) {
                        String returnedId = method.getAnnotation(CreateResourceOperation.class).returnedId();
                        supportedOperationDoc.setReturns(returnedId);
                    }

                    if (method.getAnnotation(CreateResourceOperation.class).expectedClass() != Object.class) {
                        Class<?> expectedClass = method.getAnnotation(CreateResourceOperation.class).expectedClass();
                        this.processOperationExpects(supportedOperationDoc, expectedClass);
                    }
                    if (StringUtils.isNotEmpty(method.getAnnotation(CreateResourceOperation.class).expectedId())) {
                        String expectedId = method.getAnnotation(CreateResourceOperation.class).expectedId();
                        supportedOperationDoc.setExpects(expectedId);
                    }
                }

                if (method.isAnnotationPresent(ReplaceResourceOperation.class)) {

                    String[] operationId = method.getAnnotation(ReplaceResourceOperation.class).id();
                    if (StringUtils.isNoneEmpty(operationId)) {
                        supportedOperationDoc.setId(operationId);
                    }

                    supportedOperationDoc.setType(SupportedOperationType.UpdateResourceOperation);
                    if (method.getAnnotation(ReplaceResourceOperation.class).returnedClass() != Object.class) {
                        Class<?> returnedClass = method.getAnnotation(ReplaceResourceOperation.class).returnedClass();
                        this.processOperationReturns(supportedOperationDoc, returnedClass);
                    }
                    if (StringUtils.isNoneEmpty(method.getAnnotation(ReplaceResourceOperation.class).returnedId())) {
                        String returnedId = method.getAnnotation(ReplaceResourceOperation.class).returnedId();
                        supportedOperationDoc.setReturns(returnedId);
                    }
                    if (method.getAnnotation(ReplaceResourceOperation.class).expectedClass() != Object.class) {
                        Class<?> expectedClass = method.getAnnotation(ReplaceResourceOperation.class).expectedClass();
                        this.processOperationExpects(supportedOperationDoc, expectedClass);
                    }
                    if (StringUtils.isNotEmpty(method.getAnnotation(ReplaceResourceOperation.class).expectedId())) {
                        String expectedId = method.getAnnotation(ReplaceResourceOperation.class).expectedId();
                        supportedOperationDoc.setExpects(expectedId);
                    }
                }

                if (method.isAnnotationPresent(DeleteResourceOperation.class)) {

                    String[] operationId = method.getAnnotation(DeleteResourceOperation.class).id();
                    if (StringUtils.isNoneEmpty(operationId)) {
                        supportedOperationDoc.setId(operationId);
                    }

                    supportedOperationDoc.setType(SupportedOperationType.DeleteResourceOperation);
                    if (method.getAnnotation(DeleteResourceOperation.class).returnedClass() != Object.class) {
                        Class<?> returnedClass = method.getAnnotation(DeleteResourceOperation.class).returnedClass();
                        this.processOperationReturns(supportedOperationDoc, returnedClass);
                    }
                    if (StringUtils.isNoneEmpty(method.getAnnotation(DeleteResourceOperation.class).returnedId())) {
                        String returnedId = method.getAnnotation(DeleteResourceOperation.class).returnedId();
                        supportedOperationDoc.setReturns(returnedId);
                    }
                    if (method.getAnnotation(DeleteResourceOperation.class).expectedClass() != Object.class) {
                        Class<?> expectedClass = method.getAnnotation(DeleteResourceOperation.class).expectedClass();
                        this.processOperationExpects(supportedOperationDoc, expectedClass);
                    }
                    if (StringUtils.isNotEmpty(method.getAnnotation(DeleteResourceOperation.class).expectedId())) {
                        String expectedId = method.getAnnotation(DeleteResourceOperation.class).expectedId();
                        supportedOperationDoc.setExpects(expectedId);
                    }
                }

                String queryParams = this.processQueryParams(method);
                supportedOperationDoc.setMapping(this.processParamsTemplateMapping(method));
                String url = this.getUrlMethod(scaned, method);
                url = url + queryParams;
                supportedOperationDoc.setUri(url);
                supportedOperationDoc.setHeaders(this.processHeaders(method));
                supportedOperations.add(supportedOperationDoc);
            }

        }
        if (supportedOperations.isEmpty()) {
            return null;
        }
        return supportedOperations;
    }

    private String processQueryParams(Method method) {
        List<String> params = new ArrayList<>();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (Annotation[] annotations : parameterAnnotations) {
            List<Annotation> annotationsPresent = Arrays.asList(annotations);
            for (Annotation annotation : annotationsPresent) {
                if (annotation.annotationType().equals(QueryParam.class)) {
                    QueryParam qp = (QueryParam) annotation;
                    params.add(qp.value());
                }
            }
        }

        if (params.isEmpty()) {
            return "";
        }

        StringBuilder qp = new StringBuilder("{?");
        for (int i = 0; i < params.size(); i++) {
            qp.append(params.get(i));
            if (i != params.size() - 1) {
                qp.append(",");
            }
        }
        qp.append("}");
        return qp.toString();
    }

    private List<IriTemplateMappingDoc> processParamsTemplateMapping(Method method) {
        List<IriTemplateMappingDoc> mapping = new ArrayList<>();

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        int paramControl = 0;
        for (Annotation[] annotations : parameterAnnotations) {
            List<Annotation> annotationsPresent = Arrays.asList(annotations);
            for (Annotation annotation : annotationsPresent) {
                if (annotation.annotationType().equals(IriTemplateMapping.class)) {
                    IriTemplateMapping templateAnnotation = (IriTemplateMapping) annotation;

                    String id = templateAnnotation.id();
                    String range = null;
                    if (StringUtils.isEmpty(id)) {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        id = parameterTypes[paramControl].getSimpleName();
                        // XXX: Fazer a conversão dos tipos java para XSD
                        range = parameterTypes[paramControl].getSimpleName();
                    }

                    boolean required = this.isIriMappingRequired(annotationsPresent, templateAnnotation);

                    String variableName = this.discoverParamName(annotationsPresent);
                    String variable = variableName;

                    mapping.add(new IriTemplateMappingDoc(id, variable, required, range));
                }
            }

            paramControl++;
        }

        if (mapping.isEmpty()) {
            return null;
        }
        return mapping;
    }

    private List<HeaderDoc> processHeaders(Method method) {
        List<HeaderDoc> headers = new ArrayList<>();

        // header na assinatura
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        int paramControl = 0;
        for (Annotation[] annotations : parameterAnnotations) {
            List<Annotation> annotationsPresent = Arrays.asList(annotations);
            for (Annotation annotation : annotationsPresent) {
                if (annotation.annotationType().equals(SemanticHeader.class)) {
                    SemanticHeader semanticHeaderAnnotation = (SemanticHeader) annotation;

                    String id = semanticHeaderAnnotation.id();
                    if (StringUtils.isEmpty(id)) {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        id = parameterTypes[paramControl].getSimpleName();
                    }

                    boolean required = semanticHeaderAnnotation.required();
                    String headerName = this.discoverHeaderName(annotationsPresent);

                    headers.add(new HeaderDoc(headerName, id, required));
                }
            }

            paramControl++;
        }

        // descrição do metodo
        if (method.isAnnotationPresent(SemanticHeaders.class)) {
            SemanticHeader[] methodHeaders = method.getAnnotation(SemanticHeaders.class).value();
            for (SemanticHeader semanticHeader : methodHeaders) {
                headers.add(new HeaderDoc(semanticHeader.name(), semanticHeader.id(), semanticHeader.required()));
            }
        }

        if (headers.isEmpty()) {
            return null;
        }
        return headers;
    }

    private String discoverHeaderName(List<Annotation> annotationsPresent) {
        for (Annotation annotation : annotationsPresent) {
            if (annotation.annotationType().equals(HeaderParam.class)) {
                HeaderParam hp = (HeaderParam) annotation;
                return hp.value();
            }
        }

        throw new RuntimeException("IRI Template without a query or path param");
    }

    private boolean isIriMappingRequired(List<Annotation> annotationsPresent, IriTemplateMapping templateAnnotation) {
        for (Annotation annotation : annotationsPresent) {
            if (annotation.annotationType().equals(PathParam.class)) {
                return true;
            }
        }
        return templateAnnotation.required();
    }

    private String discoverParamName(List<Annotation> annotationsPresent) {
        for (Annotation annotation : annotationsPresent) {
            if (annotation.annotationType().equals(QueryParam.class)) {
                QueryParam qp = (QueryParam) annotation;
                return qp.value();
            }
            if (annotation.annotationType().equals(PathParam.class)) {
                PathParam qp = (PathParam) annotation;
                return qp.value();
            }
        }

        throw new RuntimeException("IRI Template without a query or path param");
    }

    private void processOperationExpects(SupportedOperationDoc supportedOperationDoc, Class<?> expectedClass) {
        if (expectedClass.isAnnotationPresent(SemanticClass.class)) {
            String[] id = expectedClass.getAnnotation(SemanticClass.class).id();
            supportedOperationDoc.setExpects(new Gson().toJson(id));
            if (id[0].isEmpty()) {
                supportedOperationDoc.setExpects(expectedClass.getSimpleName());
            }

        }
        if (expectedClass.isAnnotationPresent(SemanticCollection.class)) {
            String[] id = expectedClass.getAnnotation(SemanticCollection.class).id();
            supportedOperationDoc.setExpects(new Gson().toJson(id));
            if (id[0].isEmpty()) {
                supportedOperationDoc.setExpects(expectedClass.getSimpleName());
            }
        }
    }

    private void processOperationReturns(SupportedOperationDoc supportedOperationDoc, Class<?> returnedClass) {
        if (returnedClass.isAnnotationPresent(SemanticClass.class)) {
            String[] id = returnedClass.getAnnotation(SemanticClass.class).id();
            supportedOperationDoc.setReturns(new Gson().toJson(id));
            if (id[0].isEmpty()) {
                supportedOperationDoc.setReturns(returnedClass.getSimpleName());
            }

        }
        if (returnedClass.isAnnotationPresent(SemanticCollection.class)) {
            String[] id = returnedClass.getAnnotation(SemanticCollection.class).id();
            supportedOperationDoc.setReturns(new Gson().toJson(id));
            if (id[0].isEmpty()) {
                supportedOperationDoc.setReturns(returnedClass.getSimpleName());
            }
        }
    }

    private String getUrlMethod(Class<?> scaned, Method method) {
        StringBuilder url = new StringBuilder();
        if (scaned.isAnnotationPresent(Path.class)) {
            url.append(scaned.getAnnotation(Path.class).value());
        }
        if (method.isAnnotationPresent(Path.class)) {
            String methodPath = method.getAnnotation(Path.class).value();
            if (url.toString().endsWith("/") && methodPath.startsWith("/")) {
                url = new StringBuilder(url.substring(0, url.length() - 1));
            } else if (!url.toString().endsWith("/") && !methodPath.startsWith("/")) {
                url.append("/");
            }

            url.append(methodPath);
        }

        if (org.apache.commons.lang3.StringUtils.isEmpty(url.toString())) {
            throw new RuntimeException("Supported method or supported class does not have an http url associated");
        }

        // XXX codigo que obriga a url iniciar com /
        // if (!StringUtils.startsWith(url, "/")) {
        // url.insert(0, "/");
        // }

        return url.toString();
    }

    private String getHttpMethod(Method method) {
        if (method.isAnnotationPresent(GET.class)) {
            return "GET";
        } else if (method.isAnnotationPresent(POST.class)) {
            return "POST";
        } else if (method.isAnnotationPresent(PUT.class)) {
            return "PUT";
        } else if (method.isAnnotationPresent(DELETE.class)) {
            return "DELETE";
        } else if (method.isAnnotationPresent(OPTIONS.class)) {
            return "OPTIONS";
        } else if (method.isAnnotationPresent(HEAD.class)) {
            return "HEAD";
        }

        throw new RuntimeException("Supported method does not have an http method associated");
    }
}
