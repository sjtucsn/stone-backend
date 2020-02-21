package com.fanggu.stone.controller;

import com.fanggu.stone.dao.UserMapper;
import com.fanggu.stone.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserMapper userMapper;

    @GetMapping("/index")
    public List<User> userIndexAction() {
        return userMapper.selectAll();
    }
}
