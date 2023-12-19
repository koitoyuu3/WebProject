package com.example.webproject.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Food {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String foodname;    // 食物名称
    private String description; // 食物描述
    private String imagepath;   // 食物图片

    @TableField("userid")
    private Long userid;
}
