package com.polarbookshop.catalogservice.infrastructure.configuration;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class IsbnCodeDeserializer extends StdDeserializer<String> {

    public IsbnCodeDeserializer() {
        super(String.class);
    }

    @Override
    public String deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = parser.getCodec().readTree(parser);
        String result = "";

        return node.toString();
    }
}
