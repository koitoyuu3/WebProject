package com.example.webproject.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.webproject.entity.Food;
import com.example.webproject.entity.User;
import com.example.webproject.service.IFoodService;
import com.example.webproject.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {
    @Autowired
    IUserService userService;
    @Autowired
    IFoodService foodService;

    Long USER_ID;

    String FOODPATH;

    @GetMapping()
    String loginPage() {
        return "login";
    }

    @PostMapping()
    String login(@RequestParam String username, @RequestParam String password) {
        boolean loginSuccessful = loginJudge(username, password);
        if(loginSuccessful) {
            USER_ID  = getIDbyUsername(username);
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
        USER_ID  = getIDbyUsername(username);
        return "list";
    }

    @GetMapping("/main")
    String mainPage() {return "list"; }
    @GetMapping("/person")
    String personPage() {
        return "person";
    }
    @GetMapping("/getUserInfo")
    @ResponseBody
    User getPersonInfoPage() {
        // return userService.getById(USER_ID);
        User curUser = userService.getById(USER_ID);
        return curUser;
    }


    @GetMapping("/add")
    String addPage() {
        return "add";
    }



    @PostMapping("/person")
    String addFood(@RequestParam String foodName, @RequestParam String description ){
        Food food = new Food();
        food.setFoodName(foodName);
        food.setDescription(description);
        food.setUser_id(USER_ID);
        food.setImage_path(FOODPATH);
        foodService.save(food);

        // 将食物整合到用户数据库
        User curUser = userService.getById(USER_ID);
        List<Food> foodList = curUser.getFoods();

        if (foodList == null) {
            foodList = new ArrayList<>();
            curUser.setFoods(foodList);
        }

        foodList.add(food);
        userService.updateById(curUser);
        return "./person";
    }


    @PostMapping("/uploadUserImage")
    public ResponseEntity<String> UserUpload(@RequestParam("image") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("请选择要上传的图片");
        }

        try {
            // 保存文件到服务器的指定目录，这里保存 "uploads" 文件夹中
            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
            File uploadPath = new File(uploadDir);

            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            String fileName = file.getOriginalFilename();
            String pathName = uploadPath.getAbsolutePath() + File.separator + fileName;
            String savePathName = "/uploads/" + fileName;
            //FOODPATH = pathName;
            File dest = new File(pathName);
            file.transferTo(dest);

            // 将头像的文件路径保存到数据库中
            User newUser = new User();
            newUser.setId(USER_ID);
            newUser.setUserimagepath(savePathName);
            userService.updateById(newUser);

            return ResponseEntity.ok("上传成功!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("上传失败!");
        }
    }

    @PostMapping("/uploadFoodImage")
    public ResponseEntity<String> FoodUpload(@RequestParam("image") MultipartFile file) {
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
            FOODPATH = pathName;
            File dest = new File(pathName);
            file.transferTo(dest);

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

    public Long getIDbyUsername(String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userService.getOne(queryWrapper);
        return user.getId();
    }
    //public void addPathToDB();
}
