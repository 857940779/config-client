package com.springmvc.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by luohw on 2018/5/17 0017.
 */
@Controller
@RequestMapping(value = "/controller")
public class TestController {

    //默认返回到test.jsp页面
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test() {
        System.out.println("====test====");
    }


    //直接输出到流，不需要页面，效果类似@ResponseBody返回json
    @RequestMapping(value = "/testJSON", method = RequestMethod.GET)
    public void testJSON(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("====testJSON====");

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // Proxies.
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter writer = null;
        try {
            writer = writer = response.getWriter();
            writer.write("testJSON");
        } catch (Exception e) {

        } finally {
            if(writer!=null){
                writer.flush();
                writer.close();
            }
        }
    }


    //直接通过注解返回json对象
    @ResponseBody
    @RequestMapping(value = "/testBody", method = RequestMethod.GET)
    public String testBody(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("====testBody====");
        Map map=new HashMap<>();
        map.put("userId","1");
        map.put("userName","zhangsan");
        return JSON.toJSONString(map);
    }
}


