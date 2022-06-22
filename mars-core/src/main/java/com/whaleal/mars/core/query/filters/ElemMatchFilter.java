package com.whaleal.mars.core.query.filters;


import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import java.util.List;

class ElemMatchFilter extends Filter {
    ElemMatchFilter(String field, List<Filter> query) {
        super("$elemMatch", field, query);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext context) {
        writer.writeStartDocument(path(mapper));
        if (isNot()) {
            writer.writeStartDocument("$not");
        }
        writer.writeStartDocument(getName());
        List<Filter> filters = (List<Filter>) getValue();
        if (filters != null) {
            for (Filter filter : filters) {
                filter.encode(mapper, writer, context);
            }
        }
        if (isNot()) {
            writer.writeEndDocument();
        }
        writer.writeEndDocument();
        writer.writeEndDocument();
    }

}
