package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Book;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 15:58
 * FileName: CollStatsTest
 * Description:
 */
public class CollStatsTest {
    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.createCollection(Book.class);
    }
    /**
     * {
     *    collStats: <string>,
     *    scale: <int>
     * }
     */
    @Test
    public void testForCollStats(){
        Document document = mars.executeCommand("{ collStats : \"book\" }");
        Document result = Document.parse("{\n" +
                "\t\"ns\" : \"mars.book\",\n" +
                "\t\"size\" : 0,\n" +
                "\t\"count\" : 0,\n" +
                "\t\"storageSize\" : 4,\n" +
                "\t\"freeStorageSize\" : 0,\n" +
                "\t\"capped\" : false,\n" +
                "\t\"wiredTiger\" : {\n" +
                "\t\t\"metadata\" : {\n" +
                "\t\t\t\"formatVersion\" : 1\n" +
                "\t\t},\n" +
                "\t\t\"creationString\" : \"access_pattern_hint=none,allocation_size=4KB,app_metadata=(formatVersion=1),assert=(commit_timestamp=none,durable_timestamp=none,read_timestamp=none,write_timestamp=off),block_allocation=best,block_compressor=snappy,cache_resident=false,checksum=on,colgroups=,collator=,columns=,dictionary=0,encryption=(keyid=,name=),exclusive=false,extractor=,format=btree,huffman_key=,huffman_value=,ignore_in_memory_cache_size=false,immutable=false,import=(enabled=false,file_metadata=,repair=false),internal_item_max=0,internal_key_max=0,internal_key_truncate=true,internal_page_max=4KB,key_format=q,key_gap=10,leaf_item_max=0,leaf_key_max=0,leaf_page_max=32KB,leaf_value_max=64MB,log=(enabled=true),lsm=(auto_throttle=true,bloom=true,bloom_bit_count=16,bloom_config=,bloom_hash_count=8,bloom_oldest=false,chunk_count_limit=0,chunk_max=5GB,chunk_size=10MB,merge_custom=(prefix=,start_generation=0,suffix=),merge_max=15,merge_min=0),memory_page_image_max=0,memory_page_max=10m,os_cache_dirty_max=0,os_cache_max=0,prefix_compression=false,prefix_compression_min=4,readonly=false,source=,split_deepen_min_child=0,split_deepen_per_child=0,split_pct=90,tiered_object=false,tiered_storage=(auth_token=,bucket=,bucket_prefix=,cache_directory=,local_retention=300,name=),type=file,value_format=u,verbose=[],write_timestamp_usage=none\",\n" +
                "\t\t\"type\" : \"file\",\n" +
                "\t\t\"uri\" : \"statistics:table:collection-70--2002033493638094686\",\n" +
                "\t\t\"LSM\" : {\n" +
                "\t\t\t\"bloom filter false positives\" : 0,\n" +
                "\t\t\t\"bloom filter hits\" : 0,\n" +
                "\t\t\t\"bloom filter misses\" : 0,\n" +
                "\t\t\t\"bloom filter pages evicted from cache\" : 0,\n" +
                "\t\t\t\"bloom filter pages read into cache\" : 0,\n" +
                "\t\t\t\"bloom filters in the LSM tree\" : 0,\n" +
                "\t\t\t\"chunks in the LSM tree\" : 0,\n" +
                "\t\t\t\"highest merge generation in the LSM tree\" : 0,\n" +
                "\t\t\t\"queries that could have benefited from a Bloom filter that did not exist\" : 0,\n" +
                "\t\t\t\"sleep for LSM checkpoint throttle\" : 0,\n" +
                "\t\t\t\"sleep for LSM merge throttle\" : 0,\n" +
                "\t\t\t\"total size of bloom filters\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"block-manager\" : {\n" +
                "\t\t\t\"allocations requiring file extension\" : 0,\n" +
                "\t\t\t\"blocks allocated\" : 0,\n" +
                "\t\t\t\"blocks freed\" : 0,\n" +
                "\t\t\t\"checkpoint size\" : 0,\n" +
                "\t\t\t\"file allocation unit size\" : 4096,\n" +
                "\t\t\t\"file bytes available for reuse\" : 0,\n" +
                "\t\t\t\"file magic number\" : 120897,\n" +
                "\t\t\t\"file major version number\" : 1,\n" +
                "\t\t\t\"file size in bytes\" : 4096,\n" +
                "\t\t\t\"minor version number\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"btree\" : {\n" +
                "\t\t\t\"btree checkpoint generation\" : 90,\n" +
                "\t\t\t\"btree clean tree checkpoint expiration time\" : 0,\n" +
                "\t\t\t\"btree compact pages reviewed\" : 0,\n" +
                "\t\t\t\"btree compact pages rewritten\" : 0,\n" +
                "\t\t\t\"btree compact pages skipped\" : 0,\n" +
                "\t\t\t\"btree skipped by compaction as process would not reduce size\" : 0,\n" +
                "\t\t\t\"column-store fixed-size leaf pages\" : 0,\n" +
                "\t\t\t\"column-store internal pages\" : 0,\n" +
                "\t\t\t\"column-store variable-size RLE encoded values\" : 0,\n" +
                "\t\t\t\"column-store variable-size deleted values\" : 0,\n" +
                "\t\t\t\"column-store variable-size leaf pages\" : 0,\n" +
                "\t\t\t\"fixed-record size\" : 0,\n" +
                "\t\t\t\"maximum internal page size\" : 4096,\n" +
                "\t\t\t\"maximum leaf page key size\" : 2867,\n" +
                "\t\t\t\"maximum leaf page size\" : 32768,\n" +
                "\t\t\t\"maximum leaf page value size\" : 67108864,\n" +
                "\t\t\t\"maximum tree depth\" : 0,\n" +
                "\t\t\t\"number of key/value pairs\" : 0,\n" +
                "\t\t\t\"overflow pages\" : 0,\n" +
                "\t\t\t\"row-store empty values\" : 0,\n" +
                "\t\t\t\"row-store internal pages\" : 0,\n" +
                "\t\t\t\"row-store leaf pages\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"cache\" : {\n" +
                "\t\t\t\"bytes currently in the cache\" : 182,\n" +
                "\t\t\t\"bytes dirty in the cache cumulative\" : 0,\n" +
                "\t\t\t\"bytes read into cache\" : 0,\n" +
                "\t\t\t\"bytes written from cache\" : 0,\n" +
                "\t\t\t\"checkpoint blocked page eviction\" : 0,\n" +
                "\t\t\t\"checkpoint of history store file blocked non-history store page eviction\" : 0,\n" +
                "\t\t\t\"data source pages selected for eviction unable to be evicted\" : 0,\n" +
                "\t\t\t\"eviction gave up due to detecting an out of order on disk value behind the last update on the chain\" : 0,\n" +
                "\t\t\t\"eviction gave up due to detecting an out of order tombstone ahead of the selected on disk update\" : 0,\n" +
                "\t\t\t\"eviction gave up due to detecting an out of order tombstone ahead of the selected on disk update after validating the update chain\" : 0,\n" +
                "\t\t\t\"eviction gave up due to detecting out of order timestamps on the update chain after the selected on disk update\" : 0,\n" +
                "\t\t\t\"eviction walk passes of a file\" : 0,\n" +
                "\t\t\t\"eviction walk target pages histogram - 0-9\" : 0,\n" +
                "\t\t\t\"eviction walk target pages histogram - 10-31\" : 0,\n" +
                "\t\t\t\"eviction walk target pages histogram - 128 and higher\" : 0,\n" +
                "\t\t\t\"eviction walk target pages histogram - 32-63\" : 0,\n" +
                "\t\t\t\"eviction walk target pages histogram - 64-128\" : 0,\n" +
                "\t\t\t\"eviction walk target pages reduced due to history store cache pressure\" : 0,\n" +
                "\t\t\t\"eviction walks abandoned\" : 0,\n" +
                "\t\t\t\"eviction walks gave up because they restarted their walk twice\" : 0,\n" +
                "\t\t\t\"eviction walks gave up because they saw too many pages and found no candidates\" : 0,\n" +
                "\t\t\t\"eviction walks gave up because they saw too many pages and found too few candidates\" : 0,\n" +
                "\t\t\t\"eviction walks reached end of tree\" : 0,\n" +
                "\t\t\t\"eviction walks restarted\" : 0,\n" +
                "\t\t\t\"eviction walks started from root of tree\" : 0,\n" +
                "\t\t\t\"eviction walks started from saved location in tree\" : 0,\n" +
                "\t\t\t\"hazard pointer blocked page eviction\" : 0,\n" +
                "\t\t\t\"history store table insert calls\" : 0,\n" +
                "\t\t\t\"history store table insert calls that returned restart\" : 0,\n" +
                "\t\t\t\"history store table out-of-order resolved updates that lose their durable timestamp\" : 0,\n" +
                "\t\t\t\"history store table out-of-order updates that were fixed up by reinserting with the fixed timestamp\" : 0,\n" +
                "\t\t\t\"history store table reads\" : 0,\n" +
                "\t\t\t\"history store table reads missed\" : 0,\n" +
                "\t\t\t\"history store table reads requiring squashed modifies\" : 0,\n" +
                "\t\t\t\"history store table truncation by rollback to stable to remove an unstable update\" : 0,\n" +
                "\t\t\t\"history store table truncation by rollback to stable to remove an update\" : 0,\n" +
                "\t\t\t\"history store table truncation to remove an update\" : 0,\n" +
                "\t\t\t\"history store table truncation to remove range of updates due to key being removed from the data page during reconciliation\" : 0,\n" +
                "\t\t\t\"history store table truncation to remove range of updates due to out-of-order timestamp update on data page\" : 0,\n" +
                "\t\t\t\"history store table writes requiring squashed modifies\" : 0,\n" +
                "\t\t\t\"in-memory page passed criteria to be split\" : 0,\n" +
                "\t\t\t\"in-memory page splits\" : 0,\n" +
                "\t\t\t\"internal pages evicted\" : 0,\n" +
                "\t\t\t\"internal pages split during eviction\" : 0,\n" +
                "\t\t\t\"leaf pages split during eviction\" : 0,\n" +
                "\t\t\t\"modified pages evicted\" : 0,\n" +
                "\t\t\t\"overflow pages read into cache\" : 0,\n" +
                "\t\t\t\"page split during eviction deepened the tree\" : 0,\n" +
                "\t\t\t\"page written requiring history store records\" : 0,\n" +
                "\t\t\t\"pages read into cache\" : 0,\n" +
                "\t\t\t\"pages read into cache after truncate\" : 0,\n" +
                "\t\t\t\"pages read into cache after truncate in prepare state\" : 0,\n" +
                "\t\t\t\"pages requested from the cache\" : 1,\n" +
                "\t\t\t\"pages seen by eviction walk\" : 0,\n" +
                "\t\t\t\"pages written from cache\" : 0,\n" +
                "\t\t\t\"pages written requiring in-memory restoration\" : 0,\n" +
                "\t\t\t\"the number of times full update inserted to history store\" : 0,\n" +
                "\t\t\t\"the number of times reverse modify inserted to history store\" : 0,\n" +
                "\t\t\t\"tracked dirty bytes in the cache\" : 0,\n" +
                "\t\t\t\"unmodified pages evicted\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"cache_walk\" : {\n" +
                "\t\t\t\"Average difference between current eviction generation when the page was last considered\" : 0,\n" +
                "\t\t\t\"Average on-disk page image size seen\" : 0,\n" +
                "\t\t\t\"Average time in cache for pages that have been visited by the eviction server\" : 0,\n" +
                "\t\t\t\"Average time in cache for pages that have not been visited by the eviction server\" : 0,\n" +
                "\t\t\t\"Clean pages currently in cache\" : 0,\n" +
                "\t\t\t\"Current eviction generation\" : 0,\n" +
                "\t\t\t\"Dirty pages currently in cache\" : 0,\n" +
                "\t\t\t\"Entries in the root page\" : 0,\n" +
                "\t\t\t\"Internal pages currently in cache\" : 0,\n" +
                "\t\t\t\"Leaf pages currently in cache\" : 0,\n" +
                "\t\t\t\"Maximum difference between current eviction generation when the page was last considered\" : 0,\n" +
                "\t\t\t\"Maximum page size seen\" : 0,\n" +
                "\t\t\t\"Minimum on-disk page image size seen\" : 0,\n" +
                "\t\t\t\"Number of pages never visited by eviction server\" : 0,\n" +
                "\t\t\t\"On-disk page image sizes smaller than a single allocation unit\" : 0,\n" +
                "\t\t\t\"Pages created in memory and never written\" : 0,\n" +
                "\t\t\t\"Pages currently queued for eviction\" : 0,\n" +
                "\t\t\t\"Pages that could not be queued for eviction\" : 0,\n" +
                "\t\t\t\"Refs skipped during cache traversal\" : 0,\n" +
                "\t\t\t\"Size of the root page\" : 0,\n" +
                "\t\t\t\"Total number of pages currently in cache\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"checkpoint-cleanup\" : {\n" +
                "\t\t\t\"pages added for eviction\" : 0,\n" +
                "\t\t\t\"pages removed\" : 0,\n" +
                "\t\t\t\"pages skipped during tree walk\" : 0,\n" +
                "\t\t\t\"pages visited\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"compression\" : {\n" +
                "\t\t\t\"compressed page maximum internal page size prior to compression\" : 4096,\n" +
                "\t\t\t\"compressed page maximum leaf page size prior to compression \" : 131072,\n" +
                "\t\t\t\"compressed pages read\" : 0,\n" +
                "\t\t\t\"compressed pages written\" : 0,\n" +
                "\t\t\t\"number of blocks with compress ratio greater than 64\" : 0,\n" +
                "\t\t\t\"number of blocks with compress ratio smaller than 16\" : 0,\n" +
                "\t\t\t\"number of blocks with compress ratio smaller than 2\" : 0,\n" +
                "\t\t\t\"number of blocks with compress ratio smaller than 32\" : 0,\n" +
                "\t\t\t\"number of blocks with compress ratio smaller than 4\" : 0,\n" +
                "\t\t\t\"number of blocks with compress ratio smaller than 64\" : 0,\n" +
                "\t\t\t\"number of blocks with compress ratio smaller than 8\" : 0,\n" +
                "\t\t\t\"page written failed to compress\" : 0,\n" +
                "\t\t\t\"page written was too small to compress\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"cursor\" : {\n" +
                "\t\t\t\"Total number of entries skipped by cursor next calls\" : 0,\n" +
                "\t\t\t\"Total number of entries skipped by cursor prev calls\" : 0,\n" +
                "\t\t\t\"Total number of entries skipped to position the history store cursor\" : 0,\n" +
                "\t\t\t\"Total number of times a search near has exited due to prefix config\" : 0,\n" +
                "\t\t\t\"bulk loaded cursor insert calls\" : 0,\n" +
                "\t\t\t\"cache cursors reuse count\" : 0,\n" +
                "\t\t\t\"close calls that result in cache\" : 1,\n" +
                "\t\t\t\"create calls\" : 1,\n" +
                "\t\t\t\"cursor next calls that skip due to a globally visible history store tombstone\" : 0,\n" +
                "\t\t\t\"cursor next calls that skip greater than or equal to 100 entries\" : 0,\n" +
                "\t\t\t\"cursor next calls that skip less than 100 entries\" : 1,\n" +
                "\t\t\t\"cursor prev calls that skip due to a globally visible history store tombstone\" : 0,\n" +
                "\t\t\t\"cursor prev calls that skip greater than or equal to 100 entries\" : 0,\n" +
                "\t\t\t\"cursor prev calls that skip less than 100 entries\" : 0,\n" +
                "\t\t\t\"insert calls\" : 0,\n" +
                "\t\t\t\"insert key and value bytes\" : 0,\n" +
                "\t\t\t\"modify\" : 0,\n" +
                "\t\t\t\"modify key and value bytes affected\" : 0,\n" +
                "\t\t\t\"modify value bytes modified\" : 0,\n" +
                "\t\t\t\"next calls\" : 1,\n" +
                "\t\t\t\"open cursor count\" : 0,\n" +
                "\t\t\t\"operation restarted\" : 0,\n" +
                "\t\t\t\"prev calls\" : 0,\n" +
                "\t\t\t\"remove calls\" : 0,\n" +
                "\t\t\t\"remove key bytes removed\" : 0,\n" +
                "\t\t\t\"reserve calls\" : 0,\n" +
                "\t\t\t\"reset calls\" : 2,\n" +
                "\t\t\t\"search calls\" : 0,\n" +
                "\t\t\t\"search history store calls\" : 0,\n" +
                "\t\t\t\"search near calls\" : 0,\n" +
                "\t\t\t\"truncate calls\" : 0,\n" +
                "\t\t\t\"update calls\" : 0,\n" +
                "\t\t\t\"update key and value bytes\" : 0,\n" +
                "\t\t\t\"update value size change\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"reconciliation\" : {\n" +
                "\t\t\t\"approximate byte size of timestamps in pages written\" : 0,\n" +
                "\t\t\t\"approximate byte size of transaction IDs in pages written\" : 0,\n" +
                "\t\t\t\"dictionary matches\" : 0,\n" +
                "\t\t\t\"fast-path pages deleted\" : 0,\n" +
                "\t\t\t\"internal page key bytes discarded using suffix compression\" : 0,\n" +
                "\t\t\t\"internal page multi-block writes\" : 0,\n" +
                "\t\t\t\"leaf page key bytes discarded using prefix compression\" : 0,\n" +
                "\t\t\t\"leaf page multi-block writes\" : 0,\n" +
                "\t\t\t\"leaf-page overflow keys\" : 0,\n" +
                "\t\t\t\"maximum blocks required for a page\" : 0,\n" +
                "\t\t\t\"overflow values written\" : 0,\n" +
                "\t\t\t\"page checksum matches\" : 0,\n" +
                "\t\t\t\"page reconciliation calls\" : 0,\n" +
                "\t\t\t\"page reconciliation calls for eviction\" : 0,\n" +
                "\t\t\t\"pages deleted\" : 0,\n" +
                "\t\t\t\"pages written including an aggregated newest start durable timestamp \" : 0,\n" +
                "\t\t\t\"pages written including an aggregated newest stop durable timestamp \" : 0,\n" +
                "\t\t\t\"pages written including an aggregated newest stop timestamp \" : 0,\n" +
                "\t\t\t\"pages written including an aggregated newest stop transaction ID\" : 0,\n" +
                "\t\t\t\"pages written including an aggregated newest transaction ID \" : 0,\n" +
                "\t\t\t\"pages written including an aggregated oldest start timestamp \" : 0,\n" +
                "\t\t\t\"pages written including an aggregated prepare\" : 0,\n" +
                "\t\t\t\"pages written including at least one prepare\" : 0,\n" +
                "\t\t\t\"pages written including at least one start durable timestamp\" : 0,\n" +
                "\t\t\t\"pages written including at least one start timestamp\" : 0,\n" +
                "\t\t\t\"pages written including at least one start transaction ID\" : 0,\n" +
                "\t\t\t\"pages written including at least one stop durable timestamp\" : 0,\n" +
                "\t\t\t\"pages written including at least one stop timestamp\" : 0,\n" +
                "\t\t\t\"pages written including at least one stop transaction ID\" : 0,\n" +
                "\t\t\t\"records written including a prepare\" : 0,\n" +
                "\t\t\t\"records written including a start durable timestamp\" : 0,\n" +
                "\t\t\t\"records written including a start timestamp\" : 0,\n" +
                "\t\t\t\"records written including a start transaction ID\" : 0,\n" +
                "\t\t\t\"records written including a stop durable timestamp\" : 0,\n" +
                "\t\t\t\"records written including a stop timestamp\" : 0,\n" +
                "\t\t\t\"records written including a stop transaction ID\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"session\" : {\n" +
                "\t\t\t\"object compaction\" : 0,\n" +
                "\t\t\t\"tiered operations dequeued and processed\" : 0,\n" +
                "\t\t\t\"tiered operations scheduled\" : 0,\n" +
                "\t\t\t\"tiered storage local retention time (secs)\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"transaction\" : {\n" +
                "\t\t\t\"race to read prepared update retry\" : 0,\n" +
                "\t\t\t\"rollback to stable history store records with stop timestamps older than newer records\" : 0,\n" +
                "\t\t\t\"rollback to stable inconsistent checkpoint\" : 0,\n" +
                "\t\t\t\"rollback to stable keys removed\" : 0,\n" +
                "\t\t\t\"rollback to stable keys restored\" : 0,\n" +
                "\t\t\t\"rollback to stable restored tombstones from history store\" : 0,\n" +
                "\t\t\t\"rollback to stable restored updates from history store\" : 0,\n" +
                "\t\t\t\"rollback to stable skipping delete rle\" : 0,\n" +
                "\t\t\t\"rollback to stable skipping stable rle\" : 0,\n" +
                "\t\t\t\"rollback to stable sweeping history store keys\" : 0,\n" +
                "\t\t\t\"rollback to stable updates removed from history store\" : 0,\n" +
                "\t\t\t\"transaction checkpoints due to obsolete pages\" : 0,\n" +
                "\t\t\t\"update conflicts\" : 0\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"nindexes\" : 1,\n" +
                "\t\"indexDetails\" : {\n" +
                "\t\t\"_id_\" : {\n" +
                "\t\t\t\"metadata\" : {\n" +
                "\t\t\t\t\"formatVersion\" : 8\n" +
                "\t\t\t},\n" +
                "\t\t\t\"creationString\" : \"access_pattern_hint=none,allocation_size=4KB,app_metadata=(formatVersion=8),assert=(commit_timestamp=none,durable_timestamp=none,read_timestamp=none,write_timestamp=off),block_allocation=best,block_compressor=,cache_resident=false,checksum=on,colgroups=,collator=,columns=,dictionary=0,encryption=(keyid=,name=),exclusive=false,extractor=,format=btree,huffman_key=,huffman_value=,ignore_in_memory_cache_size=false,immutable=false,import=(enabled=false,file_metadata=,repair=false),internal_item_max=0,internal_key_max=0,internal_key_truncate=true,internal_page_max=16k,key_format=u,key_gap=10,leaf_item_max=0,leaf_key_max=0,leaf_page_max=16k,leaf_value_max=0,log=(enabled=true),lsm=(auto_throttle=true,bloom=true,bloom_bit_count=16,bloom_config=,bloom_hash_count=8,bloom_oldest=false,chunk_count_limit=0,chunk_max=5GB,chunk_size=10MB,merge_custom=(prefix=,start_generation=0,suffix=),merge_max=15,merge_min=0),memory_page_image_max=0,memory_page_max=5MB,os_cache_dirty_max=0,os_cache_max=0,prefix_compression=true,prefix_compression_min=4,readonly=false,source=,split_deepen_min_child=0,split_deepen_per_child=0,split_pct=90,tiered_object=false,tiered_storage=(auth_token=,bucket=,bucket_prefix=,cache_directory=,local_retention=300,name=),type=file,value_format=u,verbose=[],write_timestamp_usage=none\",\n" +
                "\t\t\t\"type\" : \"file\",\n" +
                "\t\t\t\"uri\" : \"statistics:table:index-71--2002033493638094686\",\n" +
                "\t\t\t\"LSM\" : {\n" +
                "\t\t\t\t\"bloom filter false positives\" : 0,\n" +
                "\t\t\t\t\"bloom filter hits\" : 0,\n" +
                "\t\t\t\t\"bloom filter misses\" : 0,\n" +
                "\t\t\t\t\"bloom filter pages evicted from cache\" : 0,\n" +
                "\t\t\t\t\"bloom filter pages read into cache\" : 0,\n" +
                "\t\t\t\t\"bloom filters in the LSM tree\" : 0,\n" +
                "\t\t\t\t\"chunks in the LSM tree\" : 0,\n" +
                "\t\t\t\t\"highest merge generation in the LSM tree\" : 0,\n" +
                "\t\t\t\t\"queries that could have benefited from a Bloom filter that did not exist\" : 0,\n" +
                "\t\t\t\t\"sleep for LSM checkpoint throttle\" : 0,\n" +
                "\t\t\t\t\"sleep for LSM merge throttle\" : 0,\n" +
                "\t\t\t\t\"total size of bloom filters\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"block-manager\" : {\n" +
                "\t\t\t\t\"allocations requiring file extension\" : 0,\n" +
                "\t\t\t\t\"blocks allocated\" : 0,\n" +
                "\t\t\t\t\"blocks freed\" : 0,\n" +
                "\t\t\t\t\"checkpoint size\" : 0,\n" +
                "\t\t\t\t\"file allocation unit size\" : 4096,\n" +
                "\t\t\t\t\"file bytes available for reuse\" : 0,\n" +
                "\t\t\t\t\"file magic number\" : 120897,\n" +
                "\t\t\t\t\"file major version number\" : 1,\n" +
                "\t\t\t\t\"file size in bytes\" : 4096,\n" +
                "\t\t\t\t\"minor version number\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"btree\" : {\n" +
                "\t\t\t\t\"btree checkpoint generation\" : 90,\n" +
                "\t\t\t\t\"btree clean tree checkpoint expiration time\" : 0,\n" +
                "\t\t\t\t\"btree compact pages reviewed\" : 0,\n" +
                "\t\t\t\t\"btree compact pages rewritten\" : 0,\n" +
                "\t\t\t\t\"btree compact pages skipped\" : 0,\n" +
                "\t\t\t\t\"btree skipped by compaction as process would not reduce size\" : 0,\n" +
                "\t\t\t\t\"column-store fixed-size leaf pages\" : 0,\n" +
                "\t\t\t\t\"column-store internal pages\" : 0,\n" +
                "\t\t\t\t\"column-store variable-size RLE encoded values\" : 0,\n" +
                "\t\t\t\t\"column-store variable-size deleted values\" : 0,\n" +
                "\t\t\t\t\"column-store variable-size leaf pages\" : 0,\n" +
                "\t\t\t\t\"fixed-record size\" : 0,\n" +
                "\t\t\t\t\"maximum internal page size\" : 16384,\n" +
                "\t\t\t\t\"maximum leaf page key size\" : 1474,\n" +
                "\t\t\t\t\"maximum leaf page size\" : 16384,\n" +
                "\t\t\t\t\"maximum leaf page value size\" : 7372,\n" +
                "\t\t\t\t\"maximum tree depth\" : 0,\n" +
                "\t\t\t\t\"number of key/value pairs\" : 0,\n" +
                "\t\t\t\t\"overflow pages\" : 0,\n" +
                "\t\t\t\t\"row-store empty values\" : 0,\n" +
                "\t\t\t\t\"row-store internal pages\" : 0,\n" +
                "\t\t\t\t\"row-store leaf pages\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"cache\" : {\n" +
                "\t\t\t\t\"bytes currently in the cache\" : 182,\n" +
                "\t\t\t\t\"bytes dirty in the cache cumulative\" : 0,\n" +
                "\t\t\t\t\"bytes read into cache\" : 0,\n" +
                "\t\t\t\t\"bytes written from cache\" : 0,\n" +
                "\t\t\t\t\"checkpoint blocked page eviction\" : 0,\n" +
                "\t\t\t\t\"checkpoint of history store file blocked non-history store page eviction\" : 0,\n" +
                "\t\t\t\t\"data source pages selected for eviction unable to be evicted\" : 0,\n" +
                "\t\t\t\t\"eviction gave up due to detecting an out of order on disk value behind the last update on the chain\" : 0,\n" +
                "\t\t\t\t\"eviction gave up due to detecting an out of order tombstone ahead of the selected on disk update\" : 0,\n" +
                "\t\t\t\t\"eviction gave up due to detecting an out of order tombstone ahead of the selected on disk update after validating the update chain\" : 0,\n" +
                "\t\t\t\t\"eviction gave up due to detecting out of order timestamps on the update chain after the selected on disk update\" : 0,\n" +
                "\t\t\t\t\"eviction walk passes of a file\" : 0,\n" +
                "\t\t\t\t\"eviction walk target pages histogram - 0-9\" : 0,\n" +
                "\t\t\t\t\"eviction walk target pages histogram - 10-31\" : 0,\n" +
                "\t\t\t\t\"eviction walk target pages histogram - 128 and higher\" : 0,\n" +
                "\t\t\t\t\"eviction walk target pages histogram - 32-63\" : 0,\n" +
                "\t\t\t\t\"eviction walk target pages histogram - 64-128\" : 0,\n" +
                "\t\t\t\t\"eviction walk target pages reduced due to history store cache pressure\" : 0,\n" +
                "\t\t\t\t\"eviction walks abandoned\" : 0,\n" +
                "\t\t\t\t\"eviction walks gave up because they restarted their walk twice\" : 0,\n" +
                "\t\t\t\t\"eviction walks gave up because they saw too many pages and found no candidates\" : 0,\n" +
                "\t\t\t\t\"eviction walks gave up because they saw too many pages and found too few candidates\" : 0,\n" +
                "\t\t\t\t\"eviction walks reached end of tree\" : 0,\n" +
                "\t\t\t\t\"eviction walks restarted\" : 0,\n" +
                "\t\t\t\t\"eviction walks started from root of tree\" : 0,\n" +
                "\t\t\t\t\"eviction walks started from saved location in tree\" : 0,\n" +
                "\t\t\t\t\"hazard pointer blocked page eviction\" : 0,\n" +
                "\t\t\t\t\"history store table insert calls\" : 0,\n" +
                "\t\t\t\t\"history store table insert calls that returned restart\" : 0,\n" +
                "\t\t\t\t\"history store table out-of-order resolved updates that lose their durable timestamp\" : 0,\n" +
                "\t\t\t\t\"history store table out-of-order updates that were fixed up by reinserting with the fixed timestamp\" : 0,\n" +
                "\t\t\t\t\"history store table reads\" : 0,\n" +
                "\t\t\t\t\"history store table reads missed\" : 0,\n" +
                "\t\t\t\t\"history store table reads requiring squashed modifies\" : 0,\n" +
                "\t\t\t\t\"history store table truncation by rollback to stable to remove an unstable update\" : 0,\n" +
                "\t\t\t\t\"history store table truncation by rollback to stable to remove an update\" : 0,\n" +
                "\t\t\t\t\"history store table truncation to remove an update\" : 0,\n" +
                "\t\t\t\t\"history store table truncation to remove range of updates due to key being removed from the data page during reconciliation\" : 0,\n" +
                "\t\t\t\t\"history store table truncation to remove range of updates due to out-of-order timestamp update on data page\" : 0,\n" +
                "\t\t\t\t\"history store table writes requiring squashed modifies\" : 0,\n" +
                "\t\t\t\t\"in-memory page passed criteria to be split\" : 0,\n" +
                "\t\t\t\t\"in-memory page splits\" : 0,\n" +
                "\t\t\t\t\"internal pages evicted\" : 0,\n" +
                "\t\t\t\t\"internal pages split during eviction\" : 0,\n" +
                "\t\t\t\t\"leaf pages split during eviction\" : 0,\n" +
                "\t\t\t\t\"modified pages evicted\" : 0,\n" +
                "\t\t\t\t\"overflow pages read into cache\" : 0,\n" +
                "\t\t\t\t\"page split during eviction deepened the tree\" : 0,\n" +
                "\t\t\t\t\"page written requiring history store records\" : 0,\n" +
                "\t\t\t\t\"pages read into cache\" : 0,\n" +
                "\t\t\t\t\"pages read into cache after truncate\" : 0,\n" +
                "\t\t\t\t\"pages read into cache after truncate in prepare state\" : 0,\n" +
                "\t\t\t\t\"pages requested from the cache\" : 0,\n" +
                "\t\t\t\t\"pages seen by eviction walk\" : 0,\n" +
                "\t\t\t\t\"pages written from cache\" : 0,\n" +
                "\t\t\t\t\"pages written requiring in-memory restoration\" : 0,\n" +
                "\t\t\t\t\"the number of times full update inserted to history store\" : 0,\n" +
                "\t\t\t\t\"the number of times reverse modify inserted to history store\" : 0,\n" +
                "\t\t\t\t\"tracked dirty bytes in the cache\" : 0,\n" +
                "\t\t\t\t\"unmodified pages evicted\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"cache_walk\" : {\n" +
                "\t\t\t\t\"Average difference between current eviction generation when the page was last considered\" : 0,\n" +
                "\t\t\t\t\"Average on-disk page image size seen\" : 0,\n" +
                "\t\t\t\t\"Average time in cache for pages that have been visited by the eviction server\" : 0,\n" +
                "\t\t\t\t\"Average time in cache for pages that have not been visited by the eviction server\" : 0,\n" +
                "\t\t\t\t\"Clean pages currently in cache\" : 0,\n" +
                "\t\t\t\t\"Current eviction generation\" : 0,\n" +
                "\t\t\t\t\"Dirty pages currently in cache\" : 0,\n" +
                "\t\t\t\t\"Entries in the root page\" : 0,\n" +
                "\t\t\t\t\"Internal pages currently in cache\" : 0,\n" +
                "\t\t\t\t\"Leaf pages currently in cache\" : 0,\n" +
                "\t\t\t\t\"Maximum difference between current eviction generation when the page was last considered\" : 0,\n" +
                "\t\t\t\t\"Maximum page size seen\" : 0,\n" +
                "\t\t\t\t\"Minimum on-disk page image size seen\" : 0,\n" +
                "\t\t\t\t\"Number of pages never visited by eviction server\" : 0,\n" +
                "\t\t\t\t\"On-disk page image sizes smaller than a single allocation unit\" : 0,\n" +
                "\t\t\t\t\"Pages created in memory and never written\" : 0,\n" +
                "\t\t\t\t\"Pages currently queued for eviction\" : 0,\n" +
                "\t\t\t\t\"Pages that could not be queued for eviction\" : 0,\n" +
                "\t\t\t\t\"Refs skipped during cache traversal\" : 0,\n" +
                "\t\t\t\t\"Size of the root page\" : 0,\n" +
                "\t\t\t\t\"Total number of pages currently in cache\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"checkpoint-cleanup\" : {\n" +
                "\t\t\t\t\"pages added for eviction\" : 0,\n" +
                "\t\t\t\t\"pages removed\" : 0,\n" +
                "\t\t\t\t\"pages skipped during tree walk\" : 0,\n" +
                "\t\t\t\t\"pages visited\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"compression\" : {\n" +
                "\t\t\t\t\"compressed page maximum internal page size prior to compression\" : 16384,\n" +
                "\t\t\t\t\"compressed page maximum leaf page size prior to compression \" : 16384,\n" +
                "\t\t\t\t\"compressed pages read\" : 0,\n" +
                "\t\t\t\t\"compressed pages written\" : 0,\n" +
                "\t\t\t\t\"number of blocks with compress ratio greater than 64\" : 0,\n" +
                "\t\t\t\t\"number of blocks with compress ratio smaller than 16\" : 0,\n" +
                "\t\t\t\t\"number of blocks with compress ratio smaller than 2\" : 0,\n" +
                "\t\t\t\t\"number of blocks with compress ratio smaller than 32\" : 0,\n" +
                "\t\t\t\t\"number of blocks with compress ratio smaller than 4\" : 0,\n" +
                "\t\t\t\t\"number of blocks with compress ratio smaller than 64\" : 0,\n" +
                "\t\t\t\t\"number of blocks with compress ratio smaller than 8\" : 0,\n" +
                "\t\t\t\t\"page written failed to compress\" : 0,\n" +
                "\t\t\t\t\"page written was too small to compress\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"cursor\" : {\n" +
                "\t\t\t\t\"Total number of entries skipped by cursor next calls\" : 0,\n" +
                "\t\t\t\t\"Total number of entries skipped by cursor prev calls\" : 0,\n" +
                "\t\t\t\t\"Total number of entries skipped to position the history store cursor\" : 0,\n" +
                "\t\t\t\t\"Total number of times a search near has exited due to prefix config\" : 0,\n" +
                "\t\t\t\t\"bulk loaded cursor insert calls\" : 0,\n" +
                "\t\t\t\t\"cache cursors reuse count\" : 0,\n" +
                "\t\t\t\t\"close calls that result in cache\" : 0,\n" +
                "\t\t\t\t\"create calls\" : 0,\n" +
                "\t\t\t\t\"cursor next calls that skip due to a globally visible history store tombstone\" : 0,\n" +
                "\t\t\t\t\"cursor next calls that skip greater than or equal to 100 entries\" : 0,\n" +
                "\t\t\t\t\"cursor next calls that skip less than 100 entries\" : 0,\n" +
                "\t\t\t\t\"cursor prev calls that skip due to a globally visible history store tombstone\" : 0,\n" +
                "\t\t\t\t\"cursor prev calls that skip greater than or equal to 100 entries\" : 0,\n" +
                "\t\t\t\t\"cursor prev calls that skip less than 100 entries\" : 0,\n" +
                "\t\t\t\t\"insert calls\" : 0,\n" +
                "\t\t\t\t\"insert key and value bytes\" : 0,\n" +
                "\t\t\t\t\"modify\" : 0,\n" +
                "\t\t\t\t\"modify key and value bytes affected\" : 0,\n" +
                "\t\t\t\t\"modify value bytes modified\" : 0,\n" +
                "\t\t\t\t\"next calls\" : 0,\n" +
                "\t\t\t\t\"open cursor count\" : 0,\n" +
                "\t\t\t\t\"operation restarted\" : 0,\n" +
                "\t\t\t\t\"prev calls\" : 0,\n" +
                "\t\t\t\t\"remove calls\" : 0,\n" +
                "\t\t\t\t\"remove key bytes removed\" : 0,\n" +
                "\t\t\t\t\"reserve calls\" : 0,\n" +
                "\t\t\t\t\"reset calls\" : 0,\n" +
                "\t\t\t\t\"search calls\" : 0,\n" +
                "\t\t\t\t\"search history store calls\" : 0,\n" +
                "\t\t\t\t\"search near calls\" : 0,\n" +
                "\t\t\t\t\"truncate calls\" : 0,\n" +
                "\t\t\t\t\"update calls\" : 0,\n" +
                "\t\t\t\t\"update key and value bytes\" : 0,\n" +
                "\t\t\t\t\"update value size change\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"reconciliation\" : {\n" +
                "\t\t\t\t\"approximate byte size of timestamps in pages written\" : 0,\n" +
                "\t\t\t\t\"approximate byte size of transaction IDs in pages written\" : 0,\n" +
                "\t\t\t\t\"dictionary matches\" : 0,\n" +
                "\t\t\t\t\"fast-path pages deleted\" : 0,\n" +
                "\t\t\t\t\"internal page key bytes discarded using suffix compression\" : 0,\n" +
                "\t\t\t\t\"internal page multi-block writes\" : 0,\n" +
                "\t\t\t\t\"leaf page key bytes discarded using prefix compression\" : 0,\n" +
                "\t\t\t\t\"leaf page multi-block writes\" : 0,\n" +
                "\t\t\t\t\"leaf-page overflow keys\" : 0,\n" +
                "\t\t\t\t\"maximum blocks required for a page\" : 0,\n" +
                "\t\t\t\t\"overflow values written\" : 0,\n" +
                "\t\t\t\t\"page checksum matches\" : 0,\n" +
                "\t\t\t\t\"page reconciliation calls\" : 0,\n" +
                "\t\t\t\t\"page reconciliation calls for eviction\" : 0,\n" +
                "\t\t\t\t\"pages deleted\" : 0,\n" +
                "\t\t\t\t\"pages written including an aggregated newest start durable timestamp \" : 0,\n" +
                "\t\t\t\t\"pages written including an aggregated newest stop durable timestamp \" : 0,\n" +
                "\t\t\t\t\"pages written including an aggregated newest stop timestamp \" : 0,\n" +
                "\t\t\t\t\"pages written including an aggregated newest stop transaction ID\" : 0,\n" +
                "\t\t\t\t\"pages written including an aggregated newest transaction ID \" : 0,\n" +
                "\t\t\t\t\"pages written including an aggregated oldest start timestamp \" : 0,\n" +
                "\t\t\t\t\"pages written including an aggregated prepare\" : 0,\n" +
                "\t\t\t\t\"pages written including at least one prepare\" : 0,\n" +
                "\t\t\t\t\"pages written including at least one start durable timestamp\" : 0,\n" +
                "\t\t\t\t\"pages written including at least one start timestamp\" : 0,\n" +
                "\t\t\t\t\"pages written including at least one start transaction ID\" : 0,\n" +
                "\t\t\t\t\"pages written including at least one stop durable timestamp\" : 0,\n" +
                "\t\t\t\t\"pages written including at least one stop timestamp\" : 0,\n" +
                "\t\t\t\t\"pages written including at least one stop transaction ID\" : 0,\n" +
                "\t\t\t\t\"records written including a prepare\" : 0,\n" +
                "\t\t\t\t\"records written including a start durable timestamp\" : 0,\n" +
                "\t\t\t\t\"records written including a start timestamp\" : 0,\n" +
                "\t\t\t\t\"records written including a start transaction ID\" : 0,\n" +
                "\t\t\t\t\"records written including a stop durable timestamp\" : 0,\n" +
                "\t\t\t\t\"records written including a stop timestamp\" : 0,\n" +
                "\t\t\t\t\"records written including a stop transaction ID\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"session\" : {\n" +
                "\t\t\t\t\"object compaction\" : 0,\n" +
                "\t\t\t\t\"tiered operations dequeued and processed\" : 0,\n" +
                "\t\t\t\t\"tiered operations scheduled\" : 0,\n" +
                "\t\t\t\t\"tiered storage local retention time (secs)\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"transaction\" : {\n" +
                "\t\t\t\t\"race to read prepared update retry\" : 0,\n" +
                "\t\t\t\t\"rollback to stable history store records with stop timestamps older than newer records\" : 0,\n" +
                "\t\t\t\t\"rollback to stable inconsistent checkpoint\" : 0,\n" +
                "\t\t\t\t\"rollback to stable keys removed\" : 0,\n" +
                "\t\t\t\t\"rollback to stable keys restored\" : 0,\n" +
                "\t\t\t\t\"rollback to stable restored tombstones from history store\" : 0,\n" +
                "\t\t\t\t\"rollback to stable restored updates from history store\" : 0,\n" +
                "\t\t\t\t\"rollback to stable skipping delete rle\" : 0,\n" +
                "\t\t\t\t\"rollback to stable skipping stable rle\" : 0,\n" +
                "\t\t\t\t\"rollback to stable sweeping history store keys\" : 0,\n" +
                "\t\t\t\t\"rollback to stable updates removed from history store\" : 0,\n" +
                "\t\t\t\t\"transaction checkpoints due to obsolete pages\" : 0,\n" +
                "\t\t\t\t\"update conflicts\" : 0\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"indexBuilds\" : [ ],\n" +
                "\t\"totalIndexSize\" : 4,\n" +
                "\t\"totalSize\" : 8,\n" +
                "\t\"indexSizes\" : {\n" +
                "\t\t\"_id_\" : 4\n" +
                "\t},\n" +
                "\t\"scaleFactor\" : 1024,\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
        Document document1 = mars.executeCommand("{ collStats : \"book\", scale: 1024 }");
        Document result1 = Document.parse("{\n" +
                "\t\"ns\" : \"mars.book\",\n" +
                "\t\"size\" : 0,\n" +
                "\t\"count\" : 0,\n" +
                "\t\"storageSize\" : 4,\n" +
                "\t\"freeStorageSize\" : 0,\n" +
                "\t\"capped\" : false,\n" +
                "\t\"wiredTiger\" : {\n" +
                "\t\t\"metadata\" : {\n" +
                "\t\t\t\"formatVersion\" : 1\n" +
                "\t\t},\n" +
                "\t\t\"creationString\" : \"access_pattern_hint=none,allocation_size=4KB,app_metadata=(formatVersion=1),assert=(commit_timestamp=none,durable_timestamp=none,read_timestamp=none,write_timestamp=off),block_allocation=best,block_compressor=snappy,cache_resident=false,checksum=on,colgroups=,collator=,columns=,dictionary=0,encryption=(keyid=,name=),exclusive=false,extractor=,format=btree,huffman_key=,huffman_value=,ignore_in_memory_cache_size=false,immutable=false,import=(enabled=false,file_metadata=,repair=false),internal_item_max=0,internal_key_max=0,internal_key_truncate=true,internal_page_max=4KB,key_format=q,key_gap=10,leaf_item_max=0,leaf_key_max=0,leaf_page_max=32KB,leaf_value_max=64MB,log=(enabled=true),lsm=(auto_throttle=true,bloom=true,bloom_bit_count=16,bloom_config=,bloom_hash_count=8,bloom_oldest=false,chunk_count_limit=0,chunk_max=5GB,chunk_size=10MB,merge_custom=(prefix=,start_generation=0,suffix=),merge_max=15,merge_min=0),memory_page_image_max=0,memory_page_max=10m,os_cache_dirty_max=0,os_cache_max=0,prefix_compression=false,prefix_compression_min=4,readonly=false,source=,split_deepen_min_child=0,split_deepen_per_child=0,split_pct=90,tiered_object=false,tiered_storage=(auth_token=,bucket=,bucket_prefix=,cache_directory=,local_retention=300,name=),type=file,value_format=u,verbose=[],write_timestamp_usage=none\",\n" +
                "\t\t\"type\" : \"file\",\n" +
                "\t\t\"uri\" : \"statistics:table:collection-74--2002033493638094686\",\n" +
                "\t\t\"LSM\" : {\n" +
                "\t\t\t\"bloom filter false positives\" : 0,\n" +
                "\t\t\t\"bloom filter hits\" : 0,\n" +
                "\t\t\t\"bloom filter misses\" : 0,\n" +
                "\t\t\t\"bloom filter pages evicted from cache\" : 0,\n" +
                "\t\t\t\"bloom filter pages read into cache\" : 0,\n" +
                "\t\t\t\"bloom filters in the LSM tree\" : 0,\n" +
                "\t\t\t\"chunks in the LSM tree\" : 0,\n" +
                "\t\t\t\"highest merge generation in the LSM tree\" : 0,\n" +
                "\t\t\t\"queries that could have benefited from a Bloom filter that did not exist\" : 0,\n" +
                "\t\t\t\"sleep for LSM checkpoint throttle\" : 0,\n" +
                "\t\t\t\"sleep for LSM merge throttle\" : 0,\n" +
                "\t\t\t\"total size of bloom filters\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"block-manager\" : {\n" +
                "\t\t\t\"allocations requiring file extension\" : 0,\n" +
                "\t\t\t\"blocks allocated\" : 0,\n" +
                "\t\t\t\"blocks freed\" : 0,\n" +
                "\t\t\t\"checkpoint size\" : 0,\n" +
                "\t\t\t\"file allocation unit size\" : 4096,\n" +
                "\t\t\t\"file bytes available for reuse\" : 0,\n" +
                "\t\t\t\"file magic number\" : 120897,\n" +
                "\t\t\t\"file major version number\" : 1,\n" +
                "\t\t\t\"file size in bytes\" : 4096,\n" +
                "\t\t\t\"minor version number\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"btree\" : {\n" +
                "\t\t\t\"btree checkpoint generation\" : 0,\n" +
                "\t\t\t\"btree clean tree checkpoint expiration time\" : 0,\n" +
                "\t\t\t\"btree compact pages reviewed\" : 0,\n" +
                "\t\t\t\"btree compact pages rewritten\" : 0,\n" +
                "\t\t\t\"btree compact pages skipped\" : 0,\n" +
                "\t\t\t\"btree skipped by compaction as process would not reduce size\" : 0,\n" +
                "\t\t\t\"column-store fixed-size leaf pages\" : 0,\n" +
                "\t\t\t\"column-store internal pages\" : 0,\n" +
                "\t\t\t\"column-store variable-size RLE encoded values\" : 0,\n" +
                "\t\t\t\"column-store variable-size deleted values\" : 0,\n" +
                "\t\t\t\"column-store variable-size leaf pages\" : 0,\n" +
                "\t\t\t\"fixed-record size\" : 0,\n" +
                "\t\t\t\"maximum internal page size\" : 4096,\n" +
                "\t\t\t\"maximum leaf page key size\" : 2867,\n" +
                "\t\t\t\"maximum leaf page size\" : 32768,\n" +
                "\t\t\t\"maximum leaf page value size\" : 67108864,\n" +
                "\t\t\t\"maximum tree depth\" : 0,\n" +
                "\t\t\t\"number of key/value pairs\" : 0,\n" +
                "\t\t\t\"overflow pages\" : 0,\n" +
                "\t\t\t\"row-store empty values\" : 0,\n" +
                "\t\t\t\"row-store internal pages\" : 0,\n" +
                "\t\t\t\"row-store leaf pages\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"cache\" : {\n" +
                "\t\t\t\"bytes currently in the cache\" : 182,\n" +
                "\t\t\t\"bytes dirty in the cache cumulative\" : 0,\n" +
                "\t\t\t\"bytes read into cache\" : 0,\n" +
                "\t\t\t\"bytes written from cache\" : 0,\n" +
                "\t\t\t\"checkpoint blocked page eviction\" : 0,\n" +
                "\t\t\t\"checkpoint of history store file blocked non-history store page eviction\" : 0,\n" +
                "\t\t\t\"data source pages selected for eviction unable to be evicted\" : 0,\n" +
                "\t\t\t\"eviction gave up due to detecting an out of order on disk value behind the last update on the chain\" : 0,\n" +
                "\t\t\t\"eviction gave up due to detecting an out of order tombstone ahead of the selected on disk update\" : 0,\n" +
                "\t\t\t\"eviction gave up due to detecting an out of order tombstone ahead of the selected on disk update after validating the update chain\" : 0,\n" +
                "\t\t\t\"eviction gave up due to detecting out of order timestamps on the update chain after the selected on disk update\" : 0,\n" +
                "\t\t\t\"eviction walk passes of a file\" : 0,\n" +
                "\t\t\t\"eviction walk target pages histogram - 0-9\" : 0,\n" +
                "\t\t\t\"eviction walk target pages histogram - 10-31\" : 0,\n" +
                "\t\t\t\"eviction walk target pages histogram - 128 and higher\" : 0,\n" +
                "\t\t\t\"eviction walk target pages histogram - 32-63\" : 0,\n" +
                "\t\t\t\"eviction walk target pages histogram - 64-128\" : 0,\n" +
                "\t\t\t\"eviction walk target pages reduced due to history store cache pressure\" : 0,\n" +
                "\t\t\t\"eviction walks abandoned\" : 0,\n" +
                "\t\t\t\"eviction walks gave up because they restarted their walk twice\" : 0,\n" +
                "\t\t\t\"eviction walks gave up because they saw too many pages and found no candidates\" : 0,\n" +
                "\t\t\t\"eviction walks gave up because they saw too many pages and found too few candidates\" : 0,\n" +
                "\t\t\t\"eviction walks reached end of tree\" : 0,\n" +
                "\t\t\t\"eviction walks restarted\" : 0,\n" +
                "\t\t\t\"eviction walks started from root of tree\" : 0,\n" +
                "\t\t\t\"eviction walks started from saved location in tree\" : 0,\n" +
                "\t\t\t\"hazard pointer blocked page eviction\" : 0,\n" +
                "\t\t\t\"history store table insert calls\" : 0,\n" +
                "\t\t\t\"history store table insert calls that returned restart\" : 0,\n" +
                "\t\t\t\"history store table out-of-order resolved updates that lose their durable timestamp\" : 0,\n" +
                "\t\t\t\"history store table out-of-order updates that were fixed up by reinserting with the fixed timestamp\" : 0,\n" +
                "\t\t\t\"history store table reads\" : 0,\n" +
                "\t\t\t\"history store table reads missed\" : 0,\n" +
                "\t\t\t\"history store table reads requiring squashed modifies\" : 0,\n" +
                "\t\t\t\"history store table truncation by rollback to stable to remove an unstable update\" : 0,\n" +
                "\t\t\t\"history store table truncation by rollback to stable to remove an update\" : 0,\n" +
                "\t\t\t\"history store table truncation to remove an update\" : 0,\n" +
                "\t\t\t\"history store table truncation to remove range of updates due to key being removed from the data page during reconciliation\" : 0,\n" +
                "\t\t\t\"history store table truncation to remove range of updates due to out-of-order timestamp update on data page\" : 0,\n" +
                "\t\t\t\"history store table writes requiring squashed modifies\" : 0,\n" +
                "\t\t\t\"in-memory page passed criteria to be split\" : 0,\n" +
                "\t\t\t\"in-memory page splits\" : 0,\n" +
                "\t\t\t\"internal pages evicted\" : 0,\n" +
                "\t\t\t\"internal pages split during eviction\" : 0,\n" +
                "\t\t\t\"leaf pages split during eviction\" : 0,\n" +
                "\t\t\t\"modified pages evicted\" : 0,\n" +
                "\t\t\t\"overflow pages read into cache\" : 0,\n" +
                "\t\t\t\"page split during eviction deepened the tree\" : 0,\n" +
                "\t\t\t\"page written requiring history store records\" : 0,\n" +
                "\t\t\t\"pages read into cache\" : 0,\n" +
                "\t\t\t\"pages read into cache after truncate\" : 0,\n" +
                "\t\t\t\"pages read into cache after truncate in prepare state\" : 0,\n" +
                "\t\t\t\"pages requested from the cache\" : 1,\n" +
                "\t\t\t\"pages seen by eviction walk\" : 0,\n" +
                "\t\t\t\"pages written from cache\" : 0,\n" +
                "\t\t\t\"pages written requiring in-memory restoration\" : 0,\n" +
                "\t\t\t\"the number of times full update inserted to history store\" : 0,\n" +
                "\t\t\t\"the number of times reverse modify inserted to history store\" : 0,\n" +
                "\t\t\t\"tracked dirty bytes in the cache\" : 0,\n" +
                "\t\t\t\"unmodified pages evicted\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"cache_walk\" : {\n" +
                "\t\t\t\"Average difference between current eviction generation when the page was last considered\" : 0,\n" +
                "\t\t\t\"Average on-disk page image size seen\" : 0,\n" +
                "\t\t\t\"Average time in cache for pages that have been visited by the eviction server\" : 0,\n" +
                "\t\t\t\"Average time in cache for pages that have not been visited by the eviction server\" : 0,\n" +
                "\t\t\t\"Clean pages currently in cache\" : 0,\n" +
                "\t\t\t\"Current eviction generation\" : 0,\n" +
                "\t\t\t\"Dirty pages currently in cache\" : 0,\n" +
                "\t\t\t\"Entries in the root page\" : 0,\n" +
                "\t\t\t\"Internal pages currently in cache\" : 0,\n" +
                "\t\t\t\"Leaf pages currently in cache\" : 0,\n" +
                "\t\t\t\"Maximum difference between current eviction generation when the page was last considered\" : 0,\n" +
                "\t\t\t\"Maximum page size seen\" : 0,\n" +
                "\t\t\t\"Minimum on-disk page image size seen\" : 0,\n" +
                "\t\t\t\"Number of pages never visited by eviction server\" : 0,\n" +
                "\t\t\t\"On-disk page image sizes smaller than a single allocation unit\" : 0,\n" +
                "\t\t\t\"Pages created in memory and never written\" : 0,\n" +
                "\t\t\t\"Pages currently queued for eviction\" : 0,\n" +
                "\t\t\t\"Pages that could not be queued for eviction\" : 0,\n" +
                "\t\t\t\"Refs skipped during cache traversal\" : 0,\n" +
                "\t\t\t\"Size of the root page\" : 0,\n" +
                "\t\t\t\"Total number of pages currently in cache\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"checkpoint-cleanup\" : {\n" +
                "\t\t\t\"pages added for eviction\" : 0,\n" +
                "\t\t\t\"pages removed\" : 0,\n" +
                "\t\t\t\"pages skipped during tree walk\" : 0,\n" +
                "\t\t\t\"pages visited\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"compression\" : {\n" +
                "\t\t\t\"compressed page maximum internal page size prior to compression\" : 4096,\n" +
                "\t\t\t\"compressed page maximum leaf page size prior to compression \" : 131072,\n" +
                "\t\t\t\"compressed pages read\" : 0,\n" +
                "\t\t\t\"compressed pages written\" : 0,\n" +
                "\t\t\t\"number of blocks with compress ratio greater than 64\" : 0,\n" +
                "\t\t\t\"number of blocks with compress ratio smaller than 16\" : 0,\n" +
                "\t\t\t\"number of blocks with compress ratio smaller than 2\" : 0,\n" +
                "\t\t\t\"number of blocks with compress ratio smaller than 32\" : 0,\n" +
                "\t\t\t\"number of blocks with compress ratio smaller than 4\" : 0,\n" +
                "\t\t\t\"number of blocks with compress ratio smaller than 64\" : 0,\n" +
                "\t\t\t\"number of blocks with compress ratio smaller than 8\" : 0,\n" +
                "\t\t\t\"page written failed to compress\" : 0,\n" +
                "\t\t\t\"page written was too small to compress\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"cursor\" : {\n" +
                "\t\t\t\"Total number of entries skipped by cursor next calls\" : 0,\n" +
                "\t\t\t\"Total number of entries skipped by cursor prev calls\" : 0,\n" +
                "\t\t\t\"Total number of entries skipped to position the history store cursor\" : 0,\n" +
                "\t\t\t\"Total number of times a search near has exited due to prefix config\" : 0,\n" +
                "\t\t\t\"bulk loaded cursor insert calls\" : 0,\n" +
                "\t\t\t\"cache cursors reuse count\" : 0,\n" +
                "\t\t\t\"close calls that result in cache\" : 1,\n" +
                "\t\t\t\"create calls\" : 1,\n" +
                "\t\t\t\"cursor next calls that skip due to a globally visible history store tombstone\" : 0,\n" +
                "\t\t\t\"cursor next calls that skip greater than or equal to 100 entries\" : 0,\n" +
                "\t\t\t\"cursor next calls that skip less than 100 entries\" : 1,\n" +
                "\t\t\t\"cursor prev calls that skip due to a globally visible history store tombstone\" : 0,\n" +
                "\t\t\t\"cursor prev calls that skip greater than or equal to 100 entries\" : 0,\n" +
                "\t\t\t\"cursor prev calls that skip less than 100 entries\" : 0,\n" +
                "\t\t\t\"insert calls\" : 0,\n" +
                "\t\t\t\"insert key and value bytes\" : 0,\n" +
                "\t\t\t\"modify\" : 0,\n" +
                "\t\t\t\"modify key and value bytes affected\" : 0,\n" +
                "\t\t\t\"modify value bytes modified\" : 0,\n" +
                "\t\t\t\"next calls\" : 1,\n" +
                "\t\t\t\"open cursor count\" : 0,\n" +
                "\t\t\t\"operation restarted\" : 0,\n" +
                "\t\t\t\"prev calls\" : 0,\n" +
                "\t\t\t\"remove calls\" : 0,\n" +
                "\t\t\t\"remove key bytes removed\" : 0,\n" +
                "\t\t\t\"reserve calls\" : 0,\n" +
                "\t\t\t\"reset calls\" : 2,\n" +
                "\t\t\t\"search calls\" : 0,\n" +
                "\t\t\t\"search history store calls\" : 0,\n" +
                "\t\t\t\"search near calls\" : 0,\n" +
                "\t\t\t\"truncate calls\" : 0,\n" +
                "\t\t\t\"update calls\" : 0,\n" +
                "\t\t\t\"update key and value bytes\" : 0,\n" +
                "\t\t\t\"update value size change\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"reconciliation\" : {\n" +
                "\t\t\t\"approximate byte size of timestamps in pages written\" : 0,\n" +
                "\t\t\t\"approximate byte size of transaction IDs in pages written\" : 0,\n" +
                "\t\t\t\"dictionary matches\" : 0,\n" +
                "\t\t\t\"fast-path pages deleted\" : 0,\n" +
                "\t\t\t\"internal page key bytes discarded using suffix compression\" : 0,\n" +
                "\t\t\t\"internal page multi-block writes\" : 0,\n" +
                "\t\t\t\"leaf page key bytes discarded using prefix compression\" : 0,\n" +
                "\t\t\t\"leaf page multi-block writes\" : 0,\n" +
                "\t\t\t\"leaf-page overflow keys\" : 0,\n" +
                "\t\t\t\"maximum blocks required for a page\" : 0,\n" +
                "\t\t\t\"overflow values written\" : 0,\n" +
                "\t\t\t\"page checksum matches\" : 0,\n" +
                "\t\t\t\"page reconciliation calls\" : 0,\n" +
                "\t\t\t\"page reconciliation calls for eviction\" : 0,\n" +
                "\t\t\t\"pages deleted\" : 0,\n" +
                "\t\t\t\"pages written including an aggregated newest start durable timestamp \" : 0,\n" +
                "\t\t\t\"pages written including an aggregated newest stop durable timestamp \" : 0,\n" +
                "\t\t\t\"pages written including an aggregated newest stop timestamp \" : 0,\n" +
                "\t\t\t\"pages written including an aggregated newest stop transaction ID\" : 0,\n" +
                "\t\t\t\"pages written including an aggregated newest transaction ID \" : 0,\n" +
                "\t\t\t\"pages written including an aggregated oldest start timestamp \" : 0,\n" +
                "\t\t\t\"pages written including an aggregated prepare\" : 0,\n" +
                "\t\t\t\"pages written including at least one prepare\" : 0,\n" +
                "\t\t\t\"pages written including at least one start durable timestamp\" : 0,\n" +
                "\t\t\t\"pages written including at least one start timestamp\" : 0,\n" +
                "\t\t\t\"pages written including at least one start transaction ID\" : 0,\n" +
                "\t\t\t\"pages written including at least one stop durable timestamp\" : 0,\n" +
                "\t\t\t\"pages written including at least one stop timestamp\" : 0,\n" +
                "\t\t\t\"pages written including at least one stop transaction ID\" : 0,\n" +
                "\t\t\t\"records written including a prepare\" : 0,\n" +
                "\t\t\t\"records written including a start durable timestamp\" : 0,\n" +
                "\t\t\t\"records written including a start timestamp\" : 0,\n" +
                "\t\t\t\"records written including a start transaction ID\" : 0,\n" +
                "\t\t\t\"records written including a stop durable timestamp\" : 0,\n" +
                "\t\t\t\"records written including a stop timestamp\" : 0,\n" +
                "\t\t\t\"records written including a stop transaction ID\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"session\" : {\n" +
                "\t\t\t\"object compaction\" : 0,\n" +
                "\t\t\t\"tiered operations dequeued and processed\" : 0,\n" +
                "\t\t\t\"tiered operations scheduled\" : 0,\n" +
                "\t\t\t\"tiered storage local retention time (secs)\" : 0\n" +
                "\t\t},\n" +
                "\t\t\"transaction\" : {\n" +
                "\t\t\t\"race to read prepared update retry\" : 0,\n" +
                "\t\t\t\"rollback to stable history store records with stop timestamps older than newer records\" : 0,\n" +
                "\t\t\t\"rollback to stable inconsistent checkpoint\" : 0,\n" +
                "\t\t\t\"rollback to stable keys removed\" : 0,\n" +
                "\t\t\t\"rollback to stable keys restored\" : 0,\n" +
                "\t\t\t\"rollback to stable restored tombstones from history store\" : 0,\n" +
                "\t\t\t\"rollback to stable restored updates from history store\" : 0,\n" +
                "\t\t\t\"rollback to stable skipping delete rle\" : 0,\n" +
                "\t\t\t\"rollback to stable skipping stable rle\" : 0,\n" +
                "\t\t\t\"rollback to stable sweeping history store keys\" : 0,\n" +
                "\t\t\t\"rollback to stable updates removed from history store\" : 0,\n" +
                "\t\t\t\"transaction checkpoints due to obsolete pages\" : 0,\n" +
                "\t\t\t\"update conflicts\" : 0\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"nindexes\" : 1,\n" +
                "\t\"indexDetails\" : {\n" +
                "\t\t\"_id_\" : {\n" +
                "\t\t\t\"metadata\" : {\n" +
                "\t\t\t\t\"formatVersion\" : 8\n" +
                "\t\t\t},\n" +
                "\t\t\t\"creationString\" : \"access_pattern_hint=none,allocation_size=4KB,app_metadata=(formatVersion=8),assert=(commit_timestamp=none,durable_timestamp=none,read_timestamp=none,write_timestamp=off),block_allocation=best,block_compressor=,cache_resident=false,checksum=on,colgroups=,collator=,columns=,dictionary=0,encryption=(keyid=,name=),exclusive=false,extractor=,format=btree,huffman_key=,huffman_value=,ignore_in_memory_cache_size=false,immutable=false,import=(enabled=false,file_metadata=,repair=false),internal_item_max=0,internal_key_max=0,internal_key_truncate=true,internal_page_max=16k,key_format=u,key_gap=10,leaf_item_max=0,leaf_key_max=0,leaf_page_max=16k,leaf_value_max=0,log=(enabled=true),lsm=(auto_throttle=true,bloom=true,bloom_bit_count=16,bloom_config=,bloom_hash_count=8,bloom_oldest=false,chunk_count_limit=0,chunk_max=5GB,chunk_size=10MB,merge_custom=(prefix=,start_generation=0,suffix=),merge_max=15,merge_min=0),memory_page_image_max=0,memory_page_max=5MB,os_cache_dirty_max=0,os_cache_max=0,prefix_compression=true,prefix_compression_min=4,readonly=false,source=,split_deepen_min_child=0,split_deepen_per_child=0,split_pct=90,tiered_object=false,tiered_storage=(auth_token=,bucket=,bucket_prefix=,cache_directory=,local_retention=300,name=),type=file,value_format=u,verbose=[],write_timestamp_usage=none\",\n" +
                "\t\t\t\"type\" : \"file\",\n" +
                "\t\t\t\"uri\" : \"statistics:table:index-75--2002033493638094686\",\n" +
                "\t\t\t\"LSM\" : {\n" +
                "\t\t\t\t\"bloom filter false positives\" : 0,\n" +
                "\t\t\t\t\"bloom filter hits\" : 0,\n" +
                "\t\t\t\t\"bloom filter misses\" : 0,\n" +
                "\t\t\t\t\"bloom filter pages evicted from cache\" : 0,\n" +
                "\t\t\t\t\"bloom filter pages read into cache\" : 0,\n" +
                "\t\t\t\t\"bloom filters in the LSM tree\" : 0,\n" +
                "\t\t\t\t\"chunks in the LSM tree\" : 0,\n" +
                "\t\t\t\t\"highest merge generation in the LSM tree\" : 0,\n" +
                "\t\t\t\t\"queries that could have benefited from a Bloom filter that did not exist\" : 0,\n" +
                "\t\t\t\t\"sleep for LSM checkpoint throttle\" : 0,\n" +
                "\t\t\t\t\"sleep for LSM merge throttle\" : 0,\n" +
                "\t\t\t\t\"total size of bloom filters\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"block-manager\" : {\n" +
                "\t\t\t\t\"allocations requiring file extension\" : 0,\n" +
                "\t\t\t\t\"blocks allocated\" : 0,\n" +
                "\t\t\t\t\"blocks freed\" : 0,\n" +
                "\t\t\t\t\"checkpoint size\" : 0,\n" +
                "\t\t\t\t\"file allocation unit size\" : 4096,\n" +
                "\t\t\t\t\"file bytes available for reuse\" : 0,\n" +
                "\t\t\t\t\"file magic number\" : 120897,\n" +
                "\t\t\t\t\"file major version number\" : 1,\n" +
                "\t\t\t\t\"file size in bytes\" : 4096,\n" +
                "\t\t\t\t\"minor version number\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"btree\" : {\n" +
                "\t\t\t\t\"btree checkpoint generation\" : 0,\n" +
                "\t\t\t\t\"btree clean tree checkpoint expiration time\" : 0,\n" +
                "\t\t\t\t\"btree compact pages reviewed\" : 0,\n" +
                "\t\t\t\t\"btree compact pages rewritten\" : 0,\n" +
                "\t\t\t\t\"btree compact pages skipped\" : 0,\n" +
                "\t\t\t\t\"btree skipped by compaction as process would not reduce size\" : 0,\n" +
                "\t\t\t\t\"column-store fixed-size leaf pages\" : 0,\n" +
                "\t\t\t\t\"column-store internal pages\" : 0,\n" +
                "\t\t\t\t\"column-store variable-size RLE encoded values\" : 0,\n" +
                "\t\t\t\t\"column-store variable-size deleted values\" : 0,\n" +
                "\t\t\t\t\"column-store variable-size leaf pages\" : 0,\n" +
                "\t\t\t\t\"fixed-record size\" : 0,\n" +
                "\t\t\t\t\"maximum internal page size\" : 16384,\n" +
                "\t\t\t\t\"maximum leaf page key size\" : 1474,\n" +
                "\t\t\t\t\"maximum leaf page size\" : 16384,\n" +
                "\t\t\t\t\"maximum leaf page value size\" : 7372,\n" +
                "\t\t\t\t\"maximum tree depth\" : 0,\n" +
                "\t\t\t\t\"number of key/value pairs\" : 0,\n" +
                "\t\t\t\t\"overflow pages\" : 0,\n" +
                "\t\t\t\t\"row-store empty values\" : 0,\n" +
                "\t\t\t\t\"row-store internal pages\" : 0,\n" +
                "\t\t\t\t\"row-store leaf pages\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"cache\" : {\n" +
                "\t\t\t\t\"bytes currently in the cache\" : 182,\n" +
                "\t\t\t\t\"bytes dirty in the cache cumulative\" : 0,\n" +
                "\t\t\t\t\"bytes read into cache\" : 0,\n" +
                "\t\t\t\t\"bytes written from cache\" : 0,\n" +
                "\t\t\t\t\"checkpoint blocked page eviction\" : 0,\n" +
                "\t\t\t\t\"checkpoint of history store file blocked non-history store page eviction\" : 0,\n" +
                "\t\t\t\t\"data source pages selected for eviction unable to be evicted\" : 0,\n" +
                "\t\t\t\t\"eviction gave up due to detecting an out of order on disk value behind the last update on the chain\" : 0,\n" +
                "\t\t\t\t\"eviction gave up due to detecting an out of order tombstone ahead of the selected on disk update\" : 0,\n" +
                "\t\t\t\t\"eviction gave up due to detecting an out of order tombstone ahead of the selected on disk update after validating the update chain\" : 0,\n" +
                "\t\t\t\t\"eviction gave up due to detecting out of order timestamps on the update chain after the selected on disk update\" : 0,\n" +
                "\t\t\t\t\"eviction walk passes of a file\" : 0,\n" +
                "\t\t\t\t\"eviction walk target pages histogram - 0-9\" : 0,\n" +
                "\t\t\t\t\"eviction walk target pages histogram - 10-31\" : 0,\n" +
                "\t\t\t\t\"eviction walk target pages histogram - 128 and higher\" : 0,\n" +
                "\t\t\t\t\"eviction walk target pages histogram - 32-63\" : 0,\n" +
                "\t\t\t\t\"eviction walk target pages histogram - 64-128\" : 0,\n" +
                "\t\t\t\t\"eviction walk target pages reduced due to history store cache pressure\" : 0,\n" +
                "\t\t\t\t\"eviction walks abandoned\" : 0,\n" +
                "\t\t\t\t\"eviction walks gave up because they restarted their walk twice\" : 0,\n" +
                "\t\t\t\t\"eviction walks gave up because they saw too many pages and found no candidates\" : 0,\n" +
                "\t\t\t\t\"eviction walks gave up because they saw too many pages and found too few candidates\" : 0,\n" +
                "\t\t\t\t\"eviction walks reached end of tree\" : 0,\n" +
                "\t\t\t\t\"eviction walks restarted\" : 0,\n" +
                "\t\t\t\t\"eviction walks started from root of tree\" : 0,\n" +
                "\t\t\t\t\"eviction walks started from saved location in tree\" : 0,\n" +
                "\t\t\t\t\"hazard pointer blocked page eviction\" : 0,\n" +
                "\t\t\t\t\"history store table insert calls\" : 0,\n" +
                "\t\t\t\t\"history store table insert calls that returned restart\" : 0,\n" +
                "\t\t\t\t\"history store table out-of-order resolved updates that lose their durable timestamp\" : 0,\n" +
                "\t\t\t\t\"history store table out-of-order updates that were fixed up by reinserting with the fixed timestamp\" : 0,\n" +
                "\t\t\t\t\"history store table reads\" : 0,\n" +
                "\t\t\t\t\"history store table reads missed\" : 0,\n" +
                "\t\t\t\t\"history store table reads requiring squashed modifies\" : 0,\n" +
                "\t\t\t\t\"history store table truncation by rollback to stable to remove an unstable update\" : 0,\n" +
                "\t\t\t\t\"history store table truncation by rollback to stable to remove an update\" : 0,\n" +
                "\t\t\t\t\"history store table truncation to remove an update\" : 0,\n" +
                "\t\t\t\t\"history store table truncation to remove range of updates due to key being removed from the data page during reconciliation\" : 0,\n" +
                "\t\t\t\t\"history store table truncation to remove range of updates due to out-of-order timestamp update on data page\" : 0,\n" +
                "\t\t\t\t\"history store table writes requiring squashed modifies\" : 0,\n" +
                "\t\t\t\t\"in-memory page passed criteria to be split\" : 0,\n" +
                "\t\t\t\t\"in-memory page splits\" : 0,\n" +
                "\t\t\t\t\"internal pages evicted\" : 0,\n" +
                "\t\t\t\t\"internal pages split during eviction\" : 0,\n" +
                "\t\t\t\t\"leaf pages split during eviction\" : 0,\n" +
                "\t\t\t\t\"modified pages evicted\" : 0,\n" +
                "\t\t\t\t\"overflow pages read into cache\" : 0,\n" +
                "\t\t\t\t\"page split during eviction deepened the tree\" : 0,\n" +
                "\t\t\t\t\"page written requiring history store records\" : 0,\n" +
                "\t\t\t\t\"pages read into cache\" : 0,\n" +
                "\t\t\t\t\"pages read into cache after truncate\" : 0,\n" +
                "\t\t\t\t\"pages read into cache after truncate in prepare state\" : 0,\n" +
                "\t\t\t\t\"pages requested from the cache\" : 0,\n" +
                "\t\t\t\t\"pages seen by eviction walk\" : 0,\n" +
                "\t\t\t\t\"pages written from cache\" : 0,\n" +
                "\t\t\t\t\"pages written requiring in-memory restoration\" : 0,\n" +
                "\t\t\t\t\"the number of times full update inserted to history store\" : 0,\n" +
                "\t\t\t\t\"the number of times reverse modify inserted to history store\" : 0,\n" +
                "\t\t\t\t\"tracked dirty bytes in the cache\" : 0,\n" +
                "\t\t\t\t\"unmodified pages evicted\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"cache_walk\" : {\n" +
                "\t\t\t\t\"Average difference between current eviction generation when the page was last considered\" : 0,\n" +
                "\t\t\t\t\"Average on-disk page image size seen\" : 0,\n" +
                "\t\t\t\t\"Average time in cache for pages that have been visited by the eviction server\" : 0,\n" +
                "\t\t\t\t\"Average time in cache for pages that have not been visited by the eviction server\" : 0,\n" +
                "\t\t\t\t\"Clean pages currently in cache\" : 0,\n" +
                "\t\t\t\t\"Current eviction generation\" : 0,\n" +
                "\t\t\t\t\"Dirty pages currently in cache\" : 0,\n" +
                "\t\t\t\t\"Entries in the root page\" : 0,\n" +
                "\t\t\t\t\"Internal pages currently in cache\" : 0,\n" +
                "\t\t\t\t\"Leaf pages currently in cache\" : 0,\n" +
                "\t\t\t\t\"Maximum difference between current eviction generation when the page was last considered\" : 0,\n" +
                "\t\t\t\t\"Maximum page size seen\" : 0,\n" +
                "\t\t\t\t\"Minimum on-disk page image size seen\" : 0,\n" +
                "\t\t\t\t\"Number of pages never visited by eviction server\" : 0,\n" +
                "\t\t\t\t\"On-disk page image sizes smaller than a single allocation unit\" : 0,\n" +
                "\t\t\t\t\"Pages created in memory and never written\" : 0,\n" +
                "\t\t\t\t\"Pages currently queued for eviction\" : 0,\n" +
                "\t\t\t\t\"Pages that could not be queued for eviction\" : 0,\n" +
                "\t\t\t\t\"Refs skipped during cache traversal\" : 0,\n" +
                "\t\t\t\t\"Size of the root page\" : 0,\n" +
                "\t\t\t\t\"Total number of pages currently in cache\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"checkpoint-cleanup\" : {\n" +
                "\t\t\t\t\"pages added for eviction\" : 0,\n" +
                "\t\t\t\t\"pages removed\" : 0,\n" +
                "\t\t\t\t\"pages skipped during tree walk\" : 0,\n" +
                "\t\t\t\t\"pages visited\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"compression\" : {\n" +
                "\t\t\t\t\"compressed page maximum internal page size prior to compression\" : 16384,\n" +
                "\t\t\t\t\"compressed page maximum leaf page size prior to compression \" : 16384,\n" +
                "\t\t\t\t\"compressed pages read\" : 0,\n" +
                "\t\t\t\t\"compressed pages written\" : 0,\n" +
                "\t\t\t\t\"number of blocks with compress ratio greater than 64\" : 0,\n" +
                "\t\t\t\t\"number of blocks with compress ratio smaller than 16\" : 0,\n" +
                "\t\t\t\t\"number of blocks with compress ratio smaller than 2\" : 0,\n" +
                "\t\t\t\t\"number of blocks with compress ratio smaller than 32\" : 0,\n" +
                "\t\t\t\t\"number of blocks with compress ratio smaller than 4\" : 0,\n" +
                "\t\t\t\t\"number of blocks with compress ratio smaller than 64\" : 0,\n" +
                "\t\t\t\t\"number of blocks with compress ratio smaller than 8\" : 0,\n" +
                "\t\t\t\t\"page written failed to compress\" : 0,\n" +
                "\t\t\t\t\"page written was too small to compress\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"cursor\" : {\n" +
                "\t\t\t\t\"Total number of entries skipped by cursor next calls\" : 0,\n" +
                "\t\t\t\t\"Total number of entries skipped by cursor prev calls\" : 0,\n" +
                "\t\t\t\t\"Total number of entries skipped to position the history store cursor\" : 0,\n" +
                "\t\t\t\t\"Total number of times a search near has exited due to prefix config\" : 0,\n" +
                "\t\t\t\t\"bulk loaded cursor insert calls\" : 0,\n" +
                "\t\t\t\t\"cache cursors reuse count\" : 0,\n" +
                "\t\t\t\t\"close calls that result in cache\" : 0,\n" +
                "\t\t\t\t\"create calls\" : 0,\n" +
                "\t\t\t\t\"cursor next calls that skip due to a globally visible history store tombstone\" : 0,\n" +
                "\t\t\t\t\"cursor next calls that skip greater than or equal to 100 entries\" : 0,\n" +
                "\t\t\t\t\"cursor next calls that skip less than 100 entries\" : 0,\n" +
                "\t\t\t\t\"cursor prev calls that skip due to a globally visible history store tombstone\" : 0,\n" +
                "\t\t\t\t\"cursor prev calls that skip greater than or equal to 100 entries\" : 0,\n" +
                "\t\t\t\t\"cursor prev calls that skip less than 100 entries\" : 0,\n" +
                "\t\t\t\t\"insert calls\" : 0,\n" +
                "\t\t\t\t\"insert key and value bytes\" : 0,\n" +
                "\t\t\t\t\"modify\" : 0,\n" +
                "\t\t\t\t\"modify key and value bytes affected\" : 0,\n" +
                "\t\t\t\t\"modify value bytes modified\" : 0,\n" +
                "\t\t\t\t\"next calls\" : 0,\n" +
                "\t\t\t\t\"open cursor count\" : 0,\n" +
                "\t\t\t\t\"operation restarted\" : 0,\n" +
                "\t\t\t\t\"prev calls\" : 0,\n" +
                "\t\t\t\t\"remove calls\" : 0,\n" +
                "\t\t\t\t\"remove key bytes removed\" : 0,\n" +
                "\t\t\t\t\"reserve calls\" : 0,\n" +
                "\t\t\t\t\"reset calls\" : 0,\n" +
                "\t\t\t\t\"search calls\" : 0,\n" +
                "\t\t\t\t\"search history store calls\" : 0,\n" +
                "\t\t\t\t\"search near calls\" : 0,\n" +
                "\t\t\t\t\"truncate calls\" : 0,\n" +
                "\t\t\t\t\"update calls\" : 0,\n" +
                "\t\t\t\t\"update key and value bytes\" : 0,\n" +
                "\t\t\t\t\"update value size change\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"reconciliation\" : {\n" +
                "\t\t\t\t\"approximate byte size of timestamps in pages written\" : 0,\n" +
                "\t\t\t\t\"approximate byte size of transaction IDs in pages written\" : 0,\n" +
                "\t\t\t\t\"dictionary matches\" : 0,\n" +
                "\t\t\t\t\"fast-path pages deleted\" : 0,\n" +
                "\t\t\t\t\"internal page key bytes discarded using suffix compression\" : 0,\n" +
                "\t\t\t\t\"internal page multi-block writes\" : 0,\n" +
                "\t\t\t\t\"leaf page key bytes discarded using prefix compression\" : 0,\n" +
                "\t\t\t\t\"leaf page multi-block writes\" : 0,\n" +
                "\t\t\t\t\"leaf-page overflow keys\" : 0,\n" +
                "\t\t\t\t\"maximum blocks required for a page\" : 0,\n" +
                "\t\t\t\t\"overflow values written\" : 0,\n" +
                "\t\t\t\t\"page checksum matches\" : 0,\n" +
                "\t\t\t\t\"page reconciliation calls\" : 0,\n" +
                "\t\t\t\t\"page reconciliation calls for eviction\" : 0,\n" +
                "\t\t\t\t\"pages deleted\" : 0,\n" +
                "\t\t\t\t\"pages written including an aggregated newest start durable timestamp \" : 0,\n" +
                "\t\t\t\t\"pages written including an aggregated newest stop durable timestamp \" : 0,\n" +
                "\t\t\t\t\"pages written including an aggregated newest stop timestamp \" : 0,\n" +
                "\t\t\t\t\"pages written including an aggregated newest stop transaction ID\" : 0,\n" +
                "\t\t\t\t\"pages written including an aggregated newest transaction ID \" : 0,\n" +
                "\t\t\t\t\"pages written including an aggregated oldest start timestamp \" : 0,\n" +
                "\t\t\t\t\"pages written including an aggregated prepare\" : 0,\n" +
                "\t\t\t\t\"pages written including at least one prepare\" : 0,\n" +
                "\t\t\t\t\"pages written including at least one start durable timestamp\" : 0,\n" +
                "\t\t\t\t\"pages written including at least one start timestamp\" : 0,\n" +
                "\t\t\t\t\"pages written including at least one start transaction ID\" : 0,\n" +
                "\t\t\t\t\"pages written including at least one stop durable timestamp\" : 0,\n" +
                "\t\t\t\t\"pages written including at least one stop timestamp\" : 0,\n" +
                "\t\t\t\t\"pages written including at least one stop transaction ID\" : 0,\n" +
                "\t\t\t\t\"records written including a prepare\" : 0,\n" +
                "\t\t\t\t\"records written including a start durable timestamp\" : 0,\n" +
                "\t\t\t\t\"records written including a start timestamp\" : 0,\n" +
                "\t\t\t\t\"records written including a start transaction ID\" : 0,\n" +
                "\t\t\t\t\"records written including a stop durable timestamp\" : 0,\n" +
                "\t\t\t\t\"records written including a stop timestamp\" : 0,\n" +
                "\t\t\t\t\"records written including a stop transaction ID\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"session\" : {\n" +
                "\t\t\t\t\"object compaction\" : 0,\n" +
                "\t\t\t\t\"tiered operations dequeued and processed\" : 0,\n" +
                "\t\t\t\t\"tiered operations scheduled\" : 0,\n" +
                "\t\t\t\t\"tiered storage local retention time (secs)\" : 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"transaction\" : {\n" +
                "\t\t\t\t\"race to read prepared update retry\" : 0,\n" +
                "\t\t\t\t\"rollback to stable history store records with stop timestamps older than newer records\" : 0,\n" +
                "\t\t\t\t\"rollback to stable inconsistent checkpoint\" : 0,\n" +
                "\t\t\t\t\"rollback to stable keys removed\" : 0,\n" +
                "\t\t\t\t\"rollback to stable keys restored\" : 0,\n" +
                "\t\t\t\t\"rollback to stable restored tombstones from history store\" : 0,\n" +
                "\t\t\t\t\"rollback to stable restored updates from history store\" : 0,\n" +
                "\t\t\t\t\"rollback to stable skipping delete rle\" : 0,\n" +
                "\t\t\t\t\"rollback to stable skipping stable rle\" : 0,\n" +
                "\t\t\t\t\"rollback to stable sweeping history store keys\" : 0,\n" +
                "\t\t\t\t\"rollback to stable updates removed from history store\" : 0,\n" +
                "\t\t\t\t\"transaction checkpoints due to obsolete pages\" : 0,\n" +
                "\t\t\t\t\"update conflicts\" : 0\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"indexBuilds\" : [ ],\n" +
                "\t\"totalIndexSize\" : 4,\n" +
                "\t\"totalSize\" : 8,\n" +
                "\t\"indexSizes\" : {\n" +
                "\t\t\"_id_\" : 4\n" +
                "\t},\n" +
                "\t\"scaleFactor\" : 1024,\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result1,document1);
    }

    @After
    public void dropCollection(){
        mars.dropCollection("book");
    }
}
