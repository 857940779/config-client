package com.springmvc.service.impl;

import com.springmvc.service.DataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: luohanwen
 * @Date: 2019/9/11 16:53
 */

@Service
public class DataServiceImpl implements DataService{
    @Value("${redis.server.url}")
    private String redisUrl;

    @Override
    public void testData() {
        System.out.println("--------Test data--------");
        System.out.println(redisUrl);
    }
}
