package com.crawler.service;

import com.crawler.bean.Crawler;
import com.crawler.dao.ICrawlerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CrawlerServiceImpl implements ICrawlerService {
    @Autowired
    private ICrawlerDao crawlerDao;
    @Override
    @Transactional
    public void save(Crawler crawler) {
        crawlerDao.save(crawler);
    }

    @Override
    public List<Crawler> findAll(Crawler crawler) {
        Example<Crawler> example = Example.of(crawler);
        List<Crawler> list = crawlerDao.findAll(example);
        return list;
    }
}
