package com.whaleal.mars.core.internal;



import com.whaleal.mars.codecs.pojo.annotations.Constructor;
import com.whaleal.mars.codecs.pojo.annotations.PropIgnore;
import com.whaleal.mars.codecs.pojo.annotations.Property;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.mongodb.assertions.Assertions.isTrueArgument;
import static com.mongodb.assertions.Assertions.notNull;
import static java.util.Arrays.asList;

/**
 *
 * A MongoDB namespace, which includes a database name and collection name.
 *
 * @author wh
 *
 * @see  com.mongodb.MongoNamespace;
 *
 */
public class MongoNamespace  implements Comparable<MongoNamespace>, Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * The collection name in which to execute a command.
     */
    public static final String COMMAND_COLLECTION_NAME = "$cmd";

    private static final Set<Character> PROHIBITED_CHARACTERS_IN_DATABASE_NAME =
            new HashSet<Character>(asList('\0', '/', '\\', ' ', '"', '.'));


    private final String databaseName;

    private final String collectionName;
    @PropIgnore
    private final String fullName;  // cache to avoid repeated string building

    /**
     * Check the validity of the given database name. A valid database name is non-null, non-empty, and does not contain any of the
     * following characters: {@code '\0', '/', '\\', ' ', '"', '.'}. The server may impose additional restrictions on database names.
     *
     * @param databaseName the database name
     * @throws IllegalArgumentException if the database name is invalid
     * @since 3.4
     * @mongodb.driver.manual reference/limits/#naming-restrictions Naming Restrictions
     */
    public static void checkDatabaseNameValidity(final String databaseName) {
        notNull("databaseName", databaseName);
        isTrueArgument("databaseName is not empty", !databaseName.isEmpty());
        for (int i = 0; i < databaseName.length(); i++) {
            if (PROHIBITED_CHARACTERS_IN_DATABASE_NAME.contains(databaseName.charAt(i))) {
                throw new IllegalArgumentException("state should be: databaseName does not contain '" + databaseName.charAt(i) + "'");
            }
        }
    }

    /**
     * Check the validity of the given collection name.   A valid collection name is non-null and non-empty.  The server may impose
     * additional restrictions on collection names.
     *
     * @param collectionName the collection name
     * @throws IllegalArgumentException if the collection name is invalid
     *
     * @mongodb.driver.manual reference/limits/#naming-restrictions Naming Restrictions
     */
    public static void checkCollectionNameValidity(final String collectionName) {
        notNull("collectionName", collectionName);
        isTrueArgument("collectionName is not empty", !collectionName.isEmpty());
    }

    /**
     * Construct an instance for the given full name.  The database name is the string preceding the first {@code "."} character.
     *
     * @param fullName the non-null full namespace
     * @see #checkDatabaseNameValidity(String)
     * @see #checkCollectionNameValidity(String)
     */
    public MongoNamespace(final String fullName) {
        notNull("fullName", fullName);
        this.fullName = fullName;
        this.databaseName = getDatatabaseNameFromFullName(fullName);
        this.collectionName = getCollectionNameFullName(fullName);
        checkDatabaseNameValidity(databaseName);
        checkCollectionNameValidity(collectionName);
    }

    /**
     * Construct an instance from the given database name and collection name.
     *
     * @param databaseName   the valid database name
     * @param collectionName the valid collection name
     * @see #checkDatabaseNameValidity(String)
     * @see #checkCollectionNameValidity(String)
     */
    @Constructor
    public MongoNamespace(@Property("databaseName") final String databaseName,
                          @Property("collectionName") final String collectionName) {
        checkDatabaseNameValidity(databaseName);
        checkCollectionNameValidity(collectionName);
        this.databaseName = databaseName;
        this.collectionName = collectionName;
        this.fullName = databaseName + '.' + collectionName;
    }

    /**
     * Gets the database name.
     *
     * @return the database name
     */

    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * Gets the collection name.
     *
     * @return the collection name
     */

    public String getCollectionName() {
        return collectionName;
    }

    /**
     * Gets the full name, which is the database name and the collection name, separated by a period.
     *
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MongoNamespace that = (MongoNamespace) o;

        if (!collectionName.equals(that.collectionName)) {
            return false;
        }
        if (!databaseName.equals(that.databaseName)) {
            return false;
        }

        return true;
    }

    /**
     * Returns the standard MongoDB representation of a namespace, which is {@code &lt;database&gt;.&lt;collection&gt;}.
     *
     * @return string representation of the namespace.
     */
    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public int hashCode() {
        int result = databaseName.hashCode();
        result = 31 * result + (collectionName.hashCode());
        return result;
    }

    private static String getCollectionNameFullName(final String namespace) {
        int firstDot = namespace.indexOf('.');
        if (firstDot == -1) {
            return namespace;
        }
        return namespace.substring(firstDot + 1);
    }

    private static String getDatatabaseNameFromFullName(final String namespace) {
        int firstDot = namespace.indexOf('.');
        if (firstDot == -1) {
            return "";
        }
        return namespace.substring(0, firstDot);
    }



    @Override
    public int compareTo( MongoNamespace o ) {
        if (this == o) {
            return 0;
        }
        if (o == null ) {
            return 1;
        }


        if(this.databaseName.compareTo(o.databaseName)==0){
            return this.collectionName.compareTo(o.collectionName);
        }else {
            return this.databaseName.compareTo(o.databaseName);
        }



    }
}
