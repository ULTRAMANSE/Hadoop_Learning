package com.imooc.bigdata.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;

/**
 * 使用Java API操作HDFS文件系统
 * 关键点：
 *     （1）创建Configurtion，因为FileSystem需要
 *     （2）需要操作HDFS，就必须获取Filesystem作为入口
 *     （3）剩下就是HDFS API的操作
 */
public class HDFSApp {

    public static final String HDFS_PATH = "hdfs://192.168.5.14:8020";
    FileSystem fileSystem = null;


    //这里设置成空，那么就会自己加载Maven下载的API中的配置文件，hdfs-default.xml文件中，副本文件默认为3
    Configuration configuration = null;
    //因此需要重新自己定义一下，在对象new了之后，进行设置


    /*
        因为此类写在测试包中，因此将原有代码更改为Junit风格
     */
    @Before
    public void setUp()throws  Exception{
        System.out.println("setUP方法------");

        Configuration configuration = new Configuration();
        configuration.set("dfs.replication","1");
        /**
         *  构造一个访问指定HDFS系统的客户端对象
         *  第一个参数：HDFS的URI
         *  第二个参数：客户端指定的配置文件
         *  第三个参数：客户端身份，即用户名
         */
        fileSystem =FileSystem.get(new URI(HDFS_PATH),configuration,"hadoop");
    }

    /**
     *  创建HDFS文件夹
     * */
    @Test
    public void mkdir()throws  Exception{
        fileSystem.mkdirs(new Path("/hdfsapi/test"));
    }
    /**
     * 查看HDFS文件
     * */
    @Test
    public void text()throws Exception{
        FSDataInputStream in = fileSystem.open(new Path("/cdh_version.properties"));
        IOUtils.copyBytes(in,System.out,1024);//输出
    }
    /**
     * 创建一个文件,进行写入
     * **/
    @Test
    public void create() throws  Exception{
        FSDataOutputStream out = fileSystem.create(new Path("/hdfsapi/test/a.txt"));
        out.writeUTF("Hello World!");//写入
        out.flush();//内容在缓冲区，需要刷新一下
        out.close();
    }
    /**
     * 将文件更名
    */
     @Test
    public void rename()throws  Exception{
        Path oldPath = new Path("/hdfsapi/test/a.txt");
        Path newPath = new Path("/hdfsapi/test/b.txt");
        boolean result =  fileSystem.rename(oldPath,newPath);
        System.out.println(result);
    }
    /**
     * 拷贝小文件，从本地拷贝到远端HDFS文件系统
    */
    @Test
    public void copyFromLocalFile()throws Exception{
        Path src = new Path("/User/zyx/Desktop/A.java");//本地
        Path dst = new Path("/hdfsapi/test");//远端地址
        fileSystem.copyFromLocalFile(src,dst);
    }
    /**
     * 拷贝大文件，从本地拷贝到远端HDFS文件系统，带一个进度条
     */
    @Test
    public void copyFromLocalBigFile()throws Exception{
        // 将文件进行读取，使用流
        InputStream in = new BufferedInputStream(new FileInputStream(new File("/Users/zyx/Downloads/CentOS-6.10-x86_64-bin-DVD1.iso")));
        FSDataOutputStream out = fileSystem.create(new Path("/hdfsapi/test/CentOS6.iso"), new Progressable() {
            @Override
            public void progress() {
                System.out.println(".");
            }
        });
        IOUtils.copyBytes(in , out , 4096);
    }
    /**
     * 下载：
     * 拷贝文件，从远端HDFS文件系统到本地，带一个进度条
     */
    @Test
    public void copyToLocalFile()throws Exception{
        Path src = new Path("/Users/zyx/Downloads");//本地
        Path dst = new Path("/hdfsapi/test/b.txt");//远端地址
        fileSystem.copyToLocalFile(src,dst);
    }
    /**
     * 列出指定文件夹下所有文件，并判断是文件夹还是文件
     */
    @Test
    public void listFiles()throws Exception{
        FileStatus[] statues = fileSystem.listStatus(new Path("/hdfsapi/test"));

        for (FileStatus fileStatus : statues ){
            String isDir = fileStatus.isDirectory()?"文件夹":"文件";
            String permission = fileStatus.getPermission().toString();
            short replication = fileStatus.getReplication();
            long length = fileStatus.getLen();
            String path = fileStatus.getPath().toString();

            System.out.println(isDir+"\t"+permission+"\t"+replication+"\t"+
                            length+"\t"+path
                    );
        }
    }
    /**
     * 递归列出指定文件夹下所有文件，并判断是文件夹还是文件，此方法可以将文件夹下的子目录也列出来
     */
    @Test
    public void listFilesRecursive()throws Exception{
        RemoteIterator<LocatedFileStatus> files = fileSystem.listFiles(new Path("/hdfsapi/test"),true);
        while(files.hasNext()){
            LocatedFileStatus file = files.next();
            String isDir = file.isDirectory()?"文件夹":"文件";
            String permission = file.getPermission().toString();
            short replication = file.getReplication();
            long length = file.getLen();
            String path = file.getPath().toString();
            System.out.println(isDir+"\t"+permission+"\t"+replication+"\t"+
                    length+"\t"+path
            );
        }
    }
    /**
     * 查看块信息
     */
    @Test
    public void getFileBlockLocations()throws Exception{
        FileStatus fileStatus = fileSystem.getFileStatus(new Path("hdfsapi/test/jdk.tgz"));

        BlockLocation[] blocks = fileSystem.getFileBlockLocations(fileStatus,0,fileStatus.getLen());
        for (BlockLocation blockLocation : blocks){
            for (String name : blockLocation.getNames()){
                System.out.println(name+":"+blockLocation.getOffset()+":"+blockLocation.getLength());
            }
        }
    }

    /**
     * 删除文件
     */
    @Test
    public void delete()throws Exception{
        boolean result = fileSystem.delete(new Path("hdfsapi/test/jdk.tgz"),true);
        System.out.println(result);
    }

    @After
    public void tearDown(){
        configuration = null;
        fileSystem = null;
        System.out.println("--------tearDown--------");
    }



//    public static void main(String args[])throws Exception{
//
//        //Configuration对象封装了客户端或服务器端的配置，通过设置配置文件读取类路径来实现（conf/core-site.xml）
//        Configuration configuration = new Configuration();
//
//        /*  要对文件进行操作，要使用FileSystem类，这是一个抽象的基类，继承了Configured类
//            get()方法返回一个已经创建的文件系统，这里肯定是HDFS了，get()方法有三种传入参数
//            如果这里使用的是虚拟机，此处hadoop000应该写成地址
//        */
//        FileSystem fileSystem = FileSystem.get(new URI("hdfs://hadoop000:8020"),configuration,"Hadoop");
//
//        //设置路径地址，使得在此路径上创建一个目录
//        Path path = new Path("/hdfsapi/test");
//
//        //测试，创建一个目录
//        boolean result = fileSystem.mkdirs(path);
//        System.out.println(result);
//    }
}

//输出结果为真时，在Hadoop下查看是否存在此文件夹

