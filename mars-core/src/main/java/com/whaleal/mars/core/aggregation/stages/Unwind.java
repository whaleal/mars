package com.whaleal.mars.core.aggregation.stages;


import com.whaleal.mars.core.aggregation.expressions.impls.Expression;

import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;

/**
 * Deconstructs an array field from the input documents to output a document for each element. Each output document is the input document
 * with the value of the array field replaced by the element.
 *
 * @aggregation.expression $unwind
 */
public class Unwind extends Stage {
    private Expression path;
    private String includeArrayIndex;
    private Boolean preserveNullAndEmptyArrays;

    protected Unwind() {
        super("$unwind");
    }

    /**
     * Creates a stage with the named array field
     *
     * @param name the array field
     * @return this
     * @deprecated use {@link #unwind(String)}
     */
    @Deprecated()
    public static Unwind on(String name) {
        return new Unwind()
                   .path(name);
    }

    /**
     * Creates a stage with the named array field
     *
     * @param name the array field
     * @return this
     */
    public static Unwind unwind(String name) {
        return new Unwind()
                   .path(name);
    }

    /**
     * @return the value
     */
    public String getIncludeArrayIndex() {
        return includeArrayIndex;
    }

    /**
     * @return the value
     */
    public Expression getPath() {
        return path;
    }

    /**
     * @return the value
     */
    public Boolean getPreserveNullAndEmptyArrays() {
        return preserveNullAndEmptyArrays;
    }

    /**
     * Optional. The name of a new field to hold the array index of the element. The name cannot start with a dollar sign $.
     *
     * @param name the new name
     * @return this
     */
    public Unwind includeArrayIndex(String name) {
        this.includeArrayIndex = name;
        return this;
    }

    /**
     * @return the value
     */
    public boolean optionsPresent() {
        return includeArrayIndex != null
               || preserveNullAndEmptyArrays != null;
    }

    /**
     * Optional.
     *
     * <li>If true, if the path is null, missing, or an empty array, $unwind outputs the document.
     * <li>If false, if path is null, missing, or an empty array, $unwind does not output a document.
     *
     * @param preserveNullAndEmptyArrays true to preserve
     * @return this
     */
    public Unwind preserveNullAndEmptyArrays(Boolean preserveNullAndEmptyArrays) {
        this.preserveNullAndEmptyArrays = preserveNullAndEmptyArrays;
        return this;
    }

    private Unwind path(String path) {
        this.path = field(path);
        return this;
    }
}
