package com.rzm.rpc.provide.service.impl;

import com.rzm.rpc.entity.User;
import com.rzm.rpc.provide.service.UserService;
import com.rzm.rpc.provide.anno.RpcService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * user 服务提供者
 */
@RpcService
public class UserServiceImpl implements UserService{

    Map<String,User> map = new HashMap<String,User>(12);



    public String
    insertUser(User user) {
        map.put(user.getName(),user);
        return "success";
    }

    public List<User> getUerList(List<String> names) {
        List<User> users = new ArrayList<User>();
        for (String name : names) {
            users.add(map.get(name));
        }
        return users;
    }
}
