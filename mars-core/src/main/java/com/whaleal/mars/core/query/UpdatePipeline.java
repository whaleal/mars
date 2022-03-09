package com.whaleal.mars.core.query;

import com.whaleal.mars.core.query.updates.UpdateOperator;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class UpdatePipeline implements UpdateDefinition {

    public static final String UpdatePipeline = "_updatepipeline";

    private final List< UpdateOperator > updates = new ArrayList<>();


    public UpdatePipeline(
            List< UpdateOperator > updates ) {

        this.updates.addAll(updates);

    }


    /**
     * Adds a new operator to this update operation.
     */
    public void add( UpdateOperator operator ) {
        updates.add(operator);
    }


    protected List< UpdateOperator > getUpdates() {
        return updates;
    }

    @Override
    public Boolean isIsolated() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Document getUpdateObject() {
        Document docs = new Document();
        if (updates.isEmpty()) {
            return docs;
        } else if (updates.size() == 1) {
            docs = updates.get(0).toDocument();
            return docs;
        } else {
            List< Document > documentList = new ArrayList<>();
            for (UpdateOperator updateOperator : updates) {
                documentList.add(updateOperator.toDocument());
            }
            docs.put(UpdatePipeline, documentList);
            return docs;
        }
    }

    @Override
    public boolean modifies( String key ) {
        return false;
    }

    @Override
    public void inc( String key ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List< ArrayFilter > getArrayFilters() {
        throw new UnsupportedOperationException();
    }
}
