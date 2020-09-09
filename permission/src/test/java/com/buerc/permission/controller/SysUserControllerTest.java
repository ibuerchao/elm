package com.buerc.permission.controller;

import com.buerc.common.utils.JSONUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class SysUserControllerTest {
    private static String header = "token";
    private static String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhNDcwMzlkZS00ZGIyLTQzMGYtYmNkNy0xZTNjZWZiYzlkNTEiLCJpYXQiOjE1OTkzNjUzOTIsImV4cCI6MTU5OTk3MDE5Mn0.Hr9OxoTrYz1u7Aup0SM87V8Mw9jJxLy3j3nOdYGVbi5tmZewieia1oASXuEBpK07F0DPHauG_cJa3MPhP-nmUQ";
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void before(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void add() throws Exception {
        Map<String,Object> map = new HashMap<>();
        Random random = new Random();
        for (int i=0;i<100;i++){
            String username = "hechao"+String.format("%03d",i);
            int status = random.nextInt(3);
            String email = username+"@163.com";
            String deptId = "ff8f8e65-297c-4140-b406-0173315c2f64";
            StringBuilder telephone = new StringBuilder("135");
            for (int j=0;j<8;j++){
                telephone.append(random.nextInt(10));
            }
            map.put("username",username);
            map.put("status",status);
            map.put("email",email);
            map.put("deptId",deptId);
            map.put("telephone", telephone.toString());
            mockMvc.perform(MockMvcRequestBuilders.post("/user/add")
                    .header(header, token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(JSONUtil.toStr(map)))
                    .andDo(MockMvcResultHandlers.print());
        }
    }
}
