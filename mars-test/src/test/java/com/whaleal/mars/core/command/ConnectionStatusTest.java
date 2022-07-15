package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 16:12
 * FileName: ConnectionStatusTest
 * Description:
 */
public class ConnectionStatusTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { connectionStatus: 1, showPrivileges: <boolean> }
     */
    //todo  比较结果有问题
    @Test
    public void testForConnectionStatus(){
        Document document = new Document().append("connectionStatus", 1)
                .append("showPrivileges", true);
        Document document1 = mars.executeCommand(document);
        String s = "{\n" +
                "\t\"authInfo\" : {\n" +
                "\t\t\"authenticatedUsers\" : [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"user\" : \"root\",\n" +
                "\t\t\t\t\"db\" : \"admin\"\n" +
                "\t\t\t}\n" +
                "\t\t],\n" +
                "\t\t\"authenticatedUserRoles\" : [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"role\" : \"root\",\n" +
                "\t\t\t\t\"db\" : \"admin\"\n" +
                "\t\t\t}\n" +
                "\t\t],\n" +
                "\t\t\"authenticatedUserPrivileges\" : [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"config\",\n" +
                "\t\t\t\t\t\"collection\" : \"settings\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"admin\",\n" +
                "\t\t\t\t\t\"collection\" : \"tempusers\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"find\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"system_buckets\" : \"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"bypassDocumentValidation\",\n" +
                "\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\"clearJumboFlag\",\n" +
                "\t\t\t\t\t\"collMod\",\n" +
                "\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\"compact\",\n" +
                "\t\t\t\t\t\"convertToCapped\",\n" +
                "\t\t\t\t\t\"createCollection\",\n" +
                "\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\"dropCollection\",\n" +
                "\t\t\t\t\t\"dropDatabase\",\n" +
                "\t\t\t\t\t\"dropIndex\",\n" +
                "\t\t\t\t\t\"emptycapped\",\n" +
                "\t\t\t\t\t\"enableProfiler\",\n" +
                "\t\t\t\t\t\"enableSharding\",\n" +
                "\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\"moveChunk\",\n" +
                "\t\t\t\t\t\"planCacheIndexFilter\",\n" +
                "\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\"planCacheWrite\",\n" +
                "\t\t\t\t\t\"refineCollectionShardKey\",\n" +
                "\t\t\t\t\t\"reIndex\",\n" +
                "\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\"renameCollectionSameDB\",\n" +
                "\t\t\t\t\t\"reshardCollection\",\n" +
                "\t\t\t\t\t\"splitChunk\",\n" +
                "\t\t\t\t\t\"splitVector\",\n" +
                "\t\t\t\t\t\"storageDetails\",\n" +
                "\t\t\t\t\t\"update\",\n" +
                "\t\t\t\t\t\"validate\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"\",\n" +
                "\t\t\t\t\t\"collection\" : \"system.js\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"bypassDocumentValidation\",\n" +
                "\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\"collMod\",\n" +
                "\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\"convertToCapped\",\n" +
                "\t\t\t\t\t\"createCollection\",\n" +
                "\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\"dropCollection\",\n" +
                "\t\t\t\t\t\"dropIndex\",\n" +
                "\t\t\t\t\t\"emptycapped\",\n" +
                "\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\"renameCollectionSameDB\",\n" +
                "\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"local\",\n" +
                "\t\t\t\t\t\"collection\" : \"system.replset\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"bypassDocumentValidation\",\n" +
                "\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\"collMod\",\n" +
                "\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\"convertToCapped\",\n" +
                "\t\t\t\t\t\"createCollection\",\n" +
                "\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\"dropCollection\",\n" +
                "\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"local\",\n" +
                "\t\t\t\t\t\"collection\" : \"system.js\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"local\",\n" +
                "\t\t\t\t\t\"collection\" : \"replset.minvalid\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"bypassDocumentValidation\",\n" +
                "\t\t\t\t\t\"collMod\",\n" +
                "\t\t\t\t\t\"convertToCapped\",\n" +
                "\t\t\t\t\t\"createCollection\",\n" +
                "\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\"dropCollection\",\n" +
                "\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"config\",\n" +
                "\t\t\t\t\t\"collection\" : \"system.js\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"local\",\n" +
                "\t\t\t\t\t\"collection\" : \"system.healthlog\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"config\",\n" +
                "\t\t\t\t\t\"collection\" : \"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"bypassDocumentValidation\",\n" +
                "\t\t\t\t\t\"changeCustomData\",\n" +
                "\t\t\t\t\t\"changePassword\",\n" +
                "\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\"clearJumboFlag\",\n" +
                "\t\t\t\t\t\"collMod\",\n" +
                "\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\"convertToCapped\",\n" +
                "\t\t\t\t\t\"createCollection\",\n" +
                "\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\"createRole\",\n" +
                "\t\t\t\t\t\"createUser\",\n" +
                "\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\"dropCollection\",\n" +
                "\t\t\t\t\t\"dropRole\",\n" +
                "\t\t\t\t\t\"dropUser\",\n" +
                "\t\t\t\t\t\"enableSharding\",\n" +
                "\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\"getDatabaseVersion\",\n" +
                "\t\t\t\t\t\"getShardVersion\",\n" +
                "\t\t\t\t\t\"grantRole\",\n" +
                "\t\t\t\t\t\"indexStats\",\n" +
                "\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\"moveChunk\",\n" +
                "\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\"refineCollectionShardKey\",\n" +
                "\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\"reshardCollection\",\n" +
                "\t\t\t\t\t\"revokeRole\",\n" +
                "\t\t\t\t\t\"setAuthenticationRestriction\",\n" +
                "\t\t\t\t\t\"splitChunk\",\n" +
                "\t\t\t\t\t\"splitVector\",\n" +
                "\t\t\t\t\t\"update\",\n" +
                "\t\t\t\t\t\"viewRole\",\n" +
                "\t\t\t\t\t\"viewUser\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"admin\",\n" +
                "\t\t\t\t\t\"collection\" : \"system.users\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\"dropIndex\",\n" +
                "\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"admin\",\n" +
                "\t\t\t\t\t\"collection\" : \"temproles\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"find\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"cluster\" : true\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"addShard\",\n" +
                "\t\t\t\t\t\"appendOplogNote\",\n" +
                "\t\t\t\t\t\"applicationMessage\",\n" +
                "\t\t\t\t\t\"auditConfigure\",\n" +
                "\t\t\t\t\t\"authSchemaUpgrade\",\n" +
                "\t\t\t\t\t\"checkFreeMonitoringStatus\",\n" +
                "\t\t\t\t\t\"cleanupOrphaned\",\n" +
                "\t\t\t\t\t\"connPoolStats\",\n" +
                "\t\t\t\t\t\"connPoolSync\",\n" +
                "\t\t\t\t\t\"dropConnections\",\n" +
                "\t\t\t\t\t\"flushRouterConfig\",\n" +
                "\t\t\t\t\t\"forceUUID\",\n" +
                "\t\t\t\t\t\"fsync\",\n" +
                "\t\t\t\t\t\"getDefaultRWConcern\",\n" +
                "\t\t\t\t\t\"getCmdLineOpts\",\n" +
                "\t\t\t\t\t\"getLog\",\n" +
                "\t\t\t\t\t\"getParameter\",\n" +
                "\t\t\t\t\t\"getShardMap\",\n" +
                "\t\t\t\t\t\"hostInfo\",\n" +
                "\t\t\t\t\t\"inprog\",\n" +
                "\t\t\t\t\t\"invalidateUserCache\",\n" +
                "\t\t\t\t\t\"killAnyCursor\",\n" +
                "\t\t\t\t\t\"killAnySession\",\n" +
                "\t\t\t\t\t\"killop\",\n" +
                "\t\t\t\t\t\"listDatabases\",\n" +
                "\t\t\t\t\t\"listSessions\",\n" +
                "\t\t\t\t\t\"listShards\",\n" +
                "\t\t\t\t\t\"logRotate\",\n" +
                "\t\t\t\t\t\"netstat\",\n" +
                "\t\t\t\t\t\"oidReset\",\n" +
                "\t\t\t\t\t\"operationMetrics\",\n" +
                "\t\t\t\t\t\"removeShard\",\n" +
                "\t\t\t\t\t\"replSetConfigure\",\n" +
                "\t\t\t\t\t\"replSetGetConfig\",\n" +
                "\t\t\t\t\t\"replSetGetStatus\",\n" +
                "\t\t\t\t\t\"replSetResizeOplog\",\n" +
                "\t\t\t\t\t\"replSetStateChange\",\n" +
                "\t\t\t\t\t\"resync\",\n" +
                "\t\t\t\t\t\"rotateCertificates\",\n" +
                "\t\t\t\t\t\"runTenantMigration\",\n" +
                "\t\t\t\t\t\"serverStatus\",\n" +
                "\t\t\t\t\t\"setDefaultRWConcern\",\n" +
                "\t\t\t\t\t\"setFeatureCompatibilityVersion\",\n" +
                "\t\t\t\t\t\"setFreeMonitoring\",\n" +
                "\t\t\t\t\t\"setParameter\",\n" +
                "\t\t\t\t\t\"shardingState\",\n" +
                "\t\t\t\t\t\"shutdown\",\n" +
                "\t\t\t\t\t\"top\",\n" +
                "\t\t\t\t\t\"touch\",\n" +
                "\t\t\t\t\t\"trafficRecord\",\n" +
                "\t\t\t\t\t\"unlock\",\n" +
                "\t\t\t\t\t\"useUUID\",\n" +
                "\t\t\t\t\t\"viewUser\",\n" +
                "\t\t\t\t\t\"applyOps\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"local\",\n" +
                "\t\t\t\t\t\"collection\" : \"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"bypassDocumentValidation\",\n" +
                "\t\t\t\t\t\"changeCustomData\",\n" +
                "\t\t\t\t\t\"changePassword\",\n" +
                "\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\"clearJumboFlag\",\n" +
                "\t\t\t\t\t\"collMod\",\n" +
                "\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\"convertToCapped\",\n" +
                "\t\t\t\t\t\"createCollection\",\n" +
                "\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\"createRole\",\n" +
                "\t\t\t\t\t\"createUser\",\n" +
                "\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\"dropCollection\",\n" +
                "\t\t\t\t\t\"dropRole\",\n" +
                "\t\t\t\t\t\"dropUser\",\n" +
                "\t\t\t\t\t\"enableSharding\",\n" +
                "\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\"getDatabaseVersion\",\n" +
                "\t\t\t\t\t\"getShardVersion\",\n" +
                "\t\t\t\t\t\"grantRole\",\n" +
                "\t\t\t\t\t\"indexStats\",\n" +
                "\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\"moveChunk\",\n" +
                "\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\"refineCollectionShardKey\",\n" +
                "\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\"reshardCollection\",\n" +
                "\t\t\t\t\t\"revokeRole\",\n" +
                "\t\t\t\t\t\"setAuthenticationRestriction\",\n" +
                "\t\t\t\t\t\"splitChunk\",\n" +
                "\t\t\t\t\t\"splitVector\",\n" +
                "\t\t\t\t\t\"update\",\n" +
                "\t\t\t\t\t\"viewRole\",\n" +
                "\t\t\t\t\t\"viewUser\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"local\",\n" +
                "\t\t\t\t\t\"collection\" : \"replset.election\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"bypassDocumentValidation\",\n" +
                "\t\t\t\t\t\"collMod\",\n" +
                "\t\t\t\t\t\"convertToCapped\",\n" +
                "\t\t\t\t\t\"createCollection\",\n" +
                "\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\"dropCollection\",\n" +
                "\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"\",\n" +
                "\t\t\t\t\t\"collection\" : \"system.users\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"bypassDocumentValidation\",\n" +
                "\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\"collMod\",\n" +
                "\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\"convertToCapped\",\n" +
                "\t\t\t\t\t\"createCollection\",\n" +
                "\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\"dropCollection\",\n" +
                "\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"\",\n" +
                "\t\t\t\t\t\"collection\" : \"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"bypassDocumentValidation\",\n" +
                "\t\t\t\t\t\"changeCustomData\",\n" +
                "\t\t\t\t\t\"changePassword\",\n" +
                "\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\"clearJumboFlag\",\n" +
                "\t\t\t\t\t\"collMod\",\n" +
                "\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\"compact\",\n" +
                "\t\t\t\t\t\"convertToCapped\",\n" +
                "\t\t\t\t\t\"createCollection\",\n" +
                "\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\"createRole\",\n" +
                "\t\t\t\t\t\"createUser\",\n" +
                "\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\"dropCollection\",\n" +
                "\t\t\t\t\t\"dropDatabase\",\n" +
                "\t\t\t\t\t\"dropIndex\",\n" +
                "\t\t\t\t\t\"dropRole\",\n" +
                "\t\t\t\t\t\"dropUser\",\n" +
                "\t\t\t\t\t\"emptycapped\",\n" +
                "\t\t\t\t\t\"enableProfiler\",\n" +
                "\t\t\t\t\t\"enableSharding\",\n" +
                "\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\"getDatabaseVersion\",\n" +
                "\t\t\t\t\t\"getShardVersion\",\n" +
                "\t\t\t\t\t\"grantRole\",\n" +
                "\t\t\t\t\t\"indexStats\",\n" +
                "\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\"listCachedAndActiveUsers\",\n" +
                "\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\"moveChunk\",\n" +
                "\t\t\t\t\t\"planCacheIndexFilter\",\n" +
                "\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\"planCacheWrite\",\n" +
                "\t\t\t\t\t\"refineCollectionShardKey\",\n" +
                "\t\t\t\t\t\"reIndex\",\n" +
                "\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\"renameCollectionSameDB\",\n" +
                "\t\t\t\t\t\"reshardCollection\",\n" +
                "\t\t\t\t\t\"revokeRole\",\n" +
                "\t\t\t\t\t\"setAuthenticationRestriction\",\n" +
                "\t\t\t\t\t\"splitChunk\",\n" +
                "\t\t\t\t\t\"splitVector\",\n" +
                "\t\t\t\t\t\"storageDetails\",\n" +
                "\t\t\t\t\t\"update\",\n" +
                "\t\t\t\t\t\"validate\",\n" +
                "\t\t\t\t\t\"viewRole\",\n" +
                "\t\t\t\t\t\"viewUser\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"admin\",\n" +
                "\t\t\t\t\t\"collection\" : \"system.roles\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\"dropIndex\",\n" +
                "\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"admin\",\n" +
                "\t\t\t\t\t\"collection\" : \"system.backup_users\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"bypassDocumentValidation\",\n" +
                "\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\"collMod\",\n" +
                "\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\"convertToCapped\",\n" +
                "\t\t\t\t\t\"createCollection\",\n" +
                "\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\"dropCollection\",\n" +
                "\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"\",\n" +
                "\t\t\t\t\t\"collection\" : \"system.profile\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\"convertToCapped\",\n" +
                "\t\t\t\t\t\"createCollection\",\n" +
                "\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\"dropCollection\",\n" +
                "\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"db\" : \"admin\",\n" +
                "\t\t\t\t\t\"collection\" : \"system.version\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"bypassDocumentValidation\",\n" +
                "\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\"collMod\",\n" +
                "\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\"convertToCapped\",\n" +
                "\t\t\t\t\t\"createCollection\",\n" +
                "\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\"dropCollection\",\n" +
                "\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\"anyResource\" : true\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\"dbCheck\",\n" +
                "\t\t\t\t\t\"exportCollection\",\n" +
                "\t\t\t\t\t\"importCollection\",\n" +
                "\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\"validate\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t},\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n";
        Document result = Document.parse(s);
        Assert.assertEquals(document1,result);
    }
}
