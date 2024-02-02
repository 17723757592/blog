package cn.itsource.blog;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;

//spring  启动类注解
@SpringBootApplication
//扫面mybatis mapper接口
@MapperScan("cn.itsource.blog.mapper")
public class MainApp {
    public static void main(String[] args) {
        SpringApplication.run(MainApp.class,args);
    }
}
