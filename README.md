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
3. Hbase和Hive的区别与联系：
**_区别：_**
* Hbase： Hadoop database 的简称，也就是基于Hadoop数据库，是一种NoSQL数据库，主要适用于海量明细数据（十亿、百亿）的随机实时查询，如日志明细、交易清单、轨迹行为等。
* Hive：Hive是Hadoop数据仓库，严格来说，不是数据库，主要是让开发人员能够通过SQL来计算和处理HDFS上的结构化数据，适用于离线的批量数据计算。
*   通过元数据来描述Hdfs上的结构化文本数据，通俗点来说，就是定义一张表来描述HDFS上的结构化文本，包括各列数据名称，数据类型是什么等，方便我们处理数据，当前很多SQL ON Hadoop的计算引擎均用的是hive的元数据，如Spark SQL、Impala等；
*   基于第一点，通过SQL来处理和计算HDFS的数据，Hive会将SQL翻译为Mapreduce来处理数据；
**_关系：_**
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
![网络配置](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/resource/%E7%BD%91%E7%BB%9C%E9%85%8D%E7%BD%AE.jpg)<br>
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
  
#### 六、使用Java API操作HDFS
[HDFSApp.java](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/src/test/java/com/imooc/bigdata/hadoop/hdfs/HDFSApp.java)<br>


#### 七、MapReduce（计算）
源自Google的MapReduce论文，MapReduce是Google MapReduce的克隆版，特点：扩展性高，容错性强，海量离线数据处理。
例子：
![wordcount](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/resource/wordcount.PNG)


#### 八、YARN（调度）
YARN：Yet Another Resource Negotiator<br>
负责整个集群资源的管理和调度。特点：扩展性高，容错性强，多资源统一调度。
![YARN](https://github.com/Zhang-Yixuan/Hadoop_Learning/blob/master/resource/YARN.PNG)






