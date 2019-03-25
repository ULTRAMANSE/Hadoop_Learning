package com.immoc.bigdata.hadoop.mr.project.utils;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetPageId {

    public static String getPageId(String url) {
        String pageId = "";
        if (StringUtils.isBlank(url)) {
            return pageId;
        }
        Pattern pat = Pattern.compile("topicId=[0-9]+");
        Matcher matcher = pat.matcher(url);

        if (matcher.find()) {
            pageId = matcher.group().split("topicId=")[1];
        }

        return pageId;
    }

    public static void main(String[] args) {
        System.out.println(getPageId("http://www.yihaodian.com/cms/view.do?topicId=14572"));
        System.out.println(getPageId("http://www.yihaodian.com/cms/view.do?topicId=22372&merchant=1"));

    }
}
