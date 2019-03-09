package entity;

import java.util.List;

/**
 * 分页实体对象
 */
public class PageResult<T> {

    //new Result(xxxx,xxx,xxxx,new PageResult()<total,rows>)
    /**
     * 总记录数 spring data jpa?
     */
    private Long total;

    /**
     * 返回页面的分页数据
     */
    private List<T> rows;

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
