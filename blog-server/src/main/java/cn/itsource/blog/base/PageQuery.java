package cn.itsource.blog.base;

/*itsource*这个类用于描述分页请求
        blogcurrentPage:用户想查询第几页[当前页]
        basePagesize: 用户每页想显示多少条数据*/
import lombok.Data;

@Data
public class PageQuery {
    private Integer currentPage;
    private Integer pageSize;
    private String  keyWord;
}
