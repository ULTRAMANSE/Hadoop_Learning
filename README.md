# Hadoop学习


#### 一、大数据生态的概述
1. 大数据技术主要围绕着4个核心问题：存储、计算、查询、挖掘。
* 存储要使用的技术有：HDFS、Kafka
* 计算要使用的技术有：MapReduce、Spark、Storm、Flink
* 查询要使用的技术有：Hbase
* 挖掘：主要应用数据挖掘的算法
2. 形象描述
假设，这里将整个大数据生态比作一个江湖。天下大帮派之一的Google流传出三部心法，分别是：
*   《Google file system》：论述了怎样借助普通机器有效的存储海量的大数据；
*   《Google MapReduce》：论述了怎样快速计算海量的数据；
*   《Google BigTable》：论述了怎样实现海量数据的快速查询；

随后，致力于天下武学开放的Apache帮会，将三部心法习得并且加以改造，形成了一部武学巨著《Hadoop》。这部武学巨著因从Google心法而来，因此包括三个招式，分别是HDFS（解决海量技术的存储）、MapReduce（解决海量数据的计算）、Hbase（解决海量数据的查询），Apache将《Hadoop》发给各个帮派研读，在众多帮派的支持下，《Hadoop》不断进化，其中变化最大的当属计算和查询两个招式。
* 传统数据仓库门派说MapReduce招式太难练，我们门派SQL一把梭，照样打遍天下，于是降低难度，修成Hive、pig等Sql on hadoop的简化秘籍。
* 伯克利派说MapReduce太着重花招，内力没法施展，于是推出了基于内力的《Spark》，这里的内力指的就是内存。
* 流式门派认为Hadoop只能憋大招（批量计算），再放大招前，只能用HDFS不停地吸收别人的攻击，于是推出SparkStreaming、Storm等技术，能够实现，即时性发招。
* 这时Apache看到各大门派都不断提升了自己的名气，急了眼，推出了Flink，想要统一流失计算和批量计算。
3. Hbase和Hive的区别与联系：<br>
**_区别：_**<br>
* Hbase： Hadoop database 的简称，也就是基于Hadoop数据库，是一种NoSQL数据库，主要适用于海量明细数据（十亿、百亿）的随机实时查询，如日志明细、交易清单、轨迹行为等。
* Hive：Hive是Hadoop数据仓库，严格来说，不是数据库，主要是让开发人员能够通过SQL来计算和处理HDFS上的结构化数据，适用于离线的批量数据计算。
*   通过元数据来描述Hdfs上的结构化文本数据，通俗点来说，就是定义一张表来描述HDFS上的结构化文本，包括各列数据名称，数据类型是什么等，方便我们处理数据，当前很多SQL ON Hadoop的计算引擎均用的是hive的元数据，如Spark SQL、Impala等；
*   基于第一点，通过SQL来处理和计算HDFS的数据，Hive会将SQL翻译为Mapreduce来处理数据；<br>
**_关系：_**<br>
Hbase和Hive通常是协作关系：
1.  通过ETL工具将数据源抽取到HDFS存储；
2.  通过Hive清洗、处理和计算原始数据；
3.  HIve清洗处理后的结果，如果是面向海量数据随机查询场景的可存入Hbase
4.  数据应用从HBase查询数据；

#### 二、学习资料
学习框架时请直接查阅官网信息，官网上也有一些坑，因此在遇到问题时，先百度查看别人博客。

Apache社区的顶级项目：xxxx.apache.org
* hadoop.apache.org
* hive.apache.org
* hbase.apache.org
* spark.apache.org
* flink.apache.org
* storm.apache.org

