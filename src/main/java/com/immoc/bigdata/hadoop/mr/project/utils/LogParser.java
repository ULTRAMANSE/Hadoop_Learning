package com.immoc.bigdata.hadoop.mr.project.utils;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class LogParser {

    public Map<String , String>parse(String log){

        Map<String,String>info = new HashMap<>();
        IPParser ipParser = IPParser.getInstance();

        if(StringUtils.isNotBlank(log)){
            String[] splits = log.split("\001");
            String ip = splits[13];
            String country = " ";
            String province = " ";
            String city = " ";

            IPParser.RegionInfo regionInfo = ipParser.analyseIp(ip);

            if(regionInfo != null){
                country = regionInfo.getCountry();
                province = regionInfo.getCountry();
                city = regionInfo.getCity();
            }

            info.put("ip",ip);
            info.put("country",country);
            info.put("province",province);
            info.put("city",city);


        }
        
        return info;
    }

}
