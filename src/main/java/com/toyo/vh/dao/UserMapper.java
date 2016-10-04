package com.toyo.vh.dao;

import com.toyo.vh.entity.User;

import java.util.List;

/**
 * Created by zhangrui on 14/11/14.
 */
public interface UserMapper {
    /**useridからユーザを取得*/
    public User findByUserId(User user);

    /**useridからユーザを取得*/
    public User findByUserIdOnly(String userId);

    /**user権限からユーザ名を取得*/
    public List<String> findUserByKengen(String kengen);
}
