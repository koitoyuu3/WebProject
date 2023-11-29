package com.example.webproject.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.webproject.entity.User;
import com.example.webproject.mapper.UserMapper;
import com.example.webproject.service.IUserService;
import com.example.webproject.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class UserController {
    @Autowired
    IUserService userService;

    @GetMapping()
    String loginPage() {
        return "login";
    }

    @PostMapping()
    String login(@RequestParam String username, @RequestParam String password) {
        boolean loginSuccessful = loginJudge(username, password);
        if(loginSuccessful)
            return "list";
        else
            return "redirect:/";
    }

    @GetMapping("/register")
    String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    String register(@RequestParam String username, @RequestParam String password, @RequestParam String name) {
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        userService.save(user);
        return "list";
    }


    public boolean loginJudge(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username)
                .eq("password", password);

        User user = userService.getOne(queryWrapper);

        return user != null;
    }
}
