package com.whaleal.mars.core.aggregation.stages;

import com.mongodb.lang.Nullable;
import com.whaleal.mars.core.aggregation.AggregationException;
import com.whaleal.mars.core.aggregation.expressions.Expressions;
import com.whaleal.mars.core.aggregation.expressions.impls.DocumentExpression;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import com.whaleal.mars.core.aggregation.expressions.impls.Fields;


/**
 * Groups input documents by the specified _id expression and for each distinct grouping, outputs a document.
 *
 * @aggregation.expression $group
 */
public class Group extends Stage {
    private final GroupId id;
    private Fields<Group> fields;

    protected Group() {
        super("$group");
        id = null;
    }

    protected Group(GroupId id) {
        super("$group");
        this.id = id;
    }

    /**
     * Creates a group stage with an ID definition
     *
     * @param id the group ID
     * @return the new stage
     */
    public static Group group(GroupId id) {
        return new Group(id);
    }

    /**
     * Creates a group stage with no ID definition
     *
     * @return the new stage
     */
    public static Group group() {
        return new Group();
    }

    /**
     * Creates an unnamed group ID
     *
     * @return the new groupID
     */
    public static GroupId id() {
        return new GroupId();
    }

    /**
     * Creates a named group ID
     *
     * @param name the id name
     * @return the new groupID
     */
    public static GroupId id(String name) {
        return new GroupId(Expressions.field(name));
    }

    /**
     * Creates a named group ID
     *
     * @param name the id name
     * @return the new groupID
     */
    public static GroupId id(Expression name) {
        return new GroupId(name);
    }

    /**
     * Creates a group stage with an ID definition
     *
     * @param id the group ID
     * @return the new stage
     * @deprecated use {@link #group(GroupId)}
     */
    @Deprecated()
    public static Group of(GroupId id) {
        return new Group(id);
    }

    /**
     * Creates a group stage with no ID definition
     *
     * @return the new stage
     * @deprecated user {@link #group()}
     */
    @Deprecated()
    public static Group of() {
        return new Group();
    }

    /**
     * Adds a field to the group.  This method is equivalent to calling {@code field("name", Expression.field("name"))}
     *
     * @param name the field name
     * @return this
     */
    public Group field(String name) {
        return field(name, Expressions.field(name));
    }

    /**
     * Adds a named field to the group with an expression giving the value.
     *
     * @param name       the name of the field
     * @param expression the expression giving the value
     * @return this
     */
    public Group field(String name, Expression expression) {
        if (fields == null) {
            fields = Fields.on(this);
        }
        fields.add(name, expression);
        return this;
    }

    /**
     * @return the fields
     */
    @Nullable
    public Fields<Group> getFields() {
        return fields;
    }

    /**
     * @return the ID
     */
    @Nullable
    public GroupId getId() {
        return id;
    }

    /**
     * Defines a group ID
     */
    public static class GroupId {
        private Expression field;
        private DocumentExpression document;

        protected GroupId() {
            document = Expressions.of();
        }

        protected GroupId(Expression value) {
            if (value instanceof DocumentExpression) {
                document = (DocumentExpression) value;
            } else {
                field = value;
            }
        }

        /**
         * Adds a field to the group.  This method is equivalent to calling {@code field("name", Expression.field("name"))}
         *
         * @param name the field name
         * @return this
         */
        public GroupId field(String name) {
            return field(name, Expressions.field(name));
        }

        /**
         * Adds a named field to the group with an expression giving the value.
         *
         * @param name       the name of the field
         * @param expression the expression giving the value
         * @return this
         */
        public GroupId field(String name, Expression expression) {
            if (field != null) {
                throw new AggregationException("mixedModesNotAllowed_id");
            }
            document.field(name, expression);

            return this;
        }

        /**
         * @return the document
         */
        @Nullable
        public DocumentExpression getDocument() {
            return document;
        }

        /**
         * @return the field
         */
        @Nullable
        public Expression getField() {
            return field;
        }
    }
}