下面提供一下本仓库的环境，这是一个虚拟机环境，CentOS、Hadoop、JDK均已配置好，下载后，需要修改一些配置。<br>
[虚拟机镜像](https://pan.baidu.com/s/1kl7T3qdNBGbdkqFXZ-Wh1A)<br>
提取码：6e24<br>
* 常用的Hadoop发行版
    * Apache优点：纯开源缺点：不同版本/不同框架之间整合 jar冲突... 吐血
    * CDH：https://www.cloudera.com/   市场占比60-70%。优点：cm(cloudera manager) 通过页面一键安装各种框架、升级、impala。缺点：cm不开源、与社区版本有些许出入
    * Hortonworks：HDP  企业发布自己的数据平台可以直接基于页面框架进行改造。优点：原装Hadoop、纯开源、支持tez。缺点：企业级安全不开源
    * MapR

**Hadoop环境搭建**<br>
	使用的Hadoop相关版本：CDH <br>
	CDH相关软件包下载地址：http://archive.cloudera.com/cdh5/cdh/5/<br>
	Hadoop使用版本：hadoop-2.6.0-cdh5.15.1<br>
	Hadoop下载：wget http://archive.cloudera.com/cdh5/cdh/5/hadoop-2.6.0-cdh5.15.1.tar.gz<br>
	Hive使用版本：hive-1.1.0-cdh5.15.1<br>

**Hadoop安装前置要求**<br>
	Java  1.8+<br>
	ssh<br>

**安装Java**<br>
	拷贝本地软件包到服务器：scp jdk-8u91-linux-x64.tar.gz hadoop@192.168.199.233:~/software/<br>
	解压jdk到~/app/：tar -zvxf jdk-8u91-linux-x64.tar.gz -C ~/app/<br>
	把jdk配置系统环境变量中： ~/.bash_profile<br>
		export JAVA_HOME=/home/hadoop/app/jdk1.8.0_91<br>
		export PATH=$JAVA_HOME/bin:$PATH<br>
	使得配置修改生效：source .bash_profile<br>
	验证：java -version<br>

**安装ssh无密码登陆**<br>

```xml
	ls 
	ls -a
	ls -la  并没有发现一个.ssh的文件夹

	ssh-keygen -t rsa  一路回车
	cd ~/.ssh
	[hadoop@hadoop000 .ssh]$ ll
	总用量 12
	-rw------- 1 hadoop hadoop 1679 10月 15 02:54 id_rsa  私钥
	-rw-r--r-- 1 hadoop hadoop  398 10月 15 02:54 id_rsa.pub 公钥
	-rw-r--r-- 1 hadoop hadoop  358 10月 15 02:54 known_hosts

	cat id_rsa.pub >> authorized_keys
	chmod 600 authorized_keys  
```


#### 三、本仓库的环境搭建以及Apache Hadoop的搭建流程
**_提供的镜像安装：_**
1. 本项目主要是用CentOS7系统，CDH5.15.1（Hadoop发行版），IDEA，上一节提供了镜像，下载解压后可以运行<br>
2. CDH5.15.1:https://www.cloudera.com/ 优点：通过页面可以一键安装各种框架，升级。缺点：与社区版本有些出入。
3. 环境的使用
* ifconfig  查看自己的网络，此时是无法上网的
* sudo -i  切换到root权限
* cd /etc/sysconfig/network-scripts  进入到此目录
* rm ifcfg-lo  删除ifcfg-lo文件
* ip addr  在2：ens33中有一个link/ether地址串将其copy
* vi  ifcfg-eth0   更改HWADDR的值为刚才复制的值<br>
![网络配置](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/resource/%E7%BD%91%E7%BB%9C%E9%85%8D%E7%BD%AE.PNG)<br>
* 解压hadoop安装包（CDH压缩包）
* 配置hadoop的核心文件 hadoop-env.sh，core-site.xml , mapred-site.xml ， hdfs-site.xml
* 配置hadoop环境变量
* 格式化 hadoop namenode-format
* 启动节点start-all.sh

<br>

**_Apache Hadoop的安装步骤：_**
* 使用root账户登录
* 安装JDK(1.8版本以上)
* 修改IP   （下面的步骤参照上面的图片）
* 修改host主机名
* 配置SSH免密码登录
* 关闭防火墙
* 解压hadoop安装包（在Apache官网下载）
* 配置hadoop的核心文件 hadoop-env.sh，core-site.xml , mapred-site.xml ， hdfs-site.xml
* 配置hadoop环境变量
* 格式化 hadoop namenode-format
* 启动节点start-all.sh

<br>
**_其他问题_**
* 首先从教学linux中将hadoop文件进行压缩，即（右键compress），然后将其复制粘贴到windows盘上，最好在根目录下，解压缩。
* 在仓库中下载winutils.exe，将这个文件放到刚才你解压缩后的hadoop/bin目录下。
* 然后在wordcountapp2中添加一句话System.setProperty("hadoop.home.dir","E:/hadoop-2.6.0-cdh5.15.1");我这里是E盘。
* 这个空指针问题解决后，会有一个nativeIO问题，从仓库中下载Native.java，在项目中创建一个包（package），名字最好取成org.apache.hadoop.io.nativeio，然后将Native.java文件粘贴进去。


#### 四、Hadoop简介

Hadoop：提供分布式的存储（一个文件被拆分成很多个块，并且以副本的方式存储在各个节点中）和计算，是一个分布式的系统基础架构：用户可以在不了解分布式底层细节的情况下进行使用。去IoE(IBM、Orcale、EMC）。<br>
![Hadoop](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/resource/Hadoop.PNG)<br>
分布式文件系统：HDFS实现将文件分布式存储在很多的服务器上<br>
分布式计算框架：MapReduce实现在很多机器上分布式并行计算<br>
分布式资源调度框架：YARN实现集群资源管理以及作业的调度<br>
<br>
hadoop软件包常见目录说明<br>
	bin：hadoop客户端名单<br>
	etc/hadoop：hadoop相关的配置文件存放目录<br>
	sbin：启动hadoop相关进程的脚本<br><br>
	share：常用例子<br>

#### 五、HDFS（存储）
源自Google的GFS论文，HDFS是GFS的克隆版，特点：扩展性高，容错性强，存储量大，由Java开发。
<br>
**_工作机制_**<br>
将文件切分成指定大小的_块文件_并以_多副本_方式存储在机器上。例如：
* 假设有一个文件：test.log  200M
* 分块(block)：默认的blocksize是128M， 2个块 = 1个128M + 1个72M
* 副本：HDFS默认3副本
* 分布情况小例子：
  * node1：blk1  blk2  X  ，如果此节点挂掉，其他节点依然存在分块<br>
  * node2：blk2<br>
  * node3：blk1  blk2<br>
  * node4：<br>
  * node5：blk1<br>
<br>
**_分布式文件系统_**<br>
文件系统：windows、Mac、Linux<br>
具有目录结构，存放的是文件或者文件夹，对外提供：创建，修改，删除，查看，移动等等<br>
<br>
**_设计目的_**<br>
* 应对硬件错误(Hardware Failure)，每个机器只存储文件的部分数据，blocksize=128M，block存放在不同机器，为了容错，默认备份数为3份。<br>
* 应对批处理，流失数据访问，不适合实时处理。<br>
* 数据集大，以GB、TB为主。<br>
* 移动计算比移动数据更划算。（移动数据成本比较高）<br>
<br>
**_HDFS架构_**<br>
* HDFS是一个master/slave架构，一个HDFS集群有一个NameNode和多个DataNodes。<br>
* NameNode：管理 file system namespace，给客户端提供文件访问。   <br>
* DataNodes：负责数据存储。<br>
* 一个文件被拆分成多个block，这些block会存储到不同的DataNode，就是为了容错。NameNode可以提供增删改查的操作<br>
*   a.txt  150M   blocksize=128M。<br>
a.txt 拆分成2个block ，一个是block1：128M ，另一个是block2：22M。<br>
block1存放在哪个DN？block2存放在哪个DN？<br>
a.txt   block1：128M, 192.168.199.1<br>
         block2：22M ,  192.168.199.2<br>
get a.txt<br>
这个过程对于用户来说是不感知的。<br>

![HDFS架构](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/resource/HDFS%E6%9E%B6%E6%9E%84.PNG)


**_副本机制_**<br>
将一个文件切分，除了最后一个块的大小不一样，其他块大小都是一样的。副本数默认为3，但是可以更改。<br>
![副本机制](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/resource/%E5%89%AF%E6%9C%AC%E6%9C%BA%E5%88%B6.PNG)
<br>
**Hadoop(HDFS)安装**<br>
	下载<br>
	解压：~/app<br>
	添加HADOOP_HOME/bin到系统环境变量<br>
	修改Hadoop配置文件<br>

```xml
    hadoop-env.sh
			export JAVA_HOME=/home/hadoop/app/jdk1.8.0_91

		core-site.xml
			<property>
			    <name>fs.defaultFS</name>
			    <value>hdfs://hadoop000:8020</value>
			</property>

		hdfs-site.xml
			<property>
			    <name>dfs.replication</name>
			    <value>1</value>
			</property>
		
			<property>
			    <name>hadoop.tmp.dir</name>
			    <value>/home/hadoop/app/tmp</value>
			</property>

		slaves
			hadoop000
```




**启动HDFS：**
<br>
		第一次执行的时候一定要格式化文件系统，不要重复执行: hdfs namenode -format<br>
		启动集群：$HADOOP_HOME/sbin/start-dfs.sh<br>
    也可以单独启动DataNode和NameNode，hadoop-daemons.sh start namenode /  hadoop-daemons.sh start datanode<br>
		验证:<br>
			[hadoop@hadoop000 sbin]$ jps<br>
			60002 DataNode<br>
			60171 SecondaryNameNode<br>
			59870 NameNode<br>
<br>
			浏览器http://192.168.199.233:50070<br>
			如果发现jps ok，但是浏览器不OK？ 十有八九是防火墙问题<br>
			查看防火墙状态：sudo firewall-cmd --state<br>
			关闭防火墙: sudo systemctl stop firewalld.service<br>
			禁止防火墙开机启动：<br>
<br>
**HDFS命令行操作**<br>
与shell相似<br>

hadoop fs [generic options]<br>
	[-appendToFile <localsrc> ... <dst>]<br>
	[-cat [-ignoreCrc] <src> ...]<br>
	[-chgrp [-R] GROUP PATH...]<br>
	[-chmod [-R] <MODE[,MODE]... | OCTALMODE> PATH...]<br>
	[-chown [-R] [OWNER][:[GROUP]] PATH...]<br>
	[-copyFromLocal [-f] [-p] [-l] <localsrc> ... <dst>]<br>
	[-copyToLocal [-p] [-ignoreCrc] [-crc] <src> ... <localdst>]<br>
	[-count [-q] [-h] [-v] [-x] <path> ...]<br>
	[-cp [-f] [-p | -p[topax]] <src> ... <dst>]<br>
	[-df [-h] [<path> ...]]<br>
	[-du [-s] [-h] [-x] <path> ...]<br>
	[-get [-p] [-ignoreCrc] [-crc] <src> ... <localdst>]<br>
	[-getmerge [-nl] <src> <localdst>]<br>
	[-ls [-C] [-d] [-h] [-q] [-R] [-t] [-S] [-r] [-u] [<path> ...]]<br>
	[-mkdir [-p] <path> ...]<br>
	[-moveFromLocal <localsrc> ... <dst>]<br>
	[-moveToLocal <src> <localdst>]<br>
	[-mv <src> ... <dst>]<br>
	[-put [-f] [-p] [-l] <localsrc> ... <dst>]<br>
	[-rm [-f] [-r|-R] [-skipTrash] <src> ...]<br>
	[-rmdir [--ignore-fail-on-non-empty] <dir> ...]<br>
	[-text [-ignoreCrc] <src> ...]<br>

**_hadoop常用命令：_**

hadoop fs -ls / -------------------------显示当前目录下的所有文件 -R层层循出文件夹<br>
hadoop fs -put   -------------------------将文件上传到HDFS<br> 
hadoop fs -copyFromLocal-------------------------将文件从本地拷贝到HDFS，但是源文件还在<br> 
hadoop fs -moveFromLocal -------------------------将文件从本地拷贝到HDFS，但是源文件不存在<br>
hadoop fs -cat -------------------------查看文件<br>
hadoop fs -text -------------------------将文本文件或某些格式的非文本文件通过文本格式输出<br>
hadoop fs -get -------------------------复制文件到本地，可以忽略crc校验<br>
hadoop fs -mkdir <br>
hadoop fs -mv   -------------------------目标文件不能存在，否则命令不能执行，相当于给文件重命名并保存，源文件不存在<br>
hadoop fs -getmerge  -------------------------将hdfs指定目录下所有文件排序后合并到local指定的文件中，文件不存在时会自动创建，文件存在时会覆盖里面的内容<br>
hadoop fs -rm    -------------------------创建文件夹 后跟-p 可以创建不存在的父路径<br>
hadoop fs -rmdir<br>
hadoop fs -rm -r<br>

**HDFS存储扩展：**
	put方法: 1个file ==>拆分成 1...n block ==> 存放在不同的节点上的<br>
	get方法: 去NameNode上查找这个file对应的元数据信息<br>
<br>
**使用HDFS API的方式来操作HDFS文件系统**<br>
	IDEA/Eclipse<br>
	Java <br>
	使用Maven来管理项目[pom.xml](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/pom.xml)<br>
		拷贝jar包<br>

**客户端向HDFS写数据流程**
![HDFS写数据流程](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/resource/HDFS%E5%86%99%E6%95%B0%E6%8D%AE.PNG)
<br>

**客户端向HDFS读取数据流程**
![HDFS读取数据流程](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/resource/HDFS%E8%AF%BB%E5%8F%96%E6%95%B0%E6%8D%AE.PNG)
<br>

**HDFS元数据管理**
* 元数据：HDFS的目录结构以及每个文件的BLOCK信息(id，副本系数、block存放在哪个DN上)
* 存在什么地方：对应配置 ${hadoop.tmp.dir}/name/......
* 元数据存放在文件中

#### 六、使用Java API操作HDFS进行实战
1. HDFS API编程之jUnit封装实现增删改查[HDFSApp.java](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/test/java/com/imooc/bigdata/hadoop/hdfs/HDFSApp.java)<br>
2. 使用HDFS Java API完成HDFS文件系统上的文件的词频统计，仅仅只能使用HDFS API 完成<br>
 分析：
 
```
/**
 * 使用HDFS API完成wordcount统计
 *
 * 需求：统计HDFS上的文件的wc，然后将统计结果输出到HDFS
 *
 * 功能拆解：
 * 1）读取HDFS上的文件 ==> HDFS API
 * 2）业务处理(词频统计)：对文件中的每一行数据都要进行业务处理（按照分隔符分割） ==> Mapper
 * 3）将处理结果缓存起来   ==> Context
 * 4）将结果输出到HDFS ==> HDFS API
 *
 */
```

<br>

* [HDFSWCApp1](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/imooc/bigdata/hadoop/hdfs/HDFSWCApp1.java)<br>
* [ImoocContext](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/imooc/bigdata/hadoop/hdfs/ImoocContext.java)<br>
* [ImoocMapper](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/imooc/bigdata/hadoop/hdfs/ImoocMapper.java)<br>
* [WordCountMapper](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/imooc/bigdata/hadoop/hdfs/WordCountMapper.java)<br>

使用自定义配置文件重构代码：上述的代码称为硬编码，我们应让其具有可重用性，因此进行重构。<br>
* [wc.properties](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/resources/wc.properties)<br>
* [Constants](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/imooc/bigdata/hadoop/hdfs/Constants.java)<br>
* [ParamsUtils.java](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/imooc/bigdata/hadoop/hdfs/ParamsUtils.java)<br>
* [CaseIgnoreWordCountMapper.java](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/imooc/bigdata/hadoop/hdfs/CaseIgnoreWordCountMapper.java)<br>

#### 七、MapReduce（计算）
源自Google的MapReduce论文，MapReduce是Google MapReduce的克隆版，特点：扩展性高，容错性强，海量离线数据处理。

**MapReduce编程模型的详解**

**_例子：_**
![wordcount例子](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/resource/wordCount%E4%BE%8B%E5%AD%90.PNG)
<br>
1. 步骤：<br>
* 准备map处理的输入数据
* Map阶段将每一行读取的数据拆开，并且为每个单词出现的此数赋值。
* Shuffling阶段，将map阶段中各个任务进行总结，将相同的单词归并在一起。
* Reduce阶段，将Shuffling阶段的各个部分进行合并，统计次数。
![MP处理图示](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/resource/MP%E5%A4%84%E7%90%86%E5%9B%BE%E7%A4%BA.PNG)
<br>

**_核心_**
* Split
* InputFormat
* OutputFormat
* Combiner
* Partitioner

**_实战1，词频统计_**<br>

* 词频统计，相同单词的次数统计[WordCountMapper](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/wc/WordCountMapper.java)
* 对Map阶段的内容进行统计[WordCountReducer](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/wc/WordCountReducer.java)
* Job的编写[wordCountApp](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/wc/WordCountApp.java)
* 本地Job的编写[WordCountApp2](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/wc/WordCountApp2.java)
使用Combiner来减少IO，提升作业的执行性能<br>
局限性就是求平均数的时候。<br>
![Combiner示意图](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/resource/Combiner.PNG)<br>
* Cobiner编写[WordCountApp2](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/wc/WordCountCombinerApp.java)

<br>


**_实战2，流量统计_**<br>
1. 日志文件：https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/access/Input/access.log
2. 需求：统计上行流量和、下行流量和、总流量和
3. Access.java：手机号、上行流量、下行流量、总流量
4. 根据手机号进行分组，然后把该手机号对应的上行流量和下行流量加起来
5. Mapper：把手机号、上行流量、下行流量 拆开，把手机号作为key，把Access作为Value写出去
6. Reducer：（1372623888，<Access,Access>）

自定义复杂数据类型：[Access](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/access/Access.java)<br>

自定义Mapper：[AccessMapper](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/access/AccessMapper.java)<br>

自定义Reducer：[AccessReducer](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/access/AccessReducer.java)<br>

Job：[AccessLocalApp](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/access/AccessLocalApp.java)<br>

[AccessPartition](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/access/AccessPartition.java) <br>

#### 八、YARN（调度）
YARN：Yet Another Resource Negotiator<br>
负责整个集群资源的管理和调度。特点：扩展性高，容错性强，多资源统一调度。
![YARN](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/resource/YARN.PNG)

ARN产生背景<br>
	MapReduce1.x ==> MapReduce2.x<br>
		master/slave : JobTracker/TaskTracker<br>
		JobTracker：单点、压力大<br>
		仅仅只能够支持mapreduce作业<br>

	资源利用率<br>
		所有的计算框架运行在一个集群中，共享一个集群的资源，按需分配！<br>

<br>
master: resource management：ResourceManager (RM)<br>
job scheduling/monitoring：per-application ApplicationMaster (AM)<br>
slave: NodeManager (NM)<br>


YARN架构<br>
	Client、ResourceManager、NodeManager、ApplicationMaster<br>
	master/slave: RM/NM<br>


<br>
Client: 向RM提交任务、杀死任务等<br>
ApplicationMaster：<br>
	每个应用程序对应一个AM<br>
	AM向RM申请资源用于在NM上启动对应的Task<br>
	数据切分<br>
	为每个task向RM申请资源（container）<br>
	NodeManager通信<br>
	任务的监控<br>

NodeManager： 多个<br>
	干活<br>
	向RM发送心跳信息、任务的执行情况<br>
	接收来自RM的请求来启动任务<br>
	处理来自AM的命令<br>

ResourceManager:集群中同一时刻对外提供服务的只有1个，负责资源相关<br>
	处理来自客户端的请求：提交、杀死<br>
	启动/监控AM<br>
	监控NM<br>
	资源相关<br>

container：任务的运行抽象<br>
	memory、cpu....<br>
	task是运行在container里面的<br>
	可以运行am、也可以运行map/reduce task<br>



将上一节的流量统计案例使用YARN来发布：<br>
提交自己开发的MR作业到YARN上运行的步骤：<br>
1）使用IDEA将项目打包，打成一个JAR包
2）使用Xshell将jar包(项目根目录/target/...jar)以及测试数据上传到服务器<br>
3) 把数据上传到HDFS<br>
	hadoop fs -put xxx hdfspath<br>
4) 执行作业<br>
	hadoop jar xxx.jar 完整的类名(包名+类名) 数据文件输入地址  数据文件执行完后的输出地址<br>.	
