package com.fanggu.stone.controller;

import com.fanggu.stone.dao.UserMapper;
import com.fanggu.stone.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserMapper userMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/index")
    public List<User> userIndexAction(HttpSession httpSession, String id) {
        httpSession.setAttribute(id, new Random().nextInt(100));
        logger.error("id:{};message:{};lastAccessTime:{};maxInactiveInterval:{}", httpSession.getId(), httpSession.getAttribute(id), httpSession.getLastAccessedTime(), httpSession.getMaxInactiveInterval());
        return userMapper.selectAll();
    }
}
