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
 *     （3）剩下就是HDFS API的操作
 */
public class HDFSApp {
    public static void main(String args[])throws Exception{

        //Configuration对象封装了客户端或服务器端的配置，通过设置配置文件读取类路径来实现（conf/core-site.xml）
        Configuration configuration = new Configuration();

        /*  要对文件进行操作，要使用FileSystem类，这是一个抽象的基类，继承了Configured类
            get()方法返回一个已经创建的文件系统，这里肯定是HDFS了，get()方法有三种传入参数
            如果这里使用的是虚拟机，此处hadoop000应该写成地址
        */
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://hadoop000:8020"),configuration,"Hadoop");

        //设置路径地址，使得在此路径上创建一个目录
        Path path = new Path("/hdfsapi/test");

        //测试，创建一个目录
        boolean result = fileSystem.mkdirs(path);
        System.out.println(result);
    }
}

//输出结果为真时，在Hadoop下查看是否存在此文件夹

