package com.crawler.task;

import com.crawler.bean.Crawler;
import com.crawler.service.ICrawlerService;
import com.crawler.utils.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class CrawlerTask {
    @Autowired
    private HttpUtil httpUtil;
    @Autowired
    private ICrawlerService crawlerService;

    private static final ObjectMapper MAPPER =  new ObjectMapper();
    //当下载任务完成后间隔多长时间进行下一次任务
    @Scheduled(fixedDelay = 100 * 1000)

    public void crawlerTask() throws Exception {
       // 声明要解析的初始地址
        String url = "https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&wq=%E6%89%8B%E6%9C%BA" +
                "&cid2=653&cid3=655&s=113&click=0&page=";
        for (int i = 1; i < 10; i = i + 2) {
            String doGetHtml = httpUtil.doGetHtml(url + i);

            this.parse(doGetHtml);
        }
        System.out.println("完成");
    }
    //解析页面，获取商品数据并存储
    private void parse(String html) throws Exception {
        //解析html获取document对象
        Document document = Jsoup.parse(html);
        //获取spu
        Elements spuElements = document.select("div#J_goodsList > ul > li");
        for (Element element : spuElements) {
            //获取spu
            long spu = Long.parseLong(element.attr("data-spu"));
            //获取sku
            Elements skuEle = element.select("li.ps-item");
            for (Element e : skuEle) {
                //获取sku
                long sku = Long.parseLong(e.select("[data-sku]").attr("data-sku"));
                Crawler crawler = new Crawler();
                crawler.setSku(sku);
                List<Crawler> list = this.crawlerService.findAll(crawler);
                if (list.size() > 0) {
                    continue;
                }
                //设置商品spu
                crawler.setSpu(spu);
                //获取商品的详情url
                String itemUrl = "https://item.jd.com/" + sku + ".html";
                crawler.setUrl(itemUrl);
                //商品图片
                String pic = "https:" + skuEle.select("img[data-sku]").first().attr("data-lazy-img");
                pic = pic.replace("/n9/", "/n1/");
                String getImg = this.httpUtil.doGetImg(pic);
                crawler.setPic(getImg);

                //商品价格
                String price = this.httpUtil.doGetHtml("https://p.3.cn/prices/mgets?skuIds=J_" + sku);
                
                    double asDouble = MAPPER.readTree(price).get(0).get("p").asDouble();
               
                crawler.setPrice(asDouble);

                //商品标题
                String itemInfo = this.httpUtil.doGetHtml(crawler.getUrl());
                Document itamDoc = Jsoup.parse(itemInfo);
                String itemTile = itamDoc.select("div.sku-name").text();
                crawler.setTitle(itemTile);

                //创建时间
                crawler.setCreated(new Date());
                //更新时间
                crawler.setUpdated(crawler.getCreated());
                //保存数据
                this.crawlerService.save(crawler);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new CrawlerTask().crawlerTask();
    }
}
