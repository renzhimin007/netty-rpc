package com.rzm.rpc.comsume;

import com.alibaba.fastjson.JSON;
import com.rzm.rpc.comsume.service.UserService;
import com.rzm.rpc.entity.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sun.rmi.server.UnicastServerRef;

import java.util.ArrayList;
import java.util.List;

public class ComsumeMain {


    public static void main(String[] args) {

        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ComsumeConfig.class);
        UserService bean = annotationConfigApplicationContext.getBean(UserService.class);
        User user ;
        ArrayList<String> strings = new ArrayList<>();

        for (int i =0;i<10;i++){
            strings.add("name " + i);
            user = new User();
            user.setName("name " + i);
            user.setAge(i);
            String str = bean.insertUser(user);
            System.out.println("保存用户信息返回值为 = "+str);
        }
        System.out.println("+++++++++++++++++++++++++++++++++++++");
        List<User> uerList = bean.getUerList(strings);
        uerList.forEach(e->{
            System.out.println("获取用户信息为 = " + JSON.toJSONString(e));
        });
    }
}
