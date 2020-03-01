package com.crawler;

import com.crawler.service.ICrawlerService;
import com.crawler.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class Test {
    @Autowired
    private HttpUtil httpUtil;
    @Autowired
    private ICrawlerService crawlerService;

    @org.junit.Test
    public void v() {
        if (httpUtil == null) {
            System.out.println("1");
        }
        if (crawlerService == null) {
            System.out.println("2");
        }

    }
}
