package com.fanggu.stone.dao;

import com.fanggu.stone.model.User;

public interface UserMapper {
    int insertUser(User user);

    int updateUser(User user);

    int updateAvatar(User user);

    User getUserById(int userId);

    User getUserByTel(String userTel);

    User getUserByLogin(String userTel, String password);
}