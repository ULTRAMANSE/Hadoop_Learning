package com.immoc.bigdata.hadoop.mr.project.mr2;

import com.immoc.bigdata.hadoop.mr.project.utils.LogParser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class PVStatV2App {

    // Driver端的代码：八股文
    public static void main(String[] args) throws Exception{
        Configuration configuration = new Configuration();

        Job job = Job.getInstance(configuration);
        job.setJarByClass(PVStatV2App.class);

        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        FileInputFormat.setInputPaths(job, new Path("input/etl"));
        FileOutputFormat.setOutputPath(job, new Path("output/v2/pvstat/"));

        job.waitForCompletion(true);
    }

    static class MyMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

        private LogParser parser;
        private LongWritable ONE = new LongWritable(1);
        private Text KEY = new Text("key");

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            parser = new LogParser();
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            context.write(KEY, ONE);
        }
    }

    static class MyReducer extends Reducer<Text, LongWritable, NullWritable, LongWritable> {

        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {

            long count = 0;
            for(LongWritable access : values) {
                count++;
            }
            context.write(NullWritable.get(), new LongWritable(count));

        }
    }

}



