package com.buerc.permission.init;

import com.buerc.CodeUtil;
import com.buerc.Config;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunnerInit implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        CodeUtil.initConfig(new Config("test","123",100,10,true));
    }
}
