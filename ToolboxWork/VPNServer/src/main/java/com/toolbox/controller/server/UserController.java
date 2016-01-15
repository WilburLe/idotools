package com.toolbox.controller.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.toolbox.entity.UsersEntity;
import com.toolbox.framework.utils.SHAUtility;
import com.toolbox.service.UsersService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class UserController {
    @Autowired
    private UsersService usersService;

    @RequestMapping(value = "regist/{name}/{pass}", method = RequestMethod.GET)
    public void regist(//
            @PathVariable(value = "name") String username, // 
            @PathVariable(value = "pass") String password//
    ) {
        UsersEntity users = new UsersEntity();
        users.setUsername(username);
        users.setPassword(SHAUtility.encryp(password));
        usersService.save(users);

        
    }
}
