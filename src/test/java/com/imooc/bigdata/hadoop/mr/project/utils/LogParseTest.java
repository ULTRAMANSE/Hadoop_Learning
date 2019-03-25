package com.imooc.bigdata.hadoop.mr.project.utils;
import com.immoc.bigdata.hadoop.mr.project.utils.LogParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;

public class LogParseTest {

    LogParser logParser;

    @Before
    public void setUp(){
        logParser = new LogParser();
    }

    @After
    public void tearDown(){
        logParser = null;
    }

    @Test
    public void test01(){
        Map<String, String> map  = logParser.parse("20946835322\u0001http://www.yihaodian.com/1/?tracker_u=2225501&type=3\u0001http://www.baidu.com/s?wd=1%E5%8F%B7%E5%BA%97&rsv_bp=0&ch=&tn=baidu&bar=&rsv_spt=3&ie=utf-8&rsv_sug3=5&rsv_sug=0&rsv_sug1=4&rsv_sug4=313&inputT=4235\u00011号店\u00011\u0001SKAPHD3JZYH9EE9ACB1NGA9VDQHNJMX1NY9T\u0001\u0001\u0001\u0001\u0001PPG4SWG71358HGRJGQHQQBXY9GF96CVU\u00012225501\u0001\\N\u0001124.79.172.232\u0001\u0001msessionid:YR9H5YU7RZ8Y94EBJNZ2P5W8DT37Q9JH,unionKey:2225501\u0001\u00012013-07-21 09:30:01\u0001\\N\u0001http://www.baidu.com/s?wd=1%E5%8F%B7%E5%BA%97&rsv_bp=0&ch=&tn=baidu&bar=&rsv_spt=3&ie=utf-8&rsv_sug3=5&rsv_sug=0&rsv_sug1=4&rsv_sug4=313&inputT=4235\u00011\u0001\u0001\\N\u0001null\u0001-10\u0001\u0001\u0001\u0001\u0001Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; MATP; Media Center PC 6.0; .NET4.0C; InfoPath.2; .NET4.0E)\u0001Win32\u0001\u0001\u0001\u0001\u0001\u0001上海市\u00011\u0001\u00012013-07-21 09:30:01\u0001上海市\u0001\u000166\u0001\u0001\u0001\u0001\u0001\\N\u0001\\N\u0001\\N\u0001\\N\u00012013-07-21\n");

        for(Map.Entry<String,String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

}
