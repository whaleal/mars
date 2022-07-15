package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 15:54
 * FileName: BuildInfoTest
 * Description:
 */
public class BuildInfoTest {
    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void testForBuildInfo(){
        Document document = mars.executeCommand(new Document().append("buildInfo",1));
        System.out.println(document);
        Document result = Document.parse("{\n" +
                "\t\"version\" : \"5.0.9\",\n" +
                "\t\"gitVersion\" : \"6f7dae919422dcd7f4892c10ff20cdc721ad00e6\",\n" +
                "\t\"modules\" : [ ],\n" +
                "\t\"allocator\" : \"tcmalloc\",\n" +
                "\t\"javascriptEngine\" : \"mozjs\",\n" +
                "\t\"sysInfo\" : \"deprecated\",\n" +
                "\t\"versionArray\" : [\n" +
                "\t\t5,\n" +
                "\t\t0,\n" +
                "\t\t9,\n" +
                "\t\t0\n" +
                "\t],\n" +
                "\t\"openssl\" : {\n" +
                "\t\t\"running\" : \"OpenSSL 1.1.1 FIPS  11 Sep 2018\",\n" +
                "\t\t\"compiled\" : \"OpenSSL 1.1.1 FIPS  11 Sep 2018\"\n" +
                "\t},\n" +
                "\t\"buildEnvironment\" : {\n" +
                "\t\t\"distmod\" : \"rhel80\",\n" +
                "\t\t\"distarch\" : \"x86_64\",\n" +
                "\t\t\"cc\" : \"/opt/mongodbtoolchain/v3/bin/gcc: gcc (GCC) 8.5.0\",\n" +
                "\t\t\"ccflags\" : \"-Werror -include mongo/platform/basic.h -fasynchronous-unwind-tables -ggdb -Wall -Wsign-compare -Wno-unknown-pragmas -Winvalid-pch -fno-omit-frame-pointer -fno-strict-aliasing -O2 -march=sandybridge -mtune=generic -mprefer-vector-width=128 -Wno-unused-local-typedefs -Wno-unused-function -Wno-deprecated-declarations -Wno-unused-const-variable -Wno-unused-but-set-variable -Wno-missing-braces -fstack-protector-strong -Wa,--nocompress-debug-sections -fno-builtin-memcmp\",\n" +
                "\t\t\"cxx\" : \"/opt/mongodbtoolchain/v3/bin/g++: g++ (GCC) 8.5.0\",\n" +
                "\t\t\"cxxflags\" : \"-Woverloaded-virtual -Wno-maybe-uninitialized -fsized-deallocation -std=c++17\",\n" +
                "\t\t\"linkflags\" : \"-Wl,--fatal-warnings -pthread -Wl,-z,now -fuse-ld=gold -fstack-protector-strong -Wl,--no-threads -Wl,--build-id -Wl,--hash-style=gnu -Wl,-z,noexecstack -Wl,--warn-execstack -Wl,-z,relro -Wl,--compress-debug-sections=none -Wl,-z,origin -Wl,--enable-new-dtags\",\n" +
                "\t\t\"target_arch\" : \"x86_64\",\n" +
                "\t\t\"target_os\" : \"linux\",\n" +
                "\t\t\"cppdefines\" : \"SAFEINT_USE_INTRINSICS 0 PCRE_STATIC NDEBUG _XOPEN_SOURCE 700 _GNU_SOURCE _FORTIFY_SOURCE 2 BOOST_THREAD_VERSION 5 BOOST_THREAD_USES_DATETIME BOOST_SYSTEM_NO_DEPRECATED BOOST_MATH_NO_LONG_DOUBLE_MATH_FUNCTIONS BOOST_ENABLE_ASSERT_DEBUG_HANDLER BOOST_LOG_NO_SHORTHAND_NAMES BOOST_LOG_USE_NATIVE_SYSLOG BOOST_LOG_WITHOUT_THREAD_ATTR ABSL_FORCE_ALIGNED_ACCESS\"\n" +
                "\t},\n" +
                "\t\"bits\" : 64,\n" +
                "\t\"debug\" : false,\n" +
                "\t\"maxBsonObjectSize\" : 16777216,\n" +
                "\t\"storageEngines\" : [\n" +
                "\t\t\"devnull\",\n" +
                "\t\t\"ephemeralForTest\",\n" +
                "\t\t\"wiredTiger\"\n" +
                "\t],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }
}
