package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 15:32
 * FileName: UsersInfoTest
 * Description:
 */
public class UsersInfoTest {
    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.executeCommand("{\n" +
                "       createUser: \"testUser\",\n" +
                "       pwd: \"testPwd\",\n" +
                "       customData: { employeeId: 12345 },\n" +
                "       roles: [\n" +
                "                { role: \"clusterAdmin\", db: \"admin\" },\n" +
                "                { role: \"readAnyDatabase\", db: \"admin\" },\n" +
                "                \"readWrite\"\n" +
                "              ],\n" +
                "       writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "}");
    }
    /**
     * {
     *   usersInfo: <various>,
     *   showCredentials: <Boolean>,
     *   showPrivileges: <Boolean>,
     *   showAuthenticationRestrictions: <Boolean>,
     *   filter: <document>,
     *   comment: <any>
     * }
     */
    @Test
    public void testForSpecificUsersInfo(){
        Document document = mars.executeCommand(" {\n" +
                "     usersInfo:  { user: \"testUser\", db: \"mars\" },\n" +
                "     showPrivileges: true\n" +
                "   }");
        Document result = Document.parse("{\n" +
                "\t\"users\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"_id\" : \"mars.testUser\",\n" +
                "\t\t\t\"userId\" : UUID(\"0d8be8ad-5660-45ec-90ed-2114a1d499ba\"),\n" +
                "\t\t\t\"user\" : \"testUser\",\n" +
                "\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\"mechanisms\" : [\n" +
                "\t\t\t\t\"SCRAM-SHA-1\",\n" +
                "\t\t\t\t\"SCRAM-SHA-256\"\n" +
                "\t\t\t],\n" +
                "\t\t\t\"customData\" : {\n" +
                "\t\t\t\t\"employeeId\" : 12345\n" +
                "\t\t\t},\n" +
                "\t\t\t\"roles\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"clusterAdmin\",\n" +
                "\t\t\t\t\t\"db\" : \"admin\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"readAnyDatabase\",\n" +
                "\t\t\t\t\t\"db\" : \"admin\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"readWrite\",\n" +
                "\t\t\t\t\t\"db\" : \"mars\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"inheritedRoles\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"clusterAdmin\",\n" +
                "\t\t\t\t\t\"db\" : \"admin\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"readAnyDatabase\",\n" +
                "\t\t\t\t\t\"db\" : \"admin\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"readWrite\",\n" +
                "\t\t\t\t\t\"db\" : \"mars\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"inheritedPrivileges\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"cluster\" : true\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"addShard\",\n" +
                "\t\t\t\t\t\t\"appendOplogNote\",\n" +
                "\t\t\t\t\t\t\"applicationMessage\",\n" +
                "\t\t\t\t\t\t\"auditConfigure\",\n" +
                "\t\t\t\t\t\t\"checkFreeMonitoringStatus\",\n" +
                "\t\t\t\t\t\t\"cleanupOrphaned\",\n" +
                "\t\t\t\t\t\t\"connPoolStats\",\n" +
                "\t\t\t\t\t\t\"connPoolSync\",\n" +
                "\t\t\t\t\t\t\"dropConnections\",\n" +
                "\t\t\t\t\t\t\"flushRouterConfig\",\n" +
                "\t\t\t\t\t\t\"fsync\",\n" +
                "\t\t\t\t\t\t\"getDefaultRWConcern\",\n" +
                "\t\t\t\t\t\t\"getCmdLineOpts\",\n" +
                "\t\t\t\t\t\t\"getLog\",\n" +
                "\t\t\t\t\t\t\"getParameter\",\n" +
                "\t\t\t\t\t\t\"getShardMap\",\n" +
                "\t\t\t\t\t\t\"hostInfo\",\n" +
                "\t\t\t\t\t\t\"inprog\",\n" +
                "\t\t\t\t\t\t\"invalidateUserCache\",\n" +
                "\t\t\t\t\t\t\"killAnyCursor\",\n" +
                "\t\t\t\t\t\t\"killAnySession\",\n" +
                "\t\t\t\t\t\t\"killop\",\n" +
                "\t\t\t\t\t\t\"listDatabases\",\n" +
                "\t\t\t\t\t\t\"listSessions\",\n" +
                "\t\t\t\t\t\t\"listShards\",\n" +
                "\t\t\t\t\t\t\"logRotate\",\n" +
                "\t\t\t\t\t\t\"netstat\",\n" +
                "\t\t\t\t\t\t\"oidReset\",\n" +
                "\t\t\t\t\t\t\"operationMetrics\",\n" +
                "\t\t\t\t\t\t\"removeShard\",\n" +
                "\t\t\t\t\t\t\"replSetConfigure\",\n" +
                "\t\t\t\t\t\t\"replSetGetConfig\",\n" +
                "\t\t\t\t\t\t\"replSetGetStatus\",\n" +
                "\t\t\t\t\t\t\"replSetResizeOplog\",\n" +
                "\t\t\t\t\t\t\"replSetStateChange\",\n" +
                "\t\t\t\t\t\t\"resync\",\n" +
                "\t\t\t\t\t\t\"rotateCertificates\",\n" +
                "\t\t\t\t\t\t\"runTenantMigration\",\n" +
                "\t\t\t\t\t\t\"serverStatus\",\n" +
                "\t\t\t\t\t\t\"setDefaultRWConcern\",\n" +
                "\t\t\t\t\t\t\"setFeatureCompatibilityVersion\",\n" +
                "\t\t\t\t\t\t\"setFreeMonitoring\",\n" +
                "\t\t\t\t\t\t\"setParameter\",\n" +
                "\t\t\t\t\t\t\"shardingState\",\n" +
                "\t\t\t\t\t\t\"shutdown\",\n" +
                "\t\t\t\t\t\t\"top\",\n" +
                "\t\t\t\t\t\t\"touch\",\n" +
                "\t\t\t\t\t\t\"trafficRecord\",\n" +
                "\t\t\t\t\t\t\"unlock\",\n" +
                "\t\t\t\t\t\t\"useUUID\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"clearJumboFlag\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"dropDatabase\",\n" +
                "\t\t\t\t\t\t\"enableSharding\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"getDatabaseVersion\",\n" +
                "\t\t\t\t\t\t\"getShardVersion\",\n" +
                "\t\t\t\t\t\t\"indexStats\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"moveChunk\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\t\"refineCollectionShardKey\",\n" +
                "\t\t\t\t\t\t\"reshardCollection\",\n" +
                "\t\t\t\t\t\t\"splitChunk\",\n" +
                "\t\t\t\t\t\t\"splitVector\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"config\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"clearJumboFlag\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"enableSharding\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"getDatabaseVersion\",\n" +
                "\t\t\t\t\t\t\"getShardVersion\",\n" +
                "\t\t\t\t\t\t\"indexStats\",\n" +
                "\t\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"moveChunk\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\t\"refineCollectionShardKey\",\n" +
                "\t\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\t\"reshardCollection\",\n" +
                "\t\t\t\t\t\t\"splitChunk\",\n" +
                "\t\t\t\t\t\t\"splitVector\",\n" +
                "\t\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"local\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"clearJumboFlag\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"enableSharding\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"getDatabaseVersion\",\n" +
                "\t\t\t\t\t\t\"getShardVersion\",\n" +
                "\t\t\t\t\t\t\"indexStats\",\n" +
                "\t\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"moveChunk\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\t\"refineCollectionShardKey\",\n" +
                "\t\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\t\"reshardCollection\",\n" +
                "\t\t\t\t\t\t\"splitChunk\",\n" +
                "\t\t\t\t\t\t\"splitVector\",\n" +
                "\t\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"config\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.js\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"local\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.js\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"local\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.replset\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"local\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"replset.election\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"local\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"replset.minvalid\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.profile\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"find\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"system_buckets\" : \"\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"clearJumboFlag\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"enableSharding\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"moveChunk\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\t\"refineCollectionShardKey\",\n" +
                "\t\t\t\t\t\t\"reshardCollection\",\n" +
                "\t\t\t\t\t\t\"splitChunk\",\n" +
                "\t\t\t\t\t\t\"splitVector\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"anyResource\" : true\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"dbCheck\",\n" +
                "\t\t\t\t\t\t\"exportCollection\",\n" +
                "\t\t\t\t\t\t\"importCollection\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"local\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.healthlog\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.js\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"convertToCapped\",\n" +
                "\t\t\t\t\t\t\"createCollection\",\n" +
                "\t\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"dropCollection\",\n" +
                "\t\t\t\t\t\t\"dropIndex\",\n" +
                "\t\t\t\t\t\t\"emptycapped\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\t\"renameCollectionSameDB\",\n" +
                "\t\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.js\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"convertToCapped\",\n" +
                "\t\t\t\t\t\t\"createCollection\",\n" +
                "\t\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"dropCollection\",\n" +
                "\t\t\t\t\t\t\"dropIndex\",\n" +
                "\t\t\t\t\t\t\"emptycapped\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\t\"renameCollectionSameDB\",\n" +
                "\t\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"inheritedAuthenticationRestrictions\" : [ ]\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }

    @Test
    public void testForMultipleUsersInfo(){
        Document document = mars.executeCommand(" {\n" +
                "        usersInfo: [ { user: \"testUser\", db: \"mars\" }, { user: \"root\", db: \"admin\" } ],\n" +
                "        showPrivileges: true\n" +
                "    }");
        Document result = Document.parse("{\n" +
                "\t\"users\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"_id\" : \"mars.testUser\",\n" +
                "\t\t\t\"userId\" : UUID(\"0d8be8ad-5660-45ec-90ed-2114a1d499ba\"),\n" +
                "\t\t\t\"user\" : \"testUser\",\n" +
                "\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\"mechanisms\" : [\n" +
                "\t\t\t\t\"SCRAM-SHA-1\",\n" +
                "\t\t\t\t\"SCRAM-SHA-256\"\n" +
                "\t\t\t],\n" +
                "\t\t\t\"customData\" : {\n" +
                "\t\t\t\t\"employeeId\" : 12345\n" +
                "\t\t\t},\n" +
                "\t\t\t\"roles\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"clusterAdmin\",\n" +
                "\t\t\t\t\t\"db\" : \"admin\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"readAnyDatabase\",\n" +
                "\t\t\t\t\t\"db\" : \"admin\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"readWrite\",\n" +
                "\t\t\t\t\t\"db\" : \"mars\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"inheritedRoles\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"clusterAdmin\",\n" +
                "\t\t\t\t\t\"db\" : \"admin\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"readAnyDatabase\",\n" +
                "\t\t\t\t\t\"db\" : \"admin\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"readWrite\",\n" +
                "\t\t\t\t\t\"db\" : \"mars\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"inheritedPrivileges\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"cluster\" : true\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"addShard\",\n" +
                "\t\t\t\t\t\t\"appendOplogNote\",\n" +
                "\t\t\t\t\t\t\"applicationMessage\",\n" +
                "\t\t\t\t\t\t\"auditConfigure\",\n" +
                "\t\t\t\t\t\t\"checkFreeMonitoringStatus\",\n" +
                "\t\t\t\t\t\t\"cleanupOrphaned\",\n" +
                "\t\t\t\t\t\t\"connPoolStats\",\n" +
                "\t\t\t\t\t\t\"connPoolSync\",\n" +
                "\t\t\t\t\t\t\"dropConnections\",\n" +
                "\t\t\t\t\t\t\"flushRouterConfig\",\n" +
                "\t\t\t\t\t\t\"fsync\",\n" +
                "\t\t\t\t\t\t\"getDefaultRWConcern\",\n" +
                "\t\t\t\t\t\t\"getCmdLineOpts\",\n" +
                "\t\t\t\t\t\t\"getLog\",\n" +
                "\t\t\t\t\t\t\"getParameter\",\n" +
                "\t\t\t\t\t\t\"getShardMap\",\n" +
                "\t\t\t\t\t\t\"hostInfo\",\n" +
                "\t\t\t\t\t\t\"inprog\",\n" +
                "\t\t\t\t\t\t\"invalidateUserCache\",\n" +
                "\t\t\t\t\t\t\"killAnyCursor\",\n" +
                "\t\t\t\t\t\t\"killAnySession\",\n" +
                "\t\t\t\t\t\t\"killop\",\n" +
                "\t\t\t\t\t\t\"listDatabases\",\n" +
                "\t\t\t\t\t\t\"listSessions\",\n" +
                "\t\t\t\t\t\t\"listShards\",\n" +
                "\t\t\t\t\t\t\"logRotate\",\n" +
                "\t\t\t\t\t\t\"netstat\",\n" +
                "\t\t\t\t\t\t\"oidReset\",\n" +
                "\t\t\t\t\t\t\"operationMetrics\",\n" +
                "\t\t\t\t\t\t\"removeShard\",\n" +
                "\t\t\t\t\t\t\"replSetConfigure\",\n" +
                "\t\t\t\t\t\t\"replSetGetConfig\",\n" +
                "\t\t\t\t\t\t\"replSetGetStatus\",\n" +
                "\t\t\t\t\t\t\"replSetResizeOplog\",\n" +
                "\t\t\t\t\t\t\"replSetStateChange\",\n" +
                "\t\t\t\t\t\t\"resync\",\n" +
                "\t\t\t\t\t\t\"rotateCertificates\",\n" +
                "\t\t\t\t\t\t\"runTenantMigration\",\n" +
                "\t\t\t\t\t\t\"serverStatus\",\n" +
                "\t\t\t\t\t\t\"setDefaultRWConcern\",\n" +
                "\t\t\t\t\t\t\"setFeatureCompatibilityVersion\",\n" +
                "\t\t\t\t\t\t\"setFreeMonitoring\",\n" +
                "\t\t\t\t\t\t\"setParameter\",\n" +
                "\t\t\t\t\t\t\"shardingState\",\n" +
                "\t\t\t\t\t\t\"shutdown\",\n" +
                "\t\t\t\t\t\t\"top\",\n" +
                "\t\t\t\t\t\t\"touch\",\n" +
                "\t\t\t\t\t\t\"trafficRecord\",\n" +
                "\t\t\t\t\t\t\"unlock\",\n" +
                "\t\t\t\t\t\t\"useUUID\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"clearJumboFlag\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"dropDatabase\",\n" +
                "\t\t\t\t\t\t\"enableSharding\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"getDatabaseVersion\",\n" +
                "\t\t\t\t\t\t\"getShardVersion\",\n" +
                "\t\t\t\t\t\t\"indexStats\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"moveChunk\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\t\"refineCollectionShardKey\",\n" +
                "\t\t\t\t\t\t\"reshardCollection\",\n" +
                "\t\t\t\t\t\t\"splitChunk\",\n" +
                "\t\t\t\t\t\t\"splitVector\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"config\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"clearJumboFlag\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"enableSharding\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"getDatabaseVersion\",\n" +
                "\t\t\t\t\t\t\"getShardVersion\",\n" +
                "\t\t\t\t\t\t\"indexStats\",\n" +
                "\t\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"moveChunk\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\t\"refineCollectionShardKey\",\n" +
                "\t\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\t\"reshardCollection\",\n" +
                "\t\t\t\t\t\t\"splitChunk\",\n" +
                "\t\t\t\t\t\t\"splitVector\",\n" +
                "\t\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"local\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"clearJumboFlag\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"enableSharding\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"getDatabaseVersion\",\n" +
                "\t\t\t\t\t\t\"getShardVersion\",\n" +
                "\t\t\t\t\t\t\"indexStats\",\n" +
                "\t\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"moveChunk\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\t\"refineCollectionShardKey\",\n" +
                "\t\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\t\"reshardCollection\",\n" +
                "\t\t\t\t\t\t\"splitChunk\",\n" +
                "\t\t\t\t\t\t\"splitVector\",\n" +
                "\t\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"config\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.js\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"local\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.js\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"local\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.replset\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"local\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"replset.election\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"local\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"replset.minvalid\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.profile\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"find\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"system_buckets\" : \"\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"clearJumboFlag\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"enableSharding\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"moveChunk\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\t\"refineCollectionShardKey\",\n" +
                "\t\t\t\t\t\t\"reshardCollection\",\n" +
                "\t\t\t\t\t\t\"splitChunk\",\n" +
                "\t\t\t\t\t\t\"splitVector\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"anyResource\" : true\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"dbCheck\",\n" +
                "\t\t\t\t\t\t\"exportCollection\",\n" +
                "\t\t\t\t\t\t\"importCollection\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"local\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.healthlog\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.js\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"convertToCapped\",\n" +
                "\t\t\t\t\t\t\"createCollection\",\n" +
                "\t\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"dropCollection\",\n" +
                "\t\t\t\t\t\t\"dropIndex\",\n" +
                "\t\t\t\t\t\t\"emptycapped\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\t\"renameCollectionSameDB\",\n" +
                "\t\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.js\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"convertToCapped\",\n" +
                "\t\t\t\t\t\t\"createCollection\",\n" +
                "\t\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"dropCollection\",\n" +
                "\t\t\t\t\t\t\"dropIndex\",\n" +
                "\t\t\t\t\t\t\"emptycapped\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\t\"renameCollectionSameDB\",\n" +
                "\t\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"inheritedAuthenticationRestrictions\" : [ ]\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }

    @Test
    public void testForAllUsersInfo(){
        Document document = mars.executeCommand(" { usersInfo: 1 }");
        Document result = Document.parse("{\n" +
                "\t\"users\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"_id\" : \"mars.testUser\",\n" +
                "\t\t\t\"userId\" : UUID(\"0d8be8ad-5660-45ec-90ed-2114a1d499ba\"),\n" +
                "\t\t\t\"user\" : \"testUser\",\n" +
                "\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\"customData\" : {\n" +
                "\t\t\t\t\"employeeId\" : 12345\n" +
                "\t\t\t},\n" +
                "\t\t\t\"roles\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"clusterAdmin\",\n" +
                "\t\t\t\t\t\"db\" : \"admin\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"readAnyDatabase\",\n" +
                "\t\t\t\t\t\"db\" : \"admin\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"readWrite\",\n" +
                "\t\t\t\t\t\"db\" : \"mars\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"mechanisms\" : [\n" +
                "\t\t\t\t\"SCRAM-SHA-1\",\n" +
                "\t\t\t\t\"SCRAM-SHA-256\"\n" +
                "\t\t\t]\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }

    @Test
    public void testForSpecificFiltersUsersInfo(){
        Document document = mars.executeCommand(" { usersInfo: 1, filter: { roles: { role: \"root\", db: \"admin\" } } }");
        Document result = Document.parse("{ \"users\" : [ ], \"ok\" : 1 }");
        Assert.assertEquals(result,document);
    }

    @Test
    public void testForUsersInfoWithMechanisms(){
        Document document = mars.executeCommand("{ usersInfo: { forAllDBs: true}, filter: { mechanisms: \"SCRAM-SHA-1\" } } ");
        Document result = Document.parse("{\n" +
                "\t\"users\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"_id\" : \"mars.testUser\",\n" +
                "\t\t\t\"userId\" : UUID(\"0d8be8ad-5660-45ec-90ed-2114a1d499ba\"),\n" +
                "\t\t\t\"user\" : \"testUser\",\n" +
                "\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\"customData\" : {\n" +
                "\t\t\t\t\"employeeId\" : 12345\n" +
                "\t\t\t},\n" +
                "\t\t\t\"roles\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"clusterAdmin\",\n" +
                "\t\t\t\t\t\"db\" : \"admin\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"readAnyDatabase\",\n" +
                "\t\t\t\t\t\"db\" : \"admin\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"readWrite\",\n" +
                "\t\t\t\t\t\"db\" : \"mars\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"mechanisms\" : [\n" +
                "\t\t\t\t\"SCRAM-SHA-1\",\n" +
                "\t\t\t\t\"SCRAM-SHA-256\"\n" +
                "\t\t\t]\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"ok\" : 1.0\n" +
                "}");
        Assert.assertEquals(result,document);
    }

    @After
    public void dropUser(){
        mars.executeCommand("{\n" +
                "   dropUser: \"testUser\",\n" +
                "   writeConcern: { w: \"majority\", wtimeout: 5000 }\n" +
                "} ");
    }
}
