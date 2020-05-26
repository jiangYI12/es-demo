package com.es.es.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.*;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;

import java.io.IOException;

@Getter
@Setter
public class ParsedStringTermsBucketSerializer extends StdSerializer<ParsedStringTerms.ParsedBucket> {

    public ParsedStringTermsBucketSerializer(Class<ParsedStringTerms.ParsedBucket> t) {
        super(t);
    }

    @Override
    public void serialize(ParsedStringTerms.ParsedBucket value, JsonGenerator gen, SerializerProvider provider) throws IOException, IOException {
        gen.writeStartObject();
        gen.writeObjectField("key", value.getKey());
        gen.writeNumberField("docCount", value.getDocCount());
        gen.writeEndObject();
    }
}
