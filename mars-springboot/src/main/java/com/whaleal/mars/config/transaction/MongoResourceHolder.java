
package com.whaleal.mars.config.transaction;

import com.whaleal.mars.core.Mars;
import org.springframework.lang.Nullable;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.ResourceHolderSupport;

import com.mongodb.client.ClientSession;

/**
 * MongoDB specific {@link ResourceHolderSupport resource holder}, wrapping a {@link ClientSession}.
 * {@link MongoTransactionManager} binds instances of this class to the thread.
 * <br />
 * <strong>Note:</strong> Intended for internal usage only.
 *
 *
 * @see MongoTransactionManager
 * @see com.whaleal.mars.core.Mars ;
 */
class MongoResourceHolder extends ResourceHolderSupport {

    private @Nullable ClientSession session;
    private Mars dbFactory;

    /**
     * Create a new {@link MongoResourceHolder} for a given {@link ClientSession session}.
     *
     * @param session the associated {@link ClientSession}. Can be {@literal null}.
     * @param dbFactory the associated {@link Mars}. must not be {@literal null}.
     */
    MongoResourceHolder(@Nullable ClientSession session, Mars dbFactory) {

        this.session = session;
        this.dbFactory = dbFactory;
    }

    /**
     * @return the associated {@link ClientSession}. Can be {@literal null}.
     */
    @Nullable
    ClientSession getSession() {
        return session;
    }

    /**
     * @return the required associated {@link ClientSession}.
     * @throws IllegalStateException if no {@link ClientSession} is associated with this {@link MongoResourceHolder}.
     * @since 2.1.3
     */
    ClientSession getRequiredSession() {

        ClientSession session = getSession();

        if (session == null) {
            throw new IllegalStateException("No session available");
        }

        return session;
    }

    /**
     * @return the associated {@link Mars}.
     */
    public Mars getDbFactory() {
        return dbFactory;
    }

    /**
     * Set the {@link ClientSession} to guard.
     *
     * @param session can be {@literal null}.
     */
    public void setSession(@Nullable ClientSession session) {
        this.session = session;
    }

    /**
     * Only set the timeout if it does not match the {@link TransactionDefinition#TIMEOUT_DEFAULT default timeout}.
     *
     * @param seconds
     */
    void setTimeoutIfNotDefaulted(int seconds) {

        if (seconds != TransactionDefinition.TIMEOUT_DEFAULT) {
            setTimeoutInSeconds(seconds);
        }
    }

    /**
     * @return {@literal true} if session is not {@literal null}.
     */
    boolean hasSession() {
        return session != null;
    }

    /**
     * @return {@literal true} if the session is active and has not been closed.
     */
    boolean hasActiveSession() {

        if (!hasSession()) {
            return false;
        }

        return hasServerSession() && !getRequiredSession().getServerSession().isClosed();
    }

    /**
     * @return {@literal true} if the session has an active transaction.
     * @since 2.1.3
     * @see #hasActiveSession()
     */
    boolean hasActiveTransaction() {

        if (!hasActiveSession()) {
            return false;
        }

        return getRequiredSession().hasActiveTransaction();
    }

    /**
     * @return {@literal true} if the {@link ClientSession} has a {@link com.mongodb.session.ServerSession} associated
     *         that is accessible via {@link ClientSession#getServerSession()}.
     */
    boolean hasServerSession() {

        try {
            return getRequiredSession().getServerSession() != null;
        } catch (IllegalStateException serverSessionClosed) {
            // ignore
        }

        return false;
    }

}
