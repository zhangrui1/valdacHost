package com.toyo.vh.controller;

import com.toyo.vh.entity.User;
import com.toyo.vh.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * Created by Lsr on 11/14/14.
 */

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String login(HttpSession session){
        User user = (User) session.getAttribute("user");
//        session.setAttribute("imageRoot","http://storage.googleapis.com/valdacconstruction/");
        if(user != null){
            return "redirect:/";
        } else {
            return "login";
        }
    }

    /**
     * ユーザ名とパスワードにより、ログイン
     *
     * @param userid ユーザ名
     * @param password パスワード
     *
     * @return String　ユーザ名とパスワードは正しい場合はIndex画面へ遷移、
     * 　　　　　　　　　間違えた場合は、ログイン画面に戻す
     * */
    @RequestMapping(method = RequestMethod.POST)
    public String loginByUserid(@RequestParam("userid")String userid,
                                @RequestParam("password")String password,
                                HttpSession session){
        //password暗号化 SHA256でハッシュ
        String passwordSHA= DigestUtils.sha256Hex(password);
        User user = userService.getUserByUseridAndPassword(userid,passwordSHA);
//        session.setAttribute("imageRoot","http://storage.googleapis.com/valdacconstruction/");
        if(user == null){
            session.setAttribute("message","ユーザ名またはパスワードが間違えました");
            return "login";
        } else {
            session.setAttribute("message",null);
            session.setAttribute("user",user);
            return "redirect:/";
        }
    }
}
