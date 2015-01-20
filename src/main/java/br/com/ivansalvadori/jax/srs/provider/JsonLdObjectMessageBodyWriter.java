package br.com.ivansalvadori.jax.srs.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.internal.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Produces("application/ld+json")
@Provider
public class JsonLdObjectMessageBodyWriter implements MessageBodyWriter<Object> {

    @Override
    public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return 0;
    }

    @Override
    public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {

        try {
            String json = new Gson().toJson(t);

            JsonLdRep jsonLdRep = new JsonLdRep();
            Field[] declaredFields = t.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                jsonLdRep.addProperties(field.getName(), field.get(t));
            }
            jsonLdRep.addContext("oktoplus", "http://www.oktoplus.com.br/vocab/#");
            System.out.println(jsonLdRep);
            entityStream.write(jsonLdRep.toString().getBytes());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    private static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
        @Override
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String jsonB64 = StringUtils.replace(json.toString(), "\"", "");
            byte[] bytesB64 = jsonB64.getBytes();
            return Base64.decode(bytesB64);
        }

        @Override
        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(new String(src));
        }
    }

    public static void main(String[] args) {

    }
}