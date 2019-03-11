package com.immoc.bigdata.hadoop.mr.access;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;


public class AccessLocalApp {

    // Driver端的代码：八股文
    public static void main(String[] args) throws Exception{
        System.setProperty("hadoop.home.dir","E:/hadoop-2.6.0-cdh5.15.1");
        Configuration configuration = new Configuration();

        Job job = Job.getInstance(configuration);
        job.setJarByClass(AccessLocalApp.class);

        job.setMapperClass(AccessMapper.class);
        job.setReducerClass(AccessReducer.class);
        //如果输出目录已经存在，则先删除
        File file = new File("access/output");
        if(file.isDirectory()){
            try{
                FileUtils.deleteDirectory(file);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

         //设置自定义分区规则
        job.setPartitionerClass(AccessPartition.class);
        // 设置reduce个数
        job.setNumReduceTasks(3);


        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Access.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Access.class);

        //        FileInputFormat.setInputPaths(job, new Path(args[0]));
        //        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        FileInputFormat.setInputPaths(job, new Path("access/input"));
        FileOutputFormat.setOutputPath(job, new Path("access/output"));

        //提交job
        boolean result = job.waitForCompletion(true);

        System.exit(result ? 0 : -1);
    }

}

