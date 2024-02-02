package  cn.itsource.blog.service;

import cn.itsource.blog.base.PageQuery;
import cn.itsource.blog.domain.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IUserService {

    int remove(Long id);

    int add(User record);

    int addSelective(User record);

    User findById(Long id);

    int updateByIdSelective(User record);

    int update(User record);

    List<User> findAll();

    PageInfo<User> findByPage(PageQuery query);

    int batchRemove(List<Long> ids);
}