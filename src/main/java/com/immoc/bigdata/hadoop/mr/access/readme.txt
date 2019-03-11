自定义复杂类型


access.log中的
        第二列，手机号
        倒数第三列，上行流量
        倒数第二列，下行流量

需求一：统计每个手机号上行流量和、下行流量和、总流量和

Access.java
        手机号、上行流量、下行流量、总流量
 根据手机号进行分组，然后把手机号对应的上行和下行流量加起来

 Mapper：把手机号、上行流量、下行流量 拆开
         把手机号作为KEY，把Access作为value写出去

 Reducer：（1372623888，<Access，Access>）



需求二：
    将统计结果按照手机号的前缀进行区分，并输出到不同的输出文件中去
    13* ==>..
    15* ==>..
    other ==>..

    Partitioner决定maptask输出的数据交由那个reducetask处理
    默认实现：分发的key的hash值与reduce task个数取模