5) 到YARN UI(8088) 上去观察作业的运行情况<br>
6）到输出目录去查看对应的输出结果<br>

[AccessYARNApp](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/access/AccessYARNApp.java) 
<br>


#### 九、电商项目实战————用户行为日志分析

**什么是用户行为日志：**<br>
	用户的每一次访问的行为(访问、搜索)产生的日志<br>
	比如电商网站从历史订单    ==>历史行为数据 <br>
				==>再进行推荐<br>
				==>就是为了增加平台的订单量/率<br>


本项目中的日志样图，因为此日志文件过大，放不到github上，因此现在只显示一部分<br>

![日志文件](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/resource/%E6%97%A5%E5%BF%97.PNG)
<br>
**原始日志字段说明:**(本项目中主要要用到的字段)
	第二个字段：url<br>
	第十四字段：ip<br>
	第十八字段：time<br>

==> 字段的解析<br>
	ip => 地市：国家、省份、城市<br>
	url => 页面ID<br>

	referer<br>

**项目需求：**<br>
统计页面的浏览量<br>
统计各个省份的浏览量<br>
统计页面的访问量<br>
<br>

**整个项目的数据处理流程：**<br>
![流程图](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/resource/%E6%95%B0%E6%8D%AE%E5%A4%84%E7%90%86%E6%B5%81%E7%A8%8B.PNG)
<br>

