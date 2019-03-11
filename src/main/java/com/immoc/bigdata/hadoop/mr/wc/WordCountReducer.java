package com.immoc.bigdata.hadoop.mr.wc;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    /**
     * （hello，1） （word，1）
     * （hello，1） （word，1）
     * （hello，1） （word，1）
     * （welcome，1）
     *
     * map的输出到rudeuce端，是按照相同的key分发到一个reduce上执行
     * reduce1：(hello,1)(hello,1)(hello，1）==>(hello,<1,1,1>)
     * reduce2：（word，1）（word，1）（word，1）==>(word,<1,1,1>)
     * reduce3:(welcome,1) ==>(welcome,<1>)
     *
     * @param key
     * @param values
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int count = 0;

        Iterator<IntWritable>iterator = values.iterator();//迭代

        //输出<1,1,1>
        while(iterator.hasNext()){
            IntWritable value = iterator.next();
            count+=value.get();
        }

        context.write(key,new IntWritable(count));
    }
}
