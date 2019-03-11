package com.immoc.bigdata.hadoop.mr.access;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 */
public class AccessReducer extends Reducer<Text, Access, NullWritable, Access>{


    /**
     * @param key 手机号
     * @param values <Access,Access>
     */
    @Override
    protected void reduce(Text key, Iterable<Access> values, Context context) throws IOException, InterruptedException {

        long ups = 0;
        long downs = 0;

        for(Access access : values) {
            ups += access.getUp();
            downs += access.getDown();
        }


        context.write(NullWritable.get(), new Access(key.toString(), ups, downs));
    }
}
