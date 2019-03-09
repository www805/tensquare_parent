package com.tensquare.search.dao;

import com.tensquare.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 搜索微服务持久层
 */
public interface ArticleSearchDao extends ElasticsearchRepository<Article,String>{
    /**
     * 命名查询语句  根据标题和内容进行搜索文章
     * @param keywords
     * @param keywords1
     * @param pageable
     * @return
     */
    Page<Article> findByTitleOrContentLike(String keywords, String keywords1, Pageable pageable);
}
