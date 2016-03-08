package com.toolbox.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.toolbox.framework.utils.ConfigUtility;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class LoginController {

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ModelAndView login(String name, String password) {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ModelAndView login(String name, String password, HttpServletRequest request) {
        String luser = ConfigUtility.getInstance().getString("login.user");
        String lpass = ConfigUtility.getInstance().getString("login.password");

        if (!luser.equals(name) || !lpass.equals(password)) {
            return new ModelAndView("login").addObject("msg", "用户名或密码错误！");
        }
        request.getSession().setAttribute("user", name);
        return new ModelAndView("redirect:/");
    }
}
