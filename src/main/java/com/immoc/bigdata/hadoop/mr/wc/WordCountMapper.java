package com.immoc.bigdata.hadoop.mr.wc;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * KEYIN:Mapp任务读数据key的类型，offset，每行数据起始位置的偏移量，long
 * VALUEIN：Map任务读取数据的value类型，其实就是一行行字符串，String
 *
 * KEYOUT：map方法自定义实现输出的key类型String
 * VALUEOUT:map方法自定义输出的value类型Integer
 *
 * Long,String,String,Integer是Java数据类型，而Hadoop支持序列化和反序列化
 */
public class WordCountMapper extends Mapper<LongWritable, Text,Text, IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //把value对应的行数据按照指定分隔符分开
        String[] words = value.toString().split("\t");

        for (String word : words){
            context.write(new Text(word.toLowerCase()),new IntWritable(1));
        }
    }
}
