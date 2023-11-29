package com.example.webproject.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.webproject.entity.Food;
import com.example.webproject.mapper.FoodMapper;
import com.example.webproject.service.IFoodService;
import org.springframework.stereotype.Service;

@Service
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements IFoodService {
}
