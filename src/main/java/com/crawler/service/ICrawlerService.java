package com.crawler.service;

import com.crawler.bean.Crawler;

import java.util.List;

public interface ICrawlerService {
    void save(Crawler crawler);

    List<Crawler> findAll(Crawler crawler);
}
