package com.tensquare.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

/**
 * 文章实体类
 */
@Document(indexName="tensquare",type="article")
public class Article implements Serializable {
    @Id
    private String id;//ID 文章id

    /**
     * index:是否索引 放入索引域   true:索引 false:不索引
     * analyzer：插入文档的分词方式
     * searchAnalyzer：搜索文档的分词方式
     */
    @Field(index= true ,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
    private String title;//标题

    @Field(index= true ,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
    private String content;//文章正文

    /**
     * 文章状态  为了后续删除的时候 需要同步的标识字段
     */
    private String state;//审核状态   已经删除标识  或 未审核通过的文章 不进行搜索
  
	//getter and setter ......


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}