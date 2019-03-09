package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticleSearchService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 搜索微服务控制层
 */
@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleSearchController {
    @Autowired
    private ArticleSearchService articleSearchService;

    /**
     * 保存文章
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Article article){
        articleSearchService.save(article);
        return new Result(true, StatusCode.OK,"插入文章文档成功");
    }

    /**
     * 根据文章标题 or 文章内容  进行文章搜索
     */
    @RequestMapping(value="/search/{keywords}/{page}/{size}",method= RequestMethod.GET)
    public Result search(@PathVariable String keywords,@PathVariable int  page,@PathVariable int  size ){
        Page<Article> articlePage =  articleSearchService.search(keywords,page,size);
        return new Result(true, StatusCode.OK,"搜索文章数据成功",new PageResult<>(articlePage.getTotalElements(),articlePage.getContent()));
    }


}
