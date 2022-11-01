package com.whaleal.mars.config.transaction;

import com.mongodb.ClientSessionOptions;
import com.mongodb.MongoException;
import com.mongodb.TransactionOptions;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.whaleal.mars.codecs.MongoMappingContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.*;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * @author lyz
 * @desc
 * @create: 2022-10-31 14:24
 **/
@Component
public class MongoTransactionManager extends AbstractPlatformTransactionManager implements ResourceTransactionManager, InitializingBean {
    @Nullable
    private MongoMappingContext dbFactory;
    @Nullable
    private TransactionOptions options;

    private MongoClient mongoClient;

    public MongoTransactionManager() {
    }

    public MongoTransactionManager(MongoMappingContext dbFactory) {
        this(dbFactory, (TransactionOptions)null);
    }

    public MongoTransactionManager(MongoMappingContext dbFactory, @Nullable TransactionOptions options,MongoClient client) {
        Assert.notNull(dbFactory, "DbFactory must not be null!");
        this.dbFactory = dbFactory;
        this.options = options;
        this.mongoClient = client;
    }

    public MongoTransactionManager(MongoMappingContext dbFactory, @Nullable TransactionOptions options) {
        Assert.notNull(dbFactory, "DbFactory must not be null!");
        this.dbFactory = dbFactory;
        this.options = options;
    }

    protected Object doGetTransaction() throws TransactionException {
        MongoResourceHolder resourceHolder = (MongoResourceHolder) TransactionSynchronizationManager.getResource(this.getRequiredDbFactory());
        return new MongoTransactionObject(resourceHolder);
    }

    protected boolean isExistingTransaction(Object transaction) throws TransactionException {
        return extractMongoTransaction(transaction).hasResourceHolder();
    }

    protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
        MongoTransactionObject mongoTransactionObject = extractMongoTransaction(transaction);
        MongoResourceHolder resourceHolder = this.newResourceHolder(definition, ClientSessionOptions.builder().causallyConsistent(true).build());
        mongoTransactionObject.setResourceHolder(resourceHolder);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(String.format("About to start transaction for session %s.", debugString(resourceHolder.getSession())));
        }

        try {
            mongoTransactionObject.startTransaction(this.options);
        } catch (MongoException var6) {
            throw new TransactionSystemException(String.format("Could not start Mongo transaction for session %s.", debugString(mongoTransactionObject.getSession())), var6);
        }

        if (this.logger.isDebugEnabled()) {
            this.logger.debug(String.format("Started transaction for session %s.", debugString(resourceHolder.getSession())));
        }

        resourceHolder.setSynchronizedWithTransaction(true);
        TransactionSynchronizationManager.bindResource(this.getRequiredDbFactory(), resourceHolder);
    }

    protected Object doSuspend(Object transaction) throws TransactionException {
        MongoTransactionObject mongoTransactionObject = extractMongoTransaction(transaction);
        mongoTransactionObject.setResourceHolder((MongoResourceHolder)null);
        return TransactionSynchronizationManager.unbindResource(this.getRequiredDbFactory());
    }

    protected void doResume(@Nullable Object transaction, Object suspendedResources) {
        TransactionSynchronizationManager.bindResource(this.getRequiredDbFactory(), suspendedResources);
    }

    protected final void doCommit(DefaultTransactionStatus status) throws TransactionException {
        MongoTransactionObject mongoTransactionObject = extractMongoTransaction(status);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(String.format("About to commit transaction for session %s.", debugString(mongoTransactionObject.getSession())));
        }

        try {
            this.doCommit(mongoTransactionObject);
        } catch (Exception var4) {
            throw new TransactionSystemException(String.format("Could not commit Mongo transaction for session %s.", debugString(mongoTransactionObject.getSession())), var4);
        }
    }

    protected void doCommit(MongoTransactionObject transactionObject) throws Exception {
        transactionObject.commitTransaction();
    }

    protected void doRollback(DefaultTransactionStatus status) throws TransactionException {
        MongoTransactionObject mongoTransactionObject = extractMongoTransaction(status);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(String.format("About to abort transaction for session %s.", debugString(mongoTransactionObject.getSession())));
        }

        try {
            mongoTransactionObject.abortTransaction();
        } catch (MongoException var4) {
            throw new TransactionSystemException(String.format("Could not abort Mongo transaction for session %s.", debugString(mongoTransactionObject.getSession())), var4);
        }
    }

    protected void doSetRollbackOnly(DefaultTransactionStatus status) throws TransactionException {
        MongoTransactionObject transactionObject = extractMongoTransaction(status);
        transactionObject.getRequiredResourceHolder().setRollbackOnly();
    }

    protected void doCleanupAfterCompletion(Object transaction) {
        Assert.isInstanceOf(MongoTransactionObject.class, transaction, () -> {
            return String.format("Expected to find a %s but it turned out to be %s.", MongoTransactionObject.class, transaction.getClass());
        });
        MongoTransactionObject mongoTransactionObject = (MongoTransactionObject)transaction;
        TransactionSynchronizationManager.unbindResource(this.getRequiredDbFactory());
        mongoTransactionObject.getRequiredResourceHolder().clear();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(String.format("About to release Session %s after transaction.", debugString(mongoTransactionObject.getSession())));
        }

        mongoTransactionObject.closeSession();
    }

    public void setDbFactory(MongoMappingContext dbFactory) {
        Assert.notNull(dbFactory, "DbFactory must not be null!");
        this.dbFactory = dbFactory;
    }

    public void setOptions(@Nullable TransactionOptions options) {
        this.options = options;
    }

    @Nullable
    public MongoMappingContext getDbFactory() {
        return this.dbFactory;
    }

    public MongoMappingContext getResourceFactory() {
        return this.getRequiredDbFactory();
    }

    public void afterPropertiesSet() {
        this.getRequiredDbFactory();
    }

    private MongoResourceHolder newResourceHolder(TransactionDefinition definition, ClientSessionOptions options) {
        MongoMappingContext dbFactory = this.getResourceFactory();
        MongoResourceHolder resourceHolder = new MongoResourceHolder(mongoClient.startSession(options), dbFactory);
        resourceHolder.setTimeoutIfNotDefaulted(this.determineTimeout(definition));
        return resourceHolder;
    }

    private MongoMappingContext getRequiredDbFactory() {
        Assert.state(this.dbFactory != null, "MongoTransactionManager operates upon a MongoDbFactory. Did you forget to provide one? It's required.");
        return this.dbFactory;
    }

    private static MongoTransactionObject extractMongoTransaction(Object transaction) {
        Assert.isInstanceOf(MongoTransactionObject.class, transaction, () -> {
            return String.format("Expected to find a %s but it turned out to be %s.", MongoTransactionObject.class, transaction.getClass());
        });
        return (MongoTransactionObject)transaction;
    }

    private static MongoTransactionObject extractMongoTransaction(DefaultTransactionStatus status) {
        Assert.isInstanceOf(MongoTransactionObject.class, status.getTransaction(), () -> {
            return String.format("Expected to find a %s but it turned out to be %s.", MongoTransactionObject.class, status.getTransaction().getClass());
        });
        return (MongoTransactionObject)status.getTransaction();
    }

    private static String debugString(@Nullable ClientSession session) {
        if (session == null) {
            return "null";
        } else {
            String debugString = String.format("[%s@%s ", ClassUtils.getShortName(session.getClass()), Integer.toHexString(session.hashCode()));

            try {
                if (session.getServerSession() != null) {
                    debugString = debugString + String.format("id = %s, ", session.getServerSession().getIdentifier());
                    debugString = debugString + String.format("causallyConsistent = %s, ", session.isCausallyConsistent());
                    debugString = debugString + String.format("txActive = %s, ", session.hasActiveTransaction());
                    debugString = debugString + String.format("txNumber = %d, ", session.getServerSession().getTransactionNumber());
                    debugString = debugString + String.format("closed = %d, ", session.getServerSession().isClosed());
                    debugString = debugString + String.format("clusterTime = %s", session.getClusterTime());
                } else {
                    debugString = debugString + "id = n/a";
                    debugString = debugString + String.format("causallyConsistent = %s, ", session.isCausallyConsistent());
                    debugString = debugString + String.format("txActive = %s, ", session.hasActiveTransaction());
                    debugString = debugString + String.format("clusterTime = %s", session.getClusterTime());
                }
            } catch (RuntimeException var3) {
                debugString = debugString + String.format("error = %s", var3.getMessage());
            }

            debugString = debugString + "]";
            return debugString;
        }
    }

    protected static class MongoTransactionObject implements SmartTransactionObject {
        @Nullable
        private MongoResourceHolder resourceHolder;

        MongoTransactionObject(@Nullable MongoResourceHolder resourceHolder) {
            this.resourceHolder = resourceHolder;
        }

        void setResourceHolder(@Nullable MongoResourceHolder resourceHolder) {
            this.resourceHolder = resourceHolder;
        }

        final boolean hasResourceHolder() {
            return this.resourceHolder != null;
        }

        void startTransaction(@Nullable TransactionOptions options) {
            ClientSession session = this.getRequiredSession();
            if (options != null) {
                session.startTransaction(options);
            } else {
                session.startTransaction();
            }

        }

        public void commitTransaction() {
            this.getRequiredSession().commitTransaction();
        }

        public void abortTransaction() {
            this.getRequiredSession().abortTransaction();
        }

        void closeSession() {
            ClientSession session = this.getRequiredSession();
            if (session.getServerSession() != null && !session.getServerSession().isClosed()) {
                session.close();
            }

        }

        @Nullable
        public ClientSession getSession() {
            return this.resourceHolder != null ? this.resourceHolder.getSession() : null;
        }

        private MongoResourceHolder getRequiredResourceHolder() {
            Assert.state(this.resourceHolder != null, "MongoResourceHolder is required but not present. o_O");
            return this.resourceHolder;
        }

        private ClientSession getRequiredSession() {
            ClientSession session = this.getSession();
            Assert.state(session != null, "A Session is required but it turned out to be null.");
            return session;
        }

        public boolean isRollbackOnly() {
            return this.resourceHolder != null && this.resourceHolder.isRollbackOnly();
        }

        public void flush() {
            TransactionSynchronizationUtils.triggerFlush();
        }
    }
}
