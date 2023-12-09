package com.example.webproject.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Message {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String content;
    private String time;
    private Long sendid;
    private String sendname;
    private Long acceptid;
    private String acceptname;
}
