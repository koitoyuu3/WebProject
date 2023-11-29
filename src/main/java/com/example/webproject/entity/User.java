package com.example.webproject.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;        // 名字
    private String username;    // 用户名
    private String password;    // 密码
    private String user_image_path;   // 用户头像

    // 一对多
    @TableField(exist = false)
    private List<Food> foods;
}
