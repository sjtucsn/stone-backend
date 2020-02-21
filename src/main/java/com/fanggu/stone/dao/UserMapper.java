package com.fanggu.stone.dao;

import com.fanggu.stone.model.User;

public interface UserMapper {
    void insertUser(User user);

    void updateUser(User user);

    void updateAvatar(User user);

    User getUserById(int userId);

    User getUserByTel(String userTel);

    User getUserByLogin(String userTel, String password);
}