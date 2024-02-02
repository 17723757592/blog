package cn.itsource.blog.service.impl;

import cn.itsource.blog.base.PageQuery;
import cn.itsource.blog.domain.User;
import cn.itsource.blog.mapper.UserMapper;
import cn.itsource.blog.service.IUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements IUserService {

    //注入mapper
    @Autowired
    private UserMapper userMapper;

    @Override
    public int remove(Long id) {
        return userMapper.deleteById(id);
    }

    @Override
    public int add(User record) {
        return userMapper.insert(record);
    }

    @Override
    public int addSelective(User record) {
        return userMapper.insertSelective(record);
    }

    @Override
    public User findById(Long id) {
        return userMapper.selectId(id);
    }

    @Override
    public int updateByIdSelective(User record) {
        return userMapper.updateByIdSelective(record);
    }

    @Override
    public int update(User record) {
        return userMapper.update(record);
    }

    @Override
    public List<User> findAll() {
        return userMapper.selectAll();
    }

    @Override
    public PageInfo<User> findByPage(PageQuery query){
        PageHelper.startPage(query.getCurrentPage(), query.getPageSize());
        List<User> users = userMapper.selectPage(query);
        return new PageInfo<>(users);
    }

    @Override
    public int batchRemove(List<Long> ids) {
        return userMapper.batchDelete(ids);
    }

}