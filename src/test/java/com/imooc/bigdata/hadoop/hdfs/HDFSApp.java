package com.imooc.bigdata.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

/**
 * 使用Java API操作HDFS文件系统
 * 关键点：
 *     （1）创建Configurtion，因为FileSystem需要
 *     （2）需要操作HDFS，就必须获取Filesystem作为入口
 *     （3）就是HDFS API的操作
 */
public class HDFSApp {
    public static void main(String args[])throws Exception{

        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://hadoop000:8020"),configuration,"Hadoop");//如果这里使用的是虚拟机，此处应该写成地址
        Path path = new Path("/hdfsapi/test");
        boolean result = fileSystem.mkdirs(path);
        System.out.println(result);
    }
}

//输出结果为真时，在Hadoop下查看是否存在此文件夹

