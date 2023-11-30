package com.example.webproject.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.webproject.entity.User;
import com.example.webproject.mapper.UserMapper;
import com.example.webproject.service.IUserService;
import com.example.webproject.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class UserController {
    @Autowired
    IUserService userService;

    String USERNAME;

    @GetMapping()
    String loginPage() {
        return "login";
    }

    @PostMapping()
    String login(@RequestParam String username, @RequestParam String password) {
        boolean loginSuccessful = loginJudge(username, password);
        if(loginSuccessful) {
            USERNAME = username;
            return "list";
        }
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

    @GetMapping("/person")
    String personPage() {
        return "person";
    }

    @GetMapping("/add")
    String addPage() {
        return "add";
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<String> handleFileUpload(@RequestParam("image") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("请选择要上传的图片");
        }

        try {
            // 保存文件到服务器的指定目录，这里保存 "uploads" 文件夹中
            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/uploads/";
            File uploadPath = new File(uploadDir);

            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            String fileName = file.getOriginalFilename();
            String pathName = uploadPath.getAbsolutePath() + File.separator + fileName;
            File dest = new File(pathName);
            file.transferTo(dest);
            // 将头像的文件路径保存到数据库中
            User newUser = new User();
            newUser.setUser_image_path(pathName);
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("username", USERNAME);  // 设置更新条件，假设根据 ID 更新
            userService.update(newUser, updateWrapper);




            return ResponseEntity.ok("上传成功!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("上传失败!");
        }
    }

    public boolean loginJudge(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username)
                .eq("password", password);

        User user = userService.getOne(queryWrapper);

        return user != null;
    }


    //public void addPathToDB();
}
