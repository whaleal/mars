package com.whaleal.mars.monitor;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-16 16:02
 **/
public class ReplSetGetStatusTest {


    @Test
    public void testFor(){
//        Mars mars = new Mars(Constant.connectionStr);
        Mars mars = new Mars("mongodb://192.168.3.100:47001/mars");

        ReplSetGetStatusMetrics replSetGetStatusMetrics = new ReplSetGetStatusMetrics(mars.getMongoClient());
        System.out.println(replSetGetStatusMetrics.getDate());
        System.out.println(replSetGetStatusMetrics.getTerm());
        System.out.println(replSetGetStatusMetrics.getClusterTime());
        System.out.println(replSetGetStatusMetrics.getMyState());
        System.out.println(replSetGetStatusMetrics.getSetName());
        System.out.println(replSetGetStatusMetrics.getElectionCandidateMetrics());
        System.out.println(replSetGetStatusMetrics.getElectionParticipantMetrics());
        System.out.println(replSetGetStatusMetrics.getHeartBeatInterval());
        System.out.println(replSetGetStatusMetrics.getMembers());
        System.out.println(replSetGetStatusMetrics.getMajorityVoteCount());
        System.out.println(replSetGetStatusMetrics.getOPTimes());
        System.out.println(replSetGetStatusMetrics.getSyncSourceHost());
        System.out.println(replSetGetStatusMetrics.getSyncSourceId());
        System.out.println(replSetGetStatusMetrics.getVotingMembersCount());
        System.out.println(replSetGetStatusMetrics.getWritableVotingMembersCount());
        System.out.println(replSetGetStatusMetrics.getWriteMajorityCount());

    }

}
