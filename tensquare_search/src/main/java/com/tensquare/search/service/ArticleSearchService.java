package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleSearchDao;
import com.tensquare.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import util.IdWorker;

/**
 * 搜索微服务业务处理类
 */
@Service
public class ArticleSearchService {

    @Autowired
    private ArticleSearchDao articleSearchDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 保存文章数据到elasticsearch索引库中
     */
    public void save(Article article){
        article.setId(idWorker.nextId()+"");
        articleSearchDao.save(article);
    }

    /**
     * 搜索文章数据
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    public Page<Article> search(String keywords, int page, int size) {

        //拼接分页参数
        Pageable pageable = PageRequest.of(page-1,size);
        //搜索文章  命名查询语句
        return articleSearchDao.findByTitleOrContentLike(keywords,keywords,pageable);

    }
}
