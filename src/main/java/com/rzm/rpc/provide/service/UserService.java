package com.rzm.rpc.provide.service;

import com.rzm.rpc.entity.User;

import java.util.List;

public interface UserService {



    public String insertUser(User user);



    public List<User> getUerList(List<String> names);


}