**第一个版本**
页面浏览量统计功能：[PVStatApp](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/project/mr/PVStatApp.java)将一行数据做成一个固定的key，value值为1<br>

**统计各个省份的浏览量**<br>
select province count(1) from xxx group by province;<br>
ip如何转换成地市信息，使用一个开源工具[IP地址解析工具包](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/project/utils/IPParser.java)<br>
自定义日志文件解析类[LogParser](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/project/utils/LogParser.java)
<br>
开发MapReduce：
[ProvinceStatApp](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/project/mr/ProvinceStatApp.java)


**统计页面浏览量**<br>
把符合规则的pageID获取到，然后进行统计<br>
[PageStatApp](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/project/mr/PageStatApp.java)

存在的问题，每个MR作业都去全量读取待处理的原始日志，如果数据量很大，那么性能就会降低。<br>
使用ETL将原始数据进行相应的处理，解析相应的数据，去除一些不需要的字段<br>
[ETLApp](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/project/mr2/ETLApp.java) 
将上面的需求的实现进行改版：<br>
[PVStatV2App](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/project/mr2/PVStatV2App.java) <br>
[ProvinceStatV2App](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/project/mr2/ProvinceStatV2App.java) <br>
[PageStatV2App](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/main/java/com/immoc/bigdata/hadoop/mr/project/mr2/PageStatV2App.java)<br>

