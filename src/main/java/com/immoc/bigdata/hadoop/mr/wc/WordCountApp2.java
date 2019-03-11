package com.immoc.bigdata.hadoop.mr.wc;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;

/**
 * 使用MR统计本地文件对应的词频
 * Driver:配置Mapper，Reducer相关属性
 * 提交到本地运行：开发过程中使用
 * 统计结果输出到本地
 */
public class WordCountApp2 {
    public static void main(String[] args)throws Exception {

        System.setProperty("hadoop.home.dir","E:/hadoop-2.6.0-cdh5.15.1");
        Configuration configuration = new Configuration();

        //创建一个Job
        Job job = Job.getInstance(configuration);

        //设置Job对应的参数:主类
        job.setJarByClass(WordCountApp2.class);

        //设置Job对应参数：设置自定义的Mapper和Reducer处理类
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        //设置Job对应参数：Mapper输出key和value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        //设置Job对应参数：Reducer输出key和value类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //如果输出目录已经存在，则先删除
        File file = new File("output");
        if(file.isDirectory()){
            try{
                FileUtils.deleteDirectory(file);
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        //设置Job对应的参数：Mapper输出key和value类型：作业输入和输出路径
        FileInputFormat.setInputPaths(job,new Path("input/wc.input"));
        // 运行一次后，再次运行时，会发生已存在报错，添加上面的代码
        FileOutputFormat.setOutputPath(job,new Path("output"));

        //提交job
        boolean result = job.waitForCompletion(true);

        System.exit(result ? 0 : -1);
    }
}
