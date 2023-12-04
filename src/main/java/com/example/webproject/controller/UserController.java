package com.example.webproject.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.webproject.entity.Food;
import com.example.webproject.entity.User;
import com.example.webproject.service.IFoodService;
import com.example.webproject.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
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
    @Autowired
    private PasswordEncoder passwordEncoder;


    Long USER_ID;

    String FOODPATH;

    @GetMapping("/login")
    String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        boolean loginSuccessful = loginJudge(username, password);
        if (loginSuccessful) {
            USER_ID = getIDbyUsername(username);
            session.setAttribute("username", username);
            session.setAttribute("password", password);
            return "redirect:/main";
        } else {
            // 登录失败，向模型中添加错误信息
            model.addAttribute("error", "用户名或密码错误");

            // 重定向到登录页面，并在登录页面中显示错误信息
            return "login";
        }
    }

    @GetMapping("/register")
    String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    String register(@RequestParam String username, @RequestParam String password, @RequestParam String name, HttpSession session) {
        User user = new User();
        user.setName(name);
        user.setUsername(username);

        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        //user.setPassword(password);
        session.setAttribute("username", username);
        //session.setAttribute("password", password);
        session.setAttribute("password", encryptedPassword);
        userService.save(user);
        USER_ID = getIDbyUsername(username);
        return "redirect:/main";
    }

    @GetMapping("/main")
    String mainPage(Model model) {
        List<Food> foodList = foodService.list();
        model.addAttribute("Foods", foodList);
        return "list";
    }

    @GetMapping("/person")
    String personPage() {
        return "person";
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        // 销毁session
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getUserInfo")
    @ResponseBody
    User getPersonInfoPage() {
        // return userService.getById(USER_ID);
        User curUser = userService.getById(USER_ID);
        return curUser;
    }

    @GetMapping("/getAllFoodByUser")
    public ResponseEntity<?> getFoodByUser() {
        // List<T> list(Wrapper<T> queryWrapper);
        try {
            QueryWrapper<Food> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userid", USER_ID);
            List<Food> foodList = foodService.list(queryWrapper);

            if (foodList == null) {
                return ResponseEntity.ok("暂未添加食物");
            } else {
                return ResponseEntity.ok(foodList);
            }
        } catch (Exception e) {
            // 处理异常，例如日志记录
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/add")
    String addPage() {
        return "add";
    }


    @PostMapping("/person")
    String addFood(@RequestParam String foodname, @RequestParam String description) {
        Food food = new Food();
        food.setFoodname(foodname);
        food.setDescription(description);
        food.setUserid(USER_ID);
        food.setImagepath(FOODPATH);
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
        return "redirect:/person";
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
            // FOODPATH = pathName;
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
            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
            File uploadPath = new File(uploadDir);

            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            String fileName = file.getOriginalFilename();
            String pathName = uploadPath.getAbsolutePath() + File.separator + fileName;
            String savePathName = "/uploads/" + fileName;
            FOODPATH = savePathName;
            File dest = new File(pathName);
            file.transferTo(dest);

            return ResponseEntity.ok("上传成功!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("上传失败!");
        }
    }

    @GetMapping("/correct")
    String correctForm(Model model) {
        QueryWrapper<Food> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", USER_ID);
        List<Food> foodList = foodService.list(queryWrapper);
        model.addAttribute("Foods", foodList);
        return "foodCorrect";
    }

    @GetMapping("/editFood")
    String editForm(Model model, @RequestParam Long id) {
        Food curFood = foodService.getById(id);
        model.addAttribute("food", curFood);
        return "editFood";
    }

    @PostMapping("/editFood")
    String edit(@ModelAttribute Food food) {
        food.setImagepath(FOODPATH);
        foodService.updateById(food);
        return "redirect:/correct";
    }

    @GetMapping("/deleteFood")
    String delete(@RequestParam Long id) {
        foodService.removeById(id);
        return "redirect:/correct";
    }

    @GetMapping("/userCorrect")
    String userCorrectForm(Model model) {
        User curUser = userService.getById(USER_ID);
        model.addAttribute("user", curUser);
        return "userCorrect";
    }

    @PostMapping("/userCorrect")
    String userCorrect(@ModelAttribute User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", USER_ID);
        userService.update(user, updateWrapper);
        return "redirect:/person";
    }

    @PostMapping("/userDelete")
    public ResponseEntity<?> userDelete(HttpSession session) {
        // 销毁session
        session.invalidate();
        // 从数据库中删除用户和对应的食物
        QueryWrapper<Food> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", USER_ID);
        foodService.remove(queryWrapper);
        userService.removeById(USER_ID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getUser")
    String getUserForm() {
        return "inputUserName";
    }

    @PostMapping("/getUser")
    String getByName(Model model, @RequestParam String name) {
        //T getOne(Wrapper<T> queryWrapper);
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("name", name);
        User user = userService.getOne(qw);
        model.addAttribute("user", user);
        return "getUserByName";
    }

    @GetMapping("/getFood")
    String getFoodForm() {
        return "inputFoodname";
    }

    @PostMapping("/getFood")
    String getByFoodname(Model model, @RequestParam String foodname) {
        //T getOne(Wrapper<T> queryWrapper);
        QueryWrapper<Food> qw = new QueryWrapper<>();
        qw.eq("foodname", foodname);
        Food food = foodService.getOne(qw);
        model.addAttribute("food", food);

        Long userId = food.getUserid();
        User user = userService.getById(userId);
        model.addAttribute("user", user);
        return "getFoodByFoodname";
    }

    public boolean loginJudge(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        //.eq("password", password);
        User user = userService.getOne(queryWrapper);

        //return user != null;
        if (user != null) {
            String storedPassword = user.getPassword();
            return passwordEncoder.matches(password, storedPassword);
        }
        return false;

    }

    public Long getIDbyUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userService.getOne(queryWrapper);
        return user.getId();
    }
}
