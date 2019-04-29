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
 *
 * 关键点：
 * 1）创建Configuration
 * 2）获取FileSystem
 * 3）...就是你的HDFS API的操作
 */
public class HDFSApp {
    
    /**在操作HDFS之前，我们需要对三个参数进行定义。
    1.HDFS所在路径，我们要对HDFS进行操作，因此必须找到其对应的路径上
     2.因为是对文件系统操作，因此我们需要File类，但是这个类必须是由Hadoop提供，有区别于Java自己的File操作类。
     这个操作类，需要接收到文件系统所在地址，，接收到configuration对象的参数，hadoop所在Linux中的用户名称
     3.其次，我们需要定义一个Configure对象，这个对象也是由Hadoop提供的,需要用来设置数据备份数量。
     在IDEA开发中，如果不设置，会默认定义为3个。因为，IDEA中导入的hadoop-client包中有一个默认的设置是3.
     **/
    public static final String PATH = "hdfs://192.168.227.14:8020";
    //用来设置参数
    Configuration configuration = null;
    //用来针对hdfs进行文件操作，即访问HDFS的客户端对象
    FileSystem fileSystem = null;

    @Before
    public void setUp() throws  Exception{

        System.out.println("-------setup--------");

        //通过configuration来设置hdfs文件备份数量
        configuration = new Configuration();
        configuration.set("dfs.replication","1");

        fileSystem = FileSystem.get(new URI(PATH),configuration,"hadoop");
    }

    /**
     * 通过filesystem对象来操作HDFS.
     * mkdir()方法用来在HDFS中创建一个文件夹，此方法中需要设置文件夹创建的路径
     * 这个文件夹里如果为空，那么在Linux中的使用hadoop fs -ls，是无法显示这个文件夹的存在
     * 但是可以通过浏览器进行地址访问，能够查看到这个空的文件夹
     *
     */
    @Test
    public void mkdir() throws  Exception{
        fileSystem.mkdirs(new Path("/hdfsapi/test/zyx"));
    }

    /**
     * 查看hdfs中内容，使用数据流对文件进行读取
     */
    @Test
    public void read() throws Exception{
        FSDataInputStream inputStream = fileSystem.open(new Path("/cdh_version.properties"));
        IOUtils.copyBytes(inputStream,System.out,1024);
    }

    /**
     * 创建文件
     */
    @Test
    public void create()throws Exception {
//        FSDataOutputStream out = fileSystem.create(new Path("/hdfsapi/test/a.txt"));
        FSDataOutputStream out = fileSystem.create(new Path("/hdfsapi/test/b.txt"));
        out.writeUTF("hello pk: replication 1");
        out.flush();
        out.close();
    }

    /**
     * 测试文件名更改
     * @throws Exception
     */
    @Test
    public void rename() throws Exception {
        Path oldPath = new Path("/hdfsapi/test/b.txt");
        Path newPath = new Path("/hdfsapi/test/c.txt");
        boolean result = fileSystem.rename(oldPath, newPath);
        System.out.println(result);

    }


    /**
     * 拷贝本地文件到HDFS文件系统
     */
    @Test
    public void copyFromLocalFile() throws Exception {
        Path src = new Path("C:/Users/zyx/Downloads/hello.txt");
        Path dst = new Path("/hdfsapi/test/");
        fileSystem.copyFromLocalFile(src,dst);
    }

    /**
     * 拷贝大文件到HDFS文件系统：带进度
     */
    @Test
    public void copyFromLocalBigFile() throws Exception {

        InputStream in = new BufferedInputStream(new FileInputStream(new File("C:/Users/zyx/Downloads/jdk-8u201-linux-x64.rpm")));

        FSDataOutputStream out = fileSystem.create(new Path("/hdfsapi/test/jdk.tgz"),
                new Progressable() {
                    public void progress() {
                        System.out.print(".");
                    }
                });

        IOUtils.copyBytes(in, out ,4096);

    }

    /**
     * 拷贝HDFS文件到本地：下载
     */
    @Test
    public void copyToLocalFile() throws Exception {
        Path src = new Path("/hdfsapi/test/hello.txt");
        Path dst = new Path("C:/Users/zyx/Documents");
        fileSystem.copyToLocalFile(src, dst);
    }


    /**
     * 查看目标文件夹下的所有文件
     */
    @Test
    public void listFiles() throws Exception {
        FileStatus[] statuses = fileSystem.listStatus(new Path("/hdfsapi/test"));

        for(FileStatus file : statuses) {
            String isDir = file.isDirectory() ? "文件夹" : "文件";
            String permission = file.getPermission().toString();
            short replication = file.getReplication();
            long length = file.getLen();
            String path = file.getPath().toString();


            System.out.println(isDir + "\t" + permission
                    + "\t" + replication + "\t" + length
                    + "\t" + path
            );
        }

    }


    /**
     * 递归查看目标文件夹下的所有文件
     */
    @Test
    public void listFilesRecursive() throws Exception {

        RemoteIterator<LocatedFileStatus> files = fileSystem.listFiles(new Path("/hdfsapi/test"), true);

        while (files.hasNext()) {
            LocatedFileStatus file = files.next();
            String isDir = file.isDirectory() ? "文件夹" : "文件";
            String permission = file.getPermission().toString();
            short replication = file.getReplication();
            long length = file.getLen();
            String path = file.getPath().toString();


            System.out.println(isDir + "\t" + permission
                    + "\t" + replication + "\t" + length
                    + "\t" + path
            );
        }
    }


    /**
     * 查看文件块信息
     */
    @Test
    public void getFileBlockLocations() throws Exception {

        FileStatus fileStatus = fileSystem.getFileStatus(new Path("/hdfsapi/test/jdk.tgz"));
        BlockLocation[] blocks = fileSystem.getFileBlockLocations(fileStatus,0,fileStatus.getLen());

        for(BlockLocation block : blocks) {

            for(String name: block.getNames()) {
                System.out.println(name +" : " + block.getOffset() + " : " + block.getLength() + " : " + block.getHosts());
            }
        }
    }

    /**
     * 删除文件
     */
    @Test
    public void delete() throws Exception {
        boolean result = fileSystem.delete(new Path("/hdfsapi/test/jdk.tgz"), true);
        System.out.println(result);
    }


    @Test
    public void testReplication() {
        System.out.println(configuration.get("dfs.replication"));
    }


    @After
    public void tearDown() {
        configuration = null;
        fileSystem = null;
        System.out.println("--------tearDown---------");
    }


//    public static void main(String[] args) throws Exception {
//
//        Configuration configuration = new Configuration();
//        FileSystem fileSystem = FileSystem.get(new URI("hdfs://hadoop000:8020"), configuration, "hadoop");
//
//        Path path = new Path("/hdfsapi/test");
//        boolean result = fileSystem.mkdirs(path);
//        System.out.println(result);
//    }

}
