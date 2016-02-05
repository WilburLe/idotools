package com.toolbox.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
        request.getSession().setAttribute("user", name);
        return new ModelAndView("login_success");
    }
}
