package com.immoc.bigdata.hadoop.mr.access;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * MapReduce自定义分区规则
 */
public class AccessPartition extends Partitioner<Text,Access> {
    /**
     *
     * @param phone   手机号
     * @param access
     * @return
     */
    @Override
    public int getPartition(Text phone, Access access, int numReduceTask) {

        if (phone.toString().startsWith("13")){
            return 0;
        }else if (phone.toString().startsWith("15")){
            return 1;
        }else{
            return 2;
        }
    }
}

