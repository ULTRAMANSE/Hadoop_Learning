package com.immoc.bigdata.hadoop.mr.wc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.net.URI;

/**
 * 使用MR统计HDFS上文件对应的词频，并使用combiner函数来简化输出
 * Driver:配置Mapper，Reducer相关属性
 */
public class WordCountCombinerApp {
    public static void main(String[] args)throws Exception {

        System.setProperty("HADOOP_USER_NAME","hadoop");
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS","hdfs://192.168.5.14:8020");

        //创建一个Job
        Job job = Job.getInstance(configuration);

        //设置Job对应的参数:主类
        job.setJarByClass(WordCountCombinerApp.class);

        //设置Job对应参数：设置自定义的Mapper和Reducer处理类
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);


        //添加Combiner的设置，实际上就是一次Reducer操作
        //Combiner的优点，能够减少IO，提升作业实行效率
        //缺点，不适合求平均操作
        job.setCombinerClass(WordCountReducer.class);

        //设置Job对应参数：Mapper输出key和value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        //设置Job对应参数：Reducer输出key和value类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //如果输出目录已经存在，则先删除
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://192.168.5.14:8020"),configuration,"hadoop");
        Path outputPath = new Path("/wordcount/out");
        if(fileSystem.exists(outputPath)){
            fileSystem.delete(outputPath,true);
        }

        //设置Job对应的参数：Mapper输出key和value类型：作业输入和输出路径
        FileInputFormat.setInputPaths(job,new Path("/wordcount/input"));
        FileOutputFormat.setOutputPath(job,new Path("/wordcount/output"));
        
        //提交job
        boolean result = job.waitForCompletion(true);

        System.exit(result ? 0 : -1);
    }
}