可以使用Sqoop把HDFS上统计结果导出MySQL上面<br>

#### 十、Hive数据仓库

**Hive产生背景**
wordcount案例中使用MR会写大量的代码，这非常的麻烦，不利于效率，传统web开发人员也需要使用SQL来直接操作。<br>
HDFS上的文件并没有schema的概念<br>
	schema？就是关系型数据库中有数据库名，表名和字段这些数据。而HDFS就是普通的文本，使用Hive可对HDFS使用SQL操作，有很大的便捷性<br>

**hive是什么**
有FaceBook开源，用于解决结构化日志数据统计，可进行读写和管理。构建在Hadoop之上的数据仓库。Hive提供SQL查询语句：HQL。<br>
Hive底层执行引擎支持：MapReduce/Spark（在haoop2.x默认为Spark）/Tez<br>

**为什么要使用Hive**
1. 简单、容易上手，为超大数据集设计的计算/扩展能力
2. 提供统一元数据管理：
	* Hive数据是存放在HDFS
	* 元数据信息(记录数据的数据)是存放在MySQL中
	* SQL on Hadoop： Hive、Spark SQL、impala....

**Hive体系架构**
	* client：shell、thrift/jdbc(server/jdbc)、WebUI(HUE/Zeppelin)
	* metastore：==> MySQL
		* database：name、location、owner....
		* table：name、location、owner、column name/type ....
