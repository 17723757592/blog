package cn.itsource.blog.controller;

import cn.itsource.blog.base.AjaxResult;
import cn.itsource.blog.base.PageQuery;
import cn.itsource.blog.domain.User;
import cn.itsource.blog.service.IUserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value="用户接口模块")
@RestController
//@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    //增
    @ApiOperation("新增用户")
    @PostMapping("/user")
    public AjaxResult add(@ApiParam("新增用户对象")@RequestBody User user){
        if (user.getId()!=null){
            int results = userService.update(user);
            String msg = "修改成功";
            return AjaxResult.success(results,msg);
        }else {
            int result = userService.add(user);
            String msg = "新增成功";
            return AjaxResult.success(result,msg);
        }

    }
    //删
    @ApiOperation("删除用户")
    @DeleteMapping("/user/{id}")
    public AjaxResult delete(@ApiParam("新增用户ID")@PathVariable("id")Long id) {
//        return userService.remove(id);
        int result = userService.remove(id);
        String msg = "删除成功";
        return AjaxResult.success(result,msg);
    }
    //改
    @ApiOperation("修改用户")
    @PutMapping("/user")
    public AjaxResult update(@ApiParam("更新用户对象")@RequestBody User user){

        int result = userService.update(user);
        String msg = "修改成功";
        return AjaxResult.success(result,msg);
    }
    //查
    @ApiOperation("查找对应用户")
    @GetMapping("/user/{xx}")
    public AjaxResult findById(@ApiParam("查找用户ID")@PathVariable("xx") Long id){
        User result = userService.findById(id);
        String msg = "查询成功";
        return AjaxResult.success(result,msg);
    }
    //查全部
    @ApiOperation("查询全部")
    @GetMapping("/user/list")
    public AjaxResult findAll(){
        List<User> result = userService.findAll();
        String msg = "查询成功";
        return AjaxResult.success(result,msg);
    }
    @ApiOperation("分页查询")
    @PostMapping("/user/page")
    public AjaxResult findPage(@RequestBody PageQuery query){
        PageInfo<User> result = userService.findByPage(query);
        return AjaxResult.success(result,"分页查询成功");
    }
    /*
    *{
    *   code:200,
    *   message:"登录成功",
    *   data:{返回数据}
    *
    * */
    @ApiOperation("批量删除")
    @PatchMapping("/user")
    public AjaxResult batchRemove(@RequestBody List<Long> ids){
        int row = userService.batchRemove(ids);
        return AjaxResult.success(row,"删除成功"+row+"条数据");
    }
}
