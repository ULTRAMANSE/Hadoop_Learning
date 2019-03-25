package com.immoc.bigdata.hadoop.mr.project.mr;

import com.immoc.bigdata.hadoop.mr.project.utils.IPParser;
import com.immoc.bigdata.hadoop.mr.project.utils.LogParser;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Map;

public class ProvinceStatApp {
    static class MyMapper extends Mapper<LongWritable, Text,Text,LongWritable>{
        private LongWritable ONE = new LongWritable(1);

        private LogParser logParser;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            logParser = new LogParser();
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String log = value.toString();

            Map<String,String>infos = logParser.parse(log);

            String ip = infos.get("ip");
            if(StringUtils.isNotBlank(ip)){
                IPParser.RegionInfo regionInfo = IPParser.getInstance().analyseIp(ip);
                if (regionInfo!=null){
                    String province = regionInfo.getProvince();
                    if(StringUtils.isNotBlank(province)){
                        context.write(new Text(province),ONE);
                    }else {
                        context.write(new Text("-"), ONE);
                    }
                }else {
                    context.write(new Text("-"),ONE);
                }
            }else{
                context.write(new Text("-"),ONE);
            }
        }


    }
    static class MyReducer extends Reducer<Text,LongWritable,Text,LongWritable>{
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long count = 0;
            for (LongWritable value : values){
                count ++ ;
            }
            context.write(key,new LongWritable(count));
        }
    }
    public static void main(String[] args)throws Exception {
        System.setProperty("hadoop.home.dir","E:/hadoop-2.6.0-cdh5.15.1");
        Configuration configuration = new Configuration();

        // 如果输出目录已经存在，则先删除
        FileSystem fileSystem = FileSystem.get(configuration);
        Path outputPath = new Path("output/v1/Provincestat");

        if(fileSystem.exists(outputPath)) {
            fileSystem.delete(outputPath,true);
        }

        Job job = Job.getInstance(configuration);
        job.setJarByClass(ProvinceStatApp.class);

        job.setMapperClass(ProvinceStatApp.MyMapper.class);
        job.setReducerClass(ProvinceStatApp.MyReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        FileInputFormat.setInputPaths(job, new Path("E:/Users/zyx/IdeaProjects/hadoop-train-v2/Input/raw/trackinfo_20130721.data"));
        FileOutputFormat.setOutputPath(job, new Path("output/v1/Provincestat"));

        job.waitForCompletion(true);
    }
}