![Hive架构](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/resource/Hive%E6%9E%B6%E6%9E%84.PNG)

**Hive部署**
	1）下载
	2）解压到~/app
	3）添加HIVE_HOME到系统环境变量,source 让文件生效./bash_profile。echo HIVE_HOME。
	4）修改配置
		hive-env.sh
		hive-site.xml
	5) 拷贝MySQL驱动包到$HIVE_HOME/lib
	6) 前提是要准备安装一个MySQL数据库，yum install去安装一个MySQL数据库[Mysql安装](https://www.cnblogs.com/julyme/p/5969626.html)
<br>
![Hive部署架构](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/resource/Hive%E9%83%A8%E7%BD%B2%E6%9E%B6%E6%9E%84.PNG)

```xml

<?xml version="1.0"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>

<configuration>
<property>
  <name>javax.jdo.option.ConnectionURL</name>
  <value>jdbc:mysql://hadoop000:3306/hadoop_hive?createDatabaseIfNotExist=true</value>
</property>

<property>
  <name>javax.jdo.option.ConnectionDriverName</name>
  <value>com.mysql.jdbc.Driver</value>
</property>

<property>
  <name>javax.jdo.option.ConnectionUserName</name>
  <value>root</value>
</property>

<property>
  <name>javax.jdo.option.ConnectionPassword</name>
  <value>root</value>
</property>
</configuration>

```

**Hive和Mysql的使用：**<br><br>
mysql -uroot -proot  进入到数据库，两个root分别代表用户名和密码<br>
进入Hive目录下的bin中，直接输入hive，启动Hive，启动完了后会出现一个hive>，启动Hive前必须将所有的Hadoop相关程序启动起来<br>
hive> create database test_db;<br>
mysql>show databases; 就显示出hadoop_hive;<br>
mysql>use hadoop_hive;  使用这个hadoop_hive数据库<br>
mysql>show tables;  显示这个数据库中的表，里面大概有20张表，是hive的<br>
hive>use test_db;<br>
hive>show tables;<br>
hive>create table helloworld(id int , name string) row format delimited fields terminated by '\t';hive中创建表，后面的语句必须写<br>
hive>load data local inpath '文件所在目录/文件名' overwrite into table hellworld；将数据文件写入到数据表中<br>
hive>select count(1) from helloword;进行统计，统计的时候会转换成MR作业，在YARN的web界面上可以查看到sql语句和结果<br>

**Hive的DDL**：Hive Data Definition Language<br>
与关系型数据库中的语句非常相似：create、delete、alter...<br>

**Hive数据抽象/结构**<br>
database     HDFS一个目录<br>
table    HDFS一个目录<br>
data  文件 <br>
partition 分区表  HDFS一个目录<br>
data  文件 <br>
bucket  分桶   HDFS一个文件<br>


CREATE (DATABASE|SCHEMA) [IF NOT EXISTS] database_name<br>
  [COMMENT database_comment]<br>
  [LOCATION hdfs_path]<br>
  [WITH DBPROPERTIES (property_name=property_value, ...)];<br>
<br>
CREATE DATABASE IF NOT EXISTS hive;<br>

CREATE DATABASE IF NOT EXISTS hive2 LOCATION '/test/location';<br>


CREATE DATABASE IF NOT EXISTS hive3 <br>
WITH DBPROPERTIES('creator'='pk');<br>

/user/hive/warehouse是Hive默认的存储在HDFS上的路径<br>

CREATE TABLE emp(<br>
empno int,<br>
ename string,<br>
job string,<br>
mgr int,<br>
hiredate string,<br>
sal double,<br>
comm double,<br>
deptno int<br>
) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';<br>

LOAD DATA LOCAL INPATH '/home/hadoop/data/emp.txt' OVERWRITE INTO TABLE emp;<br><br>



CREATE [TEMPORARY] [EXTERNAL] TABLE [IF NOT EXISTS] [db_name.]table_name    -- (Note: TEMPORARY available in Hive 0.14.0 and later)<br><br>
  [(col_name data_type [COMMENT col_comment], ... [constraint_specification])]<br><br>
  [COMMENT table_comment]<br><br>
  [PARTITIONED BY (col_name data_type [COMMENT col_comment], ...)]<br><br>
  [CLUSTERED BY (col_name, col_name, ...) [SORTED BY (col_name [ASC|DESC], ...)] INTO num_buckets BUCKETS]<br><br>
  [SKEWED BY (col_name, col_name, ...)                  -- (Note: Available in Hive 0.10.0 and later)]<br><br>
     ON ((col_value, col_value, ...), (col_value, col_value, ...), ...)<br><br>
     [STORED AS DIRECTORIES]<br><br>
  [
   [ROW FORMAT row_format] <br><br>
   [STORED AS file_format]<br><br>
     | STORED BY 'storage.handler.class.name' [WITH SERDEPROPERTIES (...)]  -- (Note: Available in Hive 0.6.0 and later)<br><br>
  ]
  [LOCATION hdfs_path]<br><br>
  [TBLPROPERTIES (property_name=property_value, ...)]   -- (Note: Available in Hive 0.6.0 and later)<br><br>
  [AS select_statement];   -- (Note: Available in Hive 0.5.0 and later; not supported for external tables)<br><br>


LOAD DATA [LOCAL] INPATH 'filepath' [OVERWRITE] INTO TABLE tablename [PARTITION (partcol1=val1, partcol2=val2 ...)]<br><br>

LOCAL：本地系统，如果没有local那么就是指的HDFS的路径<br><br>
OVERWRITE：是否数据覆盖，如果没有那么就是数据追加<br><br>
<br><br>
LOAD DATA LOCAL INPATH '/home/hadoop/data/emp.txt' OVERWRITE INTO TABLE emp;<br><br>

LOAD DATA INPATH 'hdfs://hadoop000:8020/data/emp.txt' INTO TABLE emp;<br><br>

INSERT OVERWRITE LOCAL DIRECTORY '/tmp/hive/'<br><br>
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'<br><br>
select empno,ename,sal,deptno from emp;<br><br>


聚合： max/min/sum/avg<br><br>

分组函数： group by<br><br>
	求每个部门的平均工资<br><br>
	出现在select中的字段，如果没有出现在聚合函数里，那么一定要实现在group by里<br><br>
	select deptno, avg(sal) from emp group by deptno;<br>

	求每个部门、工作岗位的平均工资<br>
	select deptno,job avg(sal) from emp group by deptno,job;<br>


	求每个部门的平均工资大于2000的部门<br>
	select deptno, avg(sal) avg_sal from emp group by deptno where avg_sal>2000;<br>

	对于分组函数过滤要使用having<br>
select deptno, avg(sal) avg_sal from emp group by deptno having avg_sal>2000;	<br>



join ： 多表<br>

emp<br>
dept<br>


CREATE TABLE dept(<br>
deptno int,<br>
dname string,<br>
loc string<br>
) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';<br>

LOAD DATA LOCAL INPATH '/home/hadoop/data/dept.txt' OVERWRITE INTO TABLE dept;<br>

explain EXTENDED<br>
select <br>
e.empno,e.ename,e.sal,e.deptno,d.dname<br>
from emp e join dept d<br>
on e.deptno=d.deptno;<br>

**Hive外部表**<br>

CREATE TABLE emp(<br>
empno int,<br>
ename string,<br>
job string,
mgr int,<br>
hiredate string,<br>
sal double,<br>
comm double,<br>
deptno int<br>
) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';<br>

LOAD DATA LOCAL INPATH '/home/hadoop/data/emp.txt' OVERWRITE INTO TABLE emp;<br>

MANAGED_TABLE:内部表<br>
删除表：HDFS上的数据被删除 & Meta也被删除<br>
<br>
CREATE EXTERNAL TABLE emp_external(<br>
empno int,<br>
ename string,<br>
job string,<br>
mgr int,<br>
hiredate string,<br>
sal double,<br>
comm double,<br>
deptno int<br>
) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'<br>
location '/external/emp/';<br>

LOAD DATA LOCAL INPATH '/home/hadoop/data/emp.txt' OVERWRITE INTO TABLE emp_external;<br>


EXTERNAL_TABLE<br>
	HDFS上的数据不被删除 & Meta被删除<br>


分区表<br>

create external table track_info(<br>
ip string,<br>
country string,<br>
province string,<br>
city string,<br>
url string,<br>
time string,<br>
page string<br>
) partitioned by (day string)<br>
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'<br>
location '/project/trackinfo/';<br>

crontab表达式进行调度<br>
Azkaban调度：ETLApp==>其他的统计分析 <br>
	PySpark及调度系统<br>
		https://coding.imooc.com/class/chapter/249.html#Anchor<br>


LOAD DATA INPATH 'hdfs://hadoop000:8020/project/input/etl' OVERWRITE INTO TABLE track_info partition(day='2013-07-21');<br>

select count(*) from track_info where day='2013-07-21'  ;<br>

select province,count(*) as cnt from track_info where day='2013-07-21' group by province ;<br>



省份统计表<br>
create table track_info_province_stat(<br>
province string,<br>
cnt bigint<br>
) partitioned by (day string)<br>
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';<br>

insert overwrite table track_info_province_stat partition(day='2013-07-21') <br>
select province,count(*) as cnt from track_info where day='2013-07-21' group by province ;<br>

到现在为止，我们统计的数据已经在Hive表track_info_province_stat<br>
而且这个表是一个分区表，后续统计报表的数据可以直接从这个表中查询<br>
也可以将hive表的数据导出到RDBMS（sqoop）<br>


1）ETL<br>
2）把ETL输出的数据加载到track_info分区表里<br>
3）各个维度统计结果的数据输出到各自维度的表里（track_info_province_stat）<br>
4）将数据导出（optional）<br>


如果一个框架不能落地到SQL层面，这个框架就不是一个非常适合的框架<br>


#### Hadoop集群布置
Hadoop集群规划<br>
	HDFS: NN DN<br>
	YARN: RM NM<br>

hadoop000 192.168.199.234<br>
	NN RM<br>
	DN NM<br>
hadoop001 192.168.199.235<br>
	DN NM<br>
hadoop002 192.168.199.236<br>
	DN NM<br>

(每台)<br>
/etc/hostname: 修改hostname(hadoop000/hadoop001/hadoop002)<br>
/etc/hosts： ip和hostname的映射关系<br>
	192.168.199.234 hadoop000<br>
	192.168.199.235 hadoop001<br>
	192.168.199.236 hadoop002<br>
	192.168.199.234 localhost<br>


前置安装 ssh<br>
(每台)ssh免密码登陆：ssh-keygen -t rsa<br>
在hadoop000机器上进行caozuo <br>
ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop000<br>
ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop001<br>
ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop002<br>


JDK安装<br>
1）先在hadoop000机器上部署了jdk<br>
2）将jdk bin配置到系统环境变量<br>
3）将jdk拷贝到其他节点上去(从hadoop000机器出发)<br>
scp -r jdk1.8.0_91 hadoop@hadoop001:~/app/<br>
scp -r jdk1.8.0_91 hadoop@hadoop002:~/app/<br>
<br>
scp ~/.bash_profile hadoop@hadoop001:~/<br>
scp ~/.bash_profile hadoop@hadoop002:~/<br>

Hadoop部署<br>
1）hadoop-env.sh<br>
	JAVA_HOME<br>
2) core-site.xml<br>
<property><br>
	<name>fs.default.name</name><br>
	<value>hdfs://hadoop000:8020</value><br>
