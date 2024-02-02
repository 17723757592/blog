package cn.itsource.blog.mapper;

import cn.itsource.blog.base.PageQuery;
import cn.itsource.blog.domain.User;

import java.util.List;

/**
* @author liangbaikai
* @description 针对表【sys_user(用户信息表)】的数据库操作Mapper
* @createDate 2023-11-30 15:26:53
* @Entity cn.itsource.blog.domain.User
*/
public interface UserMapper {

    int deleteById(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectId(Long id);

    int updateByIdSelective(User record);

    int update(User record);

    List<User> selectAll();

    List<User> selectPage(PageQuery query);

    int batchDelete(List<Long> ids);
}
