package cn.itsource.blog.base;

import lombok.Data;

/*
* 描述请求状态结果
    * 状态
    * 数据
    * 提示
    * 成功标识
* */
@Data
public class AjaxResult {
    private Integer code;
    private Object data;
    private String message;
    private Boolean success;

//    public static AjaxResult success(Object data){
//        AjaxResult result = new AjaxResult();
//        result.setCode(200);
//        result.setMessage("操作成功");
//        result.setData(data);
//        result.setSuccess(true);
//        return  result;
//    }

    public static AjaxResult success(Object data,String msg){
        AjaxResult result = new AjaxResult();
        result.setCode(200);
        result.setMessage(msg);
        result.setData(data);
        result.setSuccess(true);
        return  result;
    }

//    public static  AjaxResult fail(Object data){
//        AjaxResult result = new AjaxResult();
//        result.setCode(500);
//        result.setMessage("操作失败");
//        result.setData(data);
//        result.setSuccess(false);
//        return  result;
//    }
    public static  AjaxResult fail(Object data, String msg){
        AjaxResult result = new AjaxResult();
        result.setCode(500);
        result.setMessage(msg);
        result.setData(null);
        result.setSuccess(false);
        return  result;
    }
}