</property><br>

3) hdfs-site.xml
<property><br>
  <name>dfs.namenode.name.dir</name><br>
  <value>/home/hadoop/app/tmp/dfs/name</value><br>
</property><br>
<br>
<property><br>
  <name>dfs.datanode.data.dir</name><br>
  <value>/home/hadoop/app/tmp/dfs/data</value><br>
</property><br>

4) yarn-site.xml<br>
<property><br>
  <name>yarn.nodemanager.aux-services</name><br>
  <value>mapreduce_shuffle</value><br>
 </property>
<br>
<property><br>
    <name>yarn.resourcemanager.hostname</name>
    <value>hadoop000</value><br>
</property><br>
5) mapred-site.xml<br>
<property><br>
	<name>mapreduce.framework.name</name><br>
	<value>yarn</value><br>
</property><br>

6) slaves<br>

7) 分发hadoop到其他机器<br>
scp -r hadoop-2.6.0-cdh5.15.1 hadoop@hadoop001:~/app/<br>
scp -r hadoop-2.6.0-cdh5.15.1 hadoop@hadoop002:~/app/<br>

scp ~/.bash_profile hadoop@hadoop001:~/<br>
scp ~/.bash_profile hadoop@hadoop002:~/
<br>
8) NN格式化： hadoop namenode -format<br>
9) 启动HDFS<br>
10) 启动YARN<br>
