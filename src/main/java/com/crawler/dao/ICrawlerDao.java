package com.crawler.dao;


import com.crawler.bean.Crawler;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICrawlerDao extends JpaRepository<Crawler, Long> {

}
