package cn.itsource.blog.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户信息表
 * @TableName sys_user
 */
@Data
public class User implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;
}