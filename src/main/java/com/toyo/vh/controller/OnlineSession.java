package com.toyo.vh.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by 維瑞 on 2015/07/14.
 */


public class OnlineSession implements HttpSessionListener {

    public void sessionCreated(HttpSessionEvent event) {
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        ServletContext application = session.getServletContext();

//        // 取得登录的用户名
//        User user = (User) session.getAttribute("user");
//
//        // 从在线列表中删除用户名
//        List onlineUserList = (List) application.getAttribute("user");
//        onlineUserList.remove(user);

//        User user = (User) session.getAttribute("user");
//        if(user == null){
//            System.out.println( "No");
//        }else{
//            System.out.println( "OK");
//        }
    }

}